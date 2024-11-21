package com.ezsign.window.service;

import com.ezsign.window.vo.YW610Request;
import com.ezsign.window.vo.YW610Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW610Service {

    void getYW610(String 계약ID, String 사용자ID, String 작업자ID, YW610Response response) throws Exception;

    void saveYW610(String 작업자ID, YW610Request request, int userType);
}
