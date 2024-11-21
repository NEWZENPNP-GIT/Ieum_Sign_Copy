package com.ezsign.mobile.vo;

import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.attend.vo.AttendVO;
import com.ezsign.contract.vo.ContractPersonMainVO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.vo.YS010VO;
import com.ezsign.paystub.vo.PaystubMainVO;

public class MobileMainResponse {

	public boolean success;
	public PaystubMainVO 급여정보;
	public ContractPersonMainVO 계약정보;
	public AnnualVacationVO 연차정보;
	public YS010VO 연말정산정보;
	public AttendVO 출퇴근정보;
	public YE000VO 연말대상정보;
	public boolean 암호입력대상정보;
	
}
