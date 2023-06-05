package rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import servicios.FactoriaServicios;
import servicios.IServicioRestaurante;
import servicios.ServicioRestaurante;

@WebListener
public class RestaurantesListener implements ServletContextListener {
	
	private IServicioRestaurante servicioRestaurante = FactoriaServicios.getServicio(IServicioRestaurante.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Esto se ejecutará cuando la aplicación web se cargue
        try {
        	servicioRestaurante = new ServicioRestaurante();
        	servicioRestaurante.suscribirseACola(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Esto se ejecutará cuando la aplicación web se cierre
    	servicioRestaurante.cerrarConexion();
    }
}

