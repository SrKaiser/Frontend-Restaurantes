package repositorios;

import java.util.List;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;

public interface IRepositorioRestaurante{
	
	String create(String nombre, double latitud, double longitud, String gestorId);
    
	Restaurante findById(String id) throws RepositorioException, EntidadNoEncontrada;
    
	List<ResumenRestaurante> findAll();
    
	boolean update(String idRestaurante, String nombre, double latitud, double longitud) throws RepositorioException, EntidadNoEncontrada;
    
	boolean delete(String id) throws RepositorioException, EntidadNoEncontrada;
	
	List<SitioTuristico> findSitiosTuristicosProximos(String idRestaurante) throws RepositorioException, EntidadNoEncontrada;
	
    boolean setSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitiosTuristicos) throws RepositorioException, EntidadNoEncontrada;
    
    boolean addPlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada;
    
    boolean removePlato(String idRestaurante, String nombrePlato) throws RepositorioException, EntidadNoEncontrada;
    
    boolean updatePlato(String idRestaurante, Plato plato) throws RepositorioException, EntidadNoEncontrada;

	boolean updateOpinion(String idRestaurante, String idOpinion, int numValoraciones, double calificacionMedia) throws RepositorioException, EntidadNoEncontrada;

	Restaurante findByIdOpinion(String idOpinion) throws RepositorioException, EntidadNoEncontrada;

}
