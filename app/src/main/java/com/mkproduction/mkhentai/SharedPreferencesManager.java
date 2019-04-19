package com.mkproduction.mkhentai;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferencesManager sm;
    private Gson gson = new Gson();
    private String SharePreferenceKey = "favorite_manga";

    private SharedPreferencesManager() {
//        this.context = context;
    }

    public static SharedPreferencesManager getInstance(Context context) {
        if (sm == null) {
            sm = new SharedPreferencesManager();
            sharedPreferences = context.getSharedPreferences("KEY", Context.MODE_PRIVATE);
        }
        return sm;
    }

    public void AddOrRemove(Manga manga) {

        String json = sharedPreferences.getString(SharePreferenceKey, "");
        List<Manga> mangas = new ArrayList<>();
        boolean isFav = false;
        if (!json.isEmpty())
            mangas = gson.fromJson(json, new TypeToken<List<Manga>>() {
            }.getType());

        for (Manga m : mangas) {
            if (m.getTitle().equals(manga.getTitle())) {
                isFav = true;
                mangas.remove(m);
                break;
            }

        }
        if (!isFav)
            mangas.add(manga);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharePreferenceKey, gson.toJson(mangas));
        editor.apply();
    }

    public boolean isContain(Manga manga) {
        String json = sharedPreferences.getString(SharePreferenceKey, "");
        List<Manga> mangas = new ArrayList<>();

        if (!json.isEmpty())
            mangas = gson.fromJson(json, new TypeToken<List<Manga>>() {
            }.getType());
        for (Manga m : mangas)
            if (m.getTitle().equals(manga.getTitle()))
                return true;
        return false;
    }

    public List<Manga> getAll() {

        String json = sharedPreferences.getString(SharePreferenceKey, "");
        List<Manga> mangas = new ArrayList<>();
        if (!json.isEmpty()) {
            mangas = gson.fromJson(json, new TypeToken<List<Manga>>() {
            }.getType());

        }
        return mangas;
    }
}
