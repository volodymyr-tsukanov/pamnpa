package com.vt.pamnpa.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.vt.pamnpa.R;

import java.util.ArrayList;
import java.util.List;


public class GradesRV extends RecyclerView.Adapter<GradesRV.GradesVH> {
    List<Grade> grades;
    LayoutInflater li;


    public GradesRV(Activity cotex) {
        this.li = cotex.getLayoutInflater();
        this.grades = new ArrayList<>(10);
    }

    public List<Grade> getGrades() {
        return grades;
    }


    @Override
    public GradesVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View gradeView = li.inflate(R.layout.list_grades, viewGroup, false);
        return new GradesVH(gradeView);
    }
    @Override
    public void onBindViewHolder(GradesRV.GradesVH gradesVH, int i) {
        Grade grade = grades.get(i);
        gradesVH.subjectTextView.setText(grade.getSubject());

        // Set the selected grade in the RadioGroup
        switch ((int) grade.getPoint()) {
            case 2:
                gradesVH.radioGroup.check(R.id.radio_2);
                break;
            case 3:
                gradesVH.radioGroup.check(R.id.radio_3);
                break;
            case 4:
                gradesVH.radioGroup.check(R.id.radio_4);
                break;
            case 5:
                gradesVH.radioGroup.check(R.id.radio_5);
                break;
            case 0:
                gradesVH.radioGroup.check(R.id.radio_0);
                break;
        }

        // Set the listener to handle grade changes
        gradesVH.radioGroup.setOnCheckedChangeListener(gradesVH);
    }
    @Override
    public int getItemCount() {
        return grades.size();
    }


    public class GradesVH extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener{
        float selectedGrade;
        TextView subjectTextView;
        RadioGroup radioGroup;

        public GradesVH(View itemView) {
            super(itemView);
            selectedGrade = 0;
            subjectTextView = itemView.findViewById(R.id.list_grades_t_subject);
            radioGroup = itemView.findViewById(R.id.list_grades_rg_grade);
        }

        public float getSelectedGrade() {
            return selectedGrade;
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // Handle grade selection change
            if (checkedId == R.id.radio_2) {
                selectedGrade = 2;
            } else if (checkedId == R.id.radio_3) {
                selectedGrade = 3;
            } else if (checkedId == R.id.radio_4) {
                selectedGrade = 4;
            } else if (checkedId == R.id.radio_5) {
                selectedGrade = 5;
            } else if (checkedId == R.id.radio_0) {
                selectedGrade = 0;
            }
            // Update the grade in the list
            grades.get(getAdapterPosition()).setPoint(selectedGrade);
        }
    }

    public static class Grade{
        String subject;
        float point;

        public Grade(String subject, float point) {
            this.subject = subject;
            this.point = point;
        }

        public String getSubject() {
            return subject;
        }
        public void setSubject(String subject) {
            this.subject = subject;
        }
        public float getPoint() {
            return point;
        }
        public void setPoint(float point) {
            this.point = point;
        }
    }
}
