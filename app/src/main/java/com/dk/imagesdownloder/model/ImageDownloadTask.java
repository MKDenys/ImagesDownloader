package com.dk.imagesdownloder.model;

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageDownloadTask extends AsyncTask<UnsplashImage, Integer, UnsplashImage> {
    private static final int BUFFER_SIZE_BYTES = 10240;
    private static final String EXCEPTION_TEXT = "Response code isn't 200";
    private ImageDownloadTaskCallbackTarget callbackTarget;
    private int imagePosition;
    private String filePath;

    public ImageDownloadTask(ImageDownloadTaskCallbackTarget callbackTarget, int imagePosition, String filePath) {
        this.imagePosition = imagePosition;
        this.filePath = filePath;
        this.callbackTarget = callbackTarget;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callbackTarget.onDownloadStarted(imagePosition);
    }

    @Override
    protected UnsplashImage doInBackground(UnsplashImage... unsplashImages) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(unsplashImages[0].getLargeImageUrl());
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                int fileLength = httpsURLConnection.getContentLength();
                inputStream = httpsURLConnection.getInputStream();
                outputStream = new FileOutputStream(filePath);
                byte[] buffer = new byte[BUFFER_SIZE_BYTES];
                int totalBytesDownloaded = 0;
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    totalBytesDownloaded += bytesRead;
                    if (fileLength > 0) {
                        publishProgress(getProgressInPercents(fileLength, totalBytesDownloaded));
                    }
                    outputStream.write(buffer, 0, bytesRead);
                }
                unsplashImages[0].setDownloaded(true);
                unsplashImages[0].setPathToDownloadedFile(filePath);
            } else {
                throw new Exception(EXCEPTION_TEXT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return unsplashImages[0];
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpsURLConnection != null){
                httpsURLConnection.disconnect();
            }
        }
        return unsplashImages[0];
    }

    @Override
    protected void onPostExecute(UnsplashImage unsplashImage) {
        super.onPostExecute(unsplashImage);
        callbackTarget.onDownloadEnded(imagePosition, unsplashImage);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        callbackTarget.showDownloadProgress(imagePosition, values[0]);
    }

    private int getProgressInPercents(int fileLength, int bytesDownloaded){
        return bytesDownloaded * 100 / fileLength;
    }
}
