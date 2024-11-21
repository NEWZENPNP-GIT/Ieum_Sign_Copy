package com.ezsign.schedule.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScheduleVO {

	private String scheduleId;
	private String bizId;
	private String userId;
	private String dateFrom;
	private String dateTo;
	private String scheduleType;
	private String openType;
	private String subject;
	private String comments;
	private String location;
	private String insDate;
	private String updDate;
	
	private String empName;
	private String bizName;
	
	private String searchType;
	private String searchDateFrom;
	private String searchDateTo;
	
}
