package com.dk.imagesdownloder.model;

import com.dk.imagesdownloder.database.DBDataProvider;
import com.dk.imagesdownloder.network.UnsplashDataProvider;

public class DataProviderFactory {
    private boolean isWiFiAvailable;

    public DataProviderFactory(boolean isWiFiAvailable) {
        this.isWiFiAvailable = isWiFiAvailable;
    }

    public DataProvider create(){
        if (isWiFiAvailable){
            return new UnsplashDataProvider();
        } else {
            return new DBDataProvider();
        }
    }
}
