package com.dk.imagesdownloder.database;

import com.dk.imagesdownloder.model.UnsplashImage;

class ReplaceImageAsyncTask extends InsertOrUpdateAsyncTask {

    @Override
    protected Void doInBackground(UnsplashImage... images) {
        imageDao.deleteAll();
        return super.doInBackground(images);
    }
}
