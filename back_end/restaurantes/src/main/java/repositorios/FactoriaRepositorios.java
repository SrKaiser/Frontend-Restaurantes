package repositorios;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import utils.Configuracion;

/*
 * Factoría que encapsula la implementación del repositorio.
 * 
 * Utiliza un fichero de propiedades para cargar la implementación.
 */

public class FactoriaRepositorios {
	
	private static Map<Class<?>, Object> repositorios = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <T, K, R extends IRepositorioRestaurante> R getRepositorio(Class<?> entidad) {
							
			try {
				if (repositorios.containsKey(entidad)) {
					return (R) repositorios.get(entidad);
				}
				else {
					Properties properties = Configuracion.cargarConfiguracion();
					System.out.println("Archivo de configuración cargado correctamente: " + properties);
					String className = properties.getProperty(entidad.getName());
					System.out.println("Clase de repositorio: " + className);
					R repositorio = (R) Class.forName(className).getConstructor().newInstance();
					repositorios.put(entidad, repositorio);
					return repositorio;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("No se ha podido obtener el repositorio para la entidad: " + entidad.getName());
			}
			
	}
	
}
