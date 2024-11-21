package com.ezsign.approval.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApprLineMasterVO {

    @ApiModelProperty(hidden = true)
    private String bizId;
    @ApiModelProperty(hidden = true)
    private String userId;
    
    private String apprLineId;
    private String apprLineName;
    private String insEmpNo;
    private String updEmpNo;
}
