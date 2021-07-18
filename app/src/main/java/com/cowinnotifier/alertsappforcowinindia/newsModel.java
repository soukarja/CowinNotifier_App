package com.cowinnotifier.alertsappforcowinindia;

public class newsModel {
    String newsTitle;
    String newsImage;
    String newsContent;
    String newsCredits;
    String newsLink;

    public newsModel(String newsTitle, String newsImage, String newsContent, String newsCredits, String newsLink) {
        this.newsTitle = newsTitle;
        this.newsImage = newsImage;
        this.newsContent = newsContent;
        this.newsCredits = newsCredits;
        this.newsLink = newsLink;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public String getNewsCredits() {
        return newsCredits;
    }

    public String getNewsLink() {
        return newsLink;
    }
}
