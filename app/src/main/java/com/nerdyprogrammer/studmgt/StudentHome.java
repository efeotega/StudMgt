package com.nerdyprogrammer.studmgt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class StudentHome extends AppCompatActivity {
    private LinearLayout resultContainer;
    private TextView textViewResultName, textViewResultScore, textViewResultGrade;
    private CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        resultContainer = findViewById(R.id.result_container);
        textViewResultName = findViewById(R.id.text_view_result_name);
        textViewResultScore = findViewById(R.id.text_view_result_score);
        textViewResultGrade = findViewById(R.id.text_view_result_grade);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        FileOps fileOps = new FileOps(StudentHome.this);
        String idNumber = fileOps.readIntStorage("usernumber.txt");
        if (idNumber != null) {
            searchResult(idNumber);
        } else {
            Toast.makeText(this, "Failed to read student ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchResult(String idNumber) {
        //Toast.makeText(this, idNumber, Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Results");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Result result = dataSnapshot.getValue(Result.class);
                    if (result != null) {
                        if (result.getId().equals(idNumber)) {
                            resultContainer.setVisibility(View.VISIBLE);
                            textViewResultName.setText("Name: " + result.getName());
                            textViewResultScore.setText("Score: " + result.getScore());
                            textViewResultGrade.setText("Grade: " + result.getGrade());
                            circularProgressBar.setProgressWithAnimation(result.getScore(), (long) 1000);  // Animated progress
                            break;
                        } else {
                            resultContainer.setVisibility(View.GONE);
//                            AlertDialog.Builder builder=new AlertDialog.Builder(StudentHome.this);
//                            builder.setTitle("Error").setMessage("No result has been added for your id number"+idNumber).setCancelable(false)
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alertDialog= builder.create();
//                            alertDialog.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentHome.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}