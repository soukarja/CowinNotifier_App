package com.cowinnotifier.alertsappforcowinindia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class latestUpdatesActivity extends AppCompatActivity {

    private  adsManager ads;
    private LinearLayout bannerBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_updates);

        ads = new adsManager(latestUpdatesActivity.this, false);
        getAllNews();

        bannerBox = (LinearLayout) findViewById(R.id.BannerBox);


        ((TextView) findViewById(R.id.backBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ScrollView sc = (ScrollView) findViewById(R.id.scrollView);
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.scrollToTopButton);
        fab.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sc.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    Log.d("scrollPosition", String.valueOf(scrollY));
                    if (scrollY >= 1000) {
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.GONE);
                    }
                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sc.scrollTo(0,0);
                sc.smoothScrollTo(0, 0);
            }
        });
    }


    private void getAllNews(){
        ((RecyclerView) findViewById(R.id.newsrecycler)).setVisibility(View.GONE);
        ((LottieAnimationView) findViewById(R.id.listLoading)).setVisibility(View.VISIBLE);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://cowinnotifier.site/news/api.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<newsModel> newsList = new ArrayList<>();
                    JSONArray articles = response.getJSONArray("articles");
                    for (int i = 0; i< articles.length(); i++)
                    {
                        JSONObject news = articles.getJSONObject(i);
                        JSONObject source = news.getJSONObject("source");


                        String credits = "Source: "+source.getString("name");
                        String title = news.getString("title");
                        String desc = news.getString("description");
                        String link = news.getString("url");
                        String image = news.getString("urlToImage");

                        newsList.add(new newsModel(title, image, desc, credits, link));

                    }

                    if (newsList.size() > 0)
                    {
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.newsrecycler);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        newsAdapter adapter = new newsAdapter(getApplicationContext(), newsList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        ((RecyclerView) findViewById(R.id.newsrecycler)).setVisibility(View.VISIBLE);
                        ((LottieAnimationView) findViewById(R.id.listLoading)).setVisibility(View.GONE);
                    }


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