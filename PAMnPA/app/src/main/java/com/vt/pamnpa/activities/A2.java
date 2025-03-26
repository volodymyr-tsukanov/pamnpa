package com.vt.pamnpa.activities;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vt.pamnpa.R;
import com.vt.pamnpa.adapters.GradesRV;

import java.util.ArrayList;
import java.util.List;

public class A2 extends BaseActivity {
    int numSubjects;

    RecyclerView rv_grades;
    Button b_goBack, b_calculateAverage;

    GradesRV gradesRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a2);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        rv_grades = findViewById(R.id.activity_a2_rv_grades);
        b_goBack = findViewById(R.id.activity_a2_b_goBack);
        b_calculateAverage = findViewById(R.id.activity_a2_b_calculate);
        String[] subjectNames = getResources().getStringArray(R.array.activity_2_subjects);

        // Retrieve number of subjects (points) from previous activity
        numSubjects = getIntent().getIntExtra("points", 0);
        if(numSubjects > subjectNames.length) numSubjects = subjectNames.length;    //fix

        // Set up the RecyclerView and GradesRV adapter
        gradesRV = new GradesRV(this);
        for (int i = 0; i < numSubjects; i++) {
            gradesRV.getGrades().add(new GradesRV.Grade(subjectNames[i], 0));
        }
        rv_grades.setLayoutManager(new LinearLayoutManager(this));
        rv_grades.setAdapter(gradesRV);

        b_goBack.setOnClickListener(view->{
            intent.setClass(this, A1.class);
            setResult(6,intent);
            finish();
        });
        b_calculateAverage.setOnClickListener(view -> {
            float total = 0;
            int count = 0;
            float[] grades = new float[gradesRV.getItemCount()];

            // Calculate the average based on the grades in the GradesRV
            for (GradesRV.Grade grade : gradesRV.getGrades()) {
                float grade_ = grade.getPoint();
                if(grade_==0){
                    toast("Musisz wybrać coś dla "+grade.getSubject());
                    return;
                }
                total += grade_;
                grades[count++] = grade_;
            }

            if (count > 0) {
                float average = total / count;

                // Return the average grade to the first activity
                Intent intentResult = new Intent();
                intentResult.putExtra("average", average);
                intentResult.putExtra("grades",grades);
                setResult(69, intentResult);
                finish();
            } else {
                toast("Niemaocen!");
            }
        });
    }


    @Override
    protected void onActivityResult(ActivityResult result) {}
}