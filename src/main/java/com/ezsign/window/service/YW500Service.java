package com.ezsign.window.service;

import com.ezsign.window.vo.YW500Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW500Service {

    void getYW500(String bizId, String 계약ID, String 사용자ID, YW500Response response) throws Exception;
}
