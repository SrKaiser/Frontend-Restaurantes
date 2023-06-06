package modelos;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class Valoracion {
	
    private String correoElectronico;
	private String fecha;
    private int calificacion;
    private String comentario;

    public Valoracion(String correoElectronico, int calificacion) {
        this.correoElectronico = correoElectronico;
        this.calificacion = calificacion;
    }
    
    public Valoracion(String correoElectronico, int calificacion, String comentario) {
        this.correoElectronico = correoElectronico;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }
    
    public Valoracion(String correoElectronico, String fecha, int calificacion, String comentario) {
		super();
		this.correoElectronico = correoElectronico;
		this.fecha = fecha;
		this.calificacion = calificacion;
		this.comentario = comentario;
	}

	public Valoracion() {}

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
            String dateString = jsonParser.getValueAsString();
            TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_DATE_TIME.parse(dateString);
            return LocalDateTime.from(temporalAccessor);
        }
    }


	@Override
	public String toString() {
		return "Valoracion [correoElectronico=" + correoElectronico + ", fecha=" + fecha + ", calificacion="
				+ calificacion + ", comentario=" + comentario + "]";
	}
    
    
}

