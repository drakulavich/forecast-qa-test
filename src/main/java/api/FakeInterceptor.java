package api;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import util.PathDate;
import util.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class FakeInterceptor implements Interceptor {
	ResponseBody cityBody;
    ResponseBody forecastBody;
    
    String [] actualCities = {"Dubai"};
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    PathDate pathDate = new PathDate(tomorrow);
	
	
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
    	Request req = chain.request();
        Response res = chain.proceed(req);
        Boolean search_endpoint = chain.request().url().uri().getPath().endsWith("location/search/");
        Boolean forecast_endpoint = chain.request().url().uri().getPath().endsWith(pathDate + "/");
        System.out.println("date: " + pathDate + "/");

        System.out.println("Request URi: " + chain.request().url().uri());
        System.out.println("search endpoint?: " + chain.request().url().uri().getPath().endsWith("location/search/"));
        System.out.println("forecast endpoint?: " + chain.request().url().uri().getPath().endsWith(pathDate + "/"));
      

        // Case for search endpoint
        if (search_endpoint){
            String searchQuery = chain.request().url().uri().getQuery().split("=")[1];
            // Condition for valid city
           if (Arrays.asList(actualCities).contains(searchQuery)) {
        	   cityBody = ResponseBody.create(MediaType.parse("application/json"), Utils.readResourceFileToBytes("validFakeCity.json"));
              
           }

            else {
        
            	 // Condition for in valid city
            	  cityBody = ResponseBody.create(MediaType.parse("application/json"), Utils.readResourceFileToBytes("inValidCity.json"));
                
            }


            return res.newBuilder().body(cityBody)
                    .message("OK")
                    .protocol(Protocol.HTTP_2)
                    .code(200)
                    //.request(request)
                    .build();
        }
        else {
            if (forecast_endpoint){
               
                // For no forecast City condition for a valid city , (hardcoded hyderabad value)
                if (chain.request().url().uri().getPath().endsWith("2295414/" + pathDate + "/")){
                    forecastBody = ResponseBody.create(MediaType.parse("application/json"), Utils.readResourceFileToBytes("noForecastCity.json"));
                }
                // Valid city with forecast 
                else {
                    
                    forecastBody = ResponseBody.create(MediaType.parse("application/json"), Utils.readResourceFileToBytes("fakeForcast.json"));
                }


                // Return forecast response

                return res.newBuilder().body(forecastBody)
                        .message("")
                        .protocol(Protocol.HTTP_2)
                        .code(200)
                        .build();

            }
        
        
        
        // do not change response in case not matched
        return chain.proceed(chain.request());
    }
}
}
