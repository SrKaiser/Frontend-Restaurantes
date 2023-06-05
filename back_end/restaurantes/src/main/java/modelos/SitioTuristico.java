package modelos;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SitioTuristico {
	
	@JsonProperty("titulo")
	private String titulo;
	@JsonProperty("resumen")
	private String resumen;
	@JsonProperty("categorias")
	private List<String> categorias;
	@JsonProperty("enlaces")
	private List<String> enlaces;
	@JsonProperty("imagenes")
	private List<String> imagenes;
	
	public SitioTuristico(String titulo, String resumen, List<String> categorias, List<String> enlaces, List<String> imagenes) {
		this.titulo = titulo;
		this.resumen = resumen;
		this.categorias = categorias;
		this.enlaces = enlaces;
		this.imagenes = imagenes;
	}
	
	public SitioTuristico() {
		this.categorias = new LinkedList<String>();
		this.enlaces = new LinkedList<String>();
		this.imagenes = new LinkedList<String>();
	}
		
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getResumen() {
		return resumen;
	}
	
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
	
	public List<String> getCategorias() {
		return categorias;
	}
	
	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}
	
	public void addCategoria(String categoria) {
		categorias.add(categoria);
	}
	
	public List<String> getEnlaces() {
		return enlaces;
	}
	
	public void setEnlaces(List<String> enlaces) {
		this.enlaces = enlaces;
	}
	
	public void addEnlace(String enlace) {
		enlaces.add(enlace);
	}
	
	public List<String> getImagenes() {
		return imagenes;
	}
	
	public void setImagenes(List<String> imagenes) {
		this.imagenes = imagenes;
	}
	
	public void addImagen(String imagen) {
		imagenes.add(imagen);
	}

	@Override
	public String toString() {
		return "SitioTuristico [titulo=" + titulo + ", resumen=" + resumen + ", categorias=" + categorias + ", enlaces="
				+ enlaces + ", imagenes=" + imagenes + "]";
	}

	
	
	
	
}
