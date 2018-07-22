package com.elisp.mymovies.Api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().build();
        long t1 = System.nanoTime();
        Log.d("Retrofit", String.format("Sending request %s on %s%n%s", request.url(), chain
                .connection(), request.headers()) + " Params " + bodyToString(request));
        Response response = chain.proceed(request);
        String responseBodyString = response.body().string();
        long t2 = System.nanoTime();
        Log.d("Retrofit", String.format("Received response for %s in %.1fms%n%s", response.request
                ().url(), (t2 - t1) / 1e6d, response.headers()) + "Body " + responseBodyString);
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(),
                responseBodyString)).build();
    }
    private String bodyToString(final Request request) {
        try {
            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        } catch (NullPointerException e) {
            return "did not work nullPointer";
        } catch (OutOfMemoryError e) {
            return "OutOfMemoryError ";
        }
    }

}
