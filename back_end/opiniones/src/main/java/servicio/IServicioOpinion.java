package servicio;

import java.util.List;

import modelos.Opinion;
import modelos.Valoracion;

public interface IServicioOpinion {
	
	String registrarRecurso(String nombreRecurso);
	
    boolean añadirValoracion(String idOpinion, Valoracion valoracion);
    
    Opinion obtenerOpinion(String idOpinion);
    
    boolean eliminarOpinion(String idOpinion);
    
    List<Opinion> obtenerOpiniones();
}
