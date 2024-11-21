package com.ezsign.emp.vo;

import java.util.List;

import lombok.Data;

@Data
public class EmpLogVO {
	private String bizId;
	private String userId;
	private String logType;
	private String ipAddr;
	private String insDate;
	private String insUserId;
	private String updDate;
	private List<String> userIdList;
}
