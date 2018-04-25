package com.and.ideagram.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.and.ideagram.R;
import com.and.ideagram.data.FireDataEditor;
import com.and.ideagram.entity.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {



    EditText title;
    EditText body;
    TextView post;
    TextView cancel;
    Toolbar toolbar;
    FireDataEditor fEditor;


    String oldId;
    String oldTitle;
    String oldBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initializeActivity();
        setActionBar(toolbar);
        toolbar.setTitle("ADD NEW POST");
        setListeners();
    }


    public void initializeActivity() {
        oldId = getIntent().getStringExtra("id");
        oldTitle = getIntent().getStringExtra("title");
        oldBody  = getIntent().getStringExtra("body");
        toolbar = findViewById(R.id.toolbar3);
        fEditor = new FireDataEditor(PostActivity.this);
        title = findViewById(R.id.title);
        if(oldTitle != null) title.setText(oldTitle);
        body = findViewById(R.id.body);
        if(oldBody != null) body.setText(oldBody);
        post = findViewById(R.id.postbtn);
        cancel = findViewById(R.id.cancelbtn);
    }

    private View.OnClickListener addPostListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fEditor.addPost(title.getText().toString(), body.getText().toString(), "PIC_URI");
                Toast.makeText(PostActivity.this, "POSTED", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
    }

    private View.OnClickListener editePostListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fEditor.editPost(oldId, title.getText().toString(), body.getText().toString(), "PIC_URI");
                Toast.makeText(PostActivity.this, "EDITED", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
    }

    public void setListeners() {

        if(oldId != null) post.setOnClickListener(editePostListener());
        else post.setOnClickListener(addPostListener());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
