package com.mkproduction.mkhentai;

import java.util.List;

public interface IFetchListData {

    void makeRequest(int page);

    List parseResponse(String html);

}
