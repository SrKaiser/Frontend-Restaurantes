package modelos;

public class ResumenRestaurante {
	private String id;
	private String nombre;
	private double latitud;
	private double longitud;
	private String ciudad;
	private String fechaAlta;
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
	
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	@Override
	public String toString() {
		return "ResumenRestaurante [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud
				+ ", ciudad=" + ciudad + ", fechaAlta=" + fechaAlta + ", numeroPlatos=" + numeroPlatos
				+ ", numeroSitiosTuristicos=" + numeroSitiosTuristicos + ", calificacionMedia=" + calificacionMedia
				+ "]";
	}
	
	
	
	
	
	
	
	
	
}
