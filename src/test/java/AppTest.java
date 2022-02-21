import api.FakeInterceptor;
	import api.ForecastServiceImpl;
	import model.Forecast;
	import okhttp3.OkHttpClient;
	import okhttp3.mockwebserver.MockResponse;
	import okhttp3.mockwebserver.MockWebServer;
	import org.junit.jupiter.api.*;
	import org.junit.platform.commons.logging.Logger;
	import org.junit.platform.commons.logging.LoggerFactory;
	import retrofit2.Retrofit;
	import retrofit2.converter.gson.GsonConverterFactory;
	import util.PathDate;
	import java.io.IOException;
	import java.lang.System.Logger.Level;
	import java.time.LocalDate;


	import static org.junit.jupiter.api.Assertions.assertEquals;

	class AppTest {

		 	String[] actualCities = {"Dubai"};
		    String[] fakeCities = {"hyderabad","*%$"};
		    static String body= "[{\"title\":\"MockWebServer\", \"location_type\":\"City\", \"woeid\":2487956, \"latt_long\":\"37.777119, -122.41964\"}]";
		    private static final Logger LOG = LoggerFactory.getLogger(AppTest.class);
		
		    String city;
		    private static ForecastServiceImpl service ;
		    LocalDate tomorrow = LocalDate.now().plusDays(1);
		   
		   
		    static MockWebServer server = new MockWebServer();



		    @BeforeAll
		    public static void setup() throws IOException {

		        LOG.info(() -> "****Setup Initiated*****");
		        
		        server.enqueue(new MockResponse().setBody(body));
		        System.out.println(body);
		        server.start();
		        final OkHttpClient httpClient = new OkHttpClient.Builder().addNetworkInterceptor(new FakeInterceptor()).build();
		        Retrofit retrofit = new Retrofit.Builder().baseUrl(server.url("/")).addConverterFactory(GsonConverterFactory.create()).client(httpClient).build();
		        service = new ForecastServiceImpl(retrofit);
		        
		        LOG.info(() -> "***Setup Completed****");

		    }

		    


		    @Test
		    @DisplayName("Test actual city forecast")
		    void testActualCityForecast() throws IOException {

		        LOG.info(() -> "Test actual city forecast :INITIATED");

		        try {

		        	for(int index=0;index<actualCities.length;index++) {
		        	city = actualCities[index];
		        	System.out.println(tomorrow);
		            String res = service.getForecast(city,tomorrow);
		            System.out.println("****************----->"+res);

		            String [] actualForecast  = res.split("\\R");

		            PathDate pathDate = new PathDate(tomorrow);

		            Forecast expectedForecast  = new Forecast();
		            expectedForecast.setId(12345666888000L);
		            expectedForecast.setWeatherState("Rain");
		            expectedForecast.setTemperature(67.9);
		            expectedForecast.setWindSpeed(23.0);
		            expectedForecast.setHumidity(50);
		            assertEquals(String.format("Weather on (%s) is"+city +"---->", pathDate), actualForecast[0]);
		            assertEquals(expectedForecast.getWeatherState(), actualForecast[1]);
		            assertEquals(String.format("Temp in"+city+" ---->%.1f °C",expectedForecast.getTemperature() ) , actualForecast[2]);
		            assertEquals(String.format("Speed of wind is----> %.1f",expectedForecast.getWindSpeed() ) , actualForecast[3]);
		            assertEquals(String.format("Humidity is around----> %d%%",expectedForecast.getHumidity() ) , actualForecast[4]);
		        	//}
		            LOG.info(() -> "Test actual city forecast :COMPLETED");


		        }} catch (Exception e) {
		            LOG.error( () ->  String.format(e.getMessage()));
		            System.exit(1);
		        }
		    }


		    @Test
		    @DisplayName("City Name is not valid Case")
		    @Disabled
		    void testFakeCityForecastOutput() throws IOException {

		        LOG.info(() -> "Verify Non Valid City :INITIATED");
		        for(int index=0;index<fakeCities.length;index++) {
		        try {
		        		city =  fakeCities[index];
		        		String res = service.getForecast(city,tomorrow);
		                System.out.println(res);


		        } catch (Exception e) {
		        	System.out.println(e.getMessage());
		            assertEquals(String.format("Can't find city id for '%s'", city), e.getMessage());
		        }
		      
		          
		            LOG.info(() -> "Verify non existent city forecast :COMPLETED");

		        }

		    }

		   
		    @AfterAll
		    public static void tearDown() throws IOException {
		        server.shutdown();
		    }


	}