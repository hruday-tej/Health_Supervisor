package com.example.healthsupervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class DiagnosisActivity extends AppCompatActivity {

    private WebView mwv;
    private MeowBottomNavigation meo;
    private static boolean userPressedBackAgain ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);
        mwv=(WebView)findViewById(R.id.webView);
        mwv.setWebViewClient(new WebViewClient());
        mwv.loadUrl("https://healthsuper.herokuapp.com/");
        WebSettings webSettings = mwv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        meo=(MeowBottomNavigation)findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1,R.drawable.headlines));
        meo.add(new MeowBottomNavigation.Model(2,R.drawable.diagnosis));
        meo.add(new MeowBottomNavigation.Model(3,R.drawable.reminder));
        meo.add(new MeowBottomNavigation.Model(4,R.drawable.nearest));
        meo.show(2,false);
        Toast.makeText(DiagnosisActivity.this,"Diagnosis",Toast.LENGTH_LONG).show();
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);

        CubeGrid cubeGrid = new CubeGrid();
        progressBar.setIndeterminateDrawable(cubeGrid);
        progressBar.setIndeterminateDrawable(cubeGrid);
        progressBar.setVisibility(View.VISIBLE);

        mwv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                super.onPageFinished(view, url);
            }
        });

        meo.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                switch (model.getId()){
                    case 1:
                        startActivity(new Intent(DiagnosisActivity.this,HomeActivity.class));
                        Animatoo.animateSlideRight(DiagnosisActivity.this);
                    case 2:
                        break;
                    case 3:
                        startActivity(new Intent(DiagnosisActivity.this,ReminderActivity.class));
                        Animatoo.animateSlideLeft(DiagnosisActivity.this);
                        break;
                    case 4:
                        startActivity(new Intent(DiagnosisActivity.this,MapsActivity.class));
                        Animatoo.animateSlideLeft(DiagnosisActivity.this);
                        break;
                }

                return null;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(mwv.canGoBack()){
            mwv.goBack();
        }
        else{
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
    }}

