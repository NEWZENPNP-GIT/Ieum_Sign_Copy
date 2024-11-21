package com.ezsign.point.vo;

import lombok.Data;

@Data
public class PointLogVO {

	private int    pointId;
	private String bizId;
	private String bizName;
	private String userId;
	private String serviceId;
	private String pointType;
	private String pointTypeName;
	private int	   pointPrice;
	private int	   pointResult;
	private int	   paymentAmt;
	private String etcDesc;
	private String insDate;
	private String updDate;
	
	private String searchDateFrom;
	private String searchDateTo;

	private int startPage;
	private int endPage;
	
}
