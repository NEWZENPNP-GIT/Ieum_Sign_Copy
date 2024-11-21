package com.ezsign.approval.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApprovalMasterVO {

	private String apprId;
	private String refId;
	private String apprStatus;
	private String apprDate;
	private String requesterId;
	private String requesterBizId;
	
	private String apprName;
	private String apprName1;
	private String apprEmail1;
	private String apprName2;
	private String apprEmail2;
	private String apprCnt;
	private String fileTitle;
	private String contUserName;
	private String apprSeq;
	private String maxSeq;
	private String contId;
}
