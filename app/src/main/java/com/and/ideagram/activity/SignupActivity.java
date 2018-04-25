package com.and.ideagram.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.and.ideagram.R;
import com.and.ideagram.data.FireAuth;
import com.and.ideagram.data.FireDataEditor;
import com.and.ideagram.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SIGNUP-ACTIVITY";

    EditText email;
    EditText name;
    EditText password;
    EditText birthday;
    Spinner country;
    RadioGroup gender;
    Toolbar toolbar;
    Button finish;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    RadioButton male;

    FireDataEditor fEditor;
    FireAuth fAuth;
    private String[] countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inializeActivity();
        setActionBar(toolbar);
        toolbar.setTitle("SIGN UP");
        setSpinnerAdapter();
        setListerers();
    }

    public void inializeActivity() {
        email = findViewById(R.id.emailAddress);
        name = findViewById(R.id.name);
        password = findViewById(R.id.passwordtext);
        birthday = findViewById(R.id.birthday);
        birthday.setKeyListener(null);
        country = findViewById(R.id.countrySpinner);
        gender = findViewById(R.id.genderRadio);
        male = findViewById(R.id.male);
        male.setSelected(true);
        myCalendar = Calendar.getInstance();
        toolbar = findViewById(R.id.toolbar2);
        setActionBar(toolbar);
        finish = findViewById(R.id.finish);

        fEditor = new FireDataEditor(SignupActivity.this);
        fAuth = new FireAuth(SignupActivity.this) {
            @Override
            public void updateUI(FirebaseUser user) {
                fEditor.addUser(user.getUid(), getEmail(), getName(), getCountry(), getGender(), getBirthDate(), "PicUri");
//                fAuth.sendVerificationEmail();
                saveToPref("email", getEmail());
                finish();
            }
        };
        countries = getResources().getStringArray(R.array.countries_array);
    }

    private String getGender() {
        switch (gender.getCheckedRadioButtonId()) {
            case R.id.male:
                return "MALE";
            case R.id.female:
                return "FEMALE";
            case R.id.other:
                return "OTHER";
            default:
                return "MALE";
        }
    }

    private String getEmail() {
        return email.getText().toString();
    }

    private String getName() {
        return name.getText().toString();
    }

    private String getCountry() { return country.getSelectedItem().toString(); }

    private Date getBirthDate() {
        Date d = null;
        try {
        d = new SimpleDateFormat("dd/MM/yyyy").parse(birthday.getText().toString());
        } catch (ParseException e) {
             e.printStackTrace();
        }
        return d;
    }

    private void saveToPref(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void setSpinnerAdapter() {
        country.setAdapter(new ArrayAdapter<String>(SignupActivity.this, R.layout.support_simple_spinner_dropdown_item, countries));
    }

    public void setListerers() {
        country.setOnTouchListener(new AdapterView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = SignupActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(SignupActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);}
                return false;
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };
        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signup(email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthday.setText(sdf.format(myCalendar.getTime()));
    }


}
