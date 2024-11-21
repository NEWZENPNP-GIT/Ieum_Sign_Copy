package com.ezsign.attend.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttendVO {

    @ApiModelProperty(hidden = true)
    private String bizId;
    @ApiModelProperty(hidden = true)
    private String userId;
    private String workDate;
    private String attendId;

    private String attendCode;
    private String dateFrom;
    private String dateTo;
    @ApiModelProperty(hidden = true)
    private String placeId = null;
    private String signType;
    private String closeType;

    @ApiModelProperty(hidden = true)
    private String insDate;
    @ApiModelProperty(hidden = true)
    private String updDate;

    @ApiModelProperty(hidden = true)
    private double distance = 0.0;
    @ApiModelProperty(hidden = true)
    private double latitude;
    @ApiModelProperty(hidden = true)
    private double longitude;

    @ApiModelProperty(hidden = true)
    private String startDate;
    @ApiModelProperty(hidden = true)
    private String endDate;
    @ApiModelProperty(hidden = true)
    private String searchKey;
    @ApiModelProperty(hidden = true)
    private String searchValue;
    @ApiModelProperty(hidden = true)
    private String[] attendIds;

    private String empNo;
    private String empName;
    private String deptName;
    private String positionName;
    private String placeName;
    private String placeAddr;
    private int workMinute;

    private int startPage;
    private int endPage;
    private String searchCompany;
    private String bizName;
    private String workMonth;
}
