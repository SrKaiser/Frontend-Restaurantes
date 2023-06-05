package servicios;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import modelos.Valoracion;
import opiniones.eventos.EventoNuevaValoracion;
import repositorios.FactoriaRepositorios;
import repositorios.IRepositorioRestaurante;

public class ServicioRestaurante implements IServicioRestaurante {
	
	private Connection connection;
    private Channel channel;
	private IRepositorioRestaurante repositorioRestaurante;
	private IServicioOpiniones servicioOpiniones;

	public ServicioRestaurante() {
		this.repositorioRestaurante = FactoriaRepositorios.getRepositorio(Restaurante.class);
		this.servicioOpiniones = new ServicioOpinionesRetrofit();
	}
	
	public ServicioRestaurante(IRepositorioRestaurante rep, IServicioOpiniones serv) {
		this.repositorioRestaurante = rep;
		this.servicioOpiniones = serv;
	}
	
	@Override
	public void suscribirseACola() {
		// registro del consumidor de eventos

		try {
			ConnectionFactory factory = new ConnectionFactory();

			factory.setUri("amqps://edzrfeij:KHQQWPWgL4xfzLdyGf8kazZ8XWrxNm6H@crow.rmq.cloudamqp.com/edzrfeij");

			connection = factory.newConnection();

			channel = connection.createChannel();

			/** Declaración de la cola y enlace con el exchange **/

			final String exchangeName = "evento.nueva.valoracion";
			final String queueName = "valoracion-queue";
			final String bindingKey = "";

			boolean durable = true;
			boolean exclusive = false;
			boolean autodelete = false;
			Map<String, Object> properties = null; // sin propiedades
			channel.queueDeclare(queueName, durable, exclusive, autodelete, properties);

			channel.queueBind(queueName, exchangeName, bindingKey);

			/** Configuración del consumidor **/

			boolean autoAck = false;

			String etiquetaConsumidor = "servicio-restaurante";

			// Consumidor push

			channel.basicConsume(queueName, autoAck, etiquetaConsumidor,

					new DefaultConsumer(channel) {
						@Override
						public void handleDelivery(String consumerTag, Envelope envelope,
								AMQP.BasicProperties properties, byte[] body) throws IOException {

							long deliveryTag = envelope.getDeliveryTag();

							String contenido = new String(body);
							System.out.println(contenido);

							// Crea un JsonReader a partir del contenido recibido
							JsonReader reader = Json.createReader(new StringReader(contenido));

							// Lee el objeto JSON principal
							JsonObject obj = reader.readObject();

							// Obtiene los campos necesarios para EventoNuevaValoracion
							String idOpinion = obj.getString("IdOpinion");
							int numValoraciones = obj.getInt("NumValoraciones");
							double calificacionMedia = obj.getJsonNumber("CalificacionMedia").doubleValue();

							// Obtiene y maneja la fecha manualmente
							JsonObject valoracionObj = obj.getJsonObject("NuevaValoracion");
							String correoElectronico = valoracionObj.getString("CorreoElectronico");
							String fechaStr = valoracionObj.getString("Fecha");
							LocalDateTime fecha;
							try {
								DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
								fecha = LocalDateTime.parse(fechaStr, formatter);
							} catch (DateTimeParseException e) {
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
								fecha = LocalDateTime.parse(fechaStr, formatter);
							}

							int calificacion = valoracionObj.getInt("Calificacion");
							String comentario = valoracionObj.getString("Comentario");

							Valoracion valoracion = new Valoracion(correoElectronico, fecha, calificacion, comentario);

							EventoNuevaValoracion evento = new EventoNuevaValoracion(idOpinion, valoracion,
									numValoraciones, calificacionMedia);

							// Procesamos el evento
							idOpinion = evento.getIdOpinion();
							try {
								Restaurante r = repositorioRestaurante.findByIdOpinion(idOpinion);
								repositorioRestaurante.updateOpinion(r.getId(), idOpinion, evento.getNumValoraciones(),
										evento.getCalificacionMedia());
								System.out.println(evento.getNumValoraciones());
							} catch (RepositorioException | EntidadNoEncontrada e) {

								e.printStackTrace();
							}

							// Confirma el procesamiento
							channel.basicAck(deliveryTag, false);
						}
					});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void cerrarConexion() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

	@Override
	public String altaRestaurante(String nombre, double latitud, double longitud, String idGestor) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser null o vacío");
		}
		if (latitud < -90 || latitud > 90) {
			throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
		}
		if (longitud < -180 || longitud > 180) {
			throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
		}

		return repositorioRestaurante.create(nombre, latitud, longitud, idGestor);

	}

