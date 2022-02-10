package com.cowinnotifier.alertsappforcowinindia;


import android.app.Activity;
import android.widget.LinearLayout;

import java.util.concurrent.Callable;

public class adsManager {

    // Version Ad-Free
    public String FacebookBanner1, FacebookBanner2, FacebookBanner3;
    public String GoogleBanner1, GoogleBanner2, GoogleBanner3, GoogleinterstitialAd;

    public adsManager(Activity activity, boolean testMode) {
        this.GoogleinterstitialAd = null;
    }

    //Function to show Banner Ads previously Created, with Preference given to Facebook and then Google
    public void showBannerAds(String fBBanner, String googleBanner) {
        return;
    }

    //Function Overloading to show Interstitial Ads if the Ad Codes are mentioned within the AdsManager Class
    public void showInterstitialAds() {
        return;
    }

    public String createFacebookBanner(String code, LinearLayout layout)
    {
        return  FacebookBanner1;
    }
    public String createGoogleBanner(String code, LinearLayout layout)
    {
        return  GoogleBanner1;
    }

    public void preLoadGoogleInterstitialAD(){
        return;
    }

    public void showPreloadedGoogleInterstitialAd(){
        return;
    }

    public void showPreloadedGoogleInterstitialAd(Callable<Void> onExecuted){
        try {
            onExecuted.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}