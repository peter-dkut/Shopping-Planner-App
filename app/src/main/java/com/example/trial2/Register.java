package com.example.trial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
TextView hasaccount;
private ProgressDialog md;
FirebaseAuth mauth;
CheckBox viewp;
EditText registeremail,registerpassword,confirmpassword;
Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewp = findViewById(R.id.view);
        md = new ProgressDialog(this);
        signup = findViewById(R.id.signup);
        mauth = FirebaseAuth.getInstance();
        registeremail = findViewById(R.id.register_username);
        confirmpassword = findViewById(R.id.confirm_password);
        registerpassword = findViewById(R.id.register_password);
        hasaccount = findViewById(R.id.hasaccounts);
        viewp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    registerpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    registerpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        hasaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email,password,confirmedpassword;
            email = registeremail.getText().toString();
            confirmedpassword = confirmpassword.getText().toString();
            password = registerpassword.getText().toString();
            if(email.isEmpty())
            {
                registeremail.setError("Email is required");
                return;
            }
                if(password.isEmpty())
                {
                    registerpassword.setError("Password is required");
                    return;
                }
                if(!password.equals(confirmedpassword))
                {
                    confirmpassword.setError("PASSWORD MISMATCH");
                    return;
                }

                md.setMessage("PROCESSING...");
                md.show();
                mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            md.dismiss();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"PLEASE TRY AGAIN",Toast.LENGTH_SHORT).show();
                            md.dismiss();
                        }
                    }
                })  ;
                }

        });
    }
}
