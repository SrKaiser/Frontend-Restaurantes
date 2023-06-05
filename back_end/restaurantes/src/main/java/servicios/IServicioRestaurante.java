package servicios;

import java.util.List;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import modelos.Valoracion;

public interface IServicioRestaurante {
	
	String altaRestaurante(String nombre, double latitud, double longitud, String idGestor);
	
	boolean actualizarRestaurante(String id, String nombre, double latitud, double longitud) throws RepositorioException, EntidadNoEncontrada;
	
	List<SitioTuristico> obtenerSitiosTuristicosProximos(String idRestaurante) throws RepositorioException, EntidadNoEncontrada;
	
	boolean establecerSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitiosTuristicos) throws RepositorioException, EntidadNoEncontrada;
	
	boolean añadirPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada;
	
	boolean borrarPlato(String idRestaurante, String nombrePlato) throws RepositorioException, EntidadNoEncontrada;
	
	boolean actualizarPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada;
	
	Restaurante recuperarRestaurante(String idRestaurante) throws RepositorioException, EntidadNoEncontrada;
	
	boolean borrarRestaurante(String idRestaurante) throws RepositorioException, EntidadNoEncontrada;
	
	List<ResumenRestaurante> recuperarTodosRestaurantes();
	
	String activarOpiniones(String idRestaurante) throws RepositorioException, EntidadNoEncontrada;

	List<Valoracion> recuperarTodasValoraciones(String idRestaurante) throws RepositorioException, EntidadNoEncontrada;

	void suscribirseACola();

	void cerrarConexion();
	
	
}
