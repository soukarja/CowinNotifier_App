package com.cowinnotifier.alertsappforcowinindia;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean searchMode = true;
    private boolean shownToday = false;
    private boolean notiToggleBtn = false;
    public String SHARED_PREF = "cowinPrefs";
    private String myPin = "";
    String apiURL = "";
    String selectedState = "";
    String selectedDist = "";
    String selectedStateID = "";
    String selectedDistID = "";
    private boolean filter18Plus = false;
    private boolean filter45Plus = false;
    private boolean filterCovisheild = false;
    private boolean filterCovaxin = false;
    private boolean filterSputnikV = false;
    private boolean filterFree = false;
    private boolean filterPaid = false;
    private boolean filterDose1 = true;
    private boolean filterDose2 = false;
    private boolean startedChecking = false;
    private int askForDonations = 0;
    JSONArray centerList;
    private long pressedTime;
    private boolean isPopupShowing = false;
    private AdView adView, adView2, adView3;
//    private String AdviewCode, Adview2Code, Adview3Code, intadCode;
    private InterstitialAd intad;

    private String FBAdviewCode, FBAdview2Code, FBAdview3Code, FBintadCode;
    private String GoogleBanner1AdCode, GoogleBanner2AdCode, GoogleBanner3AdCode, GoogleintadCode;

    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    public com.google.android.gms.ads.AdView adViewGoogle1, adViewGoogle2, adViewGoogle3;
    private boolean showFacebookAds = false;



    DrawerLayout drawerLayout;
    NavigationView navigationView;
//    Toolbar toolbar;

    private boolean isDrwawerOpen = false;

    private String[] errorTitles = new String[]{"No Internet Connection", "No Results Found"};
    private String[] errorSubTitles = new String[]{"Internet connection is required to check for new slots. Please connect to the internet and try again!", "Try searching for different location/filters or try again later. Turn on Notifications to stay alert whenever there's a slot."};

    private static final String ONESIGNAL_APP_ID = "492230c6-070b-4a12-8972-b103cf0cb4f7";

    RecyclerView recyclerViewVaccineInfo;
    LinearLayoutManager layoutManager;
    List<vaccineInfoModel> vaccineInfoList;
    vaccineInfoAdapter adapter;

//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
//        toolbar = (Toolbar)findViewById(R.id.navBarToolBar);

        navigationView.setNavigationItemSelectedListener(this);
//        setSupportActionBar(toolbar);
//        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrwawerOpen = false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrwawerOpen = true;
            }

        };
        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();

        TextView menuBttn = (TextView) findViewById(R.id.menuopenBtn);
        menuBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
//                if (isDrwawerOpen){
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                }
//                else {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//                isDrwawerOpen = !isDrwawerOpen;
            }
        });


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        AudienceNetworkAds.initialize(this);

//        AdviewCode = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"; //Sample Code
//        AdviewCode = "829049141062090_829050204395317"; //Sample Code
//        Adview2Code = "829049141062090_829050397728631"; //Sample Code
//        Adview3Code = "829049141062090_829050627728608"; //Sample Code
//        intadCode = "829049141062090_829049697728701"; //Sample Code
        initializeAdCodes(false);
//        AdSettings.addTestDevice("HASHED ID");
//        AdSettings.setTestMode(true); // Test Mode
//        int cntt = 0;
//        LinearLayout bannerBox = (LinearLayout) findViewById(R.id.bannerAd1);
//        adView = new AdView(this, AdviewCode,AdSize.BANNER_HEIGHT_50);
//        adView.setId(cntt++);
//        bannerBox.addView(adView);
//        adView.loadAd();
//
//        bannerBox = (LinearLayout) findViewById(R.id.bannerAd2);
//        adView2 = new AdView(this, Adview2Code, AdSize.BANNER_HEIGHT_50);
////        adView2 = new com.facebook.ads.AdView(this, Adview2Code, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//        adView2.setId(cntt++);
//        bannerBox.addView(adView2);
//        adView2.loadAd();
//
//        bannerBox = (LinearLayout) findViewById(R.id.bannerAd3);
//        adView3 = new AdView(this, Adview3Code, AdSize.BANNER_HEIGHT_50);
////        adView3 = new com.facebook.ads.AdView(this, Adview3Code, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//        adView3.setId(cntt++);
//        bannerBox.addView(adView3);
//        adView3.loadAd();

        adView = createFacebookBanner(FBAdviewCode, (LinearLayout)findViewById(R.id.bannerAd1));
        adViewGoogle1 = createGoogleBanner(GoogleBanner1AdCode, (LinearLayout)findViewById(R.id.bannerAd1));



        adView2 = createFacebookBanner(FBAdview2Code, (LinearLayout)findViewById(R.id.bannerAd2));
        adViewGoogle2 = createGoogleBanner(GoogleBanner2AdCode, (LinearLayout)findViewById(R.id.bannerAd2));

        adView3 = createFacebookBanner(FBAdview3Code, (LinearLayout)findViewById(R.id.bannerAd3));
        adViewGoogle3 = createGoogleBanner(GoogleBanner3AdCode, (LinearLayout)findViewById(R.id.bannerAd3));

