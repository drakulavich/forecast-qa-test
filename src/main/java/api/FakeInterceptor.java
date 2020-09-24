package api;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class FakeInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        // do not change response in case not matched
        return chain.proceed(chain.request());
    }
}
