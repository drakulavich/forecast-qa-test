import api.ForecastServiceImpl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.LocalDate;

public class App {

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw new IllegalArgumentException("Pass city name as an argument");
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.metaweather.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            LocalDate tomorrow = LocalDate.now().plusDays(1);

            ForecastServiceImpl service = new ForecastServiceImpl(retrofit);
            System.out.println(service.getForecast(args[0], tomorrow));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }
}