//        showFacebookBanner(adView, adViewGoogle1);
        showFacebookBanner(adView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showFacebookAds){
                    showFacebookBanner(adView2);
//                    showFacebookBanner(adView2, adViewGoogle2);
//                    showFacebookBanner(adView3, adViewGoogle3);
                }
                else{
                    showGoogleBanner(adViewGoogle2);
                    adView.destroy();
                    showGoogleBanner(adViewGoogle1);
//                    showGoogleBanner(adViewGoogle3);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (showFacebookAds){
//                            showFacebookBanner(adView2, adViewGoogle2);
                            showFacebookBanner(adView3);
//                            showFacebookBanner(adView3, adViewGoogle3);
                        }
                        else{
//                            showGoogleBanner(adViewGoogle2);
                            showGoogleBanner(adViewGoogle3);
                        }
                    }
                }, 3*1000);
            }
        }, 3*1000);

//        if (showFacebookAds){
//            showFacebookBanner(adView2, adViewGoogle2);
//            showFacebookBanner(adView3, adViewGoogle3);
//        }
//        else{
//            showGoogleBanner(adViewGoogle2);
//            showGoogleBanner(adViewGoogle3);
//        }



//        bad1.addView(adView);
//        ll2.addView(bad1); //linearLayout
//        adView.loadAd()

//        loadInterstitial();
//        adView.loadAd();
//        adView2.loadAd();
//        adView3.loadAd();
        apiURL = "";

        TextView searchPincode = (TextView) findViewById(R.id.searchPincode);
        TextView searchDistrict = (TextView) findViewById(R.id.searchDistrict);
        TextView selecter = (TextView) findViewById(R.id.backSelect);

        Button searchBtnfromPin = (Button) findViewById(R.id.searchVaccineFromPin);
        Button searchBtnfromDist = (Button) findViewById(R.id.searchVaccineFromDistrict);
        EditText pinBox = (EditText) findViewById(R.id.pinBox);
//        int animDist = searchPincode.getWidth() * 2;


        SharedPreferences sp = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        searchMode = sp.getBoolean("search_mode", true);
        myPin = sp.getString("pincode", "");
        if (!myPin.equals("")) {
            pinBox.setText(myPin);
//            if (searchMode) {
//                findSlots("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+sp.getString("pincode", "")+"&date="+getDate());
//            }
        }
        selectedDist = sp.getString("selectedDist", "");
        selectedDistID = sp.getString("selectedDistID", "");
        selectedState = sp.getString("selectedState", "");
        selectedStateID = sp.getString("selectedStateID", "");
        notiToggleBtn = sp.getBoolean("notification", false);
        askForDonations = sp.getInt("askForDonations", 0);

        filter18Plus = sp.getBoolean("filter18Plus", false);
        filter45Plus = sp.getBoolean("filter45Plus", false);
        filterCovisheild = sp.getBoolean("filterCovisheild", false);
        filterCovaxin = sp.getBoolean("filterCovaxin", false);
        filterSputnikV = sp.getBoolean("filterSputnikV", false);
        filterFree = sp.getBoolean("filterFree", false);
        filterPaid = sp.getBoolean("filterPaid", false);
        filterDose1 = sp.getBoolean("filterDose1", true);
        filterDose2 = sp.getBoolean("filterDose2", false);

        shownToday = sp.getBoolean(getDate().replace("-", ""), false);


        setFilters();


//
        searchPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMode = true;
                setSearchView(searchMode);
            }
        });

        searchDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchMode = false;
                setSearchView(searchMode);
            }
        });

        searchBtnfromPin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isInternetConnected(MainActivity.this)) {

                    String pincode = pinBox.getText().toString().trim();
                    if (pincode.length() != 6) {
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Pincode", Toast.LENGTH_SHORT).show();
                    } else {
                        findSlots("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pincode + "&date=" + getDate());
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("pincode", pincode);
                        ed.putBoolean("search_mode", true);
                        ed.apply();
                        loadInterstitial();
                        loadBannerAds();
                    }
                }

            }
        });

        searchBtnfromDist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isInternetConnected(MainActivity.this)) {

                    if (apiURL == "") {
//                        getStates();
                        Toast.makeText(getApplicationContext(), "Please Select a Valid State/District", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(getApplicationContext(), "D->"+selectedDist, Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("selectedState", selectedState);
                        ed.putString("selectedStateID", selectedStateID);
                        ed.putString("selectedDist", selectedDist);
                        ed.putString("selectedDistID", selectedDistID);
                        ed.putBoolean("search_mode", false);
                        ed.apply();
                        findSlots(apiURL);
                        loadInterstitial();
                        loadBannerAds();
                    }
                }

            }
        });


