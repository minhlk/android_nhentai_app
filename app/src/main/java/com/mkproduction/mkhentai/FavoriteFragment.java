package com.mkproduction.mkhentai;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    FloatingActionButton fab;
    RecyclerView rcFavorite;
    List<Manga> mangas = new ArrayList<>();
    HomeRcAdapter adapter;
    public static FavoriteFragment favoriteFragment = null;

    public static FavoriteFragment newInstance() {
        if (favoriteFragment == null) {
            favoriteFragment = new FavoriteFragment();

        }
        return favoriteFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = view.findViewById(R.id.fabView);
        rcFavorite = view.findViewById(R.id.rcFavorite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "something", Toast.LENGTH_SHORT).show();

            }
        });
        mangas = SharedPreferencesManager.getAll(getContext());
        adapter = new HomeRcAdapter(mangas);
        rcFavorite.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rcFavorite.setAdapter(adapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        rcFavorite.setLayoutManager(linearLayout);



    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



}
