package tests;

import java.util.List;

import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import servicios.FactoriaServicios;
import servicios.IServicioRestaurante;


public class PruebasBasicas {
	public static boolean isTestEnvironment = false;
	public static void main(String[] args) throws Exception {
		IServicioRestaurante serv = FactoriaServicios.getServicio(IServicioRestaurante.class);
		// Prueba de alta de restaurantes
		String id = serv.altaRestaurante("Goiko", 40.42039145624014, -3.6996503622016954, "César");
		String id2 = serv.altaRestaurante("McDonalds", 37.25241153058483, -3.6102678802605594, "César");
		
		// Prueba de actualizar restaurante
		serv.actualizarRestaurante(id, "Burger", 42.347384117579004, -3.699256208170313);
		
		// Prueba de obtener los sitios turisticos próximos de un restaurante
		System.out.println("Sitios Turisticos:");
		List<SitioTuristico> sits = serv.obtenerSitiosTuristicosProximos(id);
		for(SitioTuristico s : sits) {
			System.out.println(s);
		}
		
		// Prueba de establecer sitios turisticos al restaurante
		serv.establecerSitiosTuristicosDestacados(id, sits);
		
		// Prueba de añadir platos a un restaurante
		serv.añadirPlato(id, new Plato("a", "a", 15));
		serv.añadirPlato(id, new Plato("b", "b", 15));
		
		// Prueba de borrar plato a un restaurante
		serv.borrarPlato(id, "a");
		
		// Prueba de actualizar plato a un restaurante
		serv.actualizarPlato(id, new Plato("a", "a", 20));
		
		// Prueba de recuperar un restaurante
		Restaurante rest = serv.recuperarRestaurante(id);
		System.out.println("\nUn restaurante:");
		System.out.println(rest);
		
		// Prueba de borrar un restaurante
		serv.añadirPlato(id2, new Plato("a", "a", 15));
		serv.borrarRestaurante(id2);
		
		// Prueba de recuperar todos los restaurantes
		List<ResumenRestaurante> rests = serv.recuperarTodosRestaurantes();
		System.out.println("\nTodos los restaurantes:");
		for(ResumenRestaurante r : rests) {
			System.out.println(r);
		}
		System.out.println("\nFin");
	}
}
