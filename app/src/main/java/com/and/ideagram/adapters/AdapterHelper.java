package com.and.ideagram.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.and.ideagram.R;
import com.and.ideagram.activity.MainActivity;
import com.and.ideagram.data.FireDataEditor;
import com.and.ideagram.data.FireDataListener;
import com.and.ideagram.data.RoomDataEditor;
import com.and.ideagram.data.RoomDataGetter;
import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by file1 on 17/04/2018.
 */

public class AdapterHelper {

    private final static String TAG = "DATA-PROVIDER";

    private List<Post> postDataSource;
    private List<Post> userPosts;
    private Map<String, User> oneUser;
    private Map<String, User> userDataSource;
    private Context mContext;
    private FireDataEditor mEditor;
    private FireDataListener mListener;
    private RoomDataEditor roomEditor;
    private RoomDataGetter roomGetter;
    private NewsFeedsAdapter newsFeedsAdapter;
    private NewsFeedsAdapter userFeedAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;





    public AdapterHelper(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        postDataSource = new ArrayList<>();
        userDataSource = new HashMap<>();
        userPosts = new ArrayList<>();
        oneUser = new HashMap<>();
        mContext = context;
        mSwipeRefreshLayout = swipeRefreshLayout;
        roomEditor = new RoomDataEditor(getContext());
        mListener = new FireDataListener(getContext()) {
            @Override
            protected void onAdd(Post p) {
                Integer index = addPostToUserPosts(p);
                if(index != null) userFeedAdapter.notifyItemInserted(index);
            }

            @Override
            protected void onUpdate(Post p) {
                String userId = p.getUserId();
                Integer index = addPostToDataSource(p);
                addUserToDataSource(userId);
                if(index != null && newsFeedsAdapter != null) newsFeedsAdapter.notifyItemChanged(index);
                Integer in = addPostToUserPosts(p);
                if(in != null && userFeedAdapter != null) userFeedAdapter.notifyItemChanged(in);
            }

            @Override
            protected void onDelete(String pKey) {
                Integer index = removeDataFromBothDataSources(pKey);
                if(index != null) newsFeedsAdapter.notifyItemRemoved(index);
                Integer in = removePostFromUserPosts(pKey);
                if(in != null) userFeedAdapter.notifyItemRemoved(in);
            }

            @Override
            protected void getFirstPageMap(Map<String, Post> refreshDataMap) {
                if(refreshDataMap != null) {
                    roomEditor.deleteAll();
                    postDataSource.clear();
                    userDataSource.clear();
                    addUserToDataSource(mEditor.getCurrentUser().getUid());
                    for(Post p : refreshDataMap.values()) {
                        addUserToDataSource(p.getUserId());
                        addPostToDataSource(p);
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            protected void getAnotherPageMap(Map<String, Post> refreshDataMap) {
                int start = postDataSource.size();
                List<Post> newList = (List<Post>) refreshDataMap.values();
                int count = newList.size();
                postDataSource.addAll(newList);
                for(Post p : newList) {
                    addUserToDataSource(p.getUserId());
                }
                newsFeedsAdapter.notifyItemRangeInserted(start + 1, count);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            protected void getOneUser(User u) {
                addOneUser(u);
            }

            @Override
            protected void getUserPosts(Post p) {
                if(p != null) {
                    Integer index = addPostToUserPosts(p);
                    if(index != null && userFeedAdapter != null) userFeedAdapter.notifyItemChanged(index);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        roomGetter = new RoomDataGetter(getContext()) {
            @Override
            public void returnAllPosts(Object o) {
                postDataSource.clear();
                postDataSource.addAll((List<Post>) o);
                for(Post p : postDataSource) {
                    roomGetter.getUserById(p.getUserId());
                    newsFeedsAdapter.notifyDataSetChanged();

                }

            }

            @Override
            protected void returnUserById(Object o) {
                User user = (User) o;
                userDataSource.put(user.getId(),user);
                newsFeedsAdapter.notifyDataSetChanged();
            }
        };
        mEditor = new FireDataEditor(getContext());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }





    public NewsFeedsAdapter getAdapter() {

        mSwipeRefreshLayout.setRefreshing(true);
        newsFeedsAdapter = new NewsFeedsAdapter(getContext(), postDataSource, userDataSource);
        if(checkConnection()) { mListener.getFirstPage(); } else { setDataOffline(); }
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(checkConnection()) { mListener.getFirstPage(); } else { setDataOffline(); }
            }
        });
        return newsFeedsAdapter;
    }

    public NewsFeedsAdapter getAdapter(String userId) {
        mSwipeRefreshLayout.setRefreshing(true);

        userFeedAdapter = new  NewsFeedsAdapter(getContext(), userPosts, oneUser);
        mListener.getUserPostsById(userId);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.getFirstPage();
            }
        });
        return userFeedAdapter;
    }


    private boolean checkConnection() {
        NetworkInfo activeNetwork  = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public int getCountOfData() {
        return postDataSource.size();
    }

    public FireDataListener getListener() {
        return mListener;
    }

    public List<Post> getPostDataSource() {
        return postDataSource;
    }

    public Map<String, User> getUserDataSource() {
        return userDataSource;
    }

    public Context getContext() {
        return mContext;
    }

    private void addUserToDataSource(final String userId) {
        if(userId != null) {
            if(!userDataSource.containsKey(userId)) {
                mListener.getDatabase().child("user").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        userDataSource.put(userId, user);
                        roomEditor.userInsertOrUpdate(user);
                        if(newsFeedsAdapter != null) newsFeedsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Add User Canceled", databaseError.toException());
                    }
                });
            }
        }
    }

    private int addPostToDataSource(Post post) {
        Integer index = null;
        if(post != null) {
            index = postDataSource.indexOf(post);
            if(index >= 0) {
                postDataSource.set(index, post);
                roomEditor.postInsertOrUpdate(post);
            }else {
                postDataSource.add(post);
                index = postDataSource.indexOf(post);
                roomEditor.postInsertOrUpdate(post);
            }
        }
        return index;
    }

    private Integer addPostToUserPosts(Post post) {
        Integer index = null;
        if(post != null && oneUser.containsKey(post.getUserId())) {
            index = userPosts.indexOf(post);
            if(index >= 0) {
                userPosts.set(index, post);
            }else {
                userPosts.add(post);
                index = userPosts.indexOf(post);
            }
        }
        return index;
    }

    private Integer removePostFromUserPosts(String key) {
        if(key != null) {
            for(Post p : userPosts) {
                if (p.getId().equals(key)) {
                    int index = userPosts.indexOf(p);
                    userPosts.remove(index);
                    return index;
                }
            }
        }
            return null;
    }

    private void addOneUser(User user) {
        if(user != null) {
            oneUser.put(user.getId(), user);
        }
    }

    private Integer removeDataFromBothDataSources(String key) {
        for(Post p : postDataSource) {
            if(p.getId().equals(key)) {
                int index = postDataSource.indexOf(p);
                postDataSource.remove(p);
                roomEditor.postDelete(key);
                if(p.getUserId() != null) {
                    userDataSource.remove(key);
                    roomEditor.userDelete(key);
                }
                return index;
            }
        }
        return null;
    }

    private void setDataOffline() {
        roomGetter.getAllPosts();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void clearUserProfileData() {
        userFeedAdapter = null;
        userPosts.clear();
        oneUser.clear();
    }




}
