package com.ezsign.feb.hometax.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 연말정산 전잔매체 신고 VO
 *
 */
@Data
public class YE800VO {

    private String 계약ID;
    private String 전자신고ID;
    private String 제출대상구분코드;
    private String 제출년월일;
    private String 사업장명;
    private String 파일위치;
    private String 파일명;
    private String 구분자코드;
    
    private String 사용여부;

    @ApiModelProperty(hidden = true)
    private String 등록일시;
    
    private String bizId;
    private String 사업장ID;
    private String 작업자ID;
    private String 지점여부;
    private String 사업자등록번호;
    private String 종사업자일련번호;
    private String 단위과세자여부;
    private String 근로자인원수;
    private String 신고대상수;
    private String 총급여;
    private String 결정세액;
    private String 제작여부;
    private String 사용자ID;
    private String 귀속년도;
       
}
