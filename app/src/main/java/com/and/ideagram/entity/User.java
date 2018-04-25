package com.and.ideagram.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.and.ideagram.data.room.converters.DateTypeConverter;
import com.and.ideagram.data.room.converters.MapTypeConverter;

import java.util.Date;
import java.util.Map;

/**
 * Created by file1 on 13/03/2018.
 */

@Entity
public class User {

    @NonNull
    @PrimaryKey
    private String id;

    private String name;

    @TypeConverters({DateTypeConverter.class})
    private Date DateOfBirth;

    @ColumnInfo(name = "pic_uri")
    private String picUri;

    @TypeConverters({MapTypeConverter.class})
    private Map<String, Boolean> likedPostsId;

    @TypeConverters({MapTypeConverter.class})
    private Map<String, Boolean> writtenPostsId;

    @Ignore
    private String gender;

    @Ignore
    private String email;

    @Ignore
    private String country;


    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public void setId(String Id) {
        this.id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, Boolean> getLikedPostsId() {
        return likedPostsId;
    }

    public void setLikedPostsId(Map<String, Boolean> likedPostsId) {
        this.likedPostsId = likedPostsId;
    }

    public Map<String, Boolean> getWrittenPostsId() {
        return writtenPostsId;
    }

    public void setWrittenPostsId(Map<String, Boolean> writtenPostsId) {
        this.writtenPostsId = writtenPostsId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", DateOfBirth=" + DateOfBirth +
                ", picUri='" + picUri + '\'' +
                ", likedPostsId=" + likedPostsId +
                ", writtenPostsId=" + writtenPostsId +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
