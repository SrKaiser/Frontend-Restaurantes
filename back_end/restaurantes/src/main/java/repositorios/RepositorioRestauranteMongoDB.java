package repositorios;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import servicios.ServicioSitiosTuristicos;

public class RepositorioRestauranteMongoDB implements IRepositorioRestaurante {

	private final MongoCollection<Document> restauranteCollection;

	public RepositorioRestauranteMongoDB() {
		String connectionString = "mongodb://arso:arso@ac-v8ez3vj-shard-00-00.kzwz6ia.mongodb.net:27017,ac-v8ez3vj-shard-00-01.kzwz6ia.mongodb.net:27017,ac-v8ez3vj-shard-00-02.kzwz6ia.mongodb.net:27017/?ssl=true&replicaSet=atlas-b3t6zg-shard-0&authSource=admin&retryWrites=true&w=majority";
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(connectionString)).build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("proyecto-arso");
		this.restauranteCollection = database.getCollection("restaurantes");
	}

	@Override
	public String create(String nombre, double latitud, double longitud, String idGestor) {
		Document doc = new Document("nombre", nombre).append("latitud", latitud).append("longitud", longitud)
				.append("idGestor", idGestor);
		restauranteCollection.insertOne(doc);
		ObjectId id = doc.getObjectId("_id");
		return id.toHexString();

	}

	@Override
	public boolean update(String idRestaurante, String nombre, double latitud, double longitud)
			throws RepositorioException, EntidadNoEncontrada {
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("El ID proporcionado no es válido", e);
		}

		long updatedCount = restauranteCollection
				.updateOne(Filters.eq("_id", objectId), Updates.combine(Updates.set("nombre", nombre),
						Updates.set("latitud", latitud), Updates.set("longitud", longitud)))
				.getModifiedCount();
		
		if (updatedCount == 0) {
			throw new EntidadNoEncontrada(idRestaurante + " no existe en la base de datos");
		}
		return updatedCount > 0;
	}

	@Override
	public List<SitioTuristico> findSitiosTuristicosProximos(String idRestaurante, int radius, int maxRows)
			throws RepositorioException, EntidadNoEncontrada {
		ServicioSitiosTuristicos servSitiosTuristicos = new ServicioSitiosTuristicos();
		Restaurante r = getCoordenadas(idRestaurante);
		List<SitioTuristico> listaSitiosTuristicos = new LinkedList<>();
		try {
			listaSitiosTuristicos = servSitiosTuristicos.obtenerSitios(r.getLatitud(), r.getLongitud(), radius, maxRows);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaSitiosTuristicos;
	}

	private Restaurante getCoordenadas(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		Restaurante rest = findById(idRestaurante);
		Restaurante restaurante = new Restaurante();
		restaurante.setLatitud(rest.getLatitud());
		restaurante.setLongitud(rest.getLongitud());

		return restaurante;
	}

	@Override
	public boolean setSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitiosTuristicos)
			throws RepositorioException, EntidadNoEncontrada {
		
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("El ID proporcionado no es válido", e);
		}
	    
		List<Document> sitiosTuristicosDocumentos = sitiosTuristicos.stream().map(sitioTuristico -> new Document()
				.append("titulo", sitioTuristico.getTitulo()).append("resumen", sitioTuristico.getResumen())
				.append("categorias", sitioTuristico.getCategorias()).append("enlaces", sitioTuristico.getEnlaces())
				.append("imagenes", sitioTuristico.getImagenes())).collect(Collectors.toList());
		long setResult = restauranteCollection.updateOne(Filters.eq("_id", objectId),
				Updates.set("sitiosTuristicosDestacados", sitiosTuristicosDocumentos)).getModifiedCount();
		
		if (setResult == 0) {
			throw new EntidadNoEncontrada(idRestaurante + " no existe en la base de datos");
		}

		return setResult > 0;
	}

	@Override
	public boolean addPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada {
		
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("El ID proporcionado no es válido", e);
		}
		
	    
		Document platoDoc = new Document().append("nombre", plato.getNombre())
				.append("descripcion", plato.getDescripcion()).append("precio", plato.getPrecio())
				.append("disponibilidad", plato.isDisponibilidad());
		long createdCount = restauranteCollection
				.updateOne(Filters.eq("_id", objectId), Updates.push("platos", platoDoc)).getModifiedCount();

		if (createdCount == 0) {
			throw new EntidadNoEncontrada(idRestaurante + " no existe en la base de datos");
		}

		return createdCount > 0;
	}

	@Override
	public boolean removePlato(String idRestaurante, String nombrePlato)
			throws RepositorioException, EntidadNoEncontrada {
		
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("El ID proporcionado no es válido", e);
		}
		
		long deletedCount = restauranteCollection
				.updateOne(Filters.eq("_id", objectId), Updates.pull("platos", new Document("nombre", nombrePlato)))
				.getModifiedCount();

		if (deletedCount == 0) {
			long restaurantCount = restauranteCollection.countDocuments(Filters.eq("_id", objectId));
			if (restaurantCount == 0) {
				throw new EntidadNoEncontrada("No se encontró el restaurante con el idRestaurante: " + idRestaurante);
			}

			long platoCount = restauranteCollection.countDocuments(Filters.and(Filters.eq("_id", objectId),
					Filters.elemMatch("platos", Filters.eq("nombre", nombrePlato))));
			if (platoCount == 0) {
				throw new EntidadNoEncontrada("No se encontró el plato '" + nombrePlato
						+ "' en el restaurante con el idRestaurante: " + idRestaurante);
			}
		}

		return deletedCount > 0;
	}

	@Override
	public boolean updatePlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada {
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("El ID proporcionado no es válido", e);
		}
		
		Document platoDoc = new Document().append("nombre", plato.getNombre())
				.append("descripcion", plato.getDescripcion()).append("precio", plato.getPrecio()).append("disponibilidad", plato.isDisponibilidad());
		long updatedCount = restauranteCollection.updateOne(
				Filters.and(Filters.eq("_id", objectId),
						Filters.elemMatch("platos", Filters.eq("nombre", plato.getNombre()))),
				Updates.set("platos.$", platoDoc)).getModifiedCount();

		if (updatedCount == 0) {
			long restaurantCount = restauranteCollection.countDocuments(Filters.eq("_id", objectId));
			if (restaurantCount == 0) {
				throw new EntidadNoEncontrada("No se encontró el restaurante con el idRestaurante: " + idRestaurante);
			}

			long platoCount = restauranteCollection.countDocuments(Filters.and(Filters.eq("_id", objectId),
					Filters.elemMatch("platos", Filters.eq("nombre", plato.getNombre()))));
			if (platoCount == 0) {
				throw new EntidadNoEncontrada("No se encontró el plato '" + plato.getNombre()
						+ "' en el restaurante con el idRestaurante: " + idRestaurante);
			}
		}

		return updatedCount > 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Restaurante findById(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {

			throw new RepositorioException("El ID proporcionado no es válido", e);
		}

		Document doc = restauranteCollection.find(Filters.eq("_id", objectId)).first();
		if (doc == null) {
			throw new EntidadNoEncontrada(idRestaurante + " no existe en la base de datos");
		}

		Restaurante restaurante = new Restaurante();
		restaurante.setId(doc.getObjectId("_id").toString());
		restaurante.setNombre(doc.getString("nombre"));
		restaurante.setLatitud(doc.getDouble("latitud"));
		restaurante.setLongitud(doc.getDouble("longitud"));
		String idOpinion = doc.getString("idOpinion");
		if (idOpinion != null)
		{
			restaurante.setNumeroValoraciones(doc.getInteger("numeroValoraciones"));
			restaurante.setGestorId(doc.getString("idGestor"));
			restaurante.setOpinionId(doc.getString("idOpinion"));
			restaurante.setCalificacionMedia(doc.getDouble("calificacionMedia"));
		}


		List<Plato> platos = new ArrayList<>();
		List<Document> platosDocs = (List<Document>) doc.get("platos");
		if (platosDocs != null) {
			for (Document platoDoc : platosDocs) {
				Plato plato = new Plato();
				plato.setNombre(platoDoc.getString("nombre"));
				plato.setDescripcion(platoDoc.getString("descripcion"));
				plato.setPrecio(platoDoc.getDouble("precio"));
				plato.setDisponibilidad(platoDoc.getBoolean("disponibilidad"));
				platos.add(plato);
			}
		}
		restaurante.setPlatos(platos);

		List<SitioTuristico> sitiosTuristicos = new ArrayList<>();

		List<Document> sitiosTuristicosDocs = (List<Document>) doc.get("sitiosTuristicosDestacados");
		if (sitiosTuristicosDocs != null) {
			for (Document sitioTuristicoDoc : sitiosTuristicosDocs) {
				SitioTuristico sitioTuristico = new SitioTuristico();
				sitioTuristico.setTitulo(sitioTuristicoDoc.getString("titulo"));
				sitioTuristico.setResumen(sitioTuristicoDoc.getString("resumen"));
				sitioTuristico.setCategorias((List<String>) sitioTuristicoDoc.get("categorias"));
				sitioTuristico.setEnlaces((List<String>) sitioTuristicoDoc.get("enlaces"));
				sitioTuristico.setImagenes((List<String>) sitioTuristicoDoc.get("imagenes"));
				sitiosTuristicos.add(sitioTuristico);
			}
		}
		restaurante.setSitiosTuristicos(sitiosTuristicos);
		return restaurante;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Restaurante findByIdOpinion(String idOpinion) throws RepositorioException, EntidadNoEncontrada {
		if (idOpinion == null || idOpinion.trim().isEmpty()) {
	        throw new IllegalArgumentException("El ID de opinión proporcionado no es válido");
	    }
		 Document doc = restauranteCollection.find(Filters.eq("idOpinion", idOpinion)).first();
		    if (doc == null) {
		        throw new EntidadNoEncontrada("No se encontró ningún restaurante con el idOpinion " + idOpinion);
		    }
		    Restaurante restaurante = new Restaurante();
		    restaurante.setId(doc.getObjectId("_id").toString());
		    restaurante.setNombre(doc.getString("nombre"));
		    restaurante.setLatitud(doc.getDouble("latitud"));
		    restaurante.setLongitud(doc.getDouble("longitud"));
		    restaurante.setNumeroValoraciones(doc.getInteger("numeroValoraciones"));
		    restaurante.setGestorId(doc.getString("idGestor"));
		    restaurante.setOpinionId(doc.getString("idOpinion"));
		    restaurante.setCalificacionMedia(doc.getDouble("calificacionMedia"));

		    List<Plato> platos = new ArrayList<>();
		    List<Document> platosDocs = (List<Document>) doc.get("platos");
		    if (platosDocs != null) {
		        for (Document platoDoc : platosDocs) {
		            Plato plato = new Plato();
		            plato.setNombre(platoDoc.getString("nombre"));
		            plato.setDescripcion(platoDoc.getString("descripcion"));
		            plato.setPrecio(platoDoc.getDouble("precio"));
		            platos.add(plato);
		        }
		    }
		    restaurante.setPlatos(platos);

		    List<SitioTuristico> sitiosTuristicos = new ArrayList<>();
		    List<Document> sitiosTuristicosDocs = (List<Document>) doc.get("sitiosTuristicosDestacados");
		    if (sitiosTuristicosDocs != null) {
		        for (Document sitioTuristicoDoc : sitiosTuristicosDocs) {
		            SitioTuristico sitioTuristico = new SitioTuristico();
		            sitioTuristico.setTitulo(sitioTuristicoDoc.getString("titulo"));
		            sitioTuristico.setResumen(sitioTuristicoDoc.getString("resumen"));
		            sitioTuristico.setCategorias((List<String>) sitioTuristicoDoc.get("categorias"));
		            sitioTuristico.setEnlaces((List<String>) sitioTuristicoDoc.get("enlaces"));
		            sitioTuristico.setImagenes((List<String>) sitioTuristicoDoc.get("imagenes"));
		            sitiosTuristicos.add(sitioTuristico);
		        }
		    }
		    restaurante.setSitiosTuristicos(sitiosTuristicos);

		    return restaurante;
	}

	@Override
	public boolean delete(String idRestaurante) throws RepositorioException, EntidadNoEncontrada {
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("El ID proporcionado no es válido", e);
		}

		long deletedCount = restauranteCollection.deleteOne(Filters.eq("_id", objectId)).getDeletedCount();
		if (deletedCount == 0) {
			throw new EntidadNoEncontrada(idRestaurante + " no existe en la base de datos");
		}
		return deletedCount > 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ResumenRestaurante> findAll()  {
		List<ResumenRestaurante> restaurantesList = new ArrayList<>();
		try (MongoCursor<Document> cursor = restauranteCollection.find().iterator()) {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				ResumenRestaurante restaurante = new ResumenRestaurante();
				restaurante.setId(doc.getObjectId("_id").toString());
				restaurante.setNombre(doc.getString("nombre"));
				restaurante.setLatitud(doc.getDouble("latitud"));
				restaurante.setLongitud(doc.getDouble("longitud"));
				if (doc.getDouble("calificacionMedia") != null) {
					restaurante.setCalificacionMedia(doc.getDouble("calificacionMedia"));
				}
				else restaurante.setCalificacionMedia(0);
				List<Document> platosDocs = (List<Document>) doc.get("platos");
				if (platosDocs != null) {
					restaurante.setNumeroPlatos(platosDocs.size());
				} else {
					restaurante.setNumeroPlatos(0);
				}
				List<Document> sitiosTuristicosDocs = (List<Document>) doc.get("sitiosTuristicosDestacados");
				if (sitiosTuristicosDocs != null) {
					restaurante.setNumeroSitiosTuristicos(sitiosTuristicosDocs.size());
				} else {
					restaurante.setNumeroSitiosTuristicos(0);
				}
				restaurantesList.add(restaurante);
			}
		}
		return restaurantesList;
	}
	
	@Override
	public boolean updateOpinion(String idRestaurante, String idOpinion, int numValoraciones, double calificacionMedia) throws RepositorioException, EntidadNoEncontrada {
		ObjectId objectId;
		try {
			objectId = new ObjectId(idRestaurante);
		} catch (IllegalArgumentException e) {
			throw new RepositorioException("El ID proporcionado no es válido", e);
		}
		System.out.println(idRestaurante);
		long createdCount = restauranteCollection
				.updateOne(Filters.eq("_id", objectId), Updates.combine(Updates.set("idOpinion", idOpinion),
						Updates.set("numeroValoraciones", numValoraciones), Updates.set("calificacionMedia", calificacionMedia)))
				.getModifiedCount();
		
		if (createdCount == 0) {
			throw new RepositorioException("No hay información que actualizar");
		}
		return createdCount > 0;
	}

}
