package com.ezsign.attend.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttendPropVO {

    @ApiModelProperty(hidden = true)
    private String bizId;

    @ApiModelProperty(required = true)
    private String detailYn;
    @ApiModelProperty(required = true)
    private String placeYn;
    @ApiModelProperty(required = true)
    private String externalCode;
    @ApiModelProperty(required = true)
    private String externalYn;
    @ApiModelProperty(required = true)
    private String closeYn;

    @ApiModelProperty(hidden = true)
    private String insDate;
    @ApiModelProperty(hidden = true)
    private String updDate;
    @ApiModelProperty(hidden = true)
    private String searchCompany;
}
