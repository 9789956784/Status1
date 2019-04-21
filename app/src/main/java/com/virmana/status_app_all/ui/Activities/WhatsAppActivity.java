package com.virmana.status_app_all.ui.Activities;

import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.virmana.status_app_all.R;
import com.virmana.status_app_all.Adapters.RecyclerViewMediaAdapter;
import com.virmana.status_app_all.model.StatusWhatsapp;

import java.io.File;
import java.util.ArrayList;

public class WhatsAppActivity extends AppCompatActivity {
    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    private RecyclerView mRecyclerViewMediaList;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "Home";
    private SwipeRefreshLayout swipe_refreshl_whatsapp_saver;
    private ArrayList<StatusWhatsapp> inFiles;
    private RecyclerViewMediaAdapter recyclerViewMediaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.whatsapp_saver));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        inFiles = new ArrayList<StatusWhatsapp>();
        getListFiles(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION));

        swipe_refreshl_whatsapp_saver = (SwipeRefreshLayout) findViewById(R.id.swipe_refreshl_whatsapp_saver);
        mRecyclerViewMediaList = (RecyclerView) findViewById(R.id.recyclerViewMedia);
        mLinearLayoutManager = new LinearLayoutManager(this);


        recyclerViewMediaAdapter =new RecyclerViewMediaAdapter(inFiles, WhatsAppActivity.this);
        mRecyclerViewMediaList.setHasFixedSize(true);
        mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);
        mRecyclerViewMediaList.setLayoutManager(mLinearLayoutManager);
        swipe_refreshl_whatsapp_saver.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refreshl_whatsapp_saver.setRefreshing(false);
                getListFiles(new File(Environment.getExternalStorageDirectory().toString()+WHATSAPP_STATUSES_LOCATION));
                recyclerViewMediaAdapter.notifyDataSetChanged();
            }
        });
    }
    private ArrayList<StatusWhatsapp> getListFiles(File parentDir) {

        inFiles.clear();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {

                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".gif") ||  file.getName().endsWith(".mp4")) {
                    if (!inFiles.contains(file))
                        if (file.getName().endsWith(".mp4")){
                            inFiles.add(new StatusWhatsapp().setFile(file).setViewType(0));
                        }else{
                            inFiles.add(new StatusWhatsapp().setFile(file).setViewType(1));
                        }
                }
            }
        }
        return inFiles;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
