package com.dk.imagesdownloder.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

@Entity(tableName = "images")
public class UnsplashImage {
    private static final String RAW_IMAGE_KEY = "raw";
    private static final String THUMB_IMAGE_KEY = "thumb";
    private static final int LARGE_IMAGE_WIDTH = 3264;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @Ignore
    @SerializedName("urls")
    private Map<String, String> urls;

    @ColumnInfo(name = "small_image_url")
    private String smallImageUrl;

    @ColumnInfo(name = "large_image_url")
    private String largeImageUrl;

    @ColumnInfo(name = "downloaded")
    private boolean downloaded;

    @ColumnInfo(name = "path_to_downloaded_file")
    private String pathToDownloadedFile;

    @NonNull
    public String getId() {
        return id;
    }

    public String getSmallImageUrl(){
        if (smallImageUrl == null){
            smallImageUrl = urls.get(THUMB_IMAGE_KEY);
        }
        return smallImageUrl;
    }

    public String getLargeImageUrl(){
        if (largeImageUrl == null) {
            largeImageUrl = getUrlWithWidthParameter(urls.get(RAW_IMAGE_KEY));
        }
        return largeImageUrl;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public String getPathToDownloadedFile() {
        return pathToDownloadedFile;
    }

    public void setPathToDownloadedFile(String pathToDownloadedFile) {
        this.pathToDownloadedFile = pathToDownloadedFile;
    }

    private String getUrlWithWidthParameter(String baseUrl){
        String ampersand = "&";
        String urlParameterImageWidth = "w="; //https://unsplash.com/documentation#supported-parameters
        return baseUrl + ampersand + urlParameterImageWidth + LARGE_IMAGE_WIDTH;
    }
}
