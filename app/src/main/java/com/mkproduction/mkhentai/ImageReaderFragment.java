package com.mkproduction.mkhentai;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;


public class ImageReaderFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    private Manga manga;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private LinearLayout lnBottom,lnTop;
    private int selectedPosition = 0;
    SeekBar seekBar;

    static ImageReaderFragment newInstance() {
        ImageReaderFragment f = new ImageReaderFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager =  v.findViewById(R.id.viewpager);
        lblCount =  v.findViewById(R.id.lbl_count);
        lblTitle =  v.findViewById(R.id.title);
        lblDate =  v.findViewById(R.id.date);
        lnBottom = v.findViewById(R.id.lnBottom);
        lnTop = v.findViewById(R.id.lnTop);
        seekBar = v.findViewById(R.id.seekBar);
        manga = (Manga) getArguments().getSerializable("manga");
        selectedPosition = getArguments().getInt("position");

        seekBar.setMax(manga.getSize() - 1);
        seekBar.setOnSeekBarChangeListener(this);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(position);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
            seekBar.setProgress(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + manga.getSize());
        lblTitle.setText(manga.getTitle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(progress % 5 == 0)
            for (int i = progress + 1; i < manga.getImageUrls().size() && i <= progress + 5; i++) {
                Glide.with(getActivity()).downloadOnly().load(manga.getImage(i)).submit();
            }
        setCurrentItem(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.manga_image_reader, container, false);

            final SubsamplingScaleImageView imageViewPreview = view.findViewById(R.id.image_preview);

            Glide.with(getActivity())
                .asBitmap()
                .load(manga.getImage(position))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageViewPreview.setImage(ImageSource.bitmap(resource));
                    }
                });

            container.addView(view);
            imageViewPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lnBottom.getVisibility() == View.INVISIBLE){
                        lnBottom.setVisibility(View.VISIBLE);
                        lnTop.setVisibility(View.VISIBLE);
                    }

                    else{
                        lnBottom.setVisibility(View.INVISIBLE);
                        lnTop.setVisibility(View.INVISIBLE);
                    }

                }
            });

            return view;
        }

        @Override
        public int getCount() {
            return manga.getSize();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