//        TextInputLayout dist = (TextInputLayout) findViewById(R.id.distLayout);
//        dist.setVisibility(View.INVISIBLE);
        Button filter18PlusBtn = (Button) findViewById(R.id.filter18plus);
        filter18PlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter18Plus = !filter18Plus;
                setFilters();
            }
        });
        Button filter45PlusBtn = (Button) findViewById(R.id.filter45plus);
        filter45PlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter45Plus = !filter45Plus;
                setFilters();
            }
        });


        Button filterCovishieldBtn = (Button) findViewById(R.id.filterCovishield);
        filterCovishieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCovisheild = !filterCovisheild;
                setFilters();
            }
        });


        Button filterCovaxinBtn = (Button) findViewById(R.id.filterCovaxin);
        filterCovaxinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCovaxin = !filterCovaxin;
                setFilters();
            }
        });


        Button filterSputnikVBtn = (Button) findViewById(R.id.filterSputnikV);
        filterSputnikVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSputnikV = !filterSputnikV;
                setFilters();
            }
        });


        Button filterFreeBtn = (Button) findViewById(R.id.filterFree);
        filterFreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterFree = !filterFree;
                setFilters();
            }
        });


        Button filterPaidBtn = (Button) findViewById(R.id.filterPaid);
        filterPaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPaid = !filterPaid;
                setFilters();
            }
        });

        Button animBtn = (Button) findViewById(R.id.animButton);
        animBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = (TextView) findViewById(R.id.animTitle);
                if (title.getText() == errorTitles[0]) {
                    if (isInternetConnected(MainActivity.this)) {
                        startChecking();
//                        if (!apiURL.equals("") && !searchMode)
//                        {
                        getStates();
//                        }

                    } else if (title.getText() == errorTitles[1]) {
                        if (!apiURL.equals("") && !searchMode) {
                            findSlots(apiURL);
                        } else if (searchMode && myPin != "") {
                            findSlots("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + myPin + "&date=" + getDate());
                        }
                    }
                }
            }
        });

        CheckBox dose1Btn = (CheckBox) findViewById(R.id.filterDose1Btn);
        CheckBox dose2Btn = (CheckBox) findViewById(R.id.filterDose2Btn);

        dose1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDose1 = !filterDose1;
                setFilters();
            }
        });
        dose2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDose2 = !filterDose2;
                setFilters();
            }
        });


        setSearchView(searchMode);
        getStates();
        setNotificationToggle(notiToggleBtn);
