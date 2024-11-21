package com.ezsign.feb.worker.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YE750VO {

    private String 계약ID;
    private String 사용자ID;
    private String 출력물파일구분코드;

    private String 출력물파일구분명;
    private String 파일ID;
    
    private String bizId;
    private String empName;
    private String eMail;

    @ApiModelProperty(hidden = true)
    private String 등록일시;
    @ApiModelProperty(hidden = true)
    private String 수정일시;
}
