package repositorio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import modelos.Opinion;
import modelos.Valoracion;

public class RepositorioOpinion implements IRepositorioOpinion {
	private final MongoCollection<Document> opiniones;

	public RepositorioOpinion() {

		String connectionString = "mongodb://arso:arso@ac-v8ez3vj-shard-00-00.kzwz6ia.mongodb.net:27017,ac-v8ez3vj-shard-00-01.kzwz6ia.mongodb.net:27017,ac-v8ez3vj-shard-00-02.kzwz6ia.mongodb.net:27017/?ssl=true&replicaSet=atlas-b3t6zg-shard-0&authSource=admin&retryWrites=true&w=majority";
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(connectionString)).build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("proyecto-arso");
		this.opiniones = database.getCollection("opiniones");
	}

	@Override
	public String create(Opinion opinion) {
		Document doc = new Document()
				.append("nombreRecurso", opinion.getNombreRecurso())
				.append("valoraciones", opinion.getValoraciones());
		opiniones.insertOne(doc);
		ObjectId id = doc.getObjectId("_id");
        return id.toHexString();
	}

	@Override
	public Opinion findById(String id) {
		ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        
		Document doc = opiniones.find(Filters.eq("_id", objectId)).first();
		if (doc == null) {
			return null;
		}
		return documentToOpinion(doc);
	}

	@Override
	public List<Opinion> findAll() {
		List<Opinion> result = new ArrayList<>();
		for (Document doc : opiniones.find()) {
			result.add(documentToOpinion(doc));
		}
		return result;
	}

	@Override
	public boolean update(Opinion opinion) {
		ObjectId objectId;
        try {
            objectId = new ObjectId(opinion.getId());
        } catch (IllegalArgumentException e) {
            return false;
        }
		
		long updatedCount = opiniones.updateOne(Filters.eq("_id", objectId),
				Updates.combine(Updates.set("nombreRecurso", opinion.getNombreRecurso()),
						Updates.set("valoraciones", opinion.getValoraciones()))).getModifiedCount();
		return updatedCount > 0;
	}
	
	@Override
	public boolean addValoracion(String id, Valoracion valoracion) {
	    ObjectId objectId;
	    try {
	        objectId = new ObjectId(id);
	    } catch (IllegalArgumentException e) {
	        return false;
	    }

	    Document valoracionDoc = new Document()
	            .append("email", valoracion.getCorreoElectronico())
	            .append("fecha", valoracion.getFecha().toString())
	            .append("calificacion", valoracion.getCalificacion())
	            .append("comentario", valoracion.getComentario());

	    long resultCount = opiniones.updateOne(
	            Filters.eq("_id", objectId),
	            Updates.addToSet("valoraciones", valoracionDoc)
	    ).getModifiedCount();

	    return resultCount > 0;
	}

	

	@Override
	public boolean delete(String id) {
		ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return false;
        }
		long deletedCount = opiniones.deleteOne(Filters.eq("_id", objectId)).getDeletedCount();
		return deletedCount > 0;
	}

	@SuppressWarnings("unchecked")
	private Opinion documentToOpinion(Document doc) {
		Opinion opinion = new Opinion();
		opinion.setId(doc.getObjectId("_id").toString());
		opinion.setNombreRecurso(doc.getString("nombreRecurso"));
		List<Valoracion> valoraciones = new ArrayList<>();
	    for (Document valoracionDoc : (List<Document>) doc.get("valoraciones")) {
	    	LocalDateTime fecha = LocalDateTime.parse(valoracionDoc.getString("fecha"));
	        Valoracion valoracion = new Valoracion(
	                valoracionDoc.getString("email"),
	                valoracionDoc.getInteger("calificacion"),
	                valoracionDoc.getString("comentario")
	        );
	        valoracion.setFecha(fecha);
	        valoraciones.add(valoracion);
	    }
	    opinion.setValoraciones(valoraciones);
		return opinion;
	}
}