//        startChecking();
        startService(new Intent(this, MyService.class));


        Button redirectToCowinBtn = (Button) findViewById(R.id.openCowinSiteButton);
        redirectToCowinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://selfregistration.cowin.gov.in/"));
                startActivity(browserIntent);
                askForDonations+=100;
                askForDonationFromUser();

            }
        });

        Button notificationToggleButton = (Button) findViewById(R.id.toggleNotificationButton);
        notificationToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toggleBtn = false;
                notiToggleBtn = !notiToggleBtn;
                if (notiToggleBtn) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please keep the app running in background & volume turned up to get faster Notifications.")
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    loadInterstitial();
                                    loadBannerAds();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("App Needs to Run In Background");
                    alert.show();
                }
                else {
                    loadInterstitial();
                    loadBannerAds();
                }
                setNotificationToggle(notiToggleBtn);

            }
        });

        LinearLayout slotInfo = (LinearLayout) findViewById(R.id.slotInfo);
        slotInfo.setVisibility(View.GONE);


        ScrollView sc = (ScrollView)findViewById(R.id.scrollViewMain);
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.scrollToTopButton);
        fab.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sc.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    Log.d("scrollPosition", String.valueOf(scrollY));
                    if (scrollY>=2000)
                    {
                        fab.setVisibility(View.VISIBLE);
                    }
                    else {
                        fab.setVisibility(View.GONE);
                    }
                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sc.scrollTo(0,0);
                sc.smoothScrollTo(0,0);
            }
        });

    }

    private void setFilters() {
        Button filterBtn = (Button) findViewById(R.id.filter18plus);
        if (filter18Plus) {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            filterBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            filterBtn.setTextColor(getResources().getColor(R.color.black));
        }

        filterBtn = (Button) findViewById(R.id.filter45plus);
        if (filter45Plus) {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            filterBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            filterBtn.setTextColor(getResources().getColor(R.color.black));
        }

        filterBtn = (Button) findViewById(R.id.filterCovishield);
        if (filterCovisheild) {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            filterBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            filterBtn.setTextColor(getResources().getColor(R.color.black));
        }

        filterBtn = (Button) findViewById(R.id.filterCovaxin);
        if (filterCovaxin) {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            filterBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            filterBtn.setTextColor(getResources().getColor(R.color.black));
        }

        filterBtn = (Button) findViewById(R.id.filterSputnikV);
        if (filterSputnikV) {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            filterBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            filterBtn.setTextColor(getResources().getColor(R.color.black));
        }
        filterBtn = (Button) findViewById(R.id.filterFree);
        if (filterFree) {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            filterBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            filterBtn.setTextColor(getResources().getColor(R.color.black));
        }

        filterBtn = (Button) findViewById(R.id.filterPaid);
        if (filterPaid) {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            filterBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            filterBtn.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            filterBtn.setTextColor(getResources().getColor(R.color.black));
        }
        CheckBox doseBtn = (CheckBox) findViewById(R.id.filterDose1Btn);
        doseBtn.setChecked(filterDose1);
        doseBtn = (CheckBox) findViewById(R.id.filterDose2Btn);
        doseBtn.setChecked(filterDose2);


        SharedPreferences sp = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("filter18Plus", filter18Plus);
        ed.putBoolean("filter45Plus", filter45Plus);
        ed.putBoolean("filterCovisheild", filterCovisheild);
        ed.putBoolean("filterCovaxin", filterCovaxin);
        ed.putBoolean("filterSputnikV", filterSputnikV);
        ed.putBoolean("filterFree", filterFree);
        ed.putBoolean("filterPaid", filterPaid);
        ed.putBoolean("filterDose1", filterDose1);
        ed.putBoolean("filterDose2", filterDose2);
        ed.apply();


//        if (searchMode){
//            EditText pinBox = (EditText)findViewById(R.id.pinBox);
//            String pincode = pinBox.getText().toString().trim();
//                findSlots("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+pincode+"&date="+getDate());
//        }
//        else {
//            findSlots(apiURL);
//        }
        if (centerList != null) {
            displaySlots(centerList, true);
        }
    }


    private void setNotificationToggle(boolean notiToggleBtn) {
        Drawable img;
        SharedPreferences sp = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("notification", notiToggleBtn);
        ed.apply();
        Button notificationToggleButton = (Button) findViewById(R.id.toggleNotificationButton);
        if (notiToggleBtn) {

            img = notificationToggleButton.getContext().getResources().getDrawable(R.drawable.notification_icon);
            notificationToggleButton.setText("NOTIFICATION ON");
            notificationToggleButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
            startChecking();
        } else {
            img = notificationToggleButton.getContext().getResources().getDrawable(R.drawable.notification_off_icon);
            notificationToggleButton.setText("NOTIFICATION OFF");
            notificationToggleButton.setBackgroundColor(getResources().getColor(R.color.primary3));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            notificationToggleButton.setCompoundDrawablesRelativeWithIntrinsicBounds(img, null, null, null);
        }
    }


    private void setSearchView(boolean mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            closeKeyboard();
        }
        TextView searchPincode = (TextView) findViewById(R.id.searchPincode);
        TextView searchDistrict = (TextView) findViewById(R.id.searchDistrict);
        TextView selecter = (TextView) findViewById(R.id.backSelect);
        LinearLayout pinView = (LinearLayout) findViewById(R.id.pinView);
        LinearLayout distView = (LinearLayout) findViewById(R.id.distView);
        if (mode) {
            selecter.animate().x(0).setDuration(100);
            searchPincode.setTextColor(Color.WHITE);
            searchDistrict.setTextColor(Color.BLACK);
            pinView.setVisibility(View.VISIBLE);
            distView.setVisibility(View.GONE);
        } else {
            selecter.animate().x(searchPincode.getWidth()).setDuration(100);
            searchPincode.setTextColor(Color.BLACK);
            searchDistrict.setTextColor(Color.WHITE);
            pinView.setVisibility(View.GONE);
            distView.setVisibility(View.VISIBLE);
        }
    }

    private void getStates() {
//        if (!isInternetConnected(MainActivity.this)){
//            try {
//                Thread.sleep(1000);
//                getStates();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
//        selectedDistID = "";
//        if(!isInternetConnected(MainActivity.this))
//        {
//            return;
//        }
//        selectedDist = "";
        TextInputLayout dist = (TextInputLayout) findViewById(R.id.distLayout);
        AutoCompleteTextView ds = (AutoCompleteTextView) findViewById(R.id.districtList);
//        ds.setText("Choose District");
        RequestQueue requestQueue;
        apiURL = "";
        Vector<String> stLst = new Vector<String>(1);
//        Vector<String> stID = new Vector<String>(1);
        Hashtable<String, String> stateMap = new Hashtable<String, String>();
//        stateList = new String[10];
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://cdn-api.co-vin.in/api/v2/admin/location/states", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (searchMode && !myPin.equals("")) {
                    findSlots("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + myPin + "&date=" + getDate());
                }
                try {
                    JSONArray mystates = response.getJSONArray("states");
                    for (int i = 0; i < mystates.length(); i++) {
                        JSONObject stateInfo = mystates.getJSONObject(i);
                        stLst.add(stateInfo.getString("state_name"));
//                        stID.add(stateInfo.getString("state_id"));
                        stateMap.put(stateInfo.getString("state_name"), stateInfo.getString("state_id"));
                    }

                    String[] stateList = new String[stLst.size()];
                    int x = 0;
                    for (String st : stLst) {
                        stateList[x++] = st;
                    }
                    AutoCompleteTextView states = (AutoCompleteTextView) findViewById(R.id.stateList);

                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdownitem, stateList);
                    states.setAdapter(stateAdapter);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !selectedState.equals("") && !searchMode) {
                        int pos = stLst.indexOf(selectedState);
                        AutoCompleteTextView stateDD = (AutoCompleteTextView) findViewById(R.id.stateList);
                        if (pos >= 0) {
                            stateDD.setText(stateDD.getAdapter().getItem(pos).toString(), false);
                        }
                        getDistricts(selectedStateID);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setSearchView(searchMode);
                        closeKeyboard();
                    }

                    states.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String v = stateMap.get(states.getText().toString());
