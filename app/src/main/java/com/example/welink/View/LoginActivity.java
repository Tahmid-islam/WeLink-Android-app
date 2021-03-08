package com.example.welink.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welink.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
    TextView createAccount;
    Button loginButton;
    private String rootDb = "Users";
    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailLogin);
        passwordEditText = findViewById(R.id.passLogin);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccount = (TextView) findViewById(R.id.createAccount);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowLogin();
            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });

    }

    private void AllowLogin() {
        String email = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Provide a email", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please Provide your password", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Log in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(LoginActivity.this, "Email Or Password Incorrent", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
