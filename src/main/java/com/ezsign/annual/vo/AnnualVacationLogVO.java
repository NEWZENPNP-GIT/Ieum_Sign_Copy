package com.ezsign.annual.vo;

import lombok.Data;

@Data
public class AnnualVacationLogVO {
	
	private int    logId;
	private String serviceId;
	private String bizId;
	private String bizName;
	private String userId;
	private String empNo;
	private String empName;
	private double totalDay;
	private double addDay;
	private double delDay;
	private String comments;
	private String insDate;
	
}
