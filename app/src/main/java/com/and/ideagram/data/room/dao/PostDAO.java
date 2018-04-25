package com.and.ideagram.data.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.and.ideagram.entity.Post;

import java.util.List;

/**
 * Created by file1 on 13/03/2018.
 */

@Dao
public interface PostDAO {

    @Query("SELECT * FROM post")
    List<Post> getAll();

    @Insert
    void insert(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updatePost(Post post);

    @Query("Delete FROM post WHERE id IN (:id)")
    void deleteById(String id);

    @Query("Delete FROM post")
    void deleteAll();

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM post WHERE user_id IN (:userId)")
    List<Post> getPostsByUser(String userId);

    @Query("SELECT * FROM post WHERE id IN (:id)")
    Post getById(String id);
}
