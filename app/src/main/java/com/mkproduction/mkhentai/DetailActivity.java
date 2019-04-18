package com.mkproduction.mkhentai;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DetailActivity extends AppCompatActivity {
    private static final String SHARED_PREFERENCES_FAV_LIST = "favorite_manga";
    private static final String HOME_URL = "https://nhentai.net";
    TextView tags, title;
    FloatingActionButton fabView, fabFav;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView toolbarImage;
    Manga manga;

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
        String url = getIntent().getExtras().getString("url");
        makeRequest(url);


    }

    public void makeRequest(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        manga = parseInfoFromDetailPage(response);
                        manga.setUrl(url.replace(HOME_URL, ""));
                        collapsingToolbar.setTitle(manga.getTitle());
                        title.setText(manga.getTitle());
                        tags.setText(manga.getTags());
                        Glide.with(getApplicationContext()).load(manga.getCoverImage()).into(toolbarImage);
                        if (SharedPreferencesManager.isContain(getApplicationContext(), manga)) {
                            fabFav.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bookmark_white_24dp, null));
                            fabFav.setTag(1);
                        } else {
                            fabFav.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bookmark_border_white_24dp, null));
                            fabFav.setTag(0);
                        }

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
                                SharedPreferencesManager.AddOrRemove(getApplicationContext(), manga);
                                fabFav.setTag(1 - (int) fabFav.getTag());
                                if (fabFav.getTag().equals(1)) {
                                    fabFav.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bookmark_white_24dp, null));
                                } else {
                                    fabFav.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bookmark_border_white_24dp, null));
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                t1.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);
    }

    public Manga parseInfoFromDetailPage(String html) {
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


//       im1.setImage( ImageSource.uri(temp));
//        Glide.with(this).load(temp).into(im1);

//        Glide.with(this)
//                .asBitmap()
//                .load(temp)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        im1.setImage(ImageSource.bitmap(resource));
//                    }
//                });
    }

    public void startFragment(Manga manga) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("manga", manga);
        bundle.putInt("position", 0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ImageReaderFragment newFragment = ImageReaderFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");
    }

}
