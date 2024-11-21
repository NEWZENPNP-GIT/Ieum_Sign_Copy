package com.ezsign.window.service;

import com.ezsign.window.vo.YW710MedicalResponse;
import com.ezsign.window.vo.YW710Request;
import com.ezsign.window.vo.YW710Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW710Service {

    void getYW710(String bizId, String 계약ID, String 사용자ID, YW710Response response);

    void saveYW710(String bizId, String 작업자ID, YW710Request request, int userType);
    
    void putYW710(String bizId, String 작업자ID, YW710Request request);

	void getMedicalYW710(String 계약ID, String 사용자ID, String 세액공제구분코드, YW710MedicalResponse response);
}
