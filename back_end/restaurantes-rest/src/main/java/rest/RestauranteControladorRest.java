package rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import excepciones.EntidadNoEncontrada;
import excepciones.RepositorioException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import modelos.SolicitudRestaurante;
import modelos.Valoracion;
import rest.seguridad.AvailableRoles;
import rest.seguridad.Secured;
import servicios.FactoriaServicios;
import servicios.IServicioRestaurante;



@Api
@Path("restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteControladorRest {
	
	private IServicioRestaurante servicioRestaurante = FactoriaServicios.getServicio(IServicioRestaurante.class);
	@Context
	private UriInfo uriInfo;
	@Context
	private SecurityContext securityContext;
	@Context
    private HttpServletRequest request;
    
    @GET
    @Path("/{id}")
    @Secured({AvailableRoles.GESTOR, AvailableRoles.CLIENTE})
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera un restaurante por ID", response = Restaurante.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Restaurante recuperado correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "ID no válido")
    })
    // curl -i -X GET http://localhost:8080/api/restaurantes/ID_DEL_RESTAURANTE
    // curl -i -X POST -H "Authorization: Bearer %JWT%" -H "Content-Type: application/json" -d "{\"nombre\": \"NuevoNombre\", \"latitud\": 40.123456, \"longitud\": -3.654321}" http://localhost:8090/restaurantes
    public Response obtenerRestaurante(@ApiParam(value = "ID del restaurante a recuperar", required = true) @PathParam("id") String id) {
        try {
			return Response.status(Response.Status.OK).entity(servicioRestaurante.recuperarRestaurante(id)).build();
		} catch (RepositorioException e) {
	        return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
	    } catch (EntidadNoEncontrada e) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
	    }
    }
    

    @POST
    @Secured(AvailableRoles.GESTOR)
    @ApiOperation(value = "Añade un nuevo restaurante", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Restaurante creado correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parámetros no válidos")
    })
    @Consumes(MediaType.APPLICATION_JSON) 
    // curl -i -X POST -H "Content-Type: application/json" -d "{\"nombre\": \"Burger\", \"latitud\": 40.42039145624014, \"longitud\": -3.6996503622016954}" http://localhost:8080/api/restaurantes
    // curl -i -X POST -H "Authorization: Bearer %JWT%" -H "Content-Type: application/json" -d "{\"nombre\": \"NuevoNombre\", \"latitud\": 40.123456, \"longitud\": -3.654321}" http://localhost:8090/restaurantes
    public Response crearRestaurante(SolicitudRestaurante nuevoRestaurante){
    	String nombre = nuevoRestaurante.getNombre();
        double latitud = nuevoRestaurante.getLatitud();
        double longitud = nuevoRestaurante.getLongitud();
        String ciudad = nuevoRestaurante.getCiudad();
        String fecha = nuevoRestaurante.getFecha();
    	
        String idGestor;
        if (this.securityContext.getUserPrincipal() == null)
        	idGestor = null;
        else idGestor = this.securityContext.getUserPrincipal().getName();
        String id;
        try {
            id = servicioRestaurante.altaRestaurante(nombre, latitud, longitud, ciudad, fecha, idGestor);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Solicitud incorrecta").build();
        }
        
    	// "X-Forwarded-Host" contiene la URL original de la pasarela
        String pasarelaUrl = request.getHeader("X-Forwarded-Host");

        // URL del nuevo restaurante utilizando la URL de la pasarela
        String restauranteUrl = "http://" + pasarelaUrl + "/restaurantes/" + id;

        return Response.status(Response.Status.CREATED).entity(restauranteUrl).build();
    }
    
    @PUT
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}/update-restaurante")
    @ApiOperation(value = "Actualiza un restaurante por ID", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Restaurante actualizado correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parámetros no válidos"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    // curl -i -X PUT -H "Content-Type: application/json" -d '{"nombre": "NuevoNombre", "latitud": 40.123456, "longitud": -3.654321}' http://localhost:8080/api/restaurantes/ID_DEL_RESTAURANTE/update-restaurante
    // curl -i -X PUT -H "Authorization: Bearer %JWT%" -H "Content-Type: application/json" -d "{\"nombre\": \"Burger\", \"latitud\": 40.42039145624014, \"longitud\": -3.6996503622016954}" http://localhost:8090/restaurantes/ID_DEL_RESTAURANTE/update-restaurante
    public Response updateRestaurante(
        @ApiParam(value = "ID del restaurante a actualizar", required = true) @PathParam("id") String id,
        SolicitudRestaurante actualizacionRestaurante) {

        String nombre = actualizacionRestaurante.getNombre();
        double latitud = actualizacionRestaurante.getLatitud();
        double longitud = actualizacionRestaurante.getLongitud();
        String ciudad = actualizacionRestaurante.getCiudad();
        String fecha = actualizacionRestaurante.getFecha();

        boolean updated;
		try {
			updated = servicioRestaurante.actualizarRestaurante(id, nombre, latitud, longitud, ciudad, fecha);
			return Response.status(Response.Status.OK).entity(updated).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		} catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parámetros no válidos").build();
        }
    }
    
    @GET
    @Secured({AvailableRoles.GESTOR, AvailableRoles.CLIENTE})
    @Path("/{id}/sitios-turisticos")
    @ApiOperation(value = "Recupera los sitios turísticos cercanos a un restaurante", response = SitioTuristico.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Sitios turísticos recuperados correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "ID no válido")
    })
    // curl -i -X GET http://localhost:8080/api/restaurantes/ID_DEL_RESTAURANTE/sitios-turisticos?radius=VALOR_RADIO&maxRows=VALOR_MAXROWS
    // curl -i -X GET -H "Authorization: Bearer %JWT%" http://localhost:8090/restaurantes/ID_DEL_RESTAURANTE/sitios-turisticos
    public Response obtenerSitiosTuristicosCercanos(@ApiParam(value = "ID del restaurante para buscar sitios turísticos cercanos", required = true) @PathParam("id") String idRestaurante,  @QueryParam("radio") Integer radius, @QueryParam("maxRows") Integer maxRows) {
        
    	List<SitioTuristico> sitiosTuristicos;
		try {
			sitiosTuristicos = servicioRestaurante.obtenerSitiosTuristicosProximos(idRestaurante, radius, maxRows);
			 return Response.ok(sitiosTuristicos).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		}
    }
    
    @PUT
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}/sitios-turisticos")
    @ApiOperation(value = "Establece los sitios turísticos destacados de un restaurante", response = Boolean.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Sitios turísticos destacados actualizados correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "ID no válido")
        
    })
    @Consumes(MediaType.APPLICATION_JSON)
    // curl -i -X PUT -H "Content-Type: application/json" -d '[{"titulo": "Titulo1", "resumen": "Resumen1", "categorias": ["Categoria1"], "enlaces": ["Enlace1"], "imagenes": ["Imagen1"]}]' http://localhost:8080/api/restaurantes/ID_DEL_RESTAURANTE/sitios-turisticos
    public Response setSitiosTuristicosDestacados(@ApiParam(value = "ID del restaurante", required = true) @PathParam("id") String idRestaurante,
                                                  @ApiParam(value = "Lista de sitios turísticos destacados", required = true) List<SitioTuristico> sitiosTuristicos) {
        
    	boolean resultado;
		try {
			resultado = servicioRestaurante.establecerSitiosTuristicosDestacados(idRestaurante, sitiosTuristicos);
			return Response.ok(resultado).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		}    
    }

    @POST
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}/platos")
    @ApiOperation(value = "Agrega un plato a un restaurante", response = Boolean.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Plato agregado correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parámetros no válidos")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    // curl -i -X POST -H "Content-Type: application/json" -d '{"nombre": "Plato1", "descripcion": "Descripcion1", "precio": 10.0}' http://localhost:8080/api/restaurantes/647eddecab0552796934fabc/platos
    // curl -i -X POST -H "Content-Type: application/json" -d "{\"nombre\": \"Plato1\", \"descripcion\": \"Descripcion1\", \"precio\": 10.0}" http://localhost:8080/api/restaurantes/647eddecab0552796934fabc/platos
    // curl -i -X POST -H "Authorization: Bearer %JWT%" -H "Content-Type: application/json" -d "{\"nombre\": \"Plato1\", \"descripcion\": \"Descripcion1\", \"precio\": 10.0}" http://localhost:8090/restaurantes/ID_DEL_RESTAURANTE/platos
    public Response addPlato(@ApiParam(value = "ID del restaurante", required = true) @PathParam("id") String idRestaurante,
                             @ApiParam(value = "Plato a agregar", required = true) Plato plato) {
        boolean resultado;
		try {
			resultado = servicioRestaurante.añadirPlato(idRestaurante, plato);
			return Response.ok(resultado).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		} catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parámetros no válidos").build();
        }
    }
    
    @DELETE
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}/platos/{nombrePlato}")
    @ApiOperation(value = "Elimina un plato de un restaurante", response = Boolean.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Plato eliminado correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parámetros no válidos")
    })
    // curl -i -X DELETE http://localhost:8080/api/restaurantes/647eddecab0552796934fabc/platos/Cottotn Cot
    // curl -i -X DELETE -H "Authorization: Bearer %JWT%" http://localhost:8090/restaurantes/6466e57672b4e20cddb3e8a3/platos/Plato1
    public Response removePlato(@ApiParam(value = "ID del restaurante", required = true) @PathParam("id") String idRestaurante,
                                @ApiParam(value = "Nombre del plato a eliminar", required = true) @PathParam("nombrePlato") String nombrePlato) {
        
    	String nombrePlatoSinGuionesBajos = nombrePlato.replace("_", " ");
    	System.out.println(nombrePlatoSinGuionesBajos);
    	boolean resultado;
		try {
			resultado = servicioRestaurante.borrarPlato(idRestaurante, nombrePlatoSinGuionesBajos);
			return Response.ok(resultado).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		} catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parámetros no válidos").build();
        }
    }
    
    @PUT
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}/update-plato")
    @ApiOperation(value = "Actualiza un plato de un restaurante", response = Boolean.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Plato actualizado correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante o plato no encontrado"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parámetros no válidos")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    // curl -i -X PUT -H "Content-Type: application/json" -d '{"nombre": "nombre", "descripcion": "NuevaDescripcion", "precio": NuevoPrecio}' http://localhost:8080/api/restaurantes/ID_DEL_RESTAURANTE/update-plato
    public Response updatePlato(@ApiParam(value = "ID del restaurante", required = true) @PathParam("id") String idRestaurante,
                                Plato plato) {
        boolean resultado;
		try {
			resultado = servicioRestaurante.actualizarPlato(idRestaurante, plato);
			return Response.ok(resultado).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		} catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Parámetros no válidos").build();
        }
        
    }

    @DELETE
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}")
    @ApiOperation(value = "Elimina un restaurante por ID", response = Boolean.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Restaurante eliminado correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "ID no válido")
    })
    // curl -i -X DELETE http://localhost:8080/api/restaurantes/ID_DEL_RESTAURANTE
    // curl -i -X DELETE -H "Authorization: Bearer %JWT%" http://localhost:8090/restaurantes/6466e57672b4e20cddb3e8a3
    public Response borrarRestaurante(@ApiParam(value = "ID del restaurante a eliminar", required = true) @PathParam("id") String idRestaurante) {
        
    	boolean resultado;
		try {
			resultado = servicioRestaurante.borrarRestaurante(idRestaurante);
			return Response.ok(resultado).build();
		} catch (RepositorioException e) {
	        return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
	    } catch (EntidadNoEncontrada e) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
	    }
        
    }
    
    @GET
    @Secured({AvailableRoles.GESTOR, AvailableRoles.CLIENTE})
    @ApiOperation(value = "Lista todos los restaurantes", response = ResumenRestaurante.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Listado de restaurantes generado correctamente")
    })
    // curl -i -X GET http://localhost:8080/api/restaurantes
    // curl -i -X GET -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMGM5MTQ3OS03Njg1LTQ4NDYtODRlYy03YzRiNDI2NDUxZGQiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNjg0NTgzOTU2LCJzdWIiOiJTckthaXNlciIsInVzdWFyaW8iOiJjZXNhci5wYWdhbnZpbGxhZmFuZUBnbWFpbC5jb20iLCJyb2wiOiJHRVNUT1IifQ.yrcnkQGhN4PkwBtlIWlA5oHU3dGn-RF_MBKHIY4bCD4" http://localhost:8090/restaurantes
    public Response listarRestaurantes() {
        List<ResumenRestaurante> restaurantesList = servicioRestaurante.recuperarTodosRestaurantes();
        
        return Response.ok(restaurantesList).build();
    }
    
    @PUT
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}/activar-opiniones")
    @ApiOperation(value = "Activa las opiniones para un restaurante por ID", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Opiniones para el restaurante activadas correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "ID no válido"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado")
    })
    // curl -i -X PUT -H "Content-Type: application/json" http://localhost:8080/api/restaurantes/647cd25af2506e72a25fdce5/activar-opiniones
    public Response activarOpiniones(
        @ApiParam(value = "ID del restaurante para activar opiniones", required = true) @PathParam("id") String id)  {

        String idOpinion;
		try {
			idOpinion = servicioRestaurante.activarOpiniones(id);
			return Response.status(Response.Status.OK).entity(idOpinion).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		}
    }
    

	@GET
    @Secured(AvailableRoles.GESTOR)
    @Path("/{id}/valoraciones")
    @ApiOperation(value = "Recupera todas las valoraciones para un restaurante por ID", response = Valoracion.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = HttpServletResponse.SC_OK, message = "Valoraciones recuperadas correctamente"),
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "ID no válido"),
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado")
    })
    // curl -i -X GET -H "Content-Type: application/json" http://localhost:8080/api/restaurantes/6466e57672b4e20cddb3e8a3/valoraciones
    public Response recuperarTodasValoraciones(
        @ApiParam(value = "ID del restaurante para recuperar valoraciones", required = true) @PathParam("id") String id) {
      
       List<Valoracion> valoraciones;
		try {
			valoraciones = servicioRestaurante.recuperarTodasValoraciones(id);
			 return Response.status(Response.Status.OK).entity(valoraciones).build();
		} catch (RepositorioException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("ID no válido").build();
		} catch (EntidadNoEncontrada e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Restaurante no encontrado").build();
		}
            
    }

}
