package com.nerdyprogrammer.studmgt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditResultActivity extends AppCompatActivity {
    private EditText editTextName, editTextScore, editTextGrade;
    private Button buttonSave;
    private Result result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        result = (Result) getIntent().getSerializableExtra("result");

        editTextName = findViewById(R.id.edit_text_name);
        editTextScore = findViewById(R.id.edit_text_score);
        editTextGrade = findViewById(R.id.edit_text_grade);
        buttonSave = findViewById(R.id.button_save);

        editTextName.setText(result.getName());
        editTextScore.setText(String.valueOf(result.getScore()));
        editTextGrade.setText(result.getGrade());

        buttonSave.setOnClickListener(v -> {
            result.setName(editTextName.getText().toString());
            result.setScore(Integer.parseInt(editTextScore.getText().toString()));
            result.setGrade(editTextGrade.getText().toString());

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Results");
            databaseReference.child(result.getKey()).setValue(result);

            finish();
        });
    }
    }
