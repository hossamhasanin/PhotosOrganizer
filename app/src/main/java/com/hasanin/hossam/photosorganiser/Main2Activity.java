package com.hasanin.hossam.photosorganiser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle d = getIntent().getExtras();
        Intent in = new Intent(getApplicationContext() , Main3Activity.class);
        Bundle b = new Bundle();
        b.putString("mess" , d.getString("mess"));
        in.putExtras(b);
        setResult(1 , in);
        finish();


    }
}
