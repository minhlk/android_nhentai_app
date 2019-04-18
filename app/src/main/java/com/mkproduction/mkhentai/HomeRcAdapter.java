package com.mkproduction.mkhentai;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

public class HomeRcAdapter extends RecyclerView.Adapter<HomeRcAdapter.ViewHolder> {

    private static final String HOME_URL = "https://nhentai.net";
    private List<Manga> mangas;
    private Context context;

    public HomeRcAdapter(List<Manga> mangas) {
        this.mangas = mangas;
    }

    @Override
    public HomeRcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.row_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(homeView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(HomeRcAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Manga manga = mangas.get(position);
        viewHolder.tCaption.setText(manga.getTitle());
        Glide.with(context).load(manga.getCoverImage()).into(viewHolder.imCover);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mangas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tCaption;
        public ImageView imCover;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imCover = itemView.findViewById(R.id.imCover);
            tCaption = itemView.findViewById(R.id.tCaption);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it

                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("url", HOME_URL + mangas.get(position).getUrl());

                context.startActivity(i);
            }
        }
    }
}
