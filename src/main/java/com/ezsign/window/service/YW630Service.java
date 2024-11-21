package com.ezsign.window.service;

import com.ezsign.window.vo.YW630Request;
import com.ezsign.window.vo.YW630Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW630Service {

    void getYW630(String 계약ID, String 사용자ID, YW630Response response);

    void saveYW630(String 작업자ID, YW630Request request, int userType);
}
