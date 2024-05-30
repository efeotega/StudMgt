package com.nerdyprogrammer.studmgt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class StudentLogin extends AppCompatActivity {
    EditText email,password;
    Button button;
    String emailst,passwordst;
    TextView create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        create=findViewById(R.id.create);
        create.setOnClickListener(v->{
            startActivity(new Intent(StudentLogin.this,RegisterActivity.class));

            finish();
        });
        email=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        button=findViewById(R.id.button3);
        button.setOnClickListener(v -> {
            emailst=email.getText().toString();
            passwordst=password.getText().toString();
            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(emailst,passwordst).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    searchResult(emailst);
                    startActivity(new Intent(StudentLogin.this,StudentHome.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StudentLogin.this, "Wrong details", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void searchResult(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    DataUser dataUser = dataSnapshot.getValue(DataUser.class);
                    if (dataUser != null) {
                        if (dataUser.getEmail().equals(email)) {
                            FileOps fileOps = new FileOps(StudentLogin.this);
                            fileOps.writeToIntFile("useremail.txt", dataUser.getEmail());
                            fileOps.writeToIntFile("username.txt", dataUser.getFname() + " " + dataUser.getLname());
                            fileOps.writeToIntFile("userfname.txt", dataUser.getFname());
                            fileOps.writeToIntFile("userlname.txt", dataUser.getLname());
                            fileOps.writeToIntFile("usernumber.txt", dataUser.getNumber());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentLogin.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}