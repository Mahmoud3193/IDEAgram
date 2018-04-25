package com.and.ideagram.data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.and.ideagram.entity.Post;
import com.and.ideagram.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by file1 on 12/04/2018.
 */

public abstract class FireAuth {

    private static final String TAG = "FireAuth";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Activity mActivity;



    protected FireAuth(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mActivity = activity;
    }


    private Activity getActivity() {
        return mActivity;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void signin(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            updateUI(null);
                        }
                    });
    }



    public void signup(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success" + task.toString());
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")) {
                            Toast.makeText(getActivity(), "The email address is already in use by another account",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signout() {
        mAuth.signOut();
        updateUI(null);
    }

    public void deleteUser() {
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            updateUI(null);
                        }
                    }
                });
    }

    public void sendVerificationEmail() {
        currentUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    public void sendPasswordResetEmail() {
        mAuth.sendPasswordResetEmail(currentUser.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            updateUI(null);
                        }
                    }
                });
    }

    public void reAuthenticate (String email, String password) {
        if(currentUser == null) {
            updateUI(null);
            return;
        }
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                        if(task.isSuccessful()) updateUI(currentUser);
                        else updateUI(null);
                    }
                });
    }

    public void checkSignedin() {
        if(mAuth.getCurrentUser() != null) updateUI(mAuth.getCurrentUser());
        else updateUI(null);
    }


    public abstract void updateUI(FirebaseUser user);



}
