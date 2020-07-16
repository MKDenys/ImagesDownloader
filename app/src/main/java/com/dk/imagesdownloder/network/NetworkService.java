package com.dk.imagesdownloder.network;

import com.dk.imagesdownloder.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class NetworkService {
    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String AUTHORIZATION_PARAMETER_VALUE = "Client-ID " + BuildConfig.UNSPLASH_API_KEY;
    private static final String BASE_URL = "https://api.unsplash.com/";
    private static NetworkService instance;
    private Retrofit retrofit;

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }

    private NetworkService() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader(AUTHORIZATION_PARAMETER_NAME, AUTHORIZATION_PARAMETER_VALUE).build();
                return chain.proceed(request);
            }
        };

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public UnsplashApi getApi() {
        return retrofit.create(UnsplashApi.class);
    }
}
