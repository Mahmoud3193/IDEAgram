package com.and.ideagram.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.and.ideagram.R;
import com.and.ideagram.adapters.AdapterHelper;
import com.and.ideagram.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFrag extends Fragment {


    String mUId;
    User mUser;

    TextView name;
    TextView email;
    TextView gender;
    TextView birthday;
    TextView country;


    public static InfoFrag newInstance(String uId) {
        InfoFrag frag = new InfoFrag();
        Bundle args = new Bundle();
        args.putString("uid", uId);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUId = getArguments().getString("uid");
        FirebaseDatabase.getInstance().getReference().child("user").child(mUId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                updateUi();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUi() {
        name.setText(mUser.getName());
        email.setText(mUser.getEmail());
        gender.setText(mUser.getGender());
        birthday.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.UK).format(mUser.getDateOfBirth()));
        country.setText(mUser.getCountry());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_info, container, false);
        name = view.findViewById(R.id.infoName2);
        email = view.findViewById(R.id.infoEmail2);
        gender = view.findViewById(R.id.infoGender2);
        birthday = view.findViewById(R.id.infoBirthDay2);
        country = view.findViewById(R.id.infoCountry2);
        return view;
    }





}
