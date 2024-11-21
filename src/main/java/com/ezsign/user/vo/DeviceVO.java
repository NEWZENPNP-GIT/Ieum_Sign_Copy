package com.ezsign.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceVO {

    @ApiModelProperty(hidden = true)
    private String userId;
    @ApiModelProperty(required = true)
    private String deviceId;

    @ApiModelProperty(hidden = true)
    private String authToken;
    private String pushToken;
    private String model;
    private String osType;
    private String osVersion;
    private String appVersion;
    @ApiModelProperty(hidden = true)
    private String loginIp;

    @ApiModelProperty(hidden = true)
    private String insDate;
    @ApiModelProperty(hidden = true)
    private String updDate;
}
