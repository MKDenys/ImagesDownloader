package com.dk.imagesdownloder;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dk.imagesdownloder.database.LocalRepository;
import com.dk.imagesdownloder.model.DataProvider;
import com.dk.imagesdownloder.model.DataProviderFactory;
import com.dk.imagesdownloder.model.ImageDownloadTask;
import com.dk.imagesdownloder.model.ImageDownloadTaskCallbackTarget;
import com.dk.imagesdownloder.model.UnsplashImage;
import com.dk.imagesdownloder.ui.ImageListAdapter;

import java.util.List;

public class MainViewModel extends ViewModel implements ImageDownloadTaskCallbackTarget {
    private ImageListView view;
    private DataProvider dataProvider;
    private LocalRepository localRepository;
    private MutableLiveData<List<UnsplashImage>> imageLiveData;
    private List<UnsplashImage> images;
    private List<ImageListAdapter.AdapterItemSettings> adapterItemSettingsList;
    private boolean isAllCheckBoxesVisible;

    public MainViewModel() {
        super();
        DataProviderFactory dataProviderFactory = new DataProviderFactory(App.getInstance().isWifiOnAndConnected());
        dataProvider = dataProviderFactory.create();
        localRepository = new LocalRepository();
    }

    public void attachView(ImageListView view){
        this.view = view;
    }

    public void detachView(){
        this.view = null;
    }

    public MutableLiveData<List<UnsplashImage>> getImageLiveData() {
        if (imageLiveData == null){
            imageLiveData = new MutableLiveData<>();
            dataProvider.loadImages(imageLiveData, 20);
        }
        return imageLiveData;
    }

    public boolean isAllCheckBoxesVisible() {
        return isAllCheckBoxesVisible;
    }

    public void setAllCheckBoxesVisible(boolean allCheckBoxesVisible) {
        isAllCheckBoxesVisible = allCheckBoxesVisible;
    }

    public List<ImageListAdapter.AdapterItemSettings> getAdapterItemSettingsList() {
        return adapterItemSettingsList;
    }

    public void setAdapterItemSettingsList(List<ImageListAdapter.AdapterItemSettings> adapterItemSettingsList) {
        this.adapterItemSettingsList = adapterItemSettingsList;
    }

    public void setImageList(List<UnsplashImage> images) {
        this.images = images;
        view.showImageList(images);
        localRepository.replaceImages(images);
    }

    public void onSelectedToLookDownloadedImage(int position) {
        view.showLargeImage(images.get(position).getPathToDownloadedFile());
    }

    public void onStartDownloadAction(int position) {
        new ImageDownloadTask(this, position, getPathToFile(images.get(position).getId()))
                .execute(images.get(position));
    }

    @Override
    public void onDownloadStarted(int position) {
        if (view != null){
            view.showDownloadStarted(position);
        }
    }

    @Override
    public void onDownloadEnded(int position, UnsplashImage unsplashImage) {
        if (unsplashImage.isDownloaded()){
            images.get(position).setDownloaded(unsplashImage.isDownloaded());
            images.get(position).setPathToDownloadedFile(unsplashImage.getPathToDownloadedFile());
            localRepository.saveImage(unsplashImage);
            if (view != null){
                view.showDownloadSuccess(position);
            }
        } else {
            if (view != null){
                view.showDownloadError(position, R.string.download_error_message);
            }
        }
    }

    @Override
    public void showDownloadProgress(int position, int progress) {
        if (view != null){
            view.showDownloadProgress(position, progress);
        }
    }

    private String getPathToFile(String imageId){
        String splitter = "/";
        String fileExtension = ".jpeg";
        return App.getInstance().getDownloadDirectoryPath() + splitter + imageId + fileExtension;
    }
}
