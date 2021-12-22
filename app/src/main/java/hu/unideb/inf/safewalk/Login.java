package hu.unideb.inf.safewalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText textInputEditTextEmailLog, textInputEditTextPasswordLog;
    private Button buttonLogin;
    private TextView textViewSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth mSafty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSafty = FirebaseAuth.getInstance();
        textInputEditTextEmailLog = (TextInputEditText) findViewById(R.id.emailLogxml);
        textInputEditTextPasswordLog = (TextInputEditText) findViewById(R.id.passwordxml);
        buttonLogin = (Button) findViewById(R.id.buttonLoginxml);
        buttonLogin.setOnClickListener(this);
        textViewSignUp = (TextView) findViewById(R.id.signUpTextxml);
        textViewSignUp.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressxml);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLoginxml:
                userLogin();
                break;
            case R.id.signUpTextxml:
                startActivity(new Intent(this, SignUp.class));
                break;
        }

    }

    private void userLogin() {
        mSafty= FirebaseAuth.getInstance();
        String email = textInputEditTextEmailLog.getText().toString().trim();
        String password = textInputEditTextPasswordLog.getText().toString().trim();

        if (email.isEmpty()){
            textInputEditTextEmailLog.setError("Email is required!");
            textInputEditTextEmailLog.requestFocus();
            return;
        }
        if (password.isEmpty()){
            textInputEditTextPasswordLog.setError("Password is required!");
            textInputEditTextPasswordLog.requestFocus();
            return;
        }
        if (password.length() < 6 ){
            textInputEditTextPasswordLog.setError("Min password length is 6 Characters!");
            textInputEditTextPasswordLog.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mSafty.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user.isEmailVerified()){
                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Login.this, MainActivity.class));
                }else {
                    user.sendEmailVerification();
                    Toast.makeText(Login.this, "Check your Email to verify your account!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Failed to login! Please check you credentials", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

}
