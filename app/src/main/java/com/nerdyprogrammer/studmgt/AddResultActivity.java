package com.nerdyprogrammer.studmgt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddResultActivity extends AppCompatActivity {

    private EditText editTextId, editTextName, editTextScore, editTextGrade;
    private Button buttonSave;
    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result);

        editTextId = findViewById(R.id.edit_text_id);
        editTextName = findViewById(R.id.edit_text_name);
        editTextScore = findViewById(R.id.edit_text_score);
        editTextGrade = findViewById(R.id.edit_text_grade);
        buttonSave = findViewById(R.id.button_save);

        buttonSave.setOnClickListener(v -> {
            String id = editTextId.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String scoreStr = editTextScore.getText().toString().trim();
            String grade = editTextGrade.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || scoreStr.isEmpty() || grade.isEmpty()) {
                Toast.makeText(AddResultActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int score = Integer.parseInt(scoreStr);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Results");

            String key = databaseReference.push().getKey();
            Result result = new Result(id, name, score, grade, key);

// Add the result directly to the database
            databaseReference.child(key).setValue(result).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddResultActivity.this, "Result added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddResultActivity.this, "Failed to add result", Toast.LENGTH_SHORT).show();
                }
            });



        });
    }
}

