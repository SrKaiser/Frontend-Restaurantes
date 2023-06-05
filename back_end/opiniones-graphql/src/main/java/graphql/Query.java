package graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import modelos.Opinion;
import servicio.FactoriaServicio;
import servicio.IServicioOpinion;

public class Query implements GraphQLRootResolver {
	
	private IServicioOpinion servicioOpiniones = FactoriaServicio.getServicio(IServicioOpinion.class);

    public Opinion obtenerOpinion(String id) {
        return servicioOpiniones.obtenerOpinion(id);
    }

    public List<Opinion> obtenerOpiniones() {
        return servicioOpiniones.obtenerOpiniones();
    }
}
