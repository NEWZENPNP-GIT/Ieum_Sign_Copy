package com.ezsign.window.service;

import com.ezsign.window.vo.YW700Request;
import com.ezsign.window.vo.YW700Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW700Service {

    void getYW700(String 계약ID, String 사용자ID, YW700Response response);

    void saveYW700(String 작업자ID, YW700Request request, int userType);
}
