package com.example.healthsupervisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class TestActivity extends AppCompatActivity {
    private FloatingActionButton msend;
    private EditText userinp;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdapter messageAdapter;
    MeowBottomNavigation meo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        meo=(MeowBottomNavigation)findViewById(R.id.bottom_nav);
        meo.add(new MeowBottomNavigation.Model(1,R.drawable.headlines));
        meo.add(new MeowBottomNavigation.Model(2,R.drawable.diagnosis));
        meo.add(new MeowBottomNavigation.Model(3,R.drawable.reminder));
        meo.show(2,false);

//        meo.setOnClickMenuListener();
        meo.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                switch (model.getId()){
                    case 2:

                        Toast.makeText(TestActivity.this,"Diagonise",Toast.LENGTH_LONG).show();


                }
                switch (model.getId()){
                    case 1:
                        startActivity(new Intent(TestActivity.this,HomeActivity.class));
                        Toast.makeText(TestActivity.this,"Headlines",Toast.LENGTH_LONG).show();


                }
                switch (model.getId()){
                    case 3:
//                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
                        Toast.makeText(TestActivity.this,"Reminders",Toast.LENGTH_LONG).show();


                }

                return null;
            }
        });

        userinp=(EditText)findViewById(R.id.editText);
        msend = (FloatingActionButton)findViewById(R.id.send);
        recyclerView=(RecyclerView)findViewById(R.id.conv);
        responseMessageList=new ArrayList<>();
        messageAdapter=new MessageAdapter(responseMessageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userinp.getText().toString().isEmpty()){
                    ResponseMessage message = new ResponseMessage(userinp.getText().toString(),true);
                    responseMessageList.add(message);
                    ResponseMessage message2 = new ResponseMessage("Typing ...",false);
                    responseMessageList.add(message2);

                    messageAdapter.notifyDataSetChanged();
                    if(!isVisible()){
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                    }
                    userinp.setText("");
                }}
        });
//        userinp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
//                if(actionID== EditorInfo.IME_ACTION_SEND){
//                    ResponseMessage message = new ResponseMessage(userinp.getText().toString(),true);
//                    responseMessageList.add(message);
//                    ResponseMessage message2 = new ResponseMessage(userinp.getText().toString(),false);
//                    responseMessageList.add(message2);
//                    messageAdapter.notifyDataSetChanged();
//                    if(!isVisible()){
//                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount()-1);
//                    }
//                }
//                return true;
//            }
//        });
    }

    public boolean isVisible(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        int positionoflstvisibleitem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemcount = recyclerView.getAdapter().getItemCount();
        return (positionoflstvisibleitem>=itemcount);
    }

}
