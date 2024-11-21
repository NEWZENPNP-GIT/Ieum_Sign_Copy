package com.ezsign.approval.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApprLineVO {
	
	private String apprLineName;
	private String apprBizId;
	private String apprUserId;

	private List<ApprLineMasterVO> apprLineMasterList;
	
	private List<ApprLineDetailVO> apprLineDetailList;
}
