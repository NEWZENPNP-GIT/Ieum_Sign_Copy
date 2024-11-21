package com.ezsign.window.service;

import com.ezsign.window.vo.YW660Request;
import com.ezsign.window.vo.YW660Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW660Service {

    void getYW660(String 계약ID, String 사용자ID, YW660Response response);

    void saveYW660(String 작업자ID, YW660Request request, int userType);
}
