package modelos;

public class ValoracionInput {
	private String correoElectronico;
	private int calificacion;
	private String comentario;

	public ValoracionInput(String correoElectronico, int calificacion) {
		this.correoElectronico = correoElectronico;
		this.calificacion = calificacion;
	}

	public ValoracionInput(String correoElectronico, int calificacion, String comentario) {
		this.correoElectronico = correoElectronico;
		this.calificacion = calificacion;
		this.comentario = comentario;
	}

	public ValoracionInput() {
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
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
}
