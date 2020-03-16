package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.models.Work;
import com.example.todoapp.ui.firebase.FirebaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FormActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editDesc;
    Work work = new Work();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDesc);
        getIncomingIntent();
    }

    public void onClick(View view) {

        String text = editTitle.getText().toString().trim();
        String desk = editDesc.getText().toString().trim();
        Work work = new Work();
        work.setTitle(text);
        work.setDescription(desk);
        App.getDataBase().workDao().insert(work);// запись в базу данных
        saveInFirebase(work);
        Intent intent = new Intent();
        intent.putExtra("title", text);
        intent.putExtra("work", work);
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "Task is saved", Toast.LENGTH_LONG).show();
        finish();
        Log.d("pop", "Form on Clic");

    }

    private void saveInFirebase(Work work) {
        FirebaseFirestore.getInstance().collection("works").add(work)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FormActivity.this, "Успешно FB", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getIncomingIntent() {
        Log.d("pop","getIncomingIntent");
        Intent intent = getIntent();
        work = (Work) intent.getSerializableExtra("key");
        work = (Work) intent.getSerializableExtra("work");
        if (work != null) {
            editTitle.setText(work.getTitle());
            editDesc.setText(work.getDescription());
        }else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
