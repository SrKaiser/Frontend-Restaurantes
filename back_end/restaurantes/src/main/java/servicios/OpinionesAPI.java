package servicios;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OpinionesAPI {
    @POST("opiniones/registrarRecurso/{nombreRecurso}")
    Call<ResponseBody> registrarRecurso(@Path("nombreRecurso") String nombreRecurso);

    @GET("opiniones/{idOpinion}/valoraciones")
    Call<ResponseBody> obtenerValoraciones(@Path("idOpinion") String idOpinion);

}
	
