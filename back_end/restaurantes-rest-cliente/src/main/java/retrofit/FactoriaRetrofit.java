package retrofit;

import rest.RestauranteAPI;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FactoriaRetrofit {
	
	private static final String BASE_URL = "http://localhost:8080/api/";
    private static RestauranteAPI api;

    public static RestauranteAPI getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            api = retrofit.create(RestauranteAPI.class);
        }
        return api;
    }
}
