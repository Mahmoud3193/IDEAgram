package com.and.ideagram.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.and.ideagram.data.room.MyRoomBase;
import com.and.ideagram.data.room.dao.PostDAO;
import com.and.ideagram.data.room.dao.UserDAO;
import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;

import java.util.List;

/**
 * Created by file1 on 13/04/2018.
 */

public abstract class RoomDataGetter {

    private Context mContext;
    private static MyRoomBase db;


    protected RoomDataGetter(Context context) {
        mContext =context;
        db = Room.databaseBuilder(getContext(), MyRoomBase.class, "my_data").build();
    }


    private MyRoomBase getDb() {
        return db;
    }

    private Context getContext() {
        return mContext;
    }

    private PostDAO getPostRef() {
        return db.postDAO();
    }

    private UserDAO getUserRef() {
        return db.userDAO();
    }

    public void getAllPosts() {
        new AsyncDataGetAllPosts().execute();
    }

    public void getUserById(String id) {
        new AsyncDataGetUserById().execute(id);
    }

    public abstract void returnAllPosts(Object o);

    protected abstract void returnUserById(Object o);


    private class AsyncDataGetAllPosts extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... voids) {
            return getPostRef().getAll();
        }

        @Override
        protected void onPostExecute(Object o) {
            returnAllPosts(o);
        }
    }

    private class AsyncDataGetUserById extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            return getUserRef().getById(strings[0]);
        }

        @Override
        protected void onPostExecute(Object o) {
            returnUserById(o);
        }
    }


}
