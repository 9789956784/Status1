package com.virmana.status_app_all.ui.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.firebase.messaging.FirebaseMessaging;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.virmana.status_app_all.Adapters.SelectableViewHolder;
import com.virmana.status_app_all.config.Global;
import com.virmana.status_app_all.Provider.PrefManager;
import com.virmana.status_app_all.R;
import com.virmana.status_app_all.Adapters.LanguageAdapter;
import com.virmana.status_app_all.api.apiClient;
import com.virmana.status_app_all.api.apiRest;
import com.virmana.status_app_all.model.Language;
import com.virmana.status_app_all.ui.fragement.CategroiesFragement;
import com.virmana.status_app_all.ui.fragement.DownloadsFragement;
import com.virmana.status_app_all.ui.fragement.FavoritesFragment;
import com.virmana.status_app_all.ui.fragement.HomeFragment;
import com.virmana.status_app_all.ui.fragement.FollowFragment;
import com.virmana.status_app_all.ui.fragement.PopularFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ntb.NavigationTabBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements SelectableViewHolder.OnItemSelectedListener ,NavigationView.OnNavigationItemSelectedListener  {

    private static final String TAG ="MainActivity ----- : " ;
    ConsentForm form;

    private MaterialSearchView searchView;
    private ViewPagerAdapter adapter;
    private NavigationView navigationView;


    private final List<Language> languageList = new ArrayList<>();
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private AlertDialog.Builder builderLanguage;
    private PrefManager prefManager;
    private Menu menu;
    private LanguageAdapter languageAdapter;
    private ViewPager viewPager;
    private List<Fragment> fragments;// used for ViewPager Adapters

    private int tab_fab;
    private TextView text_view_name_nave_header;
    private CircleImageView circle_image_view_profile_nav_header;

    private  Boolean FromLogin = false;


    private FollowFragment followFragment;
    private Dialog dialog;
    private  Boolean DialogOpened = false;
    private TextView text_view_go_pro;

    IInAppBillingService mService;

    private static final String LOG_TAG = "iabv3";

    // put your Google merchant id here (as stated in public profile of your Payments Merchant Center)
    // if filled library will provide protection against Freedom alike Play Market simulators
    private static final String MERCHANT_ID=null;

    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            // Toast.makeText(MainActivity.this, "set null", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            //Toast.makeText(MainActivity.this, "set Stub", Toast.LENGTH_SHORT).show();

        }
    };
    private String old_language;
    private MenuItem item_language;
    private SpeedDialView speed_dial_main_activity;


    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,   Manifest.permission.READ_CONTACTS)) {
                    Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_status);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                } else {
                    Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_status);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBuy();
        prefManager= new PrefManager(getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Latest");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkPermission();
        initData();
        iniView();
        loadLang();
        initAction();
        firebaseSubscribe();
        initEvent();
        initGDPR();
    }
    private void initGDPR() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        ConsentInformation consentInformation =
                ConsentInformation.getInstance(MainActivity.this);
