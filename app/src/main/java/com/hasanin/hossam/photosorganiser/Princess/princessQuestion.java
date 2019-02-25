package com.hasanin.hossam.photosorganiser.Princess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hasanin.hossam.photosorganiser.R;

public class princessQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_princess_question);

        final EditText answer = (EditText) findViewById(R.id.answer);
        Button getEnter = (Button) findViewById(R.id.getEnter);

        getEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answer.getText().toString().isEmpty() && (answer.getText().toString().equals("Princess")
                        || answer.getText().toString().equals("princess")
                        || answer.getText().toString().equals("The princess")
                        || answer.getText().toString().equals("the princess")
                        || answer.getText().toString().equals("The Princess")
                        || answer.getText().toString().equals("the Princess")
                        || answer.getText().toString().equals("الأميرة")
                        || answer.getText().toString().equals("الاميرة"))){
                    startActivity(new Intent(getApplicationContext() , princessMessage.class));
                }
            }
        });

    }
}
