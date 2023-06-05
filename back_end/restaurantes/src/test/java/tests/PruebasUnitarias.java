package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import servicios.FactoriaServicios;
import servicios.IServicioRestaurante;

public class PruebasUnitarias {

	private IServicioRestaurante servicio;

	@Before
	public void setUp() throws Exception {
		PruebasBasicas.isTestEnvironment = true;
		servicio = FactoriaServicios.getServicio(IServicioRestaurante.class);
	}

	/* Test de funciones exitosas */

	@Test
	public void testAltaRestaurante() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		Restaurante restaurante = servicio.recuperarRestaurante(id);
		Assert.assertNotNull(restaurante);
		Assert.assertEquals("Restaurante Test", restaurante.getNombre());
		Assert.assertEquals(10.0, restaurante.getLatitud(), 0.001);
		Assert.assertEquals(20.0, restaurante.getLongitud(), 0.001);
	}

	@Test
	public void testActualizarRestaurante() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		boolean resultado = servicio.actualizarRestaurante(id, "Restaurante Actualizado", 30.0, 40.0);
		Assert.assertTrue(resultado);
		Restaurante restaurante = servicio.recuperarRestaurante(id);
		Assert.assertEquals("Restaurante Actualizado", restaurante.getNombre());
		Assert.assertEquals(30.0, restaurante.getLatitud(), 0.001);
		Assert.assertEquals(40.0, restaurante.getLongitud(), 0.001);
	}

	@Test
	public void testFindSitiosTuristicosProximos() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 40.42039145624014, -3.6996503622016954, "César");
		List<SitioTuristico> sitiosTuristicos = servicio.obtenerSitiosTuristicosProximos(id);
		Assert.assertNotNull(sitiosTuristicos);
	}

	@Test
	public void testSetSitiosTuristicosDestacados() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 40.42039145624014, -3.6996503622016954, "César");
		List<SitioTuristico> sitiosTuristicos = servicio.obtenerSitiosTuristicosProximos(id);
		boolean resultado = servicio.establecerSitiosTuristicosDestacados(id, sitiosTuristicos);
		Assert.assertTrue(resultado);
		Restaurante restaurante = servicio.recuperarRestaurante(id);
		Assert.assertNotNull(restaurante.getSitiosTuristicos());
	}

	@Test
	public void testAddPlato() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		Plato plato = new Plato("Plato Test", "Descripcion Test", 10.0);
		boolean resultado = servicio.añadirPlato(id, plato);
		Assert.assertTrue(resultado);
		Restaurante restaurante = servicio.recuperarRestaurante(id);
		Assert.assertNotNull(restaurante.getPlatos());
		Assert.assertEquals(1, restaurante.getPlatos().size());
		Assert.assertEquals("Plato Test", restaurante.getPlatos().get(0).getNombre());
		Assert.assertEquals(10.0, restaurante.getPlatos().get(0).getPrecio(), 0.001);
	}

	@Test
	public void testRemovePlato() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		Plato plato = new Plato("Plato Test", "Descripcion Test", 10.0);
		servicio.añadirPlato(id, plato);
		boolean resultado = servicio.borrarPlato(id, "Plato Test");
		Assert.assertTrue(resultado);
		Restaurante restaurante = servicio.recuperarRestaurante(id);
		Assert.assertNotNull(restaurante.getPlatos());
		Assert.assertEquals(0, restaurante.getPlatos().size());
	}

	@Test
	public void testUpdatePlato() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		Plato plato = new Plato("Plato Test", "Descripcion Test", 10.0);
		servicio.añadirPlato(id, plato);
		Plato platoActualizado = new Plato("Plato Test", "Descripcion Actualizada", 20.0);
		boolean resultado = servicio.actualizarPlato(id, platoActualizado);
		Assert.assertTrue(resultado);
		Restaurante restaurante = servicio.recuperarRestaurante(id);
		Assert.assertNotNull(restaurante.getPlatos());
		Assert.assertEquals(1, restaurante.getPlatos().size());
		Assert.assertEquals("Descripcion Actualizada", restaurante.getPlatos().get(0).getDescripcion());
		Assert.assertEquals(20.0, restaurante.getPlatos().get(0).getPrecio(), 0.001);
	}

	@Test
	public void testListarRestaurantes() throws RepositorioException, EntidadNoEncontrada {
		String id1 = servicio.altaRestaurante("Restaurante 1", 10.0, 20.0, "César");
		servicio.añadirPlato(id1, new Plato("Plato Test 1", "Descripcion Test 1", 10.0));
		String id2 = servicio.altaRestaurante("Restaurante 2", 30.0, 40.0, "César");
		servicio.añadirPlato(id2, new Plato("Plato Test 2", "Descripcion Test 2", 20.0));
		List<ResumenRestaurante> restaurantes = servicio.recuperarTodosRestaurantes();
		Assert.assertEquals(2, restaurantes.size());
		Assert.assertEquals("Restaurante 1", restaurantes.get(0).getNombre());
		Assert.assertEquals(1, restaurantes.get(0).getNumeroPlatos());
		Assert.assertEquals(0, restaurantes.get(0).getNumeroSitiosTuristicos());
		Assert.assertEquals("Restaurante 2", restaurantes.get(1).getNombre());
		Assert.assertEquals(1, restaurantes.get(1).getNumeroPlatos());
		Assert.assertEquals(0, restaurantes.get(1).getNumeroSitiosTuristicos());
	}

	/* Test de funciones que lanzan excepciones */

	@Test(expected = RepositorioException.class)
	public void testAltaRestauranteNombreVacio() throws RepositorioException, EntidadNoEncontrada {
		servicio.altaRestaurante("", 10.0, 20.0, "César");
	}

	@Test(expected = RepositorioException.class)
	public void testAltaRestauranteLatitudInvalida() throws RepositorioException, EntidadNoEncontrada {
		servicio.altaRestaurante("Restaurante Test", 200.0, 20.0, "César");
	}

	@Test(expected = RepositorioException.class)
	public void testAltaRestauranteLongitudInvalida() throws RepositorioException, EntidadNoEncontrada {
		servicio.altaRestaurante("Restaurante Test", 10.0, 200.0, "César");
	}

	@Test(expected = RepositorioException.class)
	public void testActualizarRestauranteNombreVacio() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		servicio.actualizarRestaurante(id, "", 30.0, 40.0);
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testActualizarRestauranteNoEncontrado() throws EntidadNoEncontrada, RepositorioException {
		servicio.actualizarRestaurante("ID inexistente", "Nuevo Nombre", 50.0, 60.0);
	}

	@Test(expected = RepositorioException.class)
	public void testActualizarRestauranteLatitudInvalida() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		servicio.actualizarRestaurante(id, "Restaurante Actualizado", 200.0, 40.0);
	}

	@Test(expected = RepositorioException.class)
	public void testActualizarRestauranteLongitudInvalida() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		servicio.actualizarRestaurante(id, "Restaurante Actualizado", 30.0, 200.0);
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testObtenerSitiosTuristicosProximosRestauranteNoEncontrado()
			throws EntidadNoEncontrada, RepositorioException {
		servicio.obtenerSitiosTuristicosProximos("ID inexistente");
	}

	@Test(expected = RepositorioException.class)
	public void testEstablecerSitiosTuristicosDestacadosListaVacia() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		servicio.establecerSitiosTuristicosDestacados(id, new ArrayList<>());
	}

	@Test(expected = RepositorioException.class)
	public void testEstablecerSitiosTuristicosDestacadosTituloVacio() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		List<SitioTuristico> sitiosTuristicos = new ArrayList<>();
		sitiosTuristicos.add(new SitioTuristico("", "Resumen Test", null, null, null));
		servicio.establecerSitiosTuristicosDestacados(id, sitiosTuristicos);
	}

	@Test(expected = RepositorioException.class)
	public void testEstablecerSitiosTuristicosDestacadosResumenVacio()
			throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.altaRestaurante("Restaurante Test", 10.0, 20.0, "César");
		List<SitioTuristico> sitiosTuristicos = new ArrayList<>();
		sitiosTuristicos.add(new SitioTuristico("Titulo Test", "", null, null, null));
		servicio.establecerSitiosTuristicosDestacados(id, sitiosTuristicos);
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testEstablecerSitiosTuristicosDestacadosRestauranteNoEncontrado()
			throws EntidadNoEncontrada, RepositorioException {
		List<SitioTuristico> sitiosTuristicos = new ArrayList<>();
		sitiosTuristicos.add(new SitioTuristico("Titulo Test", "Resumen Test", null, null, null));
		servicio.establecerSitiosTuristicosDestacados("ID inexistente", sitiosTuristicos);
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testAñadirPlatoRestauranteNoEncontrado() throws EntidadNoEncontrada, RepositorioException {
		Plato plato = new Plato("Plato Test", "Descripción Test", 10.0);
		servicio.añadirPlato("ID inexistente", plato);
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testRemovePlatoRestauranteNoEncontrado() throws EntidadNoEncontrada, RepositorioException {
		servicio.borrarPlato("ID inexistente", "Plato Test");
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testUpdatePlatoRestauranteNoEncontrado() throws EntidadNoEncontrada, RepositorioException {
		Plato plato = new Plato("Plato Test", "Descripción Test", 10.0);
		servicio.actualizarPlato("ID inexistente", plato);
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testFindByIdRestauranteNoEncontrado() throws EntidadNoEncontrada, RepositorioException {
		servicio.recuperarRestaurante("ID inexistente");
	}

	@Test(expected = EntidadNoEncontrada.class)
	public void testDeleteRestauranteNoEncontrado() throws EntidadNoEncontrada, RepositorioException {
		servicio.borrarRestaurante("ID inexistente");
	}

}
