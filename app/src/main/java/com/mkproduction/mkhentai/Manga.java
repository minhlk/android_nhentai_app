package com.mkproduction.mkhentai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Manga implements Serializable {
    private int mangaId;
    private String title;
    private String coverImage;
    private String tags;
    private List<String> imageUrls ;
    private List<String> thumbnailUrls ;
    private String url;
    private boolean isFav = false;
    public Manga(int mangaId, String title) {
        this.mangaId = mangaId;
        this.title = title;
        imageUrls = new ArrayList<>();
        thumbnailUrls= new ArrayList<>();
    }
    public String getImage(int position){
        return imageUrls.get(position);
    }

    public void addImage(String url){
        imageUrls.add(url);
    }

    public void addThumnail(String url){
        thumbnailUrls.add(url);
    }

    public int getSize(){
        return imageUrls.size();
    }

    public int getMangaId() {
        return mangaId;
    }

    public void setMangaId(int mangaId) {
        this.mangaId = mangaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getThumbnailUrls() {
        return thumbnailUrls;
    }

    public void setThumbnailUrls(List<String> thumbnailUrls) {
        this.thumbnailUrls = thumbnailUrls;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav() {
        isFav = !isFav;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "mangaId=" + mangaId +
                ", title='" + title + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", tags='" + tags + '\'' +
                ", imageUrls=" + imageUrls +
                ", thumbnailUrls=" + thumbnailUrls +
                ", url='" + url + '\'' +
                ", isFav=" + isFav +
                '}';
    }
}
