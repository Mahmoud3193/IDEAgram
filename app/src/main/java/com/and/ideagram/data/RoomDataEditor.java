package com.and.ideagram.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.and.ideagram.data.room.dao.PostDAO;
import com.and.ideagram.data.room.dao.UserDAO;
import com.and.ideagram.data.room.MyRoomBase;
import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;

/**
 * Created by file1 on 09/04/2018.
 */

public class RoomDataEditor {

    private Context mContext;
    private static MyRoomBase db;


    public RoomDataEditor(Context context) {
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

    public void postInsertOrUpdate(Post p) {
        new AsyncDataSave().execute(p);
    }

    public void userInsertOrUpdate(User u) {
        new AsyncDataSave().execute(u);
    }

    public void postDelete(String postId) {
        new AsyncDataDeleteById().execute(postId);
    }

    public void userDelete(String userId) {
        new AsyncDataDeleteById().execute(userId);
    }

    public void deleteAll() {
        new AsyncDataAllDelete().execute();
    }

    private class AsyncDataSave extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... objects) {
            try {
                Post p = (Post) objects[0];
                getPostRef().updatePost(p);
                return null;
            }catch (Exception e){
                Log.i("FROM OUR LITTEL TRY", e.toString());
            }
            try {
                User u = (User) objects[0];
                getUserRef().updateUser(u);
            }catch (Exception e){
                Log.i("FROM OUR LITTEL TRY", e.toString());
            }
            return null;
        }
    }

    private class AsyncDataDeleteById extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String id = strings[0];
            getPostRef().deleteById(id);
            getUserRef().deleteById(id);
            return null;
        }
    }

    private class AsyncDataAllDelete extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getPostRef().deleteAll();
            getUserRef().deleteAll();
            return null;
        }
    }
}
