package servicios;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import utils.Configuracion;

public class FactoriaServicios {
	
	private static Map<Class<?>, Object> servicios = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <T> T getServicio(Class<T> servicio) {
				
			
			try {
				if (servicios.containsKey(servicio)) {
					return (T) servicios.get(servicio);
				}
				else {
					Properties properties = Configuracion.cargarConfiguracion();
					System.out.println("Archivo de configuración cargado correctamente: " + properties);
					String clase = properties.getProperty(servicio.getName());
					System.out.println("Clase de servicio: " + clase);
					return (T) Class.forName(clase).getConstructor().newInstance();
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("No se ha podido obtener la implementación del servicio: " + servicio.getName());
			}
			
	}
	
}
