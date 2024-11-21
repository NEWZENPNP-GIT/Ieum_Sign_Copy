package com.ezsign.attend.vo;

import lombok.Data;

@Data
public class ZipcodeVO {

    private String postcd;     // 우편번호
    private String address;    // 주소(도로명)
    private String addrjibun;  // 주소(지번)
    private int totalCount;    // 검색결과 전체 건수
}
