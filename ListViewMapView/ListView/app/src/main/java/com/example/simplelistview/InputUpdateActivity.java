package com.example.simplelistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputUpdateActivity extends AppCompatActivity {

    private int position;
    private String catName;
    private EditText editTextCatName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_update);

        position=getIntent().getIntExtra("position",0);
        catName=getIntent().getStringExtra("cat_name");

        editTextCatName= InputUpdateActivity.this.findViewById(R.id.edit_text_name);
        if(null!=catName)
            editTextCatName.setText(catName);

        Button buttonOk = (Button)findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.putExtra("cat_name",editTextCatName.getText().toString());
                intent.putExtra("cat_position",position);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}