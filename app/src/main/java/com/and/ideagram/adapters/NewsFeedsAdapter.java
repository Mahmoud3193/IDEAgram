package com.and.ideagram.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.and.ideagram.R;
import com.and.ideagram.activity.PostActivity;
import com.and.ideagram.activity.UserActivity;
import com.and.ideagram.data.FireDataEditor;
import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by file1 on 30/03/2018.
 */

public class NewsFeedsAdapter extends RecyclerView.Adapter<NewsFeedsAdapter.MainViewHolder> {

    private Context mContext;
    private FireDataEditor mEditor;
    private List<Post> mPostData;
    private Map<String, User> mUserData;

    private final static String TAG = "FEED-ADAPTER";




    NewsFeedsAdapter(Context context, List<Post> posts, Map<String, User> users)  {
        mContext = context;
        mPostData = posts;
        mUserData = users;
        mEditor = new FireDataEditor(getContext());
    }


    private boolean checkConnection() {
        NetworkInfo activeNetwork  = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    private Context getContext() {
        return mContext;
    }

    private void updateUserItems(User user, TextView username, CircleImageView cImage) {
        if(user != null) {
            username.setText(user.getName());
        }

//        cImage.setImageURI(Uri.parse(user.getPicUri()));
    }

    private void updatePostItems(Post post, TextView date, TextView title, TextView postBody, TextView likes, final FloatingActionButton likebtn, int Position) {
        boolean clicked = post.getLikersId() != null && post.getLikersId().containsKey(mEditor.getCurrentUser().getUid());
        String d = "Posted on " +new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK).format(post.getDate());
        date.setText(d);
        title.setText(post.getTitle());
        postBody.setText(post.getBody());
        String l = post.getNumberOfLikes() + " Likes";
        likes.setText(l);
        if(checkConnection())likebtn.setOnClickListener(likeButtonListner(clicked, likebtn, post, Position));
        else likebtn.setOnClickListener(likeButtonListnerOffline(likebtn));
    }

    private View.OnClickListener likeButtonListner(boolean clicked, final FloatingActionButton likebtn, final Post post, final int Position) {
        if (!clicked) {
            likebtn.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.white)));
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.likePost(post);
                    notifyItemChanged(Position);

                }
            };
        } else if (clicked) {
            likebtn.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.undoLikePost(post);
                    notifyItemChanged(Position);
                }
            };
        }
            return null;
    }

    private View.OnClickListener likeButtonListnerOffline(FloatingActionButton likebtn) {
        likebtn.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.gray)));
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sorry Your Are Offline Now", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener menuListener(final PopupMenu menu) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.show();
            }
        };
    }

    private PopupMenu.OnMenuItemClickListener menu1Listener(final Post post) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editPost:
                        getContext().startActivity(new Intent(getContext(), PostActivity.class).putExtra("id", post.getId()).putExtra("title", post.getTitle()).putExtra("body", post.getBody()));
                        break;
                    case R.id.deletePost:
                        new AlertDialog.Builder(getContext())
                                .setMessage("Do You Want To Delete This Post?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mEditor.deletePost(post.getId());

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .create()
                                .show();
                        break;
                }
                return false;
            }
        };
    }

    private PopupMenu.OnMenuItemClickListener menu2Listener(final User user) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.viewProfile:
                        getContext().startActivity(new Intent(getContext(), UserActivity.class).putExtra("uid", user.getId()));
                        break;
                }
                return false;
            }
        };
    }

    private View.OnClickListener offLineMenuListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sorry Your Are Offline Now", Toast.LENGTH_SHORT).show();
            }
        };
    }



    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View post = inflater.inflate(R.layout.one_post, parent, false);
        return new MainViewHolder(post, getContext());
    }


    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

        final CircleImageView cImage = holder.cImage;
        final TextView username = holder.username;
        TextView date = holder.date;
        TextView title = holder.title;
        TextView postBody = holder.post;
        TextView likes = holder.likes;
        final FloatingActionButton likebtn = holder.likebtn;
        TextView menubtn = holder.menubtn;
        PopupMenu menu1 = holder.popupMenu;
        PopupMenu menu2 = holder.popupMenu2;

        Post post = mPostData.get(position);
        User user = mUserData.get(post.getUserId());

        if(user != null) {
            updatePostItems(post, date, title, postBody, likes, likebtn, position);
            updateUserItems(user, username, cImage);
            if(checkConnection()) {
                if(user.getId().equals(mEditor.getCurrentUser().getUid())) {
                    menubtn.setOnClickListener(menuListener(menu1));
                    menu1.setOnMenuItemClickListener(menu1Listener(post));
                } else {
                    menubtn.setOnClickListener(menuListener(menu2));
                    menu2.setOnMenuItemClickListener(menu2Listener(user));
                }
            } else menubtn.setOnClickListener(offLineMenuListener());

        }

    }




    @Override
    public int getItemCount() {
        return mPostData.size();
    }


    static class MainViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cImage;
        TextView username;
        TextView date;
        TextView title;
        TextView post;
        TextView likes;
        FloatingActionButton likebtn;
        TextView menubtn;
        PopupMenu popupMenu;
        PopupMenu popupMenu2;



        public MainViewHolder(View itemView, Context context) {
            super(itemView);
            cImage = itemView.findViewById(R.id.circleImageView);
            username = itemView.findViewById(R.id.username);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
            post = itemView.findViewById(R.id.post);
            likes = itemView.findViewById(R.id.likes);
            likebtn = itemView.findViewById(R.id.likebtn);
            menubtn = itemView.findViewById(R.id.textViewOptions);
            popupMenu = new PopupMenu(context, menubtn);
            popupMenu2 = new PopupMenu(context, menubtn);
            popupMenu.inflate(R.menu.one_item_menu);
            popupMenu2.inflate(R.menu.one_item_menu_2);

        }
    }

}
