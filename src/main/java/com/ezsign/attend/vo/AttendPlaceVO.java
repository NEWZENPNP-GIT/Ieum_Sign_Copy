package com.ezsign.attend.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttendPlaceVO {

    @ApiModelProperty(hidden = true)
    private String bizId;
    private String placeId;

    private String placeName;
    private String placeAddr;
    private double latitude;
    private double longitude;
    private String searchCompany;

    @ApiModelProperty(hidden = true)
    private String insDate;
    @ApiModelProperty(hidden = true)
    private String updDate;
}
