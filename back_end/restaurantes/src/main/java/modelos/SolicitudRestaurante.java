package modelos;

public class SolicitudRestaurante {
	private String nombre;
	private double latitud;
	private double longitud;
	private String ciudad;
	private String fecha;
	
	public SolicitudRestaurante(String nombre, double latitud, double longitud, String ciudad, String fecha) {
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.ciudad = ciudad;
		this.fecha = fecha;
	}
	
	public SolicitudRestaurante() {}
	
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

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	
	
}
