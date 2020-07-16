package com.dk.imagesdownloder.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.imagesdownloder.R;
import com.dk.imagesdownloder.model.UnsplashImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int IMAGE_ALPHA_OPAQUE = 255;
    private static final int IMAGE_ALPHA_SEMI_TRANSPARENT = 100;
    private List<UnsplashImage> imageList;
    private List<AdapterItemSettings> adapterItemSettingsList;
    private OnImageClickListener onImageClickListener;
    private OnImageLongClickListener onImageLongClickListener;
    private boolean isCheckBoxesVisible;

    public ImageListAdapter(OnImageClickListener onImageClickListener,
                            OnImageLongClickListener onImageLongClickListener) {
        this.onImageClickListener = onImageClickListener;
        this.onImageLongClickListener = onImageLongClickListener;
        this.imageList = new ArrayList<>();
        this.adapterItemSettingsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ImageViewHolder(v, onImageClickListener, onImageLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        imageViewHolder.checkBox.setChecked(adapterItemSettingsList.get(position).isChecked());
        imageViewHolder.textViewProgress.setText(String.valueOf(adapterItemSettingsList.get(position).getDownloadProgress()));
        Picasso.get().load(imageList.get(position).getSmallImageUrl()).into((imageViewHolder).thumb);
        if (imageList.get(position).isDownloaded()){
            imageViewHolder.thumb.setImageAlpha(IMAGE_ALPHA_OPAQUE);
        } else {
            imageViewHolder.thumb.setImageAlpha(IMAGE_ALPHA_SEMI_TRANSPARENT);
        }
        if (adapterItemSettingsList.get(position).isDownloadStarted()) {
            showDownloadProgress(imageViewHolder);
        } else {
            hideDownloadProgress(imageViewHolder);
        }
        setCheckBoxesVisibility(imageViewHolder);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public UnsplashImage getItem(int position){
        return imageList.get(position);
    }

    public void setItems(List<UnsplashImage> imageList){
        this.imageList = imageList;
        initAdapterItemSettings(imageList.size());
        notifyDataSetChanged();
    }

    public void setAdapterItemSettingsList(List<AdapterItemSettings> adapterItemSettingsList) {
        this.adapterItemSettingsList = adapterItemSettingsList;
        notifyDataSetChanged();
    }

    public void setCheckBoxesVisibility(boolean isCheckBoxesVisible) {
        this.isCheckBoxesVisible = isCheckBoxesVisible;
        notifyDataSetChanged();
    }

    public void changeItemCheckedValue(int position){
        adapterItemSettingsList.get(position).setChecked(!adapterItemSettingsList.get(position).isChecked());
        notifyItemChanged(position);
    }

    public void setDownloadProgress(int position, int progress){
        adapterItemSettingsList.get(position).setDownloadProgress(progress);
        notifyItemChanged(position);
    }

    public void setItemDownloadStarted(int position, boolean started){
        adapterItemSettingsList.get(position).setDownloadStarted(started);
        notifyItemChanged(position);
    }

    public boolean isCheckBoxesVisible() {
        return isCheckBoxesVisible;
    }

    public List<AdapterItemSettings> getAdapterItemSettingsList() {
        return adapterItemSettingsList;
    }

    private void initAdapterItemSettings(int count){
        for (int i = 0; i < count; i++){
            AdapterItemSettings settings = new AdapterItemSettings();
            adapterItemSettingsList.add(settings);
        }
    }

    private void showDownloadProgress(ImageViewHolder imageViewHolder){
        imageViewHolder.progressBar.setVisibility(View.VISIBLE);
        imageViewHolder.textViewProgress.setVisibility(View.VISIBLE);
    }

    private void hideDownloadProgress(ImageViewHolder imageViewHolder){
        imageViewHolder.progressBar.setVisibility(View.INVISIBLE);
        imageViewHolder.textViewProgress.setVisibility(View.INVISIBLE);
    }

    private void setCheckBoxesVisibility(ImageViewHolder imageViewHolder){
        if (isCheckBoxesVisible) {
            imageViewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            imageViewHolder.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        ImageView thumb;
        CheckBox checkBox;
        ProgressBar progressBar;
        TextView textViewProgress;
        OnImageClickListener onImageClickListener;
        OnImageLongClickListener onImageLongClickListener;
        public ImageViewHolder(@NonNull View itemView, OnImageClickListener onImageClickListener,
                               OnImageLongClickListener onImageLongClickListener) {
            super(itemView);
            thumb = itemView.findViewById(R.id.imageView_thumb);
            checkBox = itemView.findViewById(R.id.checkBox);
            progressBar = itemView.findViewById(R.id.progressBar);
            textViewProgress = itemView.findViewById(R.id.textView_progress);
            this.onImageClickListener = onImageClickListener;
            this.onImageLongClickListener = onImageLongClickListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onImageLongClickListener.onImageLongClick(getAdapterPosition());
            return checkBox.getVisibility() != View.VISIBLE;
        }

        @Override
        public void onClick(View v) {
            onImageClickListener.onImageClick(getAdapterPosition());
        }
    }

    public interface OnImageClickListener{
        void onImageClick(int position);
    }

    public interface OnImageLongClickListener{
        void onImageLongClick(int position);
    }

    public static class AdapterItemSettings {
        private boolean checked;
        private boolean downloadStarted;
        private int downloadProgress;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isDownloadStarted() {
            return downloadStarted;
        }

        public void setDownloadStarted(boolean downloadStarted) {
            this.downloadStarted = downloadStarted;
        }

        public int getDownloadProgress() {
            return downloadProgress;
        }

        public void setDownloadProgress(int downloadProgress) {
            this.downloadProgress = downloadProgress;
        }
    }
}
