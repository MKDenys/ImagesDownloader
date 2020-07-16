package com.dk.imagesdownloder.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.imagesdownloder.ImageListView;
import com.dk.imagesdownloder.MainViewModel;
import com.dk.imagesdownloder.R;
import com.dk.imagesdownloder.model.UnsplashImage;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ImageListView {
    private ImageListAdapter adapter;
    private MainViewModel viewModel;
    private Button buttonDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        buttonDownload = findViewById(R.id.button_download);
        buttonDownload.setOnClickListener(downloadButtonClickListener);
        adapter = new ImageListAdapter(onImageClickListener, onImageLongClickListener);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_image_list);
        recyclerView.setLayoutManager(getCustomGridLayoutManager());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.setAllCheckBoxesVisible(adapter.isCheckBoxesVisible());
        viewModel.setAdapterItemSettingsList(adapter.getAdapterItemSettingsList());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.setCheckBoxesVisibility(viewModel.isAllCheckBoxesVisible());
        adapter.setAdapterItemSettingsList(viewModel.getAdapterItemSettingsList());
        restoreDownloadButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.attachView(this);
        viewModel.getImageLiveData().observe(this, imageObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.detachView();
    }

    @Override
    public void showImageList(List<UnsplashImage> imageList) {
        adapter.setItems(imageList);
    }

    @Override
    public void showLargeImage(String filePath){
        LargeImageFragment fragment = LargeImageFragment.newInstance(filePath);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(android.R.id.content, fragment, LargeImageFragment.TAG);
        fragmentTransaction.addToBackStack(LargeImageFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void showDownloadProgress(int position, int progress) {
        adapter.setDownloadProgress(position, progress);
    }

    @Override
    public void showDownloadStarted(int position) {
        adapter.setItemDownloadStarted(position, true);
    }

    @Override
    public void showDownloadError(int position, int stringId) {
        adapter.setItemDownloadStarted(position, false);
        Snackbar.make(findViewById(android.R.id.content),
                String.format(Locale.getDefault(), getString(stringId), position), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDownloadSuccess(int position) {
        adapter.setItemDownloadStarted(position, false);
        adapter.getItem(position).setDownloaded(true);
    }

    private Observer<List<UnsplashImage>> imageObserver = new Observer<List<UnsplashImage>>() {
        @Override
        public void onChanged(List<UnsplashImage> images) {
            viewModel.setImageList(images);
        }
    };

    private View.OnClickListener downloadButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            adapter.setCheckBoxesVisibility(false);
            buttonDownload.setEnabled(false);
            for (int i = 0; i < adapter.getItemCount(); i++){
                if (adapter.getAdapterItemSettingsList().get(i).isChecked() && !adapter.getItem(i).isDownloaded()){
                    adapter.changeItemCheckedValue(i);
                    viewModel.onStartDownloadAction(i);
                }
            }
        }
    };

    private ImageListAdapter.OnImageClickListener onImageClickListener = new ImageListAdapter.OnImageClickListener() {
        @Override
        public void onImageClick(int position) {
            if (isNothingChecked()){
                if (adapter.getItem(position).isDownloaded()){
                    viewModel.onSelectedToLookDownloadedImage(position);
                }
            } else {
                if (!adapter.getItem(position).isDownloaded()) {
                    adapter.changeItemCheckedValue(position);
                }
                if (isNothingChecked()){
                    adapter.setCheckBoxesVisibility(false);
                    buttonDownload.setEnabled(false);
                }
            }
        }
    };

    private ImageListAdapter.OnImageLongClickListener onImageLongClickListener = new ImageListAdapter.OnImageLongClickListener() {
        @Override
        public void onImageLongClick(int position) {
            if (!adapter.getItem(position).isDownloaded()) {
                if (isNothingChecked()) {
                    adapter.setCheckBoxesVisibility(true);
                    adapter.changeItemCheckedValue(position);
                    buttonDownload.setEnabled(true);
                }
            }
        }
    };

    private boolean isNothingChecked() {
        for (ImageListAdapter.AdapterItemSettings settings: adapter.getAdapterItemSettingsList()) {
            if (settings.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private void restoreDownloadButtonState(){
        for (ImageListAdapter.AdapterItemSettings settings: viewModel.getAdapterItemSettingsList()){
            if (settings.isChecked()){
                buttonDownload.setEnabled(true);
                break;
            }
        }
    }

    private GridLayoutManager getCustomGridLayoutManager(){
        final int COLUMN_COUNT_PORTRAIT = 2;
        final int COLUMN_COUNT_LANDSCAPE = 4;
        int columnCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            columnCount = COLUMN_COUNT_PORTRAIT;
        } else {
            columnCount = COLUMN_COUNT_LANDSCAPE;
        }
        return new GridLayoutManager(this, columnCount);
    }
}