package com.dk.imagesdownloder.database;

import android.os.AsyncTask;

import com.dk.imagesdownloder.App;
import com.dk.imagesdownloder.model.UnsplashImage;

class InsertOrUpdateAsyncTask extends AsyncTask<UnsplashImage, Void, Void> {
    protected ImageDao imageDao;

    public InsertOrUpdateAsyncTask() {
        imageDao = App.getInstance().getAppDatabase().imageDao();
    }

    @Override
    protected Void doInBackground(UnsplashImage... images) {
        for (UnsplashImage image: images){
            imageDao.insertOrUpdateImage(image);
        }
        return null;
    }
}
