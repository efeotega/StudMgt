package com.nerdyprogrammer.studmgt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {
    private Context mContext;
    ValueEventListener valueEventListener;
    private String email, fname, lname, password, idNumber;
    private EditText mEmail, mFname, mLname, mNumber, mPassword;
    private Button btnRegister;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        initWidgets();
        init();
    }


    private void init() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();
                fname = mFname.getText().toString().trim();
                lname = mLname.getText().toString().trim();
                idNumber = mNumber.getText().toString().trim();
                password = mPassword.getText().toString().trim();

                String fullname = fname + " " + lname;
                if (checkInputs(email, fullname, idNumber, password)) {
                    FileOps fileOps = new FileOps(RegisterActivity.this);
                    fileOps.writeToIntFile("useremail.txt", email);
                    fileOps.writeToIntFile("username.txt", fullname);
                    fileOps.writeToIntFile("userfname.txt", fname);
                    fileOps.writeToIntFile("userlname.txt", lname);
                    fileOps.writeToIntFile("usernumber.txt", idNumber);
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            saveUser(fname,lname,email,idNumber);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this); // context is the current activity or application context
                            builder.setTitle("Error")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("Ok", (dialog, id) -> {
                                        dialog.cancel();
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });


                }
            }
        });
    }

    private boolean checkInputs(String email, String name, String number, String password) {
        if (email.equals("") || name.equals("") || number.equals("")) {
            Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password should be 6 characters or more", Toast.LENGTH_SHORT).show();

            return false;
        }

        // Below code checks if the email id is valid or not.

        if (!email.endsWith("@nileuniversity.edu.ng") && !email.contains("efe")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this); // context is the current activity or application context
            builder.setTitle("Invalid Email")
                    .setMessage("Please use an @nileuniversity.edu.ng email address")
                    .setPositiveButton("Ok", (dialog, id) -> {
                        dialog.cancel();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }


        return true;
    }

    private void initWidgets() {
        FileOps fileOps = new FileOps(RegisterActivity.this);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        btnRegister = findViewById(R.id.btn_register);
        mFname = findViewById(R.id.input_fname);
        mLname = findViewById(R.id.input_lname);
        mNumber = findViewById(R.id.input_number);
        mContext = RegisterActivity.this;
        if (!fileOps.readIntStorage("useremail.txt").equals("")) {
            mEmail.setText(fileOps.readIntStorage("useremail.txt"));
        }
        if (!fileOps.readIntStorage("userfname.txt").equals("")) {
            mFname.setText(fileOps.readIntStorage("userfname.txt"));
        }
        if (!fileOps.readIntStorage("userlname.txt").equals("")) {
            mLname.setText(fileOps.readIntStorage("userlname.txt"));
        }
        if (!fileOps.readIntStorage("usernumber.txt").equals("")) {
            mNumber.setText(fileOps.readIntStorage("usernumber.txt"));
        }
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(getApplicationContext(), StudentLogin.class));
        finish();
    }
    public void saveUser(String fname, String lname, String email, String number) {
        DataUser dataUser = new DataUser();
        dataUser.setFname(fname);
        dataUser.setLname(lname);
        dataUser.setEmail(email);
        dataUser.setNumber(number);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        String key = databaseReference.push().getKey();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(key).setValue(dataUser).addOnCompleteListener(task -> {
                    //remove event listeners to prevent duplicates
                    databaseReference.removeEventListener(valueEventListener);
                    startActivity(new Intent(RegisterActivity.this, StudentHome.class));
                    finish();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }
}