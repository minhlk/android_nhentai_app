package com.mkproduction.mkhentai;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    private static final String SHARED_PREFERENCES_FAV_LIST = "favorite_manga";
    public static SharedPreferences sharedPreferences;

    public static void AddOrRemove(Context context, Manga manga) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FAV_LIST, Context.MODE_PRIVATE);
        String jMangas = sharedPreferences.getString(SHARED_PREFERENCES_FAV_LIST, "");
        List<Manga> mangas = new ArrayList<>();
        Gson gson = new Gson();
        boolean isFav = false;
        if (!jMangas.isEmpty())
            mangas = gson.fromJson(jMangas, new TypeToken<List<Manga>>() {
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
        editor.putString(SHARED_PREFERENCES_FAV_LIST, gson.toJson(mangas));
        editor.commit();
    }

    public static boolean isContain(Context context, Manga manga) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FAV_LIST, context.MODE_PRIVATE);
        String jMangas = sharedPreferences.getString(SHARED_PREFERENCES_FAV_LIST, "");
        List<Manga> mangas = new ArrayList<>();
        Gson gson = new Gson();
        boolean isFav = false;
        if (!jMangas.isEmpty())
            mangas = gson.fromJson(jMangas, new TypeToken<List<Manga>>() {
            }.getType());
        for (Manga m : mangas)
            if (m.getTitle().equals(manga.getTitle()))
                return true;
        return false;
    }
    public static List<Manga> getAll(Context context){
        //TODO : GET SHAREDPREFERENCES MANGAS
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FAV_LIST, context.MODE_PRIVATE);
        // Get saved string data in it.
        String jsonMangas = sharedPreferences.getString(SHARED_PREFERENCES_FAV_LIST, "");
        List<Manga> mangas = new ArrayList<>();
        if(!jsonMangas.isEmpty()){
            Gson gson = new Gson();
            mangas = gson.fromJson(jsonMangas, new TypeToken<List<Manga>>(){}.getType());

        }
        return mangas;
    }
}
