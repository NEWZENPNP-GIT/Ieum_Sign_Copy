package com.ezsign.point.vo;

import lombok.Data;

@Data
public class PointVO {

	private String bizId;
	private String userId;
	private String serviceId;
	private int	   curPoint;
	private int	   deduPoint;
	private int	   chargePoint;
	private String comment;
	private String insDate;
	private String updDate;

	private String bizName;
	private String depositYn;
	private String depositDate;

	private String taxBillBizName;
	private String taxBillName;
	private String taxBillTel;
	private String taxBillEmail;
}
