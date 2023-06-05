package repositorio;

import java.util.List;

import modelos.Opinion;
import modelos.Valoracion;

public interface IRepositorioOpinion {
	
	String create(Opinion opinion);
    
	Opinion findById(String id);
    
	List<Opinion> findAll();
    
	boolean update(Opinion opinion);
    
	boolean delete(String id);
	
	boolean addValoracion(String id, Valoracion valoracion);
}
