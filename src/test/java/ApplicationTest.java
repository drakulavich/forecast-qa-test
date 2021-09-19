import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import api.ForecastServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import util.PathDate;
import util.Utils;

class ApplicationTest {

    private MockWebServer mockWebServer;
    private Retrofit retrofit;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTest.class);
    private static final String CITY = "Dubai";

    @BeforeEach
    private void before() {
        LOGGER.info("Set up mock server");
        mockWebServer = new MockWebServer();

        LOGGER.info("Set up retrofit");
        retrofit = new Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @AfterEach
    private void after() throws IOException {
        LOGGER.info("Tear down mock server");
        mockWebServer.shutdown();
    }

    @Test
    void testAppReturnsForecast() throws IOException {

        LOGGER.info("Start testAppReturnsForecast test");

        final LocalDate date = LocalDate.now();
        final String expectedMessage = String.format("Weather on (%s) in %s:\nHeavy Cloud\nTemp: 17.8 °C\nWind: 6.1 mph\nHumidity: 77%%",
                new PathDate(date), CITY);

        mockWebServer.enqueue(getCityResponse());
        mockWebServer.enqueue(getForecastResponse());

        ForecastServiceImpl forecastService = new ForecastServiceImpl(retrofit);

        assertEquals(expectedMessage, forecastService.getForecast(CITY, LocalDate.now()));

        LOGGER.info("Test testAppReturnsForecast finished successfully");

    }

    @Test
    void testAppFailsDueToCityNotFound() {

        LOGGER.info("Start testAppFailsDueToCityNotFound test");

        MockResponse emptyCity = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("[]");

        mockWebServer.enqueue(emptyCity);
        ForecastServiceImpl forecastService = new ForecastServiceImpl(retrofit);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> forecastService.getForecast(CITY, LocalDate.now()),
                "Expected error that city not found"
        );

        assertEquals(String.format("Can't find city id for '%s'", CITY), thrown.getMessage());

        LOGGER.info("Test testAppFailsDueToCityNotFound finished successfully");

    }

    @Test
    void testAppFailsDueToForecastNotFound() {

        LOGGER.info("Start testAppFailsDueToForecastNotFound test");

        MockResponse weather = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("[]");

        mockWebServer.enqueue(getCityResponse());
        mockWebServer.enqueue(weather);
        ForecastServiceImpl forecastService = new ForecastServiceImpl(retrofit);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> forecastService.getForecast(CITY, LocalDate.now()),
                "Expected error that forecast not found"
        );

        assertEquals(String.format("Can't get forecast for '%s'", CITY), thrown.getMessage());

        LOGGER.info("Test testAppFailsDueToForecastNotFound finished successfully");

    }

    @Test
    void testAppFailsDueToServerError() {

        LOGGER.info("Start testAppFailsDueToServerError test");

        MockResponse emptyCity = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR);

        mockWebServer.enqueue(emptyCity);
        ForecastServiceImpl forecastService = new ForecastServiceImpl(retrofit);

        assertThrows(
                NullPointerException.class,
                () -> forecastService.getForecast(CITY, LocalDate.now()),
                "Expected NullPointerException"
        );

        LOGGER.info("Test testAppFailsDueToServerError finished successfully");

    }

    private MockResponse getCityResponse() {
        return new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(new String(Utils.readResourceFileToBytes("city_response.json"), StandardCharsets.UTF_8));
    }

    private MockResponse getForecastResponse() {
        return new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(new String(Utils.readResourceFileToBytes("forecast_response.json"), StandardCharsets.UTF_8));
    }

}