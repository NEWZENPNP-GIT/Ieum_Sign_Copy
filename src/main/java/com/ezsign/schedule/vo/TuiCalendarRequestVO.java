package com.ezsign.schedule.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TuiCalendarRequestVO {
	
	private String bizId;
	private String userId;
	private String 유형;
	private String 기간From;
	private String 기간To;
	
}
