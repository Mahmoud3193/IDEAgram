package com.and.ideagram.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by file1 on 10/04/2018.
 */

public abstract class FireDataListener {

    private final static String TAG = "DATA-LISTENER";

    private DatabaseReference mDatabase;
    private Context mContext;
    private ChildEventListener childEventListener;
    private ValueEventListener firstPage;
    private ValueEventListener anotherPage;
    private ValueEventListener userPost;


    protected FireDataListener(Context context) {
        mContext = context;
        childEventListener = createEventListenerForData();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("post").addChildEventListener(childEventListener);
        firstPage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Post> postMap = new HashMap<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post p = postSnapshot.getValue(Post.class);
                    if(p != null) postMap.put(p.getId(), p);
                }
                getFirstPageMap(postMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        anotherPage = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Post> postMap = (Map<String, Post>) dataSnapshot.getValue();
                getAnotherPageMap(postMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        userPost = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if(u != null) {
                    getOneUser(u);
                    List<String> postIdList = new ArrayList<>(u.getWrittenPostsId().keySet());
                    for(String id : postIdList) {
                        mDatabase.child("post").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Post p = dataSnapshot.getValue(Post.class);
                                getUserPosts(p);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }


    public Context getContext() {
        return mContext;
    }

    public DatabaseReference getDatabase() {
        return mDatabase;
    }

    private ChildEventListener createEventListenerForData() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Post p = dataSnapshot.getValue(Post.class);
                onAdd(p);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                Post newP = dataSnapshot.getValue(Post.class);
                onUpdate(newP);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                String pKey = dataSnapshot.getKey();
                onDelete(pKey);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
//            Post movedp = dataSnapshot.getValue(Post.class);
//            String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load posts.",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void getFirstPage() {
        mDatabase.child("post").addValueEventListener(firstPage);
    }

    public void getAnotherPage() {
        mDatabase.child("post").addValueEventListener(anotherPage);
    }

    protected abstract void onAdd(Post p);

    protected abstract void onUpdate(Post p);

    protected abstract void onDelete(String pKey);

    protected abstract void getFirstPageMap (Map<String, Post> refreshDataMap);

    protected abstract void getAnotherPageMap (Map<String, Post> refreshDataMap);

    public void getUserPostsById(String id) { mDatabase.child("user").child(id).addListenerForSingleValueEvent(userPost); }


    protected abstract void getOneUser(User u);

    protected abstract void getUserPosts(Post p);


}







