package com.dk.imagesdownloder.model;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface DataProvider {
    MutableLiveData<List<UnsplashImage>>
    loadImages(final MutableLiveData<List<UnsplashImage>> imageLiveData, int count);
}
