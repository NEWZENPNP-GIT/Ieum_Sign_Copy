package com.ezsign.window.service;

import com.ezsign.window.vo.YW640Request;
import com.ezsign.window.vo.YW640Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW640Service {

    void getYW640(String 계약ID, String 사용자ID, YW640Response response);

    void saveYW640(String 작업자ID, YW640Request request, int userType);
}
