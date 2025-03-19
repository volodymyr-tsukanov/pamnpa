package com.vt.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class A2 extends AppCompatActivity {

    LinearLayout ll_grades;
    Button b_calculateAverage;
    int numSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);

        ll_grades = findViewById(R.id.activity_a2_ll_grades);
        b_calculateAverage = findViewById(R.id.activity_a2_b_calculate);
        String[] subjectNames = getResources().getStringArray(R.array.activity_2_subjects);

        // Retrieve number of subjects (points) from previous activity
        numSubjects = getIntent().getIntExtra("points", 0);

        // Dynamically create EditText fields for each subject
        for (int i = 0; i < numSubjects; i++) {
            EditText et_grade = new EditText(this);
            et_grade.setHint("Grade for " + subjectNames[i]);
            ll_grades.addView(et_grade);
        }

        b_calculateAverage.setOnClickListener(view -> {
            float total = 0;
            int count = 0;

            for (int i = 0; i < ll_grades.getChildCount(); i++) {
                EditText et_grade = (EditText) ll_grades.getChildAt(i);
                try {
                    total += Float.parseFloat(et_grade.getText().toString());
                    count++;
                } catch (NumberFormatException e) {
                    toast("Invalid grade entered!");
                    return;
                }
            }

            if (count > 0) {
                float average = total / count;

                //toast(String.valueOf(average));

                // Return the average grade to the first activity
                Intent intent = new Intent(this, A1.class);
                intent.putExtra("average", average);
                setResult(RESULT_OK,intent);
                //startActivityForResult(intent,69);
                finish();
            }
        });
    }


    protected void toast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}