package com.cowinnotifier.alertsappforcowinindia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.viewHolder> {
    private Context context;
    private List<newsModel> newsList;

    public newsAdapter(Context context, List<newsModel> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public newsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_update_row, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsAdapter.viewHolder holder, int position) {
        String headline = newsList.get(position).getNewsTitle();
        String description = newsList.get(position).getNewsContent();
        String img = newsList.get(position).getNewsImage();
        String creditsTxt = newsList.get(position).getNewsCredits();
        String link = newsList.get(position).getNewsLink();

        holder.setData(headline, description, img, creditsTxt, link);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title, content, credits;
        private LinearLayout newsBox;
        private Context myContext;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageHere);
            title = itemView.findViewById(R.id.headLine);
            content = itemView.findViewById(R.id.content);
            credits = itemView.findViewById(R.id.creditsTxt);
            newsBox = itemView.findViewById(R.id.newsBox);

            myContext = itemView.getContext();
        }

        public void setData(String headline, String description, String img, String creditsTxt, String link) {
            Glide.with(context).load(img).into(imageView);
            title.setText(headline);
            content.setText(description);
            credits.setText(creditsTxt);

            newsBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), inbuiltWebviewActivity.class);
                    intent.putExtra("link", link);
                    intent.putExtra("header", "Latest Updates");
                    myContext.startActivity(intent);
                }
            });


        }
    }
}