//                            Toast.makeText(getApplicationContext(), v, Toast.LENGTH_SHORT).show();
                            selectedState = states.getText().toString();
                            selectedStateID = v;
                            String[] temp = new String[0];
                            ArrayAdapter<String> stateAdapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdownitem, temp);
                            ds.setAdapter(stateAdapter2);
                            getDistricts(v);
//                            AutoCompleteTextView dist = (AutoCompleteTextView) findViewById(R.id.districtList);

//                            dist.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    private void getDistricts(String stateID) {
        String requrl = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + stateID;
        apiURL = "";
        RequestQueue requestQueue;
        Vector<String> districts = new Vector<String>(1);

        Hashtable<String, String> districtMap = new Hashtable<String, String>();
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mydistricts = response.getJSONArray("districts");
                    for (int i = 0; i < mydistricts.length(); i++) {
                        JSONObject stateInfo = mydistricts.getJSONObject(i);
                        districts.add(stateInfo.getString("district_name"));
//                        stID.add(stateInfo.getString("state_id"));
                        districtMap.put(stateInfo.getString("district_name"), stateInfo.getString("district_id"));
                    }

                    String[] dstList = new String[districts.size()];
                    int x = 0;
                    for (String st : districts) {
                        dstList[x++] = st;
                    }
                    AutoCompleteTextView distr = (AutoCompleteTextView) findViewById(R.id.districtList);

                    ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdownitem, dstList);
                    distr.setAdapter(stateAdapter);

//                    Toast.makeText(getApplicationContext(), "DIs -> "+selectedDistID, Toast.LENGTH_SHORT).show();
                    if (!selectedDist.equals("")) {
                        int pos = districts.indexOf(selectedDist);
//                        Toast.makeText(getApplicationContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
                        if (pos >= 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            distr.setText(distr.getAdapter().getItem(pos).toString(), false);
                        }
                            apiURL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + selectedDistID + "&date=" + getDate();
                            findSlots(apiURL);
                    }

                    distr.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String v = districtMap.get(distr.getText().toString());
//                            Toast.makeText(getApplicationContext(), v, Toast.LENGTH_SHORT).show();
                            apiURL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + v + "&date=" + getDate();
                            selectedDistID = v;
                            selectedDist = distr.getText().toString();
