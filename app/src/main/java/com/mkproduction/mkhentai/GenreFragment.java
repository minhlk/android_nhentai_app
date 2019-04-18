package com.mkproduction.mkhentai;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.*;
import android.view.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GenreFragment extends Fragment  {
    private String home_url = "https://nhentai.net/tags/?page=";
    RecyclerView rcGenre;
    GenreRcAdapter adapter;
    List<Genre> genres = new ArrayList<>();
    public static GenreFragment genreFragment;
    int page = 1;

    public static GenreFragment newInstance() {
        if (genreFragment == null) {
            genreFragment = new GenreFragment();
        }
        return genreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : GET GENRES
        fetchGenrePage(page++);
        //TODO : RCGENRES
        rcGenre = view.findViewById(R.id.rcGenre);
        adapter = new GenreRcAdapter(genres);
        rcGenre.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rcGenre.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);

        rcGenre.setLayoutManager(gridLayoutManager);
        rcGenre.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                fetchGenrePage(page++);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void fetchGenrePage(int page) {
        String url = home_url + page;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        genres.addAll(parseInfoFromGenre(response));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                t1.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);
    }

    public List<Genre> parseInfoFromGenre(String html) {
        Document doc = Jsoup.parse(html);
        Elements gallery = doc.getElementById("tag-container").getElementsByTag("a");
        List<Genre> genres = new ArrayList<>();

        for (Element el : gallery) {
            String url = el.attr("href");
            String title = el.text();
            Genre genre = new Genre(title, url);
            genres.add(genre);

        }


        return genres;


    }
}

