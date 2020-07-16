package com.dk.imagesdownloder;

import com.dk.imagesdownloder.model.UnsplashImage;

import java.util.List;

public interface ImageListView {
    void showImageList(List<UnsplashImage> imageList);
    void showLargeImage(String filePath);
    void showDownloadProgress(int position, int progress);
    void showDownloadStarted(int position);
    void showDownloadError(int position, int stringId);
    void showDownloadSuccess(int position);
}
