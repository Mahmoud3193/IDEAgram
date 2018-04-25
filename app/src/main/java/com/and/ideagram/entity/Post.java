package com.and.ideagram.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.and.ideagram.data.room.converters.DateTypeConverter;
import com.and.ideagram.data.room.converters.MapTypeConverter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by file1 on 11/03/2018.
 */

@Entity
public class Post {

    @NonNull
    @PrimaryKey
    private String id;

    private String title;

    private String body;

    @ColumnInfo(name = "user_id")
    private String userId;

    @TypeConverters({DateTypeConverter.class})
    private Date date;

    @ColumnInfo(name = "pic_uri")
    private String picUri;

    @ColumnInfo(name = "number_of_likes")
    private long numberOfLikes;

    @TypeConverters({MapTypeConverter.class})
    private Map<String, Boolean> likersId;






    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Map<String, Boolean> getLikersId() {
        return likersId;
    }

    public void setLikersId(Map<String, Boolean> likersId) {
        this.likersId = likersId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", userId='" + userId + '\'' +
                ", date=" + date +
                ", picUri='" + picUri + '\'' +
                ", numberOfLikes=" + numberOfLikes +
                ", likersId=" + likersId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
