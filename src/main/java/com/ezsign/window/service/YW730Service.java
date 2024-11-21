package com.ezsign.window.service;

import com.ezsign.window.vo.YW730Request;
import com.ezsign.window.vo.YW730Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW730Service {

    void getYW730(String 계약ID, String 사용자ID, YW730Response response);

    void saveYW730(String 작업자ID, YW730Request request, int userType);
}
