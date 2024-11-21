package com.ezsign.biz.vo;

import lombok.Data;

@Data
public class SalesChannelVO {

	private String channelId;
	private String salesChannel;
	private String channelDetail;
	private String personUserName;
	private String personUserTelNum;
	private String personEMail;
	private String insDate;
	private String updDate;
	
	private int channelCount;
	
	private String searchType;
	private String searchText;
	private String dbMode;
	
}
