package com.dk.imagesdownloder.database;

import androidx.lifecycle.MutableLiveData;

import com.dk.imagesdownloder.App;
import com.dk.imagesdownloder.model.DataProvider;
import com.dk.imagesdownloder.model.UnsplashImage;

import java.util.List;

public class DBDataProvider implements DataProvider {
    private ImageDao imageDao;

    public DBDataProvider() {
        imageDao = App.getInstance().getAppDatabase().imageDao();
    }

    @Override
    public MutableLiveData<List<UnsplashImage>> loadImages(final MutableLiveData<List<UnsplashImage>> imageLiveData, final int count) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<UnsplashImage> images = imageDao.getImages(count);
                imageLiveData.postValue(images);
            }
        }).start();
        return imageLiveData;
    }
}
