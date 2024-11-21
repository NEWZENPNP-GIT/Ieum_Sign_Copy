package com.ezsign.window.service;

import com.ezsign.window.vo.YW600Request;
import com.ezsign.window.vo.YW600Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW600Service {

    void getYW600(String 계약ID, String 사용자ID, YW600Response response) throws Exception;

    void saveYW600(String 작업자ID, YW600Request request, int userType);
}
