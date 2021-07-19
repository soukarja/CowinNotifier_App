package com.cowinnotifier.alertsappforcowinindia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class onBoardingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public int[] images = {
            R.raw.on_boarding_asset1,
            R.raw.on_boarding_asset2,
            R.raw.on_boarding_asset3
    };

    public String[] headlines = {
            "Track Vaccine Slot",
            "Track Covid Stats",
            "Read Latest News"
    };
    public String[] subHeadings = {
            "With Instant Alerts",
            "In Real Time",
            "And Stay Updated"
    };
    public String[] content = {
            "Get Notifications whenever vaccines are available in your locality and Never miss a Slot Again!\n#GetVaccinated",
            "Monitor Detailed Insights about the Covid Situation in India and around the World!",
            "Read and Stay Updated with Latest Articles about the pandemic from popular sources around the world."
    };

    public onBoardingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return subHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slide, container, false);

        ImageView image = (ImageView) view.findViewById(R.id.topImage);
        TextView headerBox = (TextView) view.findViewById(R.id.headLine);
        TextView subheaderBox = (TextView) view.findViewById(R.id.subLine);
        TextView contentBox = (TextView) view.findViewById(R.id.content);

        image.setImageResource(images[position]);
        headerBox.setText(headlines[position]);
        subheaderBox.setText(subHeadings[position]);
        contentBox.setText(content[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((RelativeLayout) object);
    }
}
