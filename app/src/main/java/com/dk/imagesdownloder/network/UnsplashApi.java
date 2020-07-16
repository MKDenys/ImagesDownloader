package com.dk.imagesdownloder.network;

import com.dk.imagesdownloder.model.UnsplashImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface UnsplashApi {
    @GET("/photos/random/?orientation=portrait")
    Call<List<UnsplashImage>> getRandomPhotos(@Query("count") int count);
}
