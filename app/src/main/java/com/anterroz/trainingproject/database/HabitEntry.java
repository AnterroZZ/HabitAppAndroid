package com.anterroz.trainingproject.database;

import android.widget.ImageView;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "habit")
public class HabitEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "image_view_id")
    private int imageViewId;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @ColumnInfo(name = "time_in_seconds")
    private int timeInSeconds;

    @ColumnInfo(name = "category")
    private String category;


    @Ignore
    public HabitEntry(String title, int imageViewId,Date updatedAt,int timeInSeconds,String category)
    {
        this.title = title;
        this.imageViewId = imageViewId;
        this.updatedAt = updatedAt;
        this.timeInSeconds = timeInSeconds;
        this.category = category;
    }

    public HabitEntry(int id, String title, int imageViewId,Date updatedAt, int timeInSeconds,String category)
    {
        this.id = id;
        this.title = title;
        this.imageViewId = imageViewId;
        this.updatedAt = updatedAt;
        this.timeInSeconds = timeInSeconds;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImageViewId() {
        return imageViewId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setImageViewId(int imageViewId) {
        this.imageViewId = imageViewId;
    }
    public int getTimeInSeconds() {
        return timeInSeconds;
    }
    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
