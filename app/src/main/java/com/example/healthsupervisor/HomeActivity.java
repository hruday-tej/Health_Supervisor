package com.example.healthsupervisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.healthsupervisor.Model.Articles;
import com.example.healthsupervisor.Model.Headlines;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class HomeActivity extends AppCompatActivity {

    MeowBottomNavigation meo;
    RecyclerView recyclerView;
    Adapter adapter;
    FloatingActionButton mn;
    WebView webView;
    String NEWS_URL;
    private View v1,v2,v3,v4;
    private static boolean userPressedBackAgain ;
    final String API_KEY = "YOUR_NEWSAPI_KEY";
    List<Articles> articles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mn=findViewById(R.id.next);
        meo=(MeowBottomNavigation)findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1,R.drawable.headlines));
        meo.add(new MeowBottomNavigation.Model(2,R.drawable.diagnosis));
        meo.add(new MeowBottomNavigation.Model(3,R.drawable.reminder));
        meo.add(new MeowBottomNavigation.Model(4,R.drawable.nearest));
        meo.show(1,false);
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            // Code to run once
            v1 = meo.getCellById(1);
            new GuideView.Builder(HomeActivity.this)
                    .setTitle("Headlines")
                    .setContentText("Keeps you updated with all the current heath issues")
                    .setTargetView(v1)
                    .setGuideListener(new GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            v2 = meo.getCellById(2);
                            new GuideView.Builder(HomeActivity.this)
                                    .setTitle("Get Diagonised")
                                    .setContentText("To know you're health issue, dive here!")
                                    .setTargetView(v2)
                                    .setGuideListener(new GuideListener() {
                                        @Override
                                        public void onDismiss(View view) {
                                            v3 = meo.getCellById(3);
                                            new GuideView.Builder(HomeActivity.this)
                                                    .setTitle("Want me to remind you to take medicines?")
                                                    .setContentText("To set reminders for taking medicines, jump here!")
                                                    .setTargetView(v3)
                                                    .setGuideListener(new GuideListener() {
                                                        @Override
                                                        public void onDismiss(View view) {
                                                            v4 = meo.getCellById(4);
                                                            new GuideView.Builder(HomeActivity.this)
                                                                    .setTitle("Emergency?")
                                                                    .setContentText("Find the hospitals nearest to your home and rush!")
                                                                    .setTargetView(v4)
                                                                    .setGuideListener(new GuideListener() {
                                                                        @Override
                                                                        public void onDismiss(View view) {
                                                                            new GuideView.Builder(HomeActivity.this)
                                                                                    .setTitle("Done reading the Article?")
                                                                                    .setContentText("Click here to read the next one!")
                                                                                    .setTargetView(mn)
                                                                                    .setGuideListener(new GuideListener() {
                                                                                        @Override
                                                                                        public void onDismiss(View view) {
                                                                                            Toast.makeText(HomeActivity.this,"Damnn you're a Quick Learner!",Toast.LENGTH_LONG).show();
                                                                                        }
                                                                                    })
                                                                                    .build()
                                                                                    .show();
                                                                        }
                                                                    })
                                                                    .build()
                                                                    .show();
                                                        }
                                                    })
                                                    .build()
                                                    .show();
                                        }
                                    })
                                    .build()
                                    .show();
                        }
                    })
                    .build()
                    .show();
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            //editor.commit();
            editor.apply();
        }

//        new GuideView.Builder(this)
//                .setTitle("Diagnosis")
//                .setContentText("You can check you're health issue by providing you're symptoms to us.")
//                .setTargetView(meo.getChildAt(2))
//                .setGravity(Gravity.auto)
//                .build()
//                .show();
        meo.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                switch (model.getId()){
                    case 1:
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this,DiagnosisActivity.class));
                        Animatoo.animateSlideLeft(HomeActivity.this);

                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this,ReminderActivity.class));
                        Animatoo.animateSlideLeft(HomeActivity.this);
                        break;
                    case 4:
                        startActivity(new Intent(HomeActivity.this,MapsActivity.class));
                        Animatoo.animateSlideLeft(HomeActivity.this);
                        break;
                }

                return null;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String country = getCountry();
        String category = "health";
        retrieveJson(country,category,API_KEY);
        mn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveJson(country,category,API_KEY);
            }
        });

//        NEWS_URL = getIntent().getStringExtra("key");

//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("message_subject_intent"));

//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        SwipeableTouchHelperCallback swipeableTouchHelperCallback =
//                new SwipeableTouchHelperCallback(new OnItemSwiped() {
//                    //Called after swiping view, place to remove top item from your recyclerview adapter
//                    @Override public void onItemSwiped() {
//                        retrieveJson(country,category,API_KEY);
//                    }
//
//                    @Override public void onItemSwipedLeft() {
//                        retrieveJson(country,category,API_KEY);
//                    }
//
//                    @Override public void onItemSwipedRight() {
//                        retrieveJson(country,category,API_KEY);
//                    }
//
//                    @Override
//                    public void onItemSwipedUp() {
//
//                    }
//
//                    @Override
//                    public void onItemSwipedDown() {
//
//                    }
//                });
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeableTouchHelperCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.setLayoutManager(new SwipeableLayoutManager());

    }

    private void updateGlobalString(String name) {
        NEWS_URL=name;
//        Toast.makeText(HomeActivity.this,"Url : "+ NEWS_URL ,Toast.LENGTH_LONG).show();
    }

    public void retrieveJson(String country, String category, String apiKey){

        Call<Headlines> call = ApiClient.getInstance().getApi().getHeadlines(country,category,apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles()!=null){
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(HomeActivity.this,articles);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                Toast.makeText(HomeActivity.this,t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
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
