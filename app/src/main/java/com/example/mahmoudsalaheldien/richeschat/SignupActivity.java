package com.example.mahmoudsalaheldien.richeschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText _nameText, _emailText, _passwordText;
    Button _signupButton;
    TextView _loginLink;
    ProgressBar loadingprogressBar_actSignUP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        _nameText = findViewById((R.id.input_name));
        _emailText = findViewById((R.id.input_email));
        _passwordText = findViewById(R.id.input_password);
        _signupButton = findViewById(R.id.btn_signup);
        _loginLink = findViewById(R.id.link_login);
        loadingprogressBar_actSignUP = findViewById(R.id.loadingprogressBar_actSignUP);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _signupButton.setClickable(false);
                loadingprogressBar_actSignUP.setVisibility(View.VISIBLE);
                signup();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent signup_2_login = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(signup_2_login);
                finish();
            }
        });
    }

    public void signup() {

        final String userName = _nameText.getText().toString().trim();
        final String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        if (email.equals("")){
            Toast.makeText(this, "enter Email", Toast.LENGTH_SHORT).show();
        }else if (password.equals("")){
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
        }else if (userName.equals("")){
            Toast.makeText(this, "enter user name", Toast.LENGTH_SHORT).show();
        }
        if (!userName.equals("") && !email.equals("") && !password.equals(""))
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String nickname = "";
                                String status = "";
                                String location = "";
                                String profile = "";
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                String userID = firebaseUser.getUid();
                                User userClass = new User(userID, email, userName, nickname, location, status, profile);
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Users");
                                myRef.child(user.getUid()).setValue(userClass);
                                updateUI(user);
                            } else {
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                            loadingprogressBar_actSignUP.setVisibility(View.GONE);
                            _signupButton.setClickable(true);
                        }
                    });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent signup_2_main_intent = new Intent(this, MainActivity.class);
            startActivity(signup_2_main_intent);
            finish();
        }
    }

}