//                            Toast.makeText(getApplicationContext(), selectedDist, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(), apiURL, Toast.LENGTH_SHORT).show();
//                            findSlots(apiURL);

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);


    }

    private void displaySlots(JSONArray centerList, boolean updateDisplay){
        displaySlots(centerList, updateDisplay, false);
    }

    private void displaySlots(JSONArray centerList, boolean updateDisplay, boolean pushNotification) {
//        Toast.makeText(getApplicationContext(), String.valueOf(centerList.length()), Toast.LENGTH_SHORT).show();
        vaccineInfoList = new ArrayList<>();

        boolean PVTfilter18Plus = filter18Plus;
        boolean PVTfilter45Plus = filter45Plus;
        boolean PVTfilterCovisheild = filterCovisheild;
        boolean PVTfilterCovaxin = filterCovaxin;
        boolean PVTfilterSputnikV = filterSputnikV;
        boolean PVTfilterFree = filterFree;
        boolean PVTfilterPaid = filterPaid;
        boolean PVTfilterDose1 = filterDose1;
        boolean PVTfilterDose2 = filterDose2;

//        Vector<String> filterList = new Vector<>();

        if (!PVTfilter18Plus && !PVTfilter45Plus) {
            PVTfilter18Plus = true;
            PVTfilter45Plus = true;
        }
        if (PVTfilter45Plus) {
            PVTfilter18Plus = true;
        }
        if (!PVTfilterDose1 && !PVTfilterDose2) {
            PVTfilterDose1 = true;
            PVTfilterDose2 = true;
        }
        if (!PVTfilterFree && !PVTfilterPaid) {
            PVTfilterFree = true;
            PVTfilterPaid = true;
        }

        if (!PVTfilterCovisheild && !PVTfilterSputnikV && !PVTfilterCovaxin) {
            PVTfilterCovaxin = true;
            PVTfilterSputnikV = true;
            PVTfilterCovisheild = true;
        }


        try {
            for (int i = 0; centerList != null && i < centerList.length(); i++) {
                Log.d("mycenter", "Loop Working " + String.valueOf(i));
                JSONObject centerInfo = centerList.getJSONObject(i);
                JSONArray centerSessions = centerInfo.getJSONArray("sessions");
                String feeType = centerInfo.getString("fee_type");
                String centerName = centerInfo.getString("name");
                int centerID = centerInfo.getInt("center_id");
                String centerAddress = centerInfo.getString("address");
                String centerPincode = String.valueOf(centerInfo.getInt("pincode"));
//                        districts.add(stateInfo.getString("district_name"));

                for (int j = 0; j < centerSessions.length(); j++) {
                    Log.d("mycenter", "Loop2 Working " + String.valueOf(j));
                    JSONObject currentSession = centerSessions.getJSONObject(j);
                    String sessionDate = currentSession.getString("date");
                    int minAgeLimit = currentSession.getInt("min_age_limit");
                    String VaccineName = currentSession.getString("vaccine");
                    int dose1Quantity = currentSession.getInt("available_capacity_dose1");
                    int dose2Quantity = currentSession.getInt("available_capacity_dose2");

                    if (dose1Quantity + dose2Quantity <= 0) {
                        continue;
                    }

                    if (!PVTfilter18Plus && minAgeLimit == 18) {
                        continue;
                    }
                    if (!PVTfilter45Plus && minAgeLimit == 45) {
                        continue;
                    }

                    if (!PVTfilterCovisheild && VaccineName.toLowerCase().trim().equals("covishield")) {
                        continue;
                    }
                    if (!PVTfilterCovaxin && VaccineName.toLowerCase().trim().equals("covaxin")) {
                        continue;
                    }
                    if (!PVTfilterSputnikV && VaccineName.toLowerCase().trim().equals("sputnik v")) {
                        continue;
                    }


                    if (!PVTfilterFree && feeType.toLowerCase().trim().equals("free")) {
                        continue;
                    }
                    if (!PVTfilterPaid && feeType.toLowerCase().trim().equals("paid")) {
                        continue;
                    }


                    if (!PVTfilterDose1 || dose1Quantity <= 0) {
                        if (!PVTfilterDose2 || dose2Quantity <= 0) {
                            continue;
                        }
                    }


                    if (notiToggleBtn) {
                        String locat;
                        if (searchMode) {
                            locat = myPin;
                        } else {
                            locat = selectedDist + ", " + selectedState;
                        }
                        if (pushNotification){
                            addNotification("New Vaccination Slots Available at " + locat, "Center: " + centerName + ", Slots Available: " + String.valueOf(dose1Quantity + dose2Quantity), centerID);
                        }
                    }

                    if (updateDisplay) {
                        vaccineInfoList.add(new vaccineInfoModel(sessionDate, centerName, centerAddress, VaccineName, String.valueOf(minAgeLimit), feeType, String.valueOf(dose1Quantity), String.valueOf(dose2Quantity)));
                    }
                }

            }
            LinearLayout animationBox = (LinearLayout) findViewById(R.id.animationBox);
            TextView title = (TextView) findViewById(R.id.animTitle);
            TextView subTitle = (TextView) findViewById(R.id.animSubTitle);
            LottieAnimationView anim = (LottieAnimationView) findViewById(R.id.jsonAnim);
            if (vaccineInfoList.size() <= 0) {


                animationBox.setVisibility(View.VISIBLE);
                title.setText(errorTitles[1]);
                subTitle.setText(errorSubTitles[1]);
                anim.setAnimation(R.raw.not_found);
                anim.playAnimation();
//                anim.loop(true);
            } else {
                animationBox.setVisibility(View.GONE);
            }
            if (updateDisplay) {
                recyclerViewVaccineInfo = findViewById(R.id.availableVaccineCenters);
                layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerViewVaccineInfo.setLayoutManager(layoutManager);
                adapter = new vaccineInfoAdapter(vaccineInfoList);
                recyclerViewVaccineInfo.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.d("mycenter", "Json Error New");
            e.printStackTrace();
        }
    }

    private void findSlots(String urlLink){
        findSlots(urlLink, false);
    }

    private void findSlots(String urlLink, boolean pushNoti) {
//        if(!isInternetConnected(MainActivity.this))
//        {
//            return;
//        }
        LinearLayout slotInfo = (LinearLayout) findViewById(R.id.slotInfo);
        slotInfo.setVisibility(View.VISIBLE);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlLink, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    centerList = response.getJSONArray("centers");
                    Log.d("mycenters", response.getString("centers"));
//                    Toast.makeText(getApplicationContext(), urlLink, Toast.LENGTH_SHORT).show();
                    displaySlots(centerList, true, pushNoti);


                } catch (JSONException e) {
                    Log.d("mycenters", "Error Occurred");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mycenters", "An Error Occurred " + urlLink);
                Log.d("mycenters", String.valueOf(error));
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private String getDate() {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDateTime now = LocalDateTime.now();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        return dtf.format(now);
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

    private boolean isInternetConnected(MainActivity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean connectionStatus = ((wifiConn != null && wifiConn.isConnected()) || (dataConn != null && dataConn.isConnected()));

//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        boolean connectionStatus = activeNetworkInfo != null && activeNetworkInfo.isConnected();


        LinearLayout animationBox = (LinearLayout) findViewById(R.id.animationBox);
        LinearLayout slotInfo = (LinearLayout) findViewById(R.id.slotInfo);
        TextView title = (TextView) findViewById(R.id.animTitle);
        TextView subTitle = (TextView) findViewById(R.id.animSubTitle);
        LottieAnimationView anim = (LottieAnimationView) findViewById(R.id.jsonAnim);

//        if ((wifiConn!=null && wifiConn.isConnected()) || (dataConn!=null && dataConn.isConnected()))
//        Toast.makeText(this, String.valueOf(connectionStatus), Toast.LENGTH_SHORT).show();
        if (connectionStatus) {
            animationBox.setVisibility(View.GONE);
            slotInfo.setVisibility(View.VISIBLE);
//            getStates();
            loadBannerAds();
            return true;
        } else {
            animationBox.setVisibility(View.VISIBLE);
            slotInfo.setVisibility(View.GONE);
            title.setText(errorTitles[0]);
            subTitle.setText(errorSubTitles[0]);
            anim.setAnimation(R.raw.no_internet);
            anim.playAnimation();
//            anim.loop(true);
            return false;
        }

    }

    public void startChecking() {
        if (!startedChecking && notiToggleBtn) {
            startedChecking = true;

            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            exec.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (searchMode) {
                        EditText pinBox = (EditText) findViewById(R.id.pinBox);
                        String pincode = pinBox.getText().toString().trim();
                        if (notiToggleBtn) {
                            findSlots("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pincode + "&date=" + getDate(), true);
//                            askForDonations += 1;
//                            askForDonationFromUser();
                        }
                    } else {
                        if (notiToggleBtn) {
                            findSlots(apiURL, true);
//                            askForDonations += 1;
//                            askForDonationFromUser();
                        }
                    }
                }
            }, 0, 30, TimeUnit.SECONDS); // execute every 30 seconds

        }
    }

    private void addNotification(String title, String content, int Notiid) {
        String CHANNEL_ID = "12345";
        MediaPlayer mp;
        mp = MediaPlayer.create(this, R.raw.notification_sound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = "Vaccine is Available Now";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://selfregistration.cowin.gov.in/"));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setSound(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
//        Random rand = new Random();
        notificationManager.notify(Notiid, builder.build());

        try {
            if (!mp.isPlaying() && pressedTime + 3000 < System.currentTimeMillis()) {
//                mp.stop();
//                mp.release();
//                mp = MediaPlayer.create(this, R.raw.notification_sound);
                mp.start();
                askForDonations+=10;
                askForDonationFromUser();
            }
            pressedTime = System.currentTimeMillis();
//            } mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (pressedTime + 3000 > System.currentTimeMillis()) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        pressedTime = System.currentTimeMillis();


    }


    private void loadBannerAds() {
//        adView.loadAd();
//        adView2.loadAd();
//        adView3.loadAd();
        if (showFacebookAds){
//            showFacebookBanner(adView, adViewGoogle1);
//            showFacebookBanner(adView2, adViewGoogle2);
//            showFacebookBanner(adView3, adViewGoogle3);
            showFacebookBanner(adView);
            showFacebookBanner(adView2);
            showFacebookBanner(adView3);
        }
        else {
            showGoogleBanner(adViewGoogle1);
            showGoogleBanner(adViewGoogle2);
            showGoogleBanner(adViewGoogle3);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        String appurl = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
        switch (menuItem.getItemId()) {
            case R.id.rateUsOnPlaystore:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appurl));
                startActivity(browserIntent);
                break;
            case R.id.shareBtn:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "Download this Cowin Notifier App to book Vaccination Slots easily with instant Notifications!");
                share.putExtra(Intent.EXTRA_TEXT, appurl);
                startActivity(Intent.createChooser(share, "Choose App to Share"));
                break;
            case R.id.donateBtn:
                donationGateway();
                break;
            case R.id.abousUsBtn:
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cowinnotifier.site"));
                startActivity(browserIntent2);
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);


        return true;
    }

    private void askForDonationFromUser(){


        //debug without donation
//        donationGateway();
//        if (true){
//            return;
//        }

        SharedPreferences sp = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        String shownTodayStr = "shownToday"+getDate().replace("-", "");
        shownToday = sp.getBoolean(shownTodayStr, false);
        if (shownToday){
            isPopupShowing = true;
            return;
        }

        if (askForDonations>=200 && !isPopupShowing){
            askForDonations = 0;
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.donate_popup);

            Button dialogButton = (Button) dialog.findViewById(R.id.donateBtn);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    donationGateway();
                    dialog.dismiss();
                    ed.putBoolean(shownTodayStr, shownToday);
                    ed.apply();
                }
            });
            TextView dismissBtn = (TextView) dialog.findViewById(R.id.maybeLaterDonateBtn);
            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isPopupShowing = false;
                    shownToday = true;
                    dialog.dismiss();
                    ed.putBoolean(shownTodayStr, shownToday);
                    ed.apply();
                }
            });

            isPopupShowing = true;
            dialog.show();


        }


        ed.putInt("askForDonations", askForDonations);
