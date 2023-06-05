package modelos;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Restaurante {
	
	@JsonProperty("id")
	private String id;
	@JsonProperty("nombre")
	private String nombre;
	@JsonProperty("latitud")
	private double latitud;
	@JsonProperty("longitud")
	private double longitud;
	@JsonProperty("sitiosTuristicos")
	private List<SitioTuristico> sitiosTuristicos;
	@JsonProperty("platos")
	private List<Plato> platos;
	@JsonProperty("gestorId")
	private String gestorId;
	@JsonProperty("numeroValoraciones")
	private int numeroValoraciones;
	@JsonProperty("calificacionMedia")
	private double calificacionMedia;
	@JsonProperty("opinionId")
	private String opinionId;
	
	public Restaurante(String nombre, double latitud, double longitud) {
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.sitiosTuristicos = new LinkedList<SitioTuristico>();
		this.platos = new LinkedList<Plato>();
	}
	
	public Restaurante(String id, String nombre, double latitud, double longitud) {
		this.id = id;
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.sitiosTuristicos = new LinkedList<SitioTuristico>();
		this.platos = new LinkedList<Plato>();
	}

	public Restaurante() {
		this.sitiosTuristicos = new LinkedList<SitioTuristico>();
		this.platos = new LinkedList<Plato>();
	}

	public void addSitioTuristico(SitioTuristico sitio) {
		sitiosTuristicos.add(sitio);
	}
	
	public void deleteSitioTuristico(SitioTuristico sitio) {
		sitiosTuristicos.remove(sitio);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	public List<SitioTuristico> getSitiosTuristicos() {
		return sitiosTuristicos;
	}

	public void setSitiosTuristicos(List<SitioTuristico> sitiosTuristicos) {
		this.sitiosTuristicos = sitiosTuristicos;
	}

	public List<Plato> getPlatos() {
		return platos;
	}

	public void setPlatos(List<Plato> platos) {
		this.platos = platos;
	}
	
	public void addPlato(Plato plato) {
		platos.add(plato);
	}
	
	public String getGestorId() {
		return gestorId;
	}
	
	public void setGestorId(String gestorId) {
		this.gestorId = gestorId;
	}

	public int getNumeroValoraciones() {
		return numeroValoraciones;
	}

	public void setNumeroValoraciones(int numeroValoraciones) {
		this.numeroValoraciones = numeroValoraciones;
	}

	public double getCalificacionMedia() {
		return calificacionMedia;
	}

	public void setCalificacionMedia(double calificacionMedia) {
		this.calificacionMedia = calificacionMedia;
	}

	public String getOpinionId() {
		return opinionId;
	}

	public void setOpinionId(String opinionId) {
		this.opinionId = opinionId;
	}

	@Override
	public String toString() {
		return "Restaurante [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud
				+ ", sitiosTuristicos=" + sitiosTuristicos + ", platos=" + platos + "]";
	}

}
