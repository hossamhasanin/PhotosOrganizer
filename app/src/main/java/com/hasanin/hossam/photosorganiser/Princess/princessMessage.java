package com.hasanin.hossam.photosorganiser.Princess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hasanin.hossam.photosorganiser.R;

public class princessMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_princess_message);
        Animation headerGetIn = AnimationUtils.loadAnimation(this , R.anim.princess_header_gets_in);
        Animation bottomGetIn = AnimationUtils.loadAnimation(this , R.anim.princess_bottom_gets_in);
        Animation contentGetIn = AnimationUtils.loadAnimation(this , R.anim.princess_content_get_in);
        TextView header = (TextView) findViewById(R.id.header);
        TextView bottom = (TextView) findViewById(R.id.bottom);
        TextView content = (TextView) findViewById(R.id.princess_content);
        header.setAnimation(headerGetIn);
        content.setAnimation(contentGetIn);
        bottom.setAnimation(bottomGetIn);
    }
}
