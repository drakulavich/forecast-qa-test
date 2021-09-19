package api;

import model.City;
import model.Forecast;
import retrofit2.Call;
import retrofit2.Retrofit;
import util.PathDate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ForecastServiceImpl {

    private final Retrofit retrofit;

    public ForecastServiceImpl(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public String getForecast(String cityName, LocalDate date) throws IOException {
        PathDate pathDate = new PathDate(date);

        ForecastService service = retrofit.create(ForecastService.class);
        Call<List<City>> findCityCall = service.findCityByName(cityName.toLowerCase());
        City city = null;

        Call<List<Forecast>> forecastCall = service.getForecast(city.getWoeid(), pathDate);
        Forecast forecast = Objects.requireNonNull(forecastCall.execute().body())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Can't get forecast for '%s'", cityName)));

        return String.format("Weather on (%s) in %s:\n%s", pathDate, city.getTitle(), forecast);
    }
}
