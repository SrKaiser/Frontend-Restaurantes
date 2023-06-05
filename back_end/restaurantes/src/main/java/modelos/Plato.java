package modelos;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Plato {
	
	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("precio")
	private double precio;
	
	public Plato(String nombre, String descripcion, double precio) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
	}
	
	public Plato() {}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "Plato [nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, nombre, precio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plato other = (Plato) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(nombre, other.nombre)
				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio);
	}
	
	
	
	
	
	
}
