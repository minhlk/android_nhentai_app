package com.mkproduction.mkhentai;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DetailActivity extends AppCompatActivity implements IFetchData {
    private static final String HOME_URL = "https://nhentai.net";
    private static final int IN_FAV = 1;
    private static final int NOT_IN_FAV = 0;
    private TextView tags, title;
    private FloatingActionButton fabView, fabFav;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView toolbarImage;
    private Manga manga;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabView = findViewById(R.id.fabView);
        fabFav = findViewById(R.id.fabFav);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        toolbarImage = findViewById(R.id.toolbarImage);
        tags = findViewById(R.id.tags);
        title = findViewById(R.id.title);
        context = getApplicationContext();
        String url = getIntent().getExtras().getString("url");
        makeRequest(url);


    }

    private void changeFavState(int state) {
        if (state == IN_FAV) {
            fabFav.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bookmark_white_24dp, null));
            fabFav.setTag(IN_FAV);
        } else {
            fabFav.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bookmark_border_white_24dp, null));
            fabFav.setTag(NOT_IN_FAV);
        }
    }

    public void makeRequest(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        manga = parseResponse(response);
                        manga.setUrl(url.replace(HOME_URL, ""));
                        collapsingToolbar.setTitle(manga.getTitle());
                        title.setText(manga.getTitle());
                        tags.setText(manga.getTags());
                        Glide.with(context).load(manga.getCoverImage()).into(toolbarImage);

                        if (SharedPreferencesManager.getInstance(context).isContain(manga))
                            changeFavState(IN_FAV);
                        else
                            changeFavState(NOT_IN_FAV);

                        fabView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startFragment(manga);
                            }
                        });
                        fabFav.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //SAVE OR REMOVE FAVORITE
                                SharedPreferencesManager.getInstance(context).AddOrRemove(manga);
                                changeFavState(1 - (int) fabFav.getTag());
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public Manga parseResponse(String html) {
        Document doc = Jsoup.parse(html);
        //GET MANGA INFO
        String title = doc.getElementById("info").getElementsByTag("h1").text();
        String coverImage = doc.getElementById("cover").getElementsByTag("img").attr("data-src");
        String tags = doc.getElementById("tags").getElementsByTag("a").text().replace(")", "),");
        Manga manga = new Manga(1, title);
        Elements elements = doc.getElementsByClass("thumb-container");
        for (Element el : elements) {
            String noScript = el.getElementsByTag("noscript").html().split("\"")[1];
            //GET ABSOLUTE URL FOR IMAGE
            String fullImage = noScript
                    .replace("t.", ".")
                    .replace("//.", "//i.");
            manga.addImage(fullImage);
            manga.addThumnail(noScript);
        }
        manga.setCoverImage(coverImage);
        manga.setTags(tags);

        return manga;
    }


    private void startFragment(Manga manga) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("manga", manga);
        bundle.putInt("position", 0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ImageReaderFragment newFragment = ImageReaderFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");
    }


}
