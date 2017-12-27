package com.hasanin.hossam.photosorganiser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    Button send;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        send = (Button) findViewById(R.id.send);
        result = (TextView) findViewById(R.id.result);
        Bundle d = getIntent().getExtras();

        result.setText(d.getString("mess"));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext() , Main2Activity.class);
                Bundle b = new Bundle();
                b.putString("mess" , "I was sent from the future from 2");
                in.putExtras(b);
                startActivityForResult(in , 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            Bundle b = data.getExtras();
            result.setText(b.getString("mess"));
        }

    }
}
