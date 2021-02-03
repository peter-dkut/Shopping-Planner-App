package com.example.trial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
 Button trial;
 private ProgressDialog mydialog;
 TextView noaccounts;
 EditText useremail,userpassword;
 FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trial = findViewById(R.id.trial);
        useremail = findViewById(R.id.login_username);
        userpassword = findViewById(R.id.login_password);
        mydialog = new ProgressDialog(this);
        noaccounts = findViewById(R.id.noaccounts);
        Auth = FirebaseAuth.getInstance();
        noaccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });
        trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,passkey;
                email = useremail.getText().toString();
                passkey = userpassword.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    useremail.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(passkey))
                {
                    userpassword.setError("Required");
                    return;
                }
                mydialog.setMessage("PROCESSING..");
                mydialog.show();
                Auth.signInWithEmailAndPassword(email,passkey).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful())
                      {
                         Toast.makeText(getApplicationContext(),"SUCCESS",Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(getApplicationContext(),Home.class));
                          useremail.setText("");
                          userpassword.setText("");
                         mydialog.dismiss();
                      }
                      else
                      {
                          Toast.makeText(getApplicationContext(),"TRY AGAIN",Toast.LENGTH_SHORT).show();
                          useremail.setText("");
                          userpassword.setText("");
                          mydialog.dismiss();
                      }
                    }
                });
            }
        });

















    }
}
