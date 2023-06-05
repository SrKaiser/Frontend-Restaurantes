package repositorios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import servicios.ServicioSitiosTuristicos;

public class RepositorioRestauranteMemoria implements IRepositorioRestaurante{
	private final Map<String, Restaurante> restaurantes = new HashMap<>();
	
	@Override
	public String create(String nombre, double latitud, double longitud, String gestorId) {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
        Restaurante restaurante = new Restaurante(id, nombre, latitud, longitud);
        restaurante.setGestorId(gestorId);
//        restaurante.setOpinionId(opinionId);
        restaurantes.put(id, restaurante);
        return id;
	}

	@Override
	public boolean update(String idRestaurante, String nombre, double latitud, double longitud) throws EntidadNoEncontrada {
		Restaurante restaurante = findById(idRestaurante);
	    restaurante.setNombre(nombre);
	    restaurante.setLatitud(latitud);
	    restaurante.setLongitud(longitud);

	    restaurantes.put(idRestaurante, restaurante);
	    return true;
	}
	
	@Override
	public List<SitioTuristico> findSitiosTuristicosProximos(String idRestaurante) throws EntidadNoEncontrada {
		ServicioSitiosTuristicos servSitiosTuristicos = new ServicioSitiosTuristicos();
		Restaurante restaurante = findById(idRestaurante);
	    List<SitioTuristico> listaSitiosTuristicos = new LinkedList<>();
	    try {
	        listaSitiosTuristicos = servSitiosTuristicos.obtenerSitios(restaurante.getLatitud(), restaurante.getLongitud());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return listaSitiosTuristicos;
	}
	
	@Override
	public boolean setSitiosTuristicosDestacados(String idRestaurante, List<SitioTuristico> sitiosTuristicos) throws EntidadNoEncontrada {
		Restaurante restaurante = findById(idRestaurante);
	    restaurante.setSitiosTuristicos(sitiosTuristicos);
	    return true;
	}

	@Override
	public boolean addPlato(String idRestaurante, Plato plato) throws EntidadNoEncontrada {
		Restaurante restaurante = findById(idRestaurante);
	    restaurante.addPlato(plato);
	    return true;
	}

	@Override
	public boolean removePlato(String idRestaurante, String nombrePlato) throws EntidadNoEncontrada {
		Restaurante restaurante = findById(idRestaurante);
	    List<Plato> platos = restaurante.getPlatos();
	    boolean removed = platos.removeIf(plato -> plato.getNombre().equals(nombrePlato));
	    restaurante.setPlatos(platos);
	    if (!removed) {
	    	throw new EntidadNoEncontrada("No se encontró el plato '" + nombrePlato
					+ "' en el restaurante con el idRestaurante: " + idRestaurante);
	    }
	    return removed;
	}

	@Override
	public boolean updatePlato(String idRestaurante, Plato plato) throws EntidadNoEncontrada {
		Restaurante restaurante = findById(idRestaurante);
	    List<Plato> platos = restaurante.getPlatos();
	    boolean updated = false;

	    for (int i = 0; i < platos.size(); i++) {
	        if (platos.get(i).getNombre().equals(plato.getNombre())) {
	            platos.set(i, plato);
	            updated = true;
	            break;
	        }
	    }
	    if (updated) {
	        restaurante.setPlatos(platos);
	    }
	    if (!updated) {
	    	throw new EntidadNoEncontrada("No se encontró el plato '" + plato.getNombre()
					+ "' en el restaurante con el idRestaurante: " + idRestaurante);
	    }
	    return updated;
	}


	@Override
	public Restaurante findById(String idRestaurante) throws EntidadNoEncontrada {
		Restaurante restaurante = restaurantes.get(idRestaurante);
		if (restaurante == null) {
			throw new EntidadNoEncontrada(idRestaurante + " no existe en el repositorio");
	    }
	    return restaurantes.get(idRestaurante);
	}

	@Override
	public boolean delete(String idRestaurante) throws EntidadNoEncontrada {
	    Restaurante restaurante = restaurantes.remove(idRestaurante);
	    if (restaurante == null) {
			throw new EntidadNoEncontrada(idRestaurante + " no existe en el repositorio");
	    }
	    return restaurante != null;
	}

	@Override
	public List<ResumenRestaurante> findAll() {
	    List<ResumenRestaurante> restaurantesList = new ArrayList<>();
	    for (Restaurante restaurante : restaurantes.values()) {
	        ResumenRestaurante resumen = new ResumenRestaurante();
	        resumen.setId(restaurante.getId());
	        resumen.setNombre(restaurante.getNombre());
	        resumen.setLatitud(restaurante.getLatitud());
	        resumen.setLongitud(restaurante.getLongitud());
	        resumen.setNumeroPlatos(restaurante.getPlatos().size());
	        resumen.setNumeroSitiosTuristicos(restaurante.getSitiosTuristicos().size());
	        restaurantesList.add(resumen);
	    }
	    return restaurantesList;
	}

	@Override
	public boolean updateOpinion(String idRestaurante, String idOpinion, int numValoraciones, double calificacionMedia)
			throws RepositorioException, EntidadNoEncontrada {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Restaurante findByIdOpinion(String idOpinion) throws RepositorioException, EntidadNoEncontrada {
		// TODO Auto-generated method stub
		return null;
	}


}
