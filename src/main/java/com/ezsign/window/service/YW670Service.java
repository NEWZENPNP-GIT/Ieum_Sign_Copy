package com.ezsign.window.service;

import com.ezsign.window.vo.YW670Request;
import com.ezsign.window.vo.YW670Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW670Service {

    void getYW670(String 계약ID, String 사용자ID, YW670Response response);

    void saveYW670(String 작업자ID, YW670Request request, int userType);
}
