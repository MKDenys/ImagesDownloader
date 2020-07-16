package com.dk.imagesdownloder.model;

public interface ImageDownloadTaskCallbackTarget {
    void onDownloadStarted(int position);
    void onDownloadEnded(int position, UnsplashImage unsplashImage);
    void showDownloadProgress(int position, int progress);
}
