import api.ForecastServiceImpl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Pass city name as an argument");
            System.exit(1);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.metaweather.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        ForecastServiceImpl service = new ForecastServiceImpl(retrofit);
        System.out.println(service.getForecast(args[0], tomorrow));
    }
}
