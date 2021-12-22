package hu.unideb.inf.safewalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mSafty;
    private EditText textInputEditTextFullName, textInputEditTextAge, textInputEditTextPassword, textInputEditTextEmail;
    private Button buttonSignUp;
    private TextView textViewLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSafty = FirebaseAuth.getInstance();
        textViewLogin = (TextView) findViewById(R.id.loginTextxml);
        textViewLogin.setOnClickListener(this);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUpxml);
        buttonSignUp.setOnClickListener(this);

        textInputEditTextFullName = (EditText) findViewById(R.id.fullnamexml);
        textInputEditTextAge = (EditText) findViewById(R.id.agexml);
        textInputEditTextPassword = (EditText) findViewById(R.id.passwordxml);
        textInputEditTextEmail = (EditText) findViewById(R.id.emailxml);
        progressBar = (ProgressBar) findViewById(R.id.progressxml);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginTextxml:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.buttonSignUpxml:
                buttonSignUp();
                break;
        }

    }

    private void buttonSignUp() {
        mSafty = FirebaseAuth.getInstance();


        String SfullName = textInputEditTextFullName.getText().toString().trim();
        String Sage = textInputEditTextAge.getText().toString().trim();
        String Spassword = textInputEditTextPassword.getText().toString().trim();
        String Semail = textInputEditTextEmail.getText().toString().trim();

        if (SfullName.isEmpty()) {
            textInputEditTextFullName.setError("Full name is required!");
            textInputEditTextFullName.requestFocus();
            return;
        }
        if (Sage.isEmpty()) {
            textInputEditTextAge.setError("Age is required!");
            textInputEditTextAge.requestFocus();
            return;
        }
        if (Spassword.isEmpty()) {
            textInputEditTextPassword.setError("Password is required!");
            textInputEditTextPassword.requestFocus();
            return;
        }
        if (Spassword.length() < 6) {
            textInputEditTextPassword.setError("Min password length should be 6 characters!");
            textInputEditTextPassword.requestFocus();
            return;
        }
        if (Semail.isEmpty()) {
            textInputEditTextEmail.setError("Email is required!");
            textInputEditTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Semail).matches()) {
            textInputEditTextEmail.setError("Please provide valid email");
            textInputEditTextEmail.requestFocus();
            return;
        }
            progressBar.setVisibility(View.VISIBLE);
            mSafty.createUserWithEmailAndPassword(Semail, Spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User(SfullName, Sage, Semail);
                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(SignUp.this, Login.class));
                                } else {
                                    Toast.makeText(SignUp.this, "Failed to register! Try Again", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUp.this, "Failed to register! Try Again", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
