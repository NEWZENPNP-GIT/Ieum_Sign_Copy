package com.ezsign.window.service;

import com.ezsign.window.vo.YW611Request;
import com.ezsign.window.vo.YW611Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW611Service {

    void getYW611(String 계약ID, String 사용자ID, YW611Response response);

    void saveYW611(String 작업자ID, YW611Request request, int userType);
}
