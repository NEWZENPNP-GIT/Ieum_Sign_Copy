package com.ezsign.window.service;

import com.ezsign.window.vo.YW650CreditCardResponse;
import com.ezsign.window.vo.YW650Request;
import com.ezsign.window.vo.YW650Response;

@SuppressWarnings("NonAsciiCharacters")
public interface YW650Service {

    void getYW650(String 계약ID, String 사용자ID, YW650Response response);

    void saveYW650(String 작업자ID, YW650Request request, int userType);

	void getCreditCardYW650(String 계약ID, String 사용자ID, YW650CreditCardResponse response);
}
