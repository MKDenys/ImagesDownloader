package com.dk.imagesdownloder.network;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dk.imagesdownloder.model.DataProvider;
import com.dk.imagesdownloder.model.UnsplashImage;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsplashDataProvider implements DataProvider {
    private static final int MAX_TRY_COUNT = 3;
    private int tryCounter;

    @Override
    public MutableLiveData<List<UnsplashImage>> loadImages(final MutableLiveData<List<UnsplashImage>> imageLiveData, final int count) {
        NetworkService.getInstance().getApi().getRandomPhotos(count).enqueue(new Callback<List<UnsplashImage>>() {
            @Override
            public void onResponse(@NonNull Call<List<UnsplashImage>> call, @NonNull Response<List<UnsplashImage>> response) {
                if (response.isSuccessful()){
                    imageLiveData.postValue(response.body());
                    tryCounter = 0;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UnsplashImage>> call, @NonNull Throwable t) {
                tryCounter++;
                if (tryCounter < MAX_TRY_COUNT){
                    loadImages(imageLiveData, count);
                }
                imageLiveData.postValue(Collections.<UnsplashImage>emptyList());
            }
        });
        return imageLiveData;
    }
}
