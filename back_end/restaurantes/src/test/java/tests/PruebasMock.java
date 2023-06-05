package tests;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import modelos.Restaurante;
import modelos.Valoracion;
import repositorios.IRepositorioRestaurante;
import servicios.IServicioOpiniones;
import servicios.ServicioRestaurante;

class PruebasMock {
	private ServicioRestaurante servicio;
	private IServicioOpiniones servicioOpiniones;
	private IRepositorioRestaurante repositorioRestaurante;

	@BeforeEach
	public void setUp() throws Exception {
		PruebasBasicas.isTestEnvironment = true;
		servicioOpiniones = Mockito.mock(IServicioOpiniones.class);
		repositorioRestaurante = Mockito.mock(IRepositorioRestaurante.class);
		servicio = new ServicioRestaurante();
	    servicio.setRepositorioRestaurante(repositorioRestaurante);
	    servicio.setServicioOpiniones(servicioOpiniones);
	}

	@Test
	public void activarOpinionesTest() throws RepositorioException, EntidadNoEncontrada {
		// Organizar
		String idRestaurante = "123";
		String nombre = "Test";
		Restaurante restauranteMock = new Restaurante();
		restauranteMock.setNombre(nombre);
		when(repositorioRestaurante.findById(idRestaurante)).thenReturn(restauranteMock);
		when(servicioOpiniones.registrarRecurso(nombre)).thenReturn(nombre);
		when(repositorioRestaurante.updateOpinion(anyString(), anyString(), anyInt(), anyDouble())).thenReturn(true);

		// Actuar
		String resultado = servicio.activarOpiniones(idRestaurante);

		// Afirmar
		Assert.assertNotNull(resultado);
		Assert.assertEquals(nombre, resultado);

	}

	@Test
	public void recuperarTodasValoracionesTest() throws RepositorioException, EntidadNoEncontrada {
	    // Organizar
	    String idRestaurante = "123";
	    Restaurante restauranteMock = new Restaurante();
	    restauranteMock.setOpinionId("456"); // Supongamos que el ID de la opinión es "456"
	    List<Valoracion> valoraciones = new ArrayList<>();
	    valoraciones.add(new Valoracion()); // Asume que Valoracion es tu clase de entidad
	    when(repositorioRestaurante.findById(anyString())).thenReturn(restauranteMock);
	    when(servicioOpiniones.obtenerValoraciones("456")).thenReturn(valoraciones); // Pasamos "456" como ID de opinión

	    // Actuar
	    List<Valoracion> resultado = servicio.recuperarTodasValoraciones(idRestaurante);

	    // Afirmar
	    Assert.assertNotNull(resultado);
	    Assert.assertEquals(valoraciones.size(), resultado.size());
	    Assert.assertEquals(valoraciones, resultado);
	}



}
