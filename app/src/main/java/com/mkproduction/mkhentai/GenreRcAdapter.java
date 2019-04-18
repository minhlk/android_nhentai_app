package com.mkproduction.mkhentai;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GenreRcAdapter extends RecyclerView.Adapter<GenreRcAdapter.ViewHolder> {

    private static final String HOME_URL = "https://nhentai.net";
    private List<Genre> genres, copyGenres;
    private Context context;

    public GenreRcAdapter(List<Genre> genres) {
        this.genres = genres;
        this.copyGenres = new ArrayList<>();
        this.copyGenres.addAll(genres);
    }


    @Override
    public GenreRcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.genre_row_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(homeView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(GenreRcAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Genre Genre = genres.get(position);
        viewHolder.tTitle.setText(Genre.getTitle());

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tTitle = itemView.findViewById(R.id.tTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {
                //TODO : add get all manga with this tag

//                Toast.makeText(context, genres.get(position).getUrl(),Toast.LENGTH_LONG).show();
                HomeFragment fragment = HomeFragment.newInstance();
                fragment.setTag(genres.get(position).getUrl(),HomeFragment.GENRE_OPTION);
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "home_fragment").commit();


            }
        }


    }


}
