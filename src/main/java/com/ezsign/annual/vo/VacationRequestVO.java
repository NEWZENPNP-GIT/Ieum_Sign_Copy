package com.ezsign.annual.vo;

import java.util.ArrayList;
import java.util.List;

import com.ezsign.approval.vo.ApprovalVO;

import lombok.Data;

@Data
public class VacationRequestVO {
	
	private String approvalType;
	private int    requestId;
	private String bizId;
	private String bizName;
	private String userId;
	private String empNo;
	private String empName;
	private String dateFrom;
	private String dateTo;
	private String vacationType;
	private String vacationTypeName;
	private String phoneNum;
	private double vacationDay;
	private String approvalId;
	private String scheduleId;
	private String comments;
	private String statusCode;
	private String insDate;
	private String updDate;
	private String deptName;
	
	private String joinDate;
	private double totalDay;
	private double addDay;
	private double delDay;
	private double useDay;
	
	private List<ApprovalVO> approvalList = new ArrayList<ApprovalVO>();
	
}
