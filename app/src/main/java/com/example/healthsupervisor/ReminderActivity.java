package com.example.healthsupervisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.particle.ParticleView;

import static com.example.healthsupervisor.MapsActivity.REQUEST_LOCATION;

public class ReminderActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextInputLayout meds;
    private MeowBottomNavigation meo;
    private TextView mtv;
    private static boolean userPressedBackAgain ;
    private CardView mcv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        mtv = (TextView)findViewById(R.id.current);
        Button cancel = (Button)findViewById(R.id.cancelReminder);
        meds = (TextInputLayout)findViewById(R.id.medication);
        meo=(MeowBottomNavigation)findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1,R.drawable.headlines));
        meo.add(new MeowBottomNavigation.Model(2,R.drawable.diagnosis));
        meo.add(new MeowBottomNavigation.Model(3,R.drawable.reminder));
        meo.add(new MeowBottomNavigation.Model(4,R.drawable.nearest));
        meo.show(3,false);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SET_ALARM},99);
        Toast.makeText(ReminderActivity.this,"Reminder",Toast.LENGTH_LONG).show();

        meo.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                switch (model.getId()){
                    case 1:
                        startActivity(new Intent(ReminderActivity.this,HomeActivity.class));
                        Animatoo.animateSlideRight(ReminderActivity.this);
                        break;
                    case 2:
                        startActivity(new Intent(ReminderActivity.this,DiagnosisActivity.class));
                        Animatoo.animateSlideRight(ReminderActivity.this);
                        break;
                    case 3:
                        break;
                    case 4:
                        startActivity(new Intent(ReminderActivity.this,MapsActivity.class));
                        Animatoo.animateSlideLeft(ReminderActivity.this);
                        break;
                }

                return null;
            }
        });


        Button button = (Button)findViewById(R.id.reminderstart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });


    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hr, int min) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hr);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);

        updateTImeText(c);
        startAlarm(c);

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,hr);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, min);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE,meds.getEditText().getText().toString());
        startActivity(intent);

    }
    private void updateTImeText(Calendar c){
        String timeText =  "I'll Remind you at : ";
        timeText+= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        mtv.setText(timeText);
    }

    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReceiver.class);
//        Log.i("edittext","values is : " + meds.getEditText().getText().toString());
        intent.putExtra("med",meds.getEditText().getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1,intent, 0);
        meds.getEditText().setText("");
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlertReceiver.class);
        intent.putExtra("meds",meds.getEditText().getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1,intent, 0);

        alarmManager.cancel(pendingIntent);
        mtv.setText("Alarm Cancelled");
        meds.getEditText().setText("");

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
