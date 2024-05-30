package com.nerdyprogrammer.studmgt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChooseActivity extends AppCompatActivity {
TextView admin,student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        student=findViewById(R.id.student);
        admin=findViewById(R.id.admin);
        student.setOnClickListener(v -> {
            startActivity(new Intent(ChooseActivity.this,StudentLogin.class));
        });
        admin.setOnClickListener(v -> {
            startActivity(new Intent(ChooseActivity.this,LoginActivity.class));
        });
    }
}