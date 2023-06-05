package servicio;

import java.util.List;

import modelos.Opinion;
import modelos.Valoracion;
import repositorio.FactoriaRepositorio;
import repositorio.IRepositorioOpinion;

public class ServicioOpinion implements IServicioOpinion{
	private IRepositorioOpinion repositorioOpinion;

    public ServicioOpinion() {
        this.repositorioOpinion = FactoriaRepositorio.getRepositorio(IRepositorioOpinion.class);;
    }

    @Override
    public String registrarRecurso(String nombreRecurso) {
        Opinion opinion = new Opinion(nombreRecurso);
        return repositorioOpinion.create(opinion);
    }

    @Override
    public boolean añadirValoracion(String idOpinion, Valoracion valoracion) {
        Opinion opinion = repositorioOpinion.findById(idOpinion);
        if (opinion != null) {
            opinion.getValoraciones().removeIf(v -> v.getCorreoElectronico().equals(valoracion.getCorreoElectronico()));
            opinion.addValoracion(valoracion);
            repositorioOpinion.addValoracion(idOpinion, valoracion);
            return true;
        }
        return false;
    }

    @Override
    public Opinion obtenerOpinion(String idOpinion) {
        return repositorioOpinion.findById(idOpinion);
    }

    @Override
    public boolean eliminarOpinion(String idOpinion) {
    	return repositorioOpinion.delete(idOpinion);
    }
    
    @Override
    public List<Opinion> obtenerOpiniones() {
    	return repositorioOpinion.findAll();
    }
}