//// test
/////
        String[] publisherIds = {getResources().getString(R.string.publisher_id)};
        consentInformation.requestConsentInfoUpdate(publisherIds, new
                ConsentInfoUpdateListener() {
                    @Override
                    public void onConsentInfoUpdated(ConsentStatus consentStatus) {
// User's consent status successfully updated.
                        Log.d(TAG,"onConsentInfoUpdated");
                        switch (consentStatus){
                            case PERSONALIZED:
                                Log.d(TAG,"PERSONALIZED");
                                ConsentInformation.getInstance(MainActivity.this)
                                        .setConsentStatus(ConsentStatus.PERSONALIZED);
                                break;
                            case NON_PERSONALIZED:
                                Log.d(TAG,"NON_PERSONALIZED");
                                ConsentInformation.getInstance(MainActivity.this)
                                        .setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                                break;


                            case UNKNOWN:
                                Log.d(TAG,"UNKNOWN");
                                if
                                        (ConsentInformation.getInstance(MainActivity.this).isRequestLocationInEeaOrUnknown
                                        ()){
                                    URL privacyUrl = null;
                                    try {
// TODO: Replace with your app's privacy policy URL.
                                        privacyUrl = new URL(getResources().getString(R.string.policy_privacy_url));

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
// Handle error.

                                    }
                                    form = new ConsentForm.Builder(MainActivity.this,
                                            privacyUrl)
                                            .withListener(new ConsentFormListener() {
                                                @Override
                                                public void onConsentFormLoaded() {
                                                    Log.d(TAG,"onConsentFormLoaded");
                                                    showform();
                                                }
                                                @Override
                                                public void onConsentFormOpened() {
                                                    Log.d(TAG,"onConsentFormOpened");
                                                }
                                                @Override
                                                public void onConsentFormClosed( ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                                                    Log.d(TAG,"onConsentFormClosed");
                                                }
                                                @Override
                                                public void onConsentFormError(String errorDescription) {
                                                    Log.d(TAG,"onConsentFormError");
                                                    Log.d(TAG,errorDescription);
                                                }
                                            })
                                            .withPersonalizedAdsOption()
                                            .withNonPersonalizedAdsOption()
                                            .build();
                                    form.load();
                                } else {
                                    Log.d(TAG,"PERSONALIZED else");
                                    ConsentInformation.getInstance(MainActivity.this).setConsentStatus(ConsentStatus.PERSONALIZED);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onFailedToUpdateConsentInfo(String errorDescription) {
// User's consent status failed to update.
                        Log.d(TAG,"onFailedToUpdateConsentInfo");
                        Log.d(TAG,errorDescription);
                    }
                });
    }
    private void showform(){
        if (form!=null){
            Log.d(TAG,"show ok");
            form.show();
        }
    }
    private void initBuy() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        if(!BillingProcessor.isIabServiceAvailable(this)) {
            //  showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, Global.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                //  showToast("onProductPurchased: " + productId);
                Intent intent= new Intent(MainActivity.this,SlideActivity.class);
                startActivity(intent);
                finish();
                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                // showToast("onBillingError: " + Integer.toString(errorCode));
            }
            @Override
            public void onBillingInitialized() {
                //  showToast("onBillingInitialized");
                readyToPurchase = true;
                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                // showToast("onPurchaseHistoryRestored");
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }
    private void updateTextViews() {
        PrefManager prf= new PrefManager(getApplicationContext());
        bp.loadOwnedPurchasesFromGoogle();

    }

    public Bundle getPurchases(){
        if (!bp.isInitialized()) {


            //  Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            return null;
        }
        try{
            // Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();

            return  mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        }catch (Exception e) {
            //  Toast.makeText(this, "ex", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return null;
    }
    private void firebaseSubscribe() {
        FirebaseMessaging.getInstance().subscribeToTopic("StatusAllInOne");
    }

    private void initAction() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent  = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("query",query);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        /*this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager prf= new PrefManager(getApplicationContext());
                if (prf.getString("LOGGED").toString().equals("TRUE")){


                        Intent intent_video  =  new Intent(getApplicationContext(), UploadVideoActivity.class);
                        startActivity(intent_video);
                        overridePendingTransition(R.anim.enter, R.anim.exit);


                }else{
                    Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    FromLogin=true;

                }
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        MenuItem item = menu.findItem(R.id.action_search);
        item_language = menu.findItem(R.id.action_language);
        searchView.setMenuItem(item);

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_language :
                Intent intent = new Intent(MainActivity.this, LanguageActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.action_pro :
                showDialog();
                break;
            case R.id.gplay :
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.my_google_play))));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setIconItem(final MenuItem item,String url){

        final Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                item.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                Log.d("DEBUG", "onBitmapFailed");
                item.setIcon(getResources().getDrawable(R.drawable.ic_global));

            }
            @Override
            public void onPrepareLoad(Drawable drawable) {
                Log.d("DEBUG", "onPrepareLoad");
            }
        };
        Picasso.with(this).load(url).placeholder(R.drawable.flag_placeholder).error(R.drawable.flag_placeholder).into(mTarget);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            viewPager.setCurrentItem(0);
        }else if(id == R.id.login){
            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            FromLogin=true;

        }else if (R.id.whatsapp_saver== id){
             Intent intent= new Intent(MainActivity.this, WhatsAppActivity.class);
             startActivity(intent);
        }else if (id == R.id.nav_exit) {
            final PrefManager prf = new PrefManager(getApplicationContext());
            if (prf.getString("NOT_RATE_APP").equals("TRUE")) {
                super.onBackPressed();
            } else {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.rate_our_app));
                builder.setMessage(getResources().getString(R.string.rate_our_app_message));
                // add the buttons
                builder.setPositiveButton(getResources().getString(R.string.rate_now), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prf.setString("NOT_RATE_APP", "TRUE");
                        final String appPackageName = getApplication().getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
                builder.setNeutralButton(getResources().getString(R.string.later), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        prf.setString("NOT_RATE_APP", "FALSE");


                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.no_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prf.setString("NOT_RATE_APP", "TRUE");
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                       // prf.setString("NOT_RATE_APP", "FALSE");

                    }
                })
                .setIcon(R.drawable.star_on);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();

                dialog.show();

            }
        }else if (id == R.id.nav_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (id==R.id.my_profile){
            PrefManager prf= new PrefManager(getApplicationContext());
            if (prf.getString("LOGGED").toString().equals("TRUE")){
                Intent intent  =  new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", Integer.parseInt(prf.getString("ID_USER")));
                intent.putExtra("image",prf.getString("IMAGE_USER").toString());
                intent.putExtra("name",prf.getString("NAME_USER").toString());
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }else{
                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                FromLogin=true;
            }
        }else if (id==R.id.my_earnings){
            PrefManager prf= new PrefManager(getApplicationContext());
            if (prf.getString("LOGGED").toString().equals("TRUE")){
                Intent intent  =  new Intent(getApplicationContext(), EarningActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }else{
                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                FromLogin=true;
            }
        }else if (id==R.id.logout){
            logout();
        }
        else if (id==R.id.nav_share){
            final String appPackageName=getApplication().getPackageName();
            String shareBody = "Download "+getString(R.string.app_name)+" From :  "+"http://play.google.com/store/apps/details?id=" + appPackageName;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name));
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
        }else if (id == R.id.nav_rate) {
            final String appPackageName=getApplication().getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }else if (id == R.id.nav_help){
            Intent intent = new Intent(getApplicationContext(), SupportActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else if (id == R.id.nav_policy  ){
            Intent intent = new Intent(getApplicationContext(), PolicyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else if (id == R.id.buy_now){
            showDialog();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void iniView() {
        this.speed_dial_main_activity = (SpeedDialView) findViewById(R.id.speed_dial_main_activity);
        speed_dial_main_activity.inflate(R.menu.menu_speed_dial);
        speed_dial_main_activity.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_gif,R.drawable.ic_gif)
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                        .setLabel(getString(R.string.upload_gif))
                        .setFabBackgroundColor(getResources().getColor((R.color.black)))
                        .setLabelBackgroundColor(getResources().getColor((R.color.white)))
                        .create()
        );
        speed_dial_main_activity.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_video,R.drawable.ic_videocam)
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(),  R.color.white, getTheme()))
                        .setLabel(getString(R.string.upload_video))
                        .setFabBackgroundColor(getResources().getColor((R.color.black)))
                        .setLabelBackgroundColor(getResources().getColor((R.color.white)))
                        .create()
        );
        speed_dial_main_activity.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_image,R.drawable.ic_image)
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(),  R.color.white, getTheme()))
                        .setLabel(getString(R.string.upload_image))
                        .setFabBackgroundColor(getResources().getColor((R.color.black)))
                        .setLabelBackgroundColor(getResources().getColor((R.color.white)))
                        .create()
        );
        speed_dial_main_activity.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_quote,R.drawable.ic_quote)
                        .setFabImageTintColor(ResourcesCompat.getColor(getResources(),  R.color.white, getTheme()))
                        .setLabel(getString(R.string.write_quote))
                        .setFabBackgroundColor(getResources().getColor((R.color.black)))
                        .setLabelBackgroundColor(getResources().getColor((R.color.white)))
                        .create()
        );
       final PrefManager prf= new PrefManager(getApplicationContext());

        speed_dial_main_activity.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_gif:
                        if (prf.getString("LOGGED").toString().equals("TRUE")){
                            startActivity(new Intent(MainActivity.this,UploadGifActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }else{
                            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            FromLogin=true;
                        }
                        return false; // true to keep the Speed Dial open
                    case R.id.fab_image:
                        if (prf.getString("LOGGED").toString().equals("TRUE")){
                            startActivity(new Intent(MainActivity.this,UploadImageActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }else{
                            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            FromLogin=true;
                        }
                        return false; // true to keep the Speed Dial open
                    case R.id.fab_video:
                        if (prf.getString("LOGGED").toString().equals("TRUE")){
                            startActivity(new Intent(MainActivity.this,UploadVideoActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }else{
                            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            FromLogin=true;
                        }
                        return false; // true to keep the Speed Dial open
                    case R.id.fab_quote:
                        if (prf.getString("LOGGED").toString().equals("TRUE")){
                            startActivity(new Intent(MainActivity.this,UploadQuoteActivity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }else{
                            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            FromLogin=true;
                        }
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });
        this.followFragment = new FollowFragment();
        viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setOffscreenPageLimit(100);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new PopularFragment());
        adapter.addFragment(followFragment);
        adapter.addFragment(new CategroiesFragement());
        adapter.addFragment(new FavoritesFragment());
        adapter.addFragment(new DownloadsFragement());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_home),
                        Color.parseColor(colors[1]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_whatshot),
                        Color.parseColor(colors[1]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_subscriptions),
                        Color.parseColor(colors[1]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_categories),
                        Color.parseColor(colors[1]))

                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_favorite_black),
                        Color.parseColor(colors[1]))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_folder_black),
                        Color.parseColor(colors[1]))
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);

        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(true);
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
            }
            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(final int position) {
                switch (position){
                    case 0:
                        getSupportActionBar().setTitle("Latest");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Popular");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Follow");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Categories");
                        break;
                    case 4:
                        getSupportActionBar().setTitle("Favorites");
                        break;
                    case 5:
                        getSupportActionBar().setTitle("Downloads");
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        });
        View headerview = navigationView.getHeaderView(0);
        this.text_view_name_nave_header=(TextView) headerview.findViewById(R.id.text_view_name_nave_header);
        this.circle_image_view_profile_nav_header=(CircleImageView) headerview.findViewById(R.id.circle_image_view_profile_nav_header);
        this.viewPager=(ViewPager) findViewById(R.id.vp_horizontal_ntb);
        this.viewPager.setOffscreenPageLimit(100);

        // set Adapters

        viewPager.setAdapter(adapter);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);

    }
    private void initEvent() {
        // set listener to change the current item of view pager when click bottom nav item

    }

    private void initData() {
        fragments = new ArrayList<>(4);

        // create music fragment and add it
        HomeFragment homeFragment = new HomeFragment();


        // create backup fragment and add it
        PopularFragment popularFragment = new PopularFragment();

        // create friends fragment and add it
        FavoritesFragment favorFragment = new FavoritesFragment();

        // create friends fragment and add it
        followFragment = new FollowFragment();


        // add to fragments for Adapters
        fragments.add(homeFragment);
        fragments.add(popularFragment);
        fragments.add(followFragment);
        fragments.add(favorFragment);

    }
    public void setFromLogin(){
        this.FromLogin = true;
    }


    @Override
    public void onItemSelected(Language item) {

        List<Language> selectedItems = languageAdapter.getSelectedItems();
      //  Toast.makeText(MainActivity.this,"Selected item is "+ item.getLanguage()+ ", Totally  selectem item count is "+selectedItems.size(),Toast.LENGTH_LONG).show();
    }



    public int getDefaultLangiage(){
       return prefManager.getInt("LANGUAGE_DEFAULT");
    }

    public void loadLang(){
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Language>> call = service.languageAll();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, final Response<List<Language>> response) {
                if (response.isSuccessful()){
                    if (response.body().size()>1){
                        try {
                            item_language.setVisible(true);
                        }catch (NullPointerException e){

                        }
                        if (!prefManager.getString("first_lang_set").equals("true")){
                            prefManager.setString("first_lang_set","true");
                            Intent intent_status  =  new Intent(getApplicationContext(), LanguageActivity.class);
                            startActivity(intent_status);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    }else{
                        if (item_language!=null){
                            item_language.setVisible(false);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
            }
        });
    }
    public      void logout(){
        PrefManager prf= new PrefManager(getApplicationContext());
        prf.remove("ID_USER");
        prf.remove("SALT_USER");
        prf.remove("TOKEN_USER");
        prf.remove("NAME_USER");
        prf.remove("TYPE_USER");
        prf.remove("USERN_USER");
        prf.remove("IMAGE_USER");
        prf.remove("LOGGED");
        if (prf.getString("LOGGED").toString().equals("TRUE")){
            text_view_name_nave_header.setText(prf.getString("NAME_USER").toString());
            Picasso.with(getApplicationContext()).load(prf.getString("IMAGE_USER").toString()).placeholder(R.drawable.profile).error(R.drawable.profile).resize(200,200).centerCrop().into(circle_image_view_profile_nav_header);
            if (prf.getString("TYPE_USER").toString().equals("google")){
            }else {
            }
        }else{
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.my_profile).setVisible(false);
            nav_Menu.findItem(R.id.my_earnings).setVisible(false);
            nav_Menu.findItem(R.id.logout).setVisible(false);
            nav_Menu.findItem(R.id.login).setVisible(true);
            text_view_name_nave_header.setText(getResources().getString(R.string.please_login));
            Picasso.with(getApplicationContext()).load(R.drawable.profile).placeholder(R.drawable.profile).error(R.drawable.profile).resize(200,200).centerCrop().into(circle_image_view_profile_nav_header);
        }
        followFragment.Resume();

        Toast.makeText(getApplicationContext(),getString(R.string.message_logout),Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();

        updateTextViews();

        PrefManager prf= new PrefManager(getApplicationContext());

        Menu nav_Menu = navigationView.getMenu();


        if (prf.getString("LOGGED").toString().equals("TRUE")){
            nav_Menu.findItem(R.id.my_profile).setVisible(true);
            nav_Menu.findItem(R.id.my_earnings).setVisible(true);
            nav_Menu.findItem(R.id.logout).setVisible(true);
            nav_Menu.findItem(R.id.login).setVisible(false);
            text_view_name_nave_header.setText(prf.getString("NAME_USER").toString());
            Picasso.with(getApplicationContext()).load(prf.getString("IMAGE_USER").toString()).placeholder(R.drawable.profile).error(R.drawable.profile).resize(200,200).centerCrop().into(circle_image_view_profile_nav_header);
            if (prf.getString("TYPE_USER").toString().equals("google")){
            }else {
            }
        }else{
            nav_Menu.findItem(R.id.my_earnings).setVisible(false);
            nav_Menu.findItem(R.id.my_profile).setVisible(false);
            nav_Menu.findItem(R.id.logout).setVisible(false);
            nav_Menu.findItem(R.id.login).setVisible(true);

            text_view_name_nave_header.setText(getResources().getString(R.string.please_login));
            Picasso.with(getApplicationContext()).load(R.drawable.profile).placeholder(R.drawable.profile).error(R.drawable.profile).resize(200,200).centerCrop().into(circle_image_view_profile_nav_header);
        }
        if (FromLogin){
            followFragment.Resume();
            FromLogin = false;
        }
        if(old_language==null){
            old_language = prefManager.getString("LANGUAGE_DEFAULT");
        }else{
            if (old_language!=prefManager.getString("LANGUAGE_DEFAULT")){
                old_language = prefManager.getString("LANGUAGE_DEFAULT");
                Intent intent_save = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent_save);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                final PrefManager prf = new PrefManager(getApplicationContext());
                if (prf.getString("NOT_RATE_APP").equals("TRUE")) {
                    super.onBackPressed();
                } else {

                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getResources().getString(R.string.rate_our_app));
                    builder.setMessage(getResources().getString(R.string.rate_our_app_message));
                    // add the buttons
                    builder.setPositiveButton(getResources().getString(R.string.rate_now), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prf.setString("NOT_RATE_APP", "TRUE");
                            final String appPackageName = getApplication().getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    });
                    builder.setNeutralButton(getResources().getString(R.string.later), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            prf.setString("NOT_RATE_APP", "FALSE");


                        }
                    });
                    builder.setNegativeButton(getResources().getString(R.string.no_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prf.setString("NOT_RATE_APP", "TRUE");
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }).setIcon(R.drawable.star_on);
                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();

                    dialog.show();
                    return;
                }
            }

    }
    public void showDialog(){
        this.dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_subscribe);
        this.text_view_go_pro=(TextView) dialog.findViewById(R.id.text_view_go_pro);
        text_view_go_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.subscribe(MainActivity.this, Global.SUBSCRIPTION_ID);
            }
        });
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();
        DialogOpened=true;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
