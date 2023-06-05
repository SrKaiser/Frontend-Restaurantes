package servicios;

import java.util.List;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Valoracion;

public interface IServicioOpiniones {
    Object registrarRecurso(String nombre) throws RepositorioException, EntidadNoEncontrada;
    List<Valoracion> obtenerValoraciones(String idOpinion);
}

