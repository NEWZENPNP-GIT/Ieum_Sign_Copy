package com.ezsign.approval.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApprLineDetailVO {

	private String apprLineId;
	private String apprSeq;
    private String apprBizId;
    private String apprBizName;
    private String apprUserId;
    private String apprEmpName;
    private String apprEMail;
    
    private String insEmpNo;
    private String updEmpNo;
    
}
