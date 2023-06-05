package repositorio;

import java.util.HashMap;
import java.util.Map;

import utils.PropertiesReader;

public class FactoriaRepositorio {
	private static final String PROPERTIES = "aplicacion.properties";

	private static Map<Class<?>, Object> repositorios = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <T, K, R extends RepositorioOpinion> R getRepositorio(Class<?> entidad) {

		try {
			if (repositorios.containsKey(entidad)) {
				return (R) repositorios.get(entidad);
			} else {
				PropertiesReader properties = new PropertiesReader(PROPERTIES);
				String clase = properties.getProperty(entidad.getName());
				R repositorio = (R) Class.forName(clase).getConstructor().newInstance();
				repositorios.put(entidad, repositorio);
				return repositorio;
			}
		} catch (Exception e) {

			throw new RuntimeException("No se ha podido obtener el repositorio para la entidad: " + entidad.getName());
		}

	}
}
