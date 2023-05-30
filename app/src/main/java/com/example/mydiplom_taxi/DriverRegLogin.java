package com.example.mydiplom_taxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverRegLogin extends AppCompatActivity {

    TextView driverStatus, question;
    Button signInBtn, signUpBtn;
    EditText emailET, passwordET;
    FirebaseAuth mAuth;
    private DatabaseReference DriverDatabaseRef;
    String OnlineDriverID;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reg_login);

        driverStatus = (TextView) findViewById(R.id.statusDriver);
        question = (TextView) findViewById(R.id.accountCreate);
        signInBtn = (Button) findViewById(R.id.signInDriver);
        signUpBtn = (Button) findViewById(R.id.singUpDriver);
        emailET = (EditText) findViewById(R.id.driverEmail);
        passwordET = (EditText) findViewById(R.id.driverPassword);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        signUpBtn.setVisibility(View.INVISIBLE);
        signUpBtn.setEnabled(false);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInBtn.setVisibility(View.INVISIBLE);
                question.setVisibility(View.INVISIBLE);
                signUpBtn.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(true);
                driverStatus.setText("Регистрация для водителей");
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                RegisterDriver(email, password);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                SignInDriver(email, password);
            }
        });
    }

    private void SignInDriver(String email, String password)
    {
        loadingBar.setTitle("Вход для водителя");
        loadingBar.setMessage("Пожалуйста дождитесь загрузки");
        loadingBar.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(DriverRegLogin.this, "Вход произошёл успешно", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent driverIntent = new Intent(DriverRegLogin.this, DriversMapActivity.class);
                    startActivity(driverIntent);
                }
                else
                {
                    Toast.makeText(DriverRegLogin.this, "Произошла ошибка, попробуйте снова", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void RegisterDriver(String email, String password)
    {
        loadingBar.setTitle("Регистрция водителя");
        loadingBar.setMessage("Пожалуйста дождитесь загрузки");
        loadingBar.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    OnlineDriverID = mAuth.getCurrentUser().getUid();
                    DriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child("Drivers").child(OnlineDriverID);
                    DriverDatabaseRef.setValue(true);

                    Intent driverIntent = new Intent(DriverRegLogin.this, DriversMapActivity.class);
                    startActivity(driverIntent);

                    Toast.makeText(DriverRegLogin.this, "Регистрация произошла успешно", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
                else
                {
                    Toast.makeText(DriverRegLogin.this, "Произошла ошибка, попробуйте снова", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }
}

