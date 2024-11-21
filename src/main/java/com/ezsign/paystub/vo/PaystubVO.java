package com.ezsign.paystub.vo;

import java.util.ArrayList;
import java.util.List;

import com.jarvis.common.util.StringUtil;

import lombok.Data;

@Data
public class PaystubVO {

	private String serviceId;
	private String bizId;
	private String bizName;
	private String userId;
	private String payMonth;
	private String payDate;
	private String payType;
	private String payTypeName;
	private String fileId;
	private String transType;
	private String confirmType;
	private String insDate;
	private String insEmpNo;
	private String updDate;
	private String updEmpNo;
	private String empNo;
	private String empName;
	private String eMail;
	private String phoneNum;
	private String transDate;

	private String searchYear;
	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	private String searchCompany;
	private String message;
	// 엑셀 row
	private int excelRow;
	// 엑셀 sheet
	private String excelSheet;
	// 알림
	private String paystubNonce;
	// 급여항목
	private List<PaystubDataVO> dataList = new ArrayList<PaystubDataVO>();
	
	// 수당총액
	private int totAllowAmt;
	// 공제총액
	private int totDeduAmt;
	// 사업자번호
	private String businessNo;
	
	private String payFormType;
	
	public String getPayMonthPrint() {
		String dispName = "";
		if(payMonth != null ){
			if(payMonth.length()>0) {
				dispName = StringUtil.substring(payMonth, 0, 4) + "년 " + StringUtil.substring(payMonth, 4, 6) + "월 ";
			}
		}
		return dispName;
	}
	
	public String getPayDatePrint() {
		String dispName = "";
		if(payDate != null ){
			if(payDate.length()>0) {
				dispName = StringUtil.substring(payDate, 0, 4) + "년 " + StringUtil.substring(payDate, 4, 6) + "월 "
						   + StringUtil.substring(payDate, 6, 8) + "일";
			}
		}
		return dispName;
	}
	
	public void addData(PaystubDataVO vo) {
		this.dataList.add(vo);
	}

	// pdf
	private String pdfFile;
	
	private String searchPayDate;
	
	private String seqId;
	
	private List<String> seqIds = new ArrayList<String>();
}
