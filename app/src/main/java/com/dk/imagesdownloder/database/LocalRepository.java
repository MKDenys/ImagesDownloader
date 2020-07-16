package com.dk.imagesdownloder.database;

import com.dk.imagesdownloder.model.UnsplashImage;

import java.util.List;

public class LocalRepository {

    public void replaceImages(List<UnsplashImage> unsplashImageList){
        UnsplashImage[] array = new UnsplashImage[unsplashImageList.size()];
        unsplashImageList.toArray(array);
        new ReplaceImageAsyncTask().execute(array);
    }

    public void saveImage(UnsplashImage unsplashImage){
        new InsertOrUpdateAsyncTask().execute(unsplashImage);
    }
}
