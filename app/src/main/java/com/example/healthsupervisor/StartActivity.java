package com.example.healthsupervisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.john.waveview.WaveView;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity {
    private Button test;
    private static boolean userPressedBackAgain ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        test =(Button)findViewById(R.id.test);

        final WaveView waveview = (WaveView)findViewById(R.id.WaveView);
        final WaveView waveview1 = (WaveView)findViewById(R.id.WaveView1);

        waveview.setProgress(50);
        waveview1.setProgress(51);

//        Toolbar mtool = (Toolbar)findViewById(R.id.include);
//        setSupportActionBar(mtool);
//        getSupportActionBar().setTitle("HEALTH SUPERVISOR");

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,HomeActivity.class));
                Animatoo.animateInAndOut(StartActivity.this);
            }
        });

    }
    @Override
    public void onBackPressed () {

        if (! userPressedBackAgain ) {
            Toast. makeText ( this, "Back again to exit" , Toast. LENGTH_SHORT ) .show () ;

            userPressedBackAgain = true;
        } else {
            Intent intent = new Intent (Intent. ACTION_MAIN ) ;
            intent.addCategory (Intent. CATEGORY_HOME ) ;
            intent.setFlags (Intent. FLAG_ACTIVITY_NEW_TASK ) ;
            startActivity (intent) ;
        }

        new CountDownTimer( 3000 , 1000 ) {
            @Override
            public void onTick ( long millisUntilFinished) {

            }

            @Override
            public void onFinish () {
                userPressedBackAgain = false;
            }
        } .start () ;
    }
}
