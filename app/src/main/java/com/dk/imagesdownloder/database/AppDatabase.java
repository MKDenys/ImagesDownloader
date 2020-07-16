package com.dk.imagesdownloder.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dk.imagesdownloder.model.UnsplashImage;

@Database(entities = {UnsplashImage.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static String DATABASE_NAME = "database";
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE images ADD COLUMN downloaded BOOLEAN DEFAULT 0 NOT NULL");
            database.execSQL("ALTER TABLE images ADD COLUMN path_to_downloaded_file STRING");
        }
    };

    public abstract ImageDao imageDao();
}
