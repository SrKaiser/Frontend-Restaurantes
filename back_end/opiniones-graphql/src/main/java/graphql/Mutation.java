package graphql;


import com.coxautodev.graphql.tools.GraphQLRootResolver;

import modelos.Valoracion;
import modelos.ValoracionInput;
import servicio.FactoriaServicio;
import servicio.IServicioOpinion;

public class Mutation implements GraphQLRootResolver {

    private IServicioOpinion servicioOpiniones = FactoriaServicio.getServicio(IServicioOpinion.class);

    public String crearOpinion(String nombreRecurso) {
        return servicioOpiniones.registrarRecurso(nombreRecurso);
    }

    public boolean anadirValoracion(String id, ValoracionInput valoracionInput) {
    	Valoracion valoracion = new Valoracion(
                valoracionInput.getCorreoElectronico(),
                valoracionInput.getCalificacion(),
                valoracionInput.getComentario()
        );
        return servicioOpiniones.añadirValoracion(id, valoracion);
    }

    public boolean eliminarOpinion(String id) {
        return servicioOpiniones.eliminarOpinion(id);
    }
}