//        ed.putBoolean(shownTodayStr, shownToday);
//        ed.putBoolean(getDate().replace("-", ""), shownToday);


        ed.apply();

    }

    private void donationGateway(){
//        Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cowinnotifier.site/donate"));
        startActivity(browserIntent);
    }

    public void initializeAdCodes(boolean testMode) {


        AudienceNetworkAds.initialize(this);

        if (testMode) {
            AdSettings.setTestMode(true);  // Test Mode
            FBAdviewCode = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
            FBAdview2Code = FBAdviewCode;
            FBAdview3Code = FBAdviewCode;
            FBintadCode = FBAdviewCode; //Sample Code
//            FBintadCode = "FBAdviewCode"; //Sample Code


            GoogleBanner1AdCode = "ca-app-pub-3940256099942544/6300978111";
            GoogleBanner2AdCode = GoogleBanner1AdCode;
            GoogleBanner3AdCode = GoogleBanner1AdCode;
            GoogleintadCode = "ca-app-pub-3940256099942544/1033173712";
        } else {

            FBAdviewCode = "829049141062090_829050204395317"; //Banner Top
            FBAdview2Code = "829049141062090_829050397728631"; // Banner Middle Big
            FBAdview3Code = "829049141062090_829050627728608"; //Banner Bottom
            FBintadCode = "829049141062090_829049697728701"; //Interstitial Ad


            GoogleBanner1AdCode = "ca-app-pub-8318706732545213/9313616493";
            GoogleBanner2AdCode = "ca-app-pub-8318706732545213/5182799795";
            GoogleBanner3AdCode = "ca-app-pub-8318706732545213/9901893563";
            GoogleintadCode = "ca-app-pub-8318706732545213/4991228102";
        }
    }


    public void loadInterstitial() {

        // initializing InterstitialAd Object

        // InterstitialAd Constructor Takes 2 Arguments

        // 1)Context

        // 2)Placement Id

        intad = new InterstitialAd(this, FBintadCode);

        // loading Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

            @Override

            public void onInterstitialDisplayed(Ad ad) {

                // Showing Toast Message

//                Toast.makeText(MainActivity.this, "onInterstitialDisplayed", Toast.LENGTH_SHORT).show();

            }

            @Override

            public void onInterstitialDismissed(Ad ad) {

                // Showing Toast Message

//                Toast.makeText(MainActivity.this, "onInterstitialDismissed", Toast.LENGTH_SHORT).show();

            }

            @Override

            public void onError(Ad ad, AdError adError) {

                // Showing Toast Message

//                Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                loadGoogleInterestitialAd();

            }

            @Override

            public void onAdLoaded(Ad ad) {

                // Showing Toast Message

//                Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                intad.show();

            }

            @Override

            public void onAdClicked(Ad ad) {

                // Showing Toast Message

//                Toast.makeText(MainActivity.this, "onAdClicked", Toast.LENGTH_SHORT).show();

            }

            @Override

            public void onLoggingImpression(Ad ad) {

                // Showing Toast Message

//                Toast.makeText(MainActivity.this, "onLoggingImpression", Toast.LENGTH_SHORT).show();

            }

        };


