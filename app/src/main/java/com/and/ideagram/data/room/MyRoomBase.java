package com.and.ideagram.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.and.ideagram.data.room.dao.PostDAO;
import com.and.ideagram.data.room.dao.UserDAO;
import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;

/**
 * Created by file1 on 13/03/2018.
 */

@Database(entities = {Post.class, User.class}, version = 1)
public abstract class MyRoomBase extends RoomDatabase {
    public abstract PostDAO postDAO();
    public abstract UserDAO userDAO();
}
