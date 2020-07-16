package com.dk.imagesdownloder;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;

import androidx.room.Room;

import com.dk.imagesdownloder.database.AppDatabase;

import java.util.Objects;

public class App extends Application {
    public static App instance;

    public static App getInstance() {
        return instance;
    }

    private AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public String getDownloadDirectoryPath(){
        return Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getPath();
    }

    public boolean isWifiOnAndConnected() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        if (wifiManager.isWifiEnabled()) {
            return isWifiConnected(wifiManager);
        } else {
            return false;
        }
    }

    private boolean isWifiConnected(WifiManager wifiManager){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getIpAddress() > 0;
    }
}
