package com.jnu.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputUpdateActivity extends AppCompatActivity{
    private int position;
    private EditText editTextBookName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_update);

        position = getIntent().getIntExtra("position", 0);
        String bookName = getIntent().getStringExtra("book_name");

        editTextBookName = InputUpdateActivity.this.findViewById(R.id.edit_text_name);
        if(null != bookName)
            editTextBookName.setText(bookName);

        Button button = (Button)findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("book_name", editTextBookName.getText().toString());
                intent.putExtra("book_position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