	@Override
	public boolean actualizarRestaurante(String idRestaurante, String nombre, double latitud, double longitud)
			throws RepositorioException, EntidadNoEncontrada {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser null o vacío");
		}
		if (latitud < -90 || latitud > 90) {
			throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
		}
		if (longitud < -180 || longitud > 180) {
			throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
		}
		return repositorioRestaurante.update(idRestaurante, nombre, latitud, longitud);
	}

	@Override
	public List<SitioTuristico> obtenerSitiosTuristicosProximos(String idRestaurante)
			throws RepositorioException, EntidadNoEncontrada {
		return repositorioRestaurante.findSitiosTuristicosProximos(idRestaurante);
	}

	@Override
	public boolean establecerSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitiosTuristicos)
			throws RepositorioException, EntidadNoEncontrada {
		if (sitiosTuristicos == null || sitiosTuristicos.isEmpty()) {
			throw new IllegalArgumentException("La lista de sitios turísticos no puede ser nula o vacía");
		}
		for (SitioTuristico sitio : sitiosTuristicos) {
			if (sitio.getTitulo() == null || sitio.getTitulo().trim().isEmpty()) {
				throw new IllegalArgumentException("El título del sitio turístico no puede ser null o vacío");
			}
			if (sitio.getResumen() == null || sitio.getResumen().trim().isEmpty()) {
				throw new IllegalArgumentException("El resumen del sitio turístico no puede ser null o vacío");
			}
		}

		return repositorioRestaurante.setSitiosTuristicosDestacados(idRestaurante, sitiosTuristicos);
	}

	@Override
	public boolean añadirPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada {
		if (plato.getNombre() == null || plato.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del plato no puede ser null o vacío");
		}
		if (plato.getDescripcion() == null || plato.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción del plato no puede ser null o vacía");
		}
		if (plato.getPrecio() <= 0) {
			throw new IllegalArgumentException("El precio del plato debe ser mayor que 0");
		}

		return repositorioRestaurante.addPlato(idRestaurante, plato);
	}

	@Override
	public boolean borrarPlato(String idRestaurante, String nombrePlato)
			throws RepositorioException, EntidadNoEncontrada {
		if (nombrePlato == null || nombrePlato.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del plato no puede ser null o vacío");
		}

		return repositorioRestaurante.removePlato(idRestaurante, nombrePlato);
	}

	@Override
	public boolean actualizarPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada {
		if (plato.getNombre() == null || plato.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del plato no puede ser null o vacío");
		}
		if (plato.getDescripcion() == null || plato.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción del plato no puede ser null o vacía");
		}
		if (plato.getPrecio() <= 0) {
			throw new IllegalArgumentException("El precio del plato debe ser mayor que 0");
		}

		return repositorioRestaurante.updatePlato(idRestaurante, plato);
	}

	@Override
	public Restaurante recuperarRestaurante(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		return repositorioRestaurante.findById(idRestaurante);
	}

	@Override
	public boolean borrarRestaurante(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		return repositorioRestaurante.delete(idRestaurante);
	}

	@Override
	public List<ResumenRestaurante> recuperarTodosRestaurantes(){
		return repositorioRestaurante.findAll();
	}

	@Override
	public String activarOpiniones(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		Restaurante restaurante = repositorioRestaurante.findById(idRestaurante);

		if (restaurante == null) {
			throw new EntidadNoEncontrada("El restaurante no existe");
		}

		String idOpinion = servicioOpiniones.registrarRecurso(restaurante.getNombre()).toString();

		restaurante.setOpinionId(idOpinion);
		restaurante.setNumeroValoraciones(0);
		restaurante.setCalificacionMedia(0);

		repositorioRestaurante.updateOpinion(idRestaurante, idOpinion, 0, 0);

		return idOpinion;
	}

	@Override
	public List<Valoracion> recuperarTodasValoraciones(String idRestaurante)
			throws RepositorioException, EntidadNoEncontrada {
		Restaurante restaurante = repositorioRestaurante.findById(idRestaurante);

		if (restaurante == null) {
			throw new EntidadNoEncontrada("El restaurante no existe");
		} else {
			String idOpinion = restaurante.getOpinionId();
			return servicioOpiniones.obtenerValoraciones(idOpinion);
		}

	}
	
	public void setRepositorioRestaurante(IRepositorioRestaurante repositorioRestaurante) {
		this.repositorioRestaurante = repositorioRestaurante;
	}
	
	public void setServicioOpiniones(IServicioOpiniones servicioOpiniones) {
		this.servicioOpiniones = servicioOpiniones;
	}
}