//        intad.loadAd();
        intad.loadAd(
                intad.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }


    public com.google.android.gms.ads.AdView createGoogleBanner(String adCode, LinearLayout bannerBox){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        com.google.android.gms.ads.AdView adViewG = new com.google.android.gms.ads.AdView(this);
        adViewG.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
//        adViewG.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        adViewG.setAdUnitId(adCode);
//        ((LinearLayout)findViewById(R.id.BannerBox)).addView(adViewG);
        bannerBox.addView(adViewG);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adViewG.loadAd(adRequest);
        return adViewG;

    }

    public void showGoogleBanner(com.google.android.gms.ads.AdView adViewG){
        adViewG.loadAd(new AdRequest.Builder().build());
//        boolean temp = false;
//        if (adView!=null)
//
//        {
////            adView.setVisibility(View.GONE);
//            adView.destroy();
//            showGoogleBanner(adViewGoogle1);
//            temp = true;
//        }
//        if (adView2!=null)
//        {
//            adView2.destroy();
//            showGoogleBanner(adViewGoogle2);
//            temp = true;
//        }
//        if (adView3!=null)
//        {
//            adView3.destroy();
//            showGoogleBanner(adViewGoogle3);
//            temp = true;
//        }
//
//        if (temp){
//            loadBannerAds();
//        }

    }

    public AdView createFacebookBanner(String adCode, LinearLayout bannerBox){
        AdView adViewF = new AdView(this, adCode, AdSize.BANNER_HEIGHT_50);
//        adView.setId(cntt++);
//        ((LinearLayout)findViewById(R.id.BannerBox)).addView(adViewF);
        bannerBox.addView(adViewF);
        return adViewF;

    }

    public void showFacebookBanner(AdView adV){
        Log.d("adsStatus", String.valueOf(showFacebookAds));
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
//                Toast.makeText(
//                        MainActivity.this,
//                        "Error: " + adError.getErrorMessage(),
//                        Toast.LENGTH_LONG)
//                        .show();
//                cntt = 0;
//                AdView adView = new AdView(this);
//
//                adView.setAdSize(AdSize.BANNER);
//                adView = new AdView(this, AdviewCode, AdSize.BANNER_HEIGHT_50);
//                adView.setId(cntt++);
//                ((LinearLayout)findViewById(R.id.BannerBox)).addView(adView);
//                adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
//                Banner1();
                showFacebookAds = false;
//                ReplacementGoogleBanner.setVisibility(View.VISIBLE);
                adV.setVisibility(View.GONE);
                adV.destroy();

//                showGoogleBanner(ReplacementGoogleBanner);
//                adView.setVisibility(View.GONE);
//                adView.destroy();





            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
//                showFacebookAds = true;
//                ReplacementGoogleBanner.setVisibility(View.GONE);
                adV.setVisibility(View.VISIBLE);
//                ReplacementGoogleBanner.destroy();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };
        adV.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }


    public void loadGoogleInterestitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        com.google.android.gms.ads.interstitial.InterstitialAd.load(this,GoogleintadCode, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
//                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(MainActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
            }
        });
    }




}
//startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:"+getPackageName())));