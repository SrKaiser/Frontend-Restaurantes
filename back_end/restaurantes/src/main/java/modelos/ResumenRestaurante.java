package modelos;

public class ResumenRestaurante {
	private String id;
	private String nombre;
	private double latitud;
	private double longitud;
	private int numeroPlatos;
	private int numeroSitiosTuristicos;
	private double calificacionMedia;
	
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
	public int getNumeroPlatos() {
		return numeroPlatos;
	}
	public void setNumeroPlatos(int numeroPlatos) {
		this.numeroPlatos = numeroPlatos;
	}
	public int getNumeroSitiosTuristicos() {
		return numeroSitiosTuristicos;
	}
	public void setNumeroSitiosTuristicos(int numeroSitiosTuristicos) {
		this.numeroSitiosTuristicos = numeroSitiosTuristicos;
	}
	
	public double getCalificacionMedia() {
		return calificacionMedia;
	}
	public void setCalificacionMedia(double calificacionMedia) {
		this.calificacionMedia = calificacionMedia;
	}
	
	@Override
	public String toString() {
		return "ResumenRestaurante [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud
				+ ", numeroPlatos=" + numeroPlatos + ", numeroSitiosTuristicos=" + numeroSitiosTuristicos
				+ ", calificacionMedia=" + calificacionMedia + "]";
	}
	
	
	
	
	
}
