package com.and.ideagram.data;

import android.content.Context;
import android.widget.Toast;

import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by file1 on 12/04/2018.
 */

public class FireDataEditor {

    private final static String TAG = "DATA-EDITOR";


    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private Context mContext;


    public FireDataEditor(Context context) {
        mContext = context;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private Context getContext() {
        return mContext;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void addPost(String title, String body, String picUri) {
        if(title != null || body != null) {
            Post post = new Post();
            post.setTitle(title);
            post.setBody(body);
            post.setUserId(currentUser.getUid());
            post.setPicUri(picUri);
            post.setNumberOfLikes(0);
            post.setLikersId(new HashMap<String, Boolean>());
            post.setDate(new Date());
            post.setId(mDatabase.child("post").push().getKey());
            mDatabase.child("post").child(post.getId()).setValue(post);
            mDatabase.child("user").child(currentUser.getUid()).child("writtenPostsId").child(post.getId()).setValue(true);
        }
    }

    public void editPost(String postId, String title, String body, String picUri) {
        if(postId != null) {
            mDatabase.child("post").child(postId).child("title").setValue(title);
            mDatabase.child("post").child(postId).child("body").setValue(body);
            mDatabase.child("post").child(postId).child("picUri").setValue(picUri);
        }
    }

    public void deletePost(String postId) {
        if(postId != null) {
            mDatabase.child("post").child(postId).removeValue();
            mDatabase.child("user").child(currentUser.getUid()).child("writtenPostsId").child(postId).removeValue();
        }
    }

    public void addUser(String id, String email, String name , String country, String gender, Date birhtDate, String picUri) {
        if(name != null && email != null && country != null && gender != null && birhtDate != null) {
            User user = new User();
            user.setName(name);
            user.setPicUri(picUri);
            user.setCountry(country);
            user.setGender(gender);
            user.setEmail(email);
            user.setDateOfBirth(birhtDate);
            user.setId(id);
            user.setLikedPostsId(new HashMap<String, Boolean>());
            user.setWrittenPostsId(new HashMap<String, Boolean>());
            mDatabase.child("user").child(user.getId()).setValue(user);
        }
    }

    public void editUser(User newUser) {
        if(newUser != null) {
            if(newUser.getName() != null && newUser.getCountry() != null && newUser.getGender() != null && newUser.getDateOfBirth() != null) {
                mDatabase.child("user").child(newUser.getId()).setValue(newUser);
            }
        }
    }

    public void deleteUser(final String userId) {
        if(userId != null) {
            mDatabase.child("user").child("userId").child("writtenPostsId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Boolean> posts = dataSnapshot.getValue(HashMap.class);
                    for(String postId : posts.keySet()) mDatabase.child("post").child(postId).removeValue();
                    mDatabase.child("user").child(userId).removeValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public void likePost(Post post) {
        Toast.makeText(getContext(), "liked", Toast.LENGTH_SHORT).show();
        mDatabase.child("post").child(post.getId()).child("likersId").child(currentUser.getUid()).setValue(true);
        mDatabase.child("post").child(post.getId()).child("numberOfLikes").setValue(post.getNumberOfLikes()+1);
        mDatabase.child("user").child(currentUser.getUid()).child("likedPostsId").child(post.getId()).setValue(true);
    }

    public void undoLikePost(Post post) {
        Toast.makeText(getContext(), "disliked", Toast.LENGTH_SHORT).show();
        mDatabase.child("post").child(post.getId()).child("likersId").child(currentUser.getUid()).removeValue();
        mDatabase.child("post").child(post.getId()).child("numberOfLikes").setValue(post.getNumberOfLikes()-1);
        mDatabase.child("user").child(currentUser.getUid()).child("likedPostsId").child(post.getId()).removeValue();
    }


}
