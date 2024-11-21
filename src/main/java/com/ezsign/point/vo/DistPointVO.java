package com.ezsign.point.vo;

import lombok.Data;

@Data
public class DistPointVO {

	private String bizId;
	private int	   deduPoint;
	private int	   chargePoint;
	private String userId;
	private String comment;
}
