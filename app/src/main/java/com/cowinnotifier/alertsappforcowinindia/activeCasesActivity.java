package com.cowinnotifier.alertsappforcowinindia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kotlin.reflect.KFunction;

public class activeCasesActivity extends AppCompatActivity {
    private  adsManager ads;
    private AdView adView, adView2;
    public com.google.android.gms.ads.AdView adViewGoogle1, adViewGoogle2;
    private long adsTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_cases);

        ads = new adsManager(activeCasesActivity.this, false);

        setLastUpdated();
        showGlobalData();
        showIndiaData();
        loadBannerAds();


        ((TextView) findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((Button) findViewById(R.id.trackVaccinesBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setLastUpdated();
                        ads.showInterstitialAds();
                        loadBannerAds();
                        ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                    }
                }, 1500);

            }
        });

    }

    private void loadBannerAds() {

        if (adsTime + 10000 > System.currentTimeMillis()) {
            return;
        }
        adsTime = System.currentTimeMillis();

        if (adView == null)
            adView = ads.createFacebookBanner(ads.FacebookBanner1, (LinearLayout) findViewById(R.id.BannerBox1));

        if (adViewGoogle1 == null)
            adViewGoogle1 = ads.createGoogleBanner(ads.GoogleBanner1, (LinearLayout) findViewById(R.id.BannerBox1));

        if (adView2 == null)
            adView2 = ads.createFacebookBanner(ads.FacebookBanner2, (LinearLayout) findViewById(R.id.BannerBox2));

        if (adViewGoogle2 == null)
            adViewGoogle2 = ads.createGoogleBanner(ads.GoogleBanner2, (LinearLayout) findViewById(R.id.BannerBox2));


        ads.showBannerAds(adView, adViewGoogle1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ads.showBannerAds(adView2, adViewGoogle2);
            }
        }, 2000);
    }

    private void setLastUpdated() {
        String date = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(new Date());
        String dateTxt = "Last Updated On: " + date;
        ((TextView) findViewById(R.id.lastUpdatedIndia)).setText(dateTxt);
        ((TextView) findViewById(R.id.lastUpdatedGlobal)).setText(dateTxt);
    }

    private String formatNumber(int n) {
        BigDecimal number = new BigDecimal(n);
        return formatNumber(number);
    }

    private String formatNumber(BigDecimal n) {
        return new DecimalFormat("##,##,###.##").format(n);
    }

    private void showGlobalData() {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://disease.sh/v3/covid-19/all", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String totalCases = formatNumber(response.getInt("cases"));
                    String todayCases = formatNumber(response.getInt("todayCases"));
                    String deaths = formatNumber(response.getInt("deaths"));
                    String todayDeaths = formatNumber(response.getInt("todayDeaths"));
                    String recovered = formatNumber(response.getInt("recovered"));
                    String todayRecovered = formatNumber(response.getInt("todayRecovered"));
                    String affectedCountries = formatNumber(response.getInt("affectedCountries"));

                    ((TextView)findViewById(R.id.totalCasesNumber)).setText(totalCases);
                    ((TextView)findViewById(R.id.totalCasesNumberToday)).setText("+ "+todayCases);
                    ((TextView)findViewById(R.id.deathsNumber)).setText(deaths);
                    ((TextView)findViewById(R.id.deathsToday)).setText("+ "+todayDeaths);
                    ((TextView)findViewById(R.id.recoveredNumber)).setText(recovered);
                    ((TextView)findViewById(R.id.recoveredToday)).setText("+ "+todayRecovered);
                    ((TextView)findViewById(R.id.affectedCountriesNumber)).setText(affectedCountries);


                } catch (JSONException e) {
                    Log.d("mycenters", "Error Occurred");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Log.d("mycenters", String.valueOf(error));
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
    private void showIndiaData() {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://disease.sh/v3/covid-19/countries/India", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String totalCases = formatNumber(response.getInt("cases"));
                    String todayCases = formatNumber(response.getInt("todayCases"));
                    String deaths = formatNumber(response.getInt("deaths"));
                    String todayDeaths = formatNumber(response.getInt("todayDeaths"));
                    String recovered = formatNumber(response.getInt("recovered"));
                    String todayRecovered = formatNumber(response.getInt("todayRecovered"));
                    int tst = response.getInt("tests");
                    String tests = formatNumber(tst);
//                    BigDecimal perc = (new BigDecimal(response.getInt("tests")).multiply(new BigDecimal("100"))).divide(new BigDecimal("1366400000"));
//                    BigDecimal perc = (new BigDecimal(response.getInt("tests"))).divide(new BigDecimal("1366400000"));
//                    BigDecimal perc = (new BigDecimal(tst)).divide(new BigDecimal("1366400000"));
                    BigDecimal perc = new BigDecimal((double)tst/13664000);
//                    float perc = (float) (tst / 1366400000);
//                    String percentage = formatNumber(new BigDecimal(perc));
                    String percentage = formatNumber(perc);
//                    String percentage = perc.toString();

                    ((TextView)findViewById(R.id.totalCasesIndiaNumber)).setText(totalCases);
                    ((TextView)findViewById(R.id.totalCasesNumberIndiaToday)).setText("+ "+todayCases);
                    ((TextView)findViewById(R.id.deathsIndiaNumber)).setText(deaths);
                    ((TextView)findViewById(R.id.deathsIndiaToday)).setText("+ "+todayDeaths);
                    ((TextView)findViewById(R.id.recoveredIndiaNumber)).setText(recovered);
                    ((TextView)findViewById(R.id.recoveredIndiaToday)).setText("+ "+todayRecovered);
                    ((TextView)findViewById(R.id.vaccinatedIndiaNumber)).setText(tests);
                    ((TextView)findViewById(R.id.vaccinatedIndiaPercentage)).setText(percentage+" %");


                } catch (JSONException e) {
                    Log.d("mycenters", "Error Occurred");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Log.d("mycenters", String.valueOf(error));
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}