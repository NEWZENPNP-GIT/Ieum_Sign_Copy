package com.ezsign.window.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezsign.window.vo.*;

@SuppressWarnings("NonAsciiCharacters")
public interface YW800Service {

    void getDonationYW800(String 계약ID, String 사용자ID, String 세액공제구분코드, YW800DonationResponse response) throws Exception;

    void getYW800(String bizId, String 계약ID, String 사용자ID, YW800Response response) throws Exception;

    void saveYW800(String bizId, String 작업자ID, YW800Request request) throws Exception;

    void updYW800(String bizId, String 작업자ID, YW800UpdateRequest request, HttpServletRequest httpRequest) throws Exception;

    void confirmYW800(String bizId, String 작업자ID, String 진행상태코드, YW800ConfirmRequest request, HttpServletRequest httpRequest) throws Exception;

	void saveAllYW800(String bizId, String 작업자ID, List<YW800Request> list) throws Exception;

	void confirmAllYW800(String 작업자ID, List<YW800VO> list) throws Exception;
}
