package com.mkproduction.mkhentai;

public interface IFetchData {

    void makeRequest(String url);

    Object parseResponse(String html);

}
