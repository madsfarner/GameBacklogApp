package com.example.gamebacklog.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "game")
public class Game implements Parcelable {

    public enum STATUS {
        WANT("Want to play"),
        PLAYING("Playing"),
        STALLED("Stalled"),
        DROPPED("Dropped");

        private final String status;

        STATUS(String text){
            this.status = text;
        }

        @TypeConverter
        public String statusToString() {
            return "";
        }
    }

    public Game (String gameName, String gamePlatform, String gameStatus, String gameEdited){
        this.gameName = gameName;
        this.gamePlatform = gamePlatform;
        this.gameStatus = gameStatus;
        this.gameEdited = gameEdited;

    }

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gameName")
    private String gameName;

    @ColumnInfo(name = "gamePlatform")
    private String gamePlatform;

    @ColumnInfo(name = "gameStatus")
    private String gameStatus;

    @ColumnInfo(name = "date")
    private String gameEdited;

    protected Game(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        this.gameName = in.readString();
        this.gamePlatform = in.readString();
        //this.gameStatus = STATUS.values()[in.readInt()];
        this.gameStatus = in.readString();
        this.gameEdited = in.readString();
    }

    @Override
    public String toString() {
        return "Game: " + this.getGameName();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(this.gameName);
        dest.writeString(this.gamePlatform);
        dest.writeString(this.gameStatus);
        dest.writeString(this.gameEdited);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGamePlatform() {
        return gamePlatform;
    }

    public void setGamePlatform(String gamePlatform) {
        this.gamePlatform = gamePlatform;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameEdited() {
        return gameEdited;
    }

    public void setGameEdited(String gameEdited) {
        this.gameEdited = gameEdited;
    }
}
