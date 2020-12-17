package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    EditText userETLogin, passETLogin;
    Button LoginBtn,RegisterBtn;
    // FIREBASE:

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Checking for users existance: saving the current user
        if (firebaseUser !=null ){
          Intent intent = new Intent(Login_Activity.this, MainActivity.class);
          startActivity(intent);
          finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

      userETLogin = findViewById(R.id.editText);
      passETLogin = findViewById(R.id.editText3);
      LoginBtn = findViewById(R.id.buttonLogin);
      RegisterBtn = findViewById(R.id.registerBtn);

     // Firebase Auth;

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // Checking for users existance;
        if (firebaseUser !=null){
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



      // Register Button;
      RegisterBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(Login_Activity.this, RegisterActivity.class);
              startActivity(intent);
          }
      });



     // Login Button

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = userETLogin.getText().toString();
                String pass_text = passETLogin.getText().toString();


              // Checking if it is empty:

              if (TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)){

                }
                else{
                    auth.signInWithEmailAndPassword(email_text, pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){

                               Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                               finish();

                           }

                        }
                    });
              }

            }
        });






    }
}