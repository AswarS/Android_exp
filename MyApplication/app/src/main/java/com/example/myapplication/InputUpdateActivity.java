package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputUpdateActivity extends AppCompatActivity {

    private int position;
    private String BookName;
    private EditText editTextBookName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_update);

        position=getIntent().getIntExtra("position",0);
        BookName =getIntent().getStringExtra("book_title");

        editTextBookName = InputUpdateActivity.this.findViewById(R.id.edit_text_name);
        if(null!= BookName)
            editTextBookName.setText(BookName);

        Button buttonOk = findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(v -> {
            Intent intent = new Intent();

            intent.putExtra("book_title", editTextBookName.getText().toString());
            intent.putExtra("book_position",position);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}