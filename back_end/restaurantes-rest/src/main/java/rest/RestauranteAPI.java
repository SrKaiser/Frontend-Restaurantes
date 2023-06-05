package rest;

import java.util.List;

import modelos.Plato;
import modelos.Restaurante;
import modelos.ResumenRestaurante;
import modelos.SitioTuristico;
import modelos.SolicitudRestaurante;
import modelos.Valoracion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestauranteAPI {

    @GET("restaurantes/{id}")
    Call<Restaurante> getRestaurante(@Path("id") String id);

    @POST("restaurantes")
    Call<ResponseBody> crearRestaurante(@Body SolicitudRestaurante restauranteRequest);

    @PUT("restaurantes/{id}/update-restaurante")
    Call<Boolean> updateRestaurante(@Path("id") String idRestaurante, @Body SolicitudRestaurante restauranteRequest);

    @GET("restaurantes/{id}/sitios-turisticos")
    Call<List<SitioTuristico>> obtenerSitiosTuristicosCercanos(@Path("id") String idRestaurante);

    @PUT("restaurantes/{id}/sitios-turisticos")
    Call<Boolean> setSitiosTuristicosDestacados(@Path("id") String idRestaurante, @Body List<SitioTuristico> sitiosTuristicos);

    @POST("restaurantes/{id}/platos")
    Call<Boolean> addPlato(@Path("id") String idRestaurante, @Body Plato plato);

    @DELETE("restaurantes/{id}/platos/{nombrePlato}")
    Call<Boolean> removePlato(@Path("id") String idRestaurante, @Path("nombrePlato") String nombrePlato);

    @PUT("restaurantes/{id}/update-plato")
    Call<Boolean> updatePlato(@Path("id") String idRestaurante, @Body Plato plato);

    @DELETE("restaurantes/{id}")
    Call<Boolean> borrarRestaurante(@Path("id") String idRestaurante);

    @GET("restaurantes")
    Call<List<ResumenRestaurante>> listarRestaurantes();
    
    @POST("restaurantes/{id}/activar-opiniones")
    Call<String> activarOpiniones(@Path("nombreRecurso") String nombreRecurso);

    @GET("/api/opiniones/{opinionId}/valoraciones")
    Call<List<Valoracion>> obtenerValoraciones(@Path("opinionId") String opinionId);


}

