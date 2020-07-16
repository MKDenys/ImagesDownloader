package com.dk.imagesdownloder.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dk.imagesdownloder.model.UnsplashImage;

import java.util.List;

@Dao
interface ImageDao {
    @Query("SELECT * FROM images LIMIT :count")
    List<UnsplashImage> getImages(int count);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateImage(UnsplashImage unsplashImage);

    @Query("DELETE FROM images")
    void deleteAll();
}
