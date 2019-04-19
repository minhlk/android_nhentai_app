package com.mkproduction.mkhentai;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.Toast;
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


public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, IFetchListData {
    public static final int HOME_OPTION = 1;
    public static final int GENRE_OPTION = 2;
    public static final int TITLE_OPTION = 3;
    private static HomeFragment homeFragment = null;
    private FloatingActionButton fab;
    private RecyclerView rcHome;
    private List<Manga> mangas = new ArrayList<>();
    private boolean isHomePage = true;
    private HomeRcAdapter adapter;
    private String homeUrl = "https://nhentai.net?page=";
    private int page = 1;

    public static HomeFragment newInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();

        }
        return homeFragment;
    }

    public void setTag(String tagUrl, int option) {
        page = 1;
        mangas = new ArrayList<>();
        switch (option) {
            case HOME_OPTION:
                homeUrl = "https://nhentai.net/?page=";
                setHomePage(true);
                break;
            case GENRE_OPTION:
                homeUrl = "https://nhentai.net" + tagUrl + "?page=";
                setHomePage(false);
                break;
            case TITLE_OPTION:
                homeUrl = "https://nhentai.net/search/?q=" + tagUrl + "&page=";
                setHomePage(false);
                break;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fabView);
        rcHome = view.findViewById(R.id.rcHome);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "something", Toast.LENGTH_SHORT).show();

            }
        });
        makeRequest(page++);
        adapter = new HomeRcAdapter(mangas);
        rcHome.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rcHome.setAdapter(adapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        rcHome.setLayoutManager(linearLayout);
        rcHome.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                makeRequest(page++);
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchBar).getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean isHomePage() {
        return isHomePage;
    }

    public void setHomePage(boolean homePage) {
        isHomePage = homePage;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        setTag(s, TITLE_OPTION);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }


    @Override
    public void makeRequest(int page) {
        String url = homeUrl + page;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mangas.addAll(parseResponse(response));
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public List<Manga> parseResponse(String html) {
        Document doc = Jsoup.parse(html);
        Elements gallery = doc.getElementsByClass("gallery");
        List<Manga> mangas = new ArrayList<>();

        for (Element el : gallery) {
            String url = el.getElementsByTag("a").attr("href");
            String coverImage = el.getElementsByTag("img").attr("data-src");
            String caption = el.getElementsByClass("caption").text();
            //GET ABSOLUTE URL FOR IMAGE
            Manga manga = new Manga(1, caption);
            manga.setCoverImage(coverImage);
            manga.setUrl(url);
            mangas.add(manga);
        }
        return mangas;
    }
}
