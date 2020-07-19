package com.a1611.riya.wifidirectmessaging;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.a3331.hrishi.wifidirectmessaging.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private ImageView spash_image;
    private LinearLayout anim_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spash_image = (ImageView)findViewById(R.id.splash_image);
        anim_layout = (LinearLayout)findViewById(R.id.splash_anim);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        animate();
    }

    private void animate() {

        spash_image.animate().alpha(1).setDuration(1200).start();
        anim_layout.animate().scaleX(300).setDuration(1200).start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (mUser == null){
                    intent = new Intent(MainActivity.this, Authentication.class);
                }
                else {
                    intent = new Intent(MainActivity.this, Home.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1500);

    }
}
