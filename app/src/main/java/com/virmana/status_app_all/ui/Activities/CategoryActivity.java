package com.virmana.status_app_all.ui.Activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.virmana.status_app_all.R;
import com.virmana.status_app_all.Adapters.StatusAdapter;
import com.virmana.status_app_all.api.apiClient;
import com.virmana.status_app_all.api.apiRest;
import com.virmana.status_app_all.model.Category;
import com.virmana.status_app_all.model.Status;
import com.virmana.status_app_all.Provider.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryActivity extends AppCompatActivity {

    private int id;
    private String title;
    private String image;

    private Integer page = 0;
    private String language = "0";
    private Boolean loaded=false;

    private SwipeRefreshLayout swipe_refreshl_image_category;
    private RecyclerView recycler_view_image_category;
    private List<Status> statusList =new ArrayList<>();
    private List<Category> categoryList =new ArrayList<>();
    private StatusAdapter statusAdapter;
    private LinearLayoutManager linearLayoutManager;
    private PrefManager prefManager;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private ImageView imageView_empty_category;
    private PeekAndPop peekAndPop;
    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    private String from = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras() ;
        this.id =  bundle.getInt("id");
        this.from =  bundle.getString("from");
        this.title =  bundle.getString("title");
        this.image =  bundle.getString("image");
        this.prefManager= new PrefManager(getApplicationContext());
        this.language=prefManager.getString("LANGUAGE_DEFAULT");

        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        loadStatus();
        showAdsBanner();
        initAction();

    }
    private void showAdsBanner() {
        if (prefManager.getString("SUBSCRIBED").equals("FALSE")) {
            final AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
        }

    }
    @Override
    public void onBackPressed(){
        if (from!=null){
            Intent intent = new Intent(CategoryActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            finish();
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        }
        return;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (from!=null){
                    Intent intent = new Intent(CategoryActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                    finish();
                }else{
                    super.onBackPressed();
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView(){

        if (getResources().getString(R.string.FACEBOOK_ADS_ENABLED_NATIVE).equals("true")){
            native_ads_enabled=true;
            lines_beetween_ads=Integer.parseInt(getResources().getString(R.string.FACEBOOK_ADS_ITEM_BETWWEN_ADS));
        }
        PrefManager prefManager= new PrefManager(getApplicationContext());
        if (prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            native_ads_enabled=false;
        }

        this.imageView_empty_category=(ImageView) findViewById(R.id.imageView_empty_category);
        this.relative_layout_load_more=(RelativeLayout) findViewById(R.id.relative_layout_load_more);
        this.button_try_again =(Button) findViewById(R.id.button_try_again);
        this.swipe_refreshl_image_category=(SwipeRefreshLayout) findViewById(R.id.swipe_refreshl_image_category);
        this.linear_layout_page_error=(LinearLayout) findViewById(R.id.linear_layout_page_error);
        this.recycler_view_image_category=(RecyclerView) findViewById(R.id.recycler_view_image_category);
        this.linearLayoutManager=  new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        this.peekAndPop = new PeekAndPop.Builder(this)
                .parentViewGroupToDisallowTouchEvents(recycler_view_image_category)
                .peekLayout(R.layout.dialog_view)
                .build();
        statusAdapter =new StatusAdapter(statusList,null,this,peekAndPop,true);
        recycler_view_image_category.setHasFixedSize(true);
        recycler_view_image_category.setAdapter(statusAdapter);
        recycler_view_image_category.setLayoutManager(linearLayoutManager);
        recycler_view_image_category.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {

                    visibleItemCount    = linearLayoutManager.getChildCount();
                    totalItemCount      = linearLayoutManager.getItemCount();
                    pastVisiblesItems   = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadNextStatus();
                        }
                    }
                }else{

                }
            }
        });

    }
    public void initAction(){
        swipe_refreshl_image_category.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                statusList.clear();
                page = 0;
                item = 0;
                loading=true;
                loadStatus();
            }
        });
    }
    public void loadNextStatus(){
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call = service.imageByCategory(page,prefManager.getString("ORDER_DEFAULT_STATUS"),language,id);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                apiClient.FormatData(CategoryActivity.this,response);

                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        for (int i=0;i<response.body().size();i++){
                            statusList.add(response.body().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    statusList.add(new Status().setViewType(6));
                                }
                            }

                        }
                        statusAdapter.notifyDataSetChanged();
                        page++;
                        loading=true;

                    }else {

                    }
                    relative_layout_load_more.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                relative_layout_load_more.setVisibility(View.GONE);
            }
        });
    }
    public void loadStatus(){
        swipe_refreshl_image_category.setRefreshing(true);
        imageView_empty_category.setVisibility(View.GONE);
        recycler_view_image_category.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call = service.imageByCategory(page,prefManager.getString("ORDER_DEFAULT_STATUS"),language,id);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                swipe_refreshl_image_category.setRefreshing(false);
                apiClient.FormatData(CategoryActivity.this,response);

                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        statusList.clear();
                        for (int i=0;i<response.body().size();i++){
                            statusList.add(response.body().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    statusList.add(new Status().setViewType(6));
                                }
                            }
                        }
                        statusAdapter.notifyDataSetChanged();
                        page++;
                        loaded=true;
                        imageView_empty_category.setVisibility(View.GONE);
                        recycler_view_image_category.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }else {
                        imageView_empty_category.setVisibility(View.VISIBLE);
                        recycler_view_image_category.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }


                }else{
                    imageView_empty_category.setVisibility(View.GONE);
                    recycler_view_image_category.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                imageView_empty_category.setVisibility(View.GONE);
                recycler_view_image_category.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
            }
        });
    }
}
