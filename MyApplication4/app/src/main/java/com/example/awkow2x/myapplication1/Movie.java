package com.example.awkow2x.myapplication1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private int photo;
    private String name;
    private String score;
    private String year;
    private String description;
    private String img;
    private String id;
    private String type;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScore() { return score; }

    public void setScore(String score) { this.score = score; }

    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public String getImg() { return img; }

    public void setImg(String img) { this.img = img; }

    public Movie() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.photo);
        dest.writeString(this.name);
        dest.writeString(this.score);
        dest.writeString(this.year);
        dest.writeString(this.description);
        dest.writeString(this.img);
        dest.writeString(this.id);
        dest.writeString(this.type);
    }

    protected Movie(Parcel in) {
        this.photo = in.readInt();
        this.name = in.readString();
        this.score = in.readString();
        this.year = in.readString();
        this.description = in.readString();
        this.img = in.readString();
        this.id = in.readString();
        this.type = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
