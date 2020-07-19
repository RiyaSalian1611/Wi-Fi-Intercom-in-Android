package com.a1611.riya.wifidirectmessaging;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Authentication extends AppCompatActivity {

    private EditText login_email, login_password, signup_email, signup_password, signup_confpassword, signup_name;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mref;
    private LinearLayout login_interface;
    private LinearLayout signup_interface;
    private CoordinatorLayout coordinatorLayout;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        login_email = (EditText)findViewById(R.id.user_email);
        login_password = (EditText)findViewById(R.id.user_password);
        signup_email = (EditText)findViewById(R.id.auth_signup_email);
        signup_password= (EditText)findViewById(R.id.auth_signup_pass);
        signup_confpassword = (EditText)findViewById(R.id.auth_signup_conf_pass);
        signup_name = (EditText)findViewById(R.id.auth_name);
        signup_interface = (LinearLayout)findViewById(R.id.signup_interface);
        login_interface = (LinearLayout)findViewById(R.id.login_interface);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.colayout);



        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(Authentication.this);
        dialog.setTitle("Authenticating user");

        mref = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    public void loginTab(View view){
        signup_interface.animate().alpha(0).setDuration(250).withEndAction(new Runnable() {
            @Override
            public void run() {
                signup_interface.setVisibility(View.GONE);
                login_interface.setAlpha(0);
                login_interface.setVisibility(View.VISIBLE);
                login_interface.animate().alpha(1).setDuration(250);
            }
        });
    }

    public void signupTab(View view) {
        login_interface.animate().alpha(0).setDuration(250).withEndAction(new Runnable() {
            @Override
            public void run() {
                login_interface.setVisibility(View.GONE);
                signup_interface.setAlpha(0);
                signup_interface.setVisibility(View.VISIBLE);
                signup_interface.animate().alpha(1).setDuration(250);
            }
        });
    }

    public void loginUser(View view){


        String email = login_email.getText().toString();
        String pass = login_password.getText().toString();
        dialog.setMessage("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        if (!email.isEmpty() && !pass.isEmpty()){
            dialog.show();

            try {
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (!task.isSuccessful()){
                            Exception exception = task.getException();
                            assert exception != null;
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, exception.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else if (task.isSuccessful()){
                            Intent intent = new Intent(Authentication.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }catch (Exception e){
                dialog.dismiss();
                e.printStackTrace();
            }

        }
        else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please enter email and password", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }


    public void signupUser(View view){
        String email, pass, confpass, name;
        email = signup_email.getText().toString();
        pass = signup_password.getText().toString();
        confpass = signup_confpassword.getText().toString();
        name = signup_name.getText().toString();


        if(!email.isEmpty() && !pass.isEmpty() && !name.isEmpty()){
            if(!confpass.isEmpty()){
                if (confpass.equals(pass)){
                    signUp(email, pass, name);
                }
                else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Passwords did not match!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }
        else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please fill all details!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

    private void signUp(String email, String pass, final String name) {
        dialog.setMessage("Creating account..");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        try {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mUser = mAuth.getCurrentUser();
                        mref.child(mUser.getUid()).child("name").setValue(name);
                        mref.child(mUser.getUid()).child("ip").setValue("0.0.0.0");
                        mref.child(mUser.getUid()).child("status").setValue("Active").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(Authentication.this, Home.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    else {
                        dialog.dismiss();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "SignUp Failed!, try again", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
