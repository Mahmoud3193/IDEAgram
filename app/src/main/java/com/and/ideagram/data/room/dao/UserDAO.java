package com.and.ideagram.data.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.and.ideagram.entity.User;

import java.util.List;

/**
 * Created by file1 on 13/03/2018.
 */

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insert(User u);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("Delete FROM user WHERE id IN (:id)")
    void deleteById(String id);

    @Query("Delete FROM user")
    void deleteAll();



    @Delete
    void delete(User user);

    @Query("SELECT * FROM user WHERE name IN (:name)")
    User getByName(String name);

    @Query("SELECT * FROM user WHERE id IN (:id)")
    User getById(String id);


}
