package api;

import model.City;
import model.Forecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import util.PathDate;

import java.util.List;

public interface ForecastService {

    @GET("/api/location/{city_id}/{date}/")
    Call<List<Forecast>> getForecast(@Path("city_id") Long cityId, @Path("date") PathDate date);

    @GET("/api/location/search/")
    Call<List<City>> findCityByName(@Query("query") String city);
}
