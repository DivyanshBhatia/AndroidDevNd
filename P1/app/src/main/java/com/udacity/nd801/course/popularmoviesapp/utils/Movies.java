package com.udacity.nd801.course.popularmoviesapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dnbhatia on 12/15/2016.
 * This is a custom object used to fetch movies, it is being made parceable to transfer details from one activity to another easily
 */

public class Movies implements Parcelable {
    private String originalTitle;
    private String moviePosterUrl;
    private String plotSynopsis;
    private Double userRating;
    private String releaseDate;

    public Movies(String originalTitle,String moviePosterUrl,String plotSynopsis,Double userRating,String releaseDate){
        this.originalTitle=originalTitle;
        this.moviePosterUrl=moviePosterUrl;
        this.plotSynopsis=plotSynopsis;
        this.userRating=userRating;
        this.releaseDate=releaseDate;
    }

    protected Movies(Parcel in) {
        originalTitle = in.readString();
        moviePosterUrl = in.readString();
        plotSynopsis = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(moviePosterUrl);
        dest.writeString(plotSynopsis);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString(){
        return moviePosterUrl+";"+originalTitle+";"+plotSynopsis.substring(0,10)+";"+userRating+";"+releaseDate+";";
    }
}
