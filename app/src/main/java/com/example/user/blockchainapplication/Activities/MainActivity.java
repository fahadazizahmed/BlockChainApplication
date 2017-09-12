package com.example.user.blockchainapplication.Activities;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.example.user.blockchainapplication.Adapters.MainActivityAdapter;
import com.example.user.blockchainapplication.R;

public class MainActivity extends AppCompatActivity {
    private String activityName [];
    RecyclerView recyclerView;
    MainActivityAdapter mainActivityAdapter ;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        activityName  = getResources().getStringArray(R.array.actitvity);
        mainActivityAdapter = new MainActivityAdapter(activityName,this);
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mainActivityAdapter);

    }
}
