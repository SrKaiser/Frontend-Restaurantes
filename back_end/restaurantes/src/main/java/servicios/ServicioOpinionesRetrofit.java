package servicios;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import modelos.Valoracion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServicioOpinionesRetrofit implements IServicioOpiniones {
	private OpinionesAPI api;

	public ServicioOpinionesRetrofit() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:5193/api/")
				.addConverterFactory(JacksonConverterFactory.create(mapper)).build();

		api = retrofit.create(OpinionesAPI.class);
	}

	@Override
	public String registrarRecurso(String nombreRecurso) {
		Call<ResponseBody> registrarRecursoCall = api.registrarRecurso(nombreRecurso);

		try {
			Response<ResponseBody> response = registrarRecursoCall.execute();
			System.out.println(response.code());

			String responseBody = response.body().string();
			System.out.println(responseBody);

			JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();

			return jsonObject.getString("id");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public List<Valoracion> obtenerValoraciones(String idOpinion) {

		Call<ResponseBody> obtenerValoracionesCall = api.obtenerValoraciones(idOpinion);

		try {
			Response<ResponseBody> response = obtenerValoracionesCall.execute();
			System.out.println(response.code());
			String responseBody = response.body().string();
			System.out.println(responseBody);

			JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
			JsonArray jsonArray = jsonReader.readArray();
			jsonReader.close();

			List<Valoracion> valoraciones = new ArrayList<>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.getJsonObject(i);
				Valoracion valoracion = new Valoracion();

				valoracion.setCorreoElectronico(jsonObject.getString("correoElectronico"));

				String fechaStr = jsonObject.getString("fecha");
				valoracion.setFecha(fechaStr);

				valoracion.setCalificacion(jsonObject.getInt("calificacion"));

				valoracion.setComentario(jsonObject.getString("comentario"));

				valoraciones.add(valoracion);
			}
			return valoraciones;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
