package com.ezsign.window.service;

import com.ezsign.window.vo.YW720Request;
import com.ezsign.window.vo.YW720Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW720Service {

    void getYW720(String bizId, String 계약ID, String 사용자ID, YW720Response response);

    void saveYW720(String 작업자ID, YW720Request request, int userType);
}
