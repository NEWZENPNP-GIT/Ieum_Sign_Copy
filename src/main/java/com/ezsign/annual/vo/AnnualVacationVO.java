package com.ezsign.annual.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AnnualVacationVO {
	
	private String serviceId;
	private String bizId;
	private String bizName;
	private String userId;
	private String empNo;
	private String empName;
	private String annualYear;
	private double totalDay;
	private double addDay;
	private double delDay;
	private	double useDay;
	private	double usedDay;
	private String insDate;
	
	// 상세내역정보
	private String joinDate;
	private String leaveDate;
	private String deptName;
	private String positionName;
	private String eMail;

	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	
	private String code;
	private String message;
	// 엑셀 row
	private int excelRow;
	// 엑셀 sheet
	private String excelSheet;
	
}
