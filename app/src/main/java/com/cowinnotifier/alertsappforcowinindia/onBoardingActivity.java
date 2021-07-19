package com.cowinnotifier.alertsappforcowinindia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class onBoardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;

    private TextView[] dots;
    private TextView skipBtn, nextBtn, prevBtn;
    private int currentPage;

    private onBoardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.dotsLayout);

        skipBtn = findViewById(R.id.skipBtn);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < dots.length - 1)
                    viewPager.setCurrentItem(currentPage + 1);
                else
                    startApp();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPage > 0)
                    viewPager.setCurrentItem(currentPage - 1);
            }
        });

        prevBtn.setVisibility(View.GONE);
        prevBtn.setText("< Previous");

        adapter = new onBoardingAdapter(this);
        viewPager.setAdapter(adapter);
        addDots(0);
        viewPager.addOnPageChangeListener(viewListner);


    }

    private void startApp(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void addDots(int position) {
        dots = new TextView[adapter.images.length];
        linearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.lightWhite));
            linearLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.darkgreen));
        }
    }

    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPage = position;
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            skipBtn.setVisibility(View.VISIBLE);
            nextBtn.setText("Next >");
            if (position == 0)
            {
                prevBtn.setVisibility(View.GONE);
            }
            else if (position == adapter.images.length -1)
            {
                nextBtn.setText("Get Started");
                skipBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        if (currentPage > 0)
            viewPager.setCurrentItem(currentPage - 1);
    }
}