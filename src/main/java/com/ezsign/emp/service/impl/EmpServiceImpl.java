package com.ezsign.emp.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizGrpVO;
import com.ezsign.biztalk.dao.BizTalkDAO;
import com.ezsign.biztalk.vo.BizTalkKKOVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpLogVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.util.ExcelUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.meta.dao.MetaDAO;
import com.ezsign.meta.vo.MetaDataVO;
import com.ezsign.meta.vo.MetaFileVO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.vo.UserVO;

import jxl.Sheet;
import jxl.Workbook;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

@Service("empService")
public class EmpServiceImpl implements EmpService {

	Logger logger = Logger.getLogger(this.getClass());

	private static int _oneByte = 1024;
	
	@Resource(name="empDAO")
	private EmpDAO empDAO;
	
	@Resource(name="userDAO")
	private UserDAO userDAO;

	@Resource(name="bizTalkDAO")
	private BizTalkDAO bizTalkDAO;

	@Resource(name="metaDAO")
	private MetaDAO metaDAO;

	@Resource(name="bizDAO")
	private BizDAO bizDAO;

	@Override
	public List<EmpVO> getEmpList(EmpVO vo, SessionVO loginVO)  throws Exception {
		List<EmpVO> list 		= empDAO.getEmpList(vo);
		List<String> userIdList = new ArrayList<>();

        for (EmpVO empVO : list) { userIdList.add(empVO.getUserId()); }

		if (!list.isEmpty() && StringUtil.isNotNull(loginVO.getIpAddr())) {
			EmpLogVO logVO = new EmpLogVO();
			logVO.setInsUserId(loginVO.getUserId());
			logVO.setUserIdList(userIdList);
			logVO.setLogType("001");
			logVO.setIpAddr(loginVO.getIpAddr());
			logVO.setBizId(loginVO.getBizId());
			empDAO.insEmpLog(logVO);
		}
		return list;
	}
	
	
	@Override
	public List<EmpVO> getDupleEmpList(EmpVO vo)  throws Exception { return empDAO.getDupleEmpList(vo); }

	@Override
	public int getEmpListCount(EmpVO vo)  throws Exception { return empDAO.getEmpListCount(vo); }

	@Override
	public EmpVO getEmp(EmpVO vo, SessionVO loginVO)  throws Exception {

		EmpVO resultVO 			= empDAO.getEmp(vo);
		List<String> userIdList = new ArrayList();

		if(resultVO != null && StringUtil.isNotNull(loginVO.getIpAddr())){
			userIdList.add(resultVO.getUserId());
			EmpLogVO logVO = new EmpLogVO();
			logVO.setInsUserId(loginVO.getUserId());
			logVO.setUserIdList(userIdList);
			logVO.setLogType("001");
			logVO.setIpAddr(loginVO.getIpAddr());
			logVO.setBizId(resultVO.getBizId());
			empDAO.insEmpLog(logVO);
		}

		return resultVO;
	}

	@Override
	public EmpVO getEmpNo(EmpVO vo)  throws Exception { return empDAO.getEmpNo(vo); }

	@Override
	public String insEmp(EmpVO vo) throws Exception {


		EmpVO result = empDAO.getEmpNo(vo);
		EmpVO check  = new EmpVO();
		check.setBizId(vo.getBizId());
		check.setEmpNo(vo.getEmpNo());

		EmpVO dupEmp = empDAO.getEmpNo(check);
		String empNonce;

		if (dupEmp != null && result != null) {

			if (areValuesEqual(dupEmp.getBizId(),vo.getBizId()) &&
				areValuesEqual(dupEmp.getPhoneNum(),vo.getPhoneNum()) &&
				areValuesEqual(dupEmp.getEMail(),vo.getEMail()) &&
				areValuesEqual(dupEmp.getDeptName(),vo.getDeptName()) &&
				areValuesEqual(dupEmp.getUserDate(),vo.getUserDate()) &&
				areValuesEqual(dupEmp.getJoinDate(),vo.getJoinDate()) &&
				areValuesEqual(dupEmp.getAddr1(),vo.getAddr1()) &&
				areValuesEqual(dupEmp.getPositionName(),vo.getPositionName()) &&
				areValuesEqual(dupEmp.getStepName(),vo.getStepName())) {

				empNonce = "-1"; // 모든 데이터 동일
			} else {
				empDAO.updEmp(vo);
				empNonce = "-2"; // 사번,이름 동일 인사정보 업데이트
			}
		} else if (dupEmp != null && result == null) {
			empNonce = "-3"; // 존재하는 사번, 또는 이름 입력
		} else {
			empDAO.insEmp(vo);
			empNonce = "1"; // 인사정보 입력
			if (StringUtil.isNotNull(vo.getLoginId())) {
				UserVO userVO = new UserVO();
				userVO.setUserId(vo.getLoginId());
				userVO.setUserName(vo.getEmpName());
				userVO.setUserPwd(SecurityUtil.encrypt(vo.getUserPwd()));
				userVO.setUserType(vo.getEmpType());
				userVO.setAppType("N");
				userDAO.insUser(userVO);
			}
		}
		return empNonce;
	}

	@Override
	public int updEmp(EmpVO vo) throws Exception { return empDAO.updEmp(vo); }
	
	@Override
	public void updEmpLoginID(UserVO vo) throws Exception {
		
		UserVO resultUserVO = userDAO.loginProcess(vo);
		
		// 로그인정보 존재시 처리
		if(resultUserVO!=null) { userDAO.updLoginId(vo); }

	}

	@Override
	public int delEmp(EmpVO vo) throws Exception {
		int result  = 0;
		EmpVO empVO = empDAO.getEmp(vo);
		if(empVO==null) {
			System.out.println("[delEmp] 사용자정보를 찾을수 없습니다."+vo.getUserId());
			return 0;
		}
		
		// 계정이 생성된 사용자는 삭제처리
		if(StringUtil.isNotNull(empVO.getLoginId())) {
			UserVO userVO = new UserVO();
			userVO.setUserId(empVO.getLoginId());
			userDAO.delUser(userVO);
		}
		result = empDAO.delEmp(vo);
		// 자료삭제
		// 급여명세서
		// 연말정산
		return result;
	}


	@Override
	public int updEmpType(EmpVO vo) throws Exception {
		int result    = 0;
		EmpVO paramVO = new EmpVO();
		paramVO.setBizId(vo.getBizId());
		paramVO.setUserId(vo.getUserId());
				
		EmpVO empVO = empDAO.getEmp(paramVO);
		if(empVO==null) {
			System.out.println("[updEmpType] 사용자정보를 찾을수 없습니다."+vo.getUserId());
			return 0;
		}
		
		// 계정이 생성된 사용자는 업데이트 처리
		if(StringUtil.isNotNull(empVO.getLoginId())) {
			UserVO userVO = new UserVO();
			userVO.setUserId(empVO.getLoginId());
			userVO.setUserType(vo.getEmpType());
			userDAO.updUser(userVO);
		}
		paramVO.setEmpType(vo.getEmpType());
		result = empDAO.updEmp(paramVO);
		return result;
	}
	
	
	@Override
	public int updEmpNonce(EmpVO vo) throws Exception {
		int result  = 0;
		EmpVO empVO = empDAO.getEmp(vo);
		if(empVO==null) {
			System.out.println("[updEmpNonce] 사용자정보를 찾을수 없습니다."+vo.getUserId());
			return 0;
		}
		
		// 계정이 생성된 사용자는 삭제처리
		if(StringUtil.isNotNull(empVO.getLoginId())) {
			UserVO userVO = new UserVO();
			userVO.setUserId(empVO.getLoginId());
			userDAO.delUser(userVO);
		}
		
		String empNonce = getEmpNonce();
		
		vo.setEmpNonce(empNonce);
		vo.setLoginId("-");
		
		result = empDAO.updEmp(vo);
		
		return result; 
	}
	
	@Override
	public boolean updLoginId(EmpVO vo) throws Exception {
		boolean result = false;
		int count 	   = empDAO.updLoginId(vo);
		if (count > 0) {
			UserVO userVO = new UserVO();
			userVO.setUserId(vo.getLoginId());
			userVO.setUserType("1");
			userVO.setAppType("Y");
			userDAO.insUser(userVO);
			result = true;
		}
		return result;
	}
	
	@Override
    public String getEmpNonce() {
		
		String empNonce = StringUtil.getRandom(6);
		EmpVO paramVO   = new EmpVO();
		paramVO.setEmpNonce(empNonce);
		
		List<EmpVO> list = empDAO.getEmpList(paramVO);
		
		if (list.size()>0) { getEmpNonce(); }

		return empNonce;
    }


	// 인사 목록 엑셀
	@Override
	public EmpVO sendEmpExcel(String bizId, String xlsPath, String smsSendType, String deptCode) throws Exception {

		List<EmpVO> empList 	  = new ArrayList();
		EmpVO result 			  = new EmpVO();
		List<String> emptyRowList = new ArrayList<String>();
		System.out.println("[sendEmpExcel]  기업id -> " + bizId);

		String bizName = "";
		
		try {
			File file = new File(xlsPath);
			if (!file.exists()) {
				System.out.println("[sendEmpExcel] XLS파일이 존재하지 않습니다.");
				return null;
			}
			
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet[] sheets    = workbook.getSheets();
			
			for(int s=0;s<sheets.length;s++) {				 
				Sheet sheet = sheets[s];
				int colNum  = sheet.getColumns();
				int rowNum  = sheet.getRows();
				
				System.out.println("sheet=>"+sheet.getName()+"_"+colNum+"_"+rowNum);
				
				for(int r=3; r < rowNum; r++) {
					
					String businessNo	= ExcelUtil.getCellValue(sheet.getCell(2, r));
					String empNo		= ExcelUtil.getCellValue(sheet.getCell(3, r));
					String deptName		= ExcelUtil.getCellValue(sheet.getCell(4, r));
					String empName		= ExcelUtil.getCellValue(sheet.getCell(5, r));
					String userDate		= ExcelUtil.getCellValue(sheet.getCell(6, r));
					String joinDate		= ExcelUtil.getCellValue(sheet.getCell(7, r));
					String leaveDate	= ExcelUtil.getCellValue(sheet.getCell(8, r));
					String addr1		= ExcelUtil.getCellValue(sheet.getCell(9, r));
					String positionName	= ExcelUtil.getCellValue(sheet.getCell(10, r));
					String stepName		= ExcelUtil.getCellValue(sheet.getCell(11, r));
					String telNum		= ExcelUtil.getCellValue(sheet.getCell(12, r));
					String phoneNumCellType   = ExcelUtil.getCellType(sheet.getCell(13, r));
					String phoneNum		= ExcelUtil.getCellValue(sheet.getCell(13, r));
					String eMail		= ExcelUtil.getCellValue(sheet.getCell(14, r));

					userDate   = StringUtil.StringReplace(userDate);
					joinDate   = StringUtil.StringReplace(joinDate);
					leaveDate  = StringUtil.StringReplace(leaveDate);
					telNum	   = StringUtil.StringReplace(telNum);
					telNum 	   = telNum.replaceAll(" ",""); // 전화번호 공백허용 제거
					phoneNum   = StringUtil.StringReplace(phoneNum);
					phoneNum   = phoneNum.replaceAll(" ",""); // 휴대폰번호 공백허용 제거
					businessNo = StringUtil.StringReplace(businessNo);

					// 사업자번호가 오류인경우 무시하고 다음행을 처리하도록 요청(9/1 설계운영팀 김정인 요청)
					if(businessNo.length() < 10){
						emptyRowList.add(Integer.toString(r+1));
						System.out.println("[sendEmpExcel] 사업자번호 오류로 pass한 행 >> " + r+1);
						continue;
					}
					
					BizGrpVO bizGrpVO = new BizGrpVO();
					bizGrpVO.setBusinessNo(businessNo);
					bizGrpVO.setBizId(bizId);
					BizGrpVO checkBizVO = bizDAO.getBizExsit(bizGrpVO);

					if (checkBizVO == null) {
						System.out.println("입력하신 [ "+empName+" ] 님의 사업자등록번호가 존재하지 않습니다.");
						result.setEmpName(empName);
						result.setEmpNonce("-1");
						return result;
					}

					bizName = checkBizVO.getBizName();
					// 사원정보 입력
					EmpVO empVO = new EmpVO();
					empVO.setBizId(checkBizVO.getBizId());
					empVO.setEmpNo(empNo);
					empVO.setBizName(bizName);
					empVO.setEmpName(empName);
					empVO.setLoginId("");
					empVO.setEmpType("1");
					empVO.setAddr1(addr1);
					empVO.setAddr2("");
					empVO.setTelNum(telNum);
					empVO.setPhoneNum(phoneNum);
					empVO.setUserDate(userDate);
					empVO.setEMail(eMail);
					empVO.setJoinDate(joinDate);
					empVO.setLeaveDate(leaveDate);
					empVO.setEmpNonce("0");
					empVO.setDeptName(deptName);
					empVO.setPositionName(positionName);
					empVO.setStepName(stepName);
					empVO.setCountryType("81");
					empVO.setConfirmType("N");
					empVO.setDeptCode(deptCode);
					
					if(!StringUtil.isNotNull(empNo) ) break;

					if(!StringUtil.isNotNull(empName) || !StringUtil.isNotNull(phoneNum) || !StringUtil.isNotNull(eMail)) {
						System.out.println(empVO.getEmpName()+"님의 필수입력항목이 없습니다.");
						result.setEmpName(empVO.getEmpName());
						return result;
					}

					// 2024.08.29 휴대폰번호 형식 검사 추가
					if(StringUtil.isNotNull(empName) && phoneNumCellType.equals("NUMBER")){
						System.out.println("입력하신 [ "+empName+" ] 님의 휴대폰번호가 숫자타입으로 형식이 올바르지않습니다.");
						result.setEmpName(empName);
						result.setEmpNonce("-5");
						return result;
					}
					if(StringUtil.isNotNull(empName) && (phoneNum.isEmpty() || phoneNum.charAt(0) != '0')){
						System.out.println("입력하신 [ "+empName+" ] 님의 휴대폰번호가 '0'으로 시작하지않습니다.");
						result.setEmpName(empName);
						result.setEmpNonce("-5");
						return result;
					}

					try {
						if(StringUtil.isNotNull(userDate)){
				            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd"); //검증할 날짜 포맷 설정
				            dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
				            dateFormatParser.parse(userDate); //대상 값 포맷에 적용되는지 확인
						}
			        } catch (Exception e) {
			        	System.out.println(empVO.getEmpName()+"님의 생년월일 형식이 올바르지 않습니다.");
			        	result.setEmpName(empVO.getEmpName());
			        	result.setEmpNonce("-2");
						return result;
			        }

					try {
						if(StringUtil.isNotNull(joinDate)){
				            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd"); //검증할 날짜 포맷 설정
				            dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
				            dateFormatParser.parse(joinDate); //대상 값 포맷에 적용되는지 확인
						}
			        } catch (Exception e) {
			        	System.out.println(empVO.getEmpName()+"님의 입사일자 형식이 올바르지 않습니다.");
			        	result.setEmpName(empVO.getEmpName());
			        	result.setEmpNonce("-3");
						return result;
			        }
					
					try {
						if(StringUtil.isNotNull(leaveDate)){
				            SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd"); //검증할 날짜 포맷 설정
				            dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
				            dateFormatParser.parse(leaveDate); //대상 값 포맷에 적용되는지 확인
						}
			        } catch (Exception e) {
			        	System.out.println(empVO.getEmpName()+"님의 퇴사일자 형식이 올바르지 않습니다.");
			        	result.setEmpName(empVO.getEmpName());
			        	result.setEmpNonce("-4");
						return result;
			        }
					empList.add(empVO);
				}				
			}
			
			int insCount	  = 0;
			int updCount	  = 0;
			int dupCount	  = 0;
			int checkCount	  = 0;
			int emptyRowCount = 0;

			List<String> dupEmpRow   = new ArrayList<String>();
			List<String> checkEmpRow = new ArrayList<String>();

			// 인사정보 등록
			for(int i=0;i<empList.size();i++) {

				EmpVO empVO   = empList.get(i);
				EmpVO empNoVO = new EmpVO();

				empNoVO.setBizId(empVO.getBizId());
				empNoVO.setEmpNo(empVO.getEmpNo());
				empNoVO.setEmpType("1");

				EmpVO checkEmpVO = empDAO.getEmpNo(empNoVO);
				String empNonce  = "";

				if (smsSendType.equals("Y")) { empNonce = StringUtil.getRandom(6); }

				if (checkEmpVO != null) {
					//인사정보 변경
					System.out.println("[sendEmpExcel] 이미 존재하는 임직원["+empVO.getEmpName()+"] 정보입니다.");

					if( areValuesEqual(checkEmpVO.getEmpNo(),empVO.getEmpNo()) &&
						areValuesEqual(checkEmpVO.getEmpName(),empVO.getEmpName())) {
						// 모든 데이터가 같은지 확인
						if( areValuesEqual(checkEmpVO.getBizName(),empVO.getBizName()) &&
							areValuesEqual(checkEmpVO.getBizId(),empVO.getBizId()) &&
							areValuesEqual(checkEmpVO.getDeptName(),empVO.getDeptName()) &&
							areValuesEqual(checkEmpVO.getUserDate(),empVO.getUserDate()) &&
							areValuesEqual(checkEmpVO.getJoinDate(),empVO.getJoinDate()) &&
							areValuesEqual(checkEmpVO.getLeaveDate(),empVO.getLeaveDate()) &&
							areValuesEqual(checkEmpVO.getAddr1(),empVO.getAddr1()) &&
							areValuesEqual(checkEmpVO.getPositionName(),empVO.getPositionName()) &&
							areValuesEqual(checkEmpVO.getStepName(),empVO.getStepName()) &&
							areValuesEqual(checkEmpVO.getPhoneNum(),empVO.getPhoneNum()) &&
							areValuesEqual(checkEmpVO.getEMail(),empVO.getEMail())) {

							int rowNum = i+4;
							rowNum	   = rowNum + emptyRowCount;

							for (String emptyRow : emptyRowList) {
								if (Integer.toString(rowNum).equals(emptyRow)) {
									emptyRowCount++;
									rowNum++;
								}
							}
							System.out.println("[sendEmpExcel] 중복된 데이터 행번호 >> " + rowNum);
							dupCount++;

						} else { // 사번, 이름이 기존데이터와 동일하고, 수정사항이 있을 시
							EmpVO tmpEmpVO = new EmpVO();
							tmpEmpVO.setBizId(empVO.getBizId());
							tmpEmpVO.setUserId(checkEmpVO.getUserId());
							tmpEmpVO.setDeptName(empVO.getDeptName());
							tmpEmpVO.setUserDate(empVO.getUserDate());
							tmpEmpVO.setJoinDate(empVO.getJoinDate());
							tmpEmpVO.setLeaveDate(empVO.getLeaveDate());
							tmpEmpVO.setAddr1(empVO.getAddr1());
							tmpEmpVO.setPositionName(empVO.getPositionName());
							tmpEmpVO.setStepName(empVO.getStepName());
							tmpEmpVO.setTelNum(empVO.getTelNum());
							tmpEmpVO.setPhoneNum(empVO.getPhoneNum());
							tmpEmpVO.setEMail(empVO.getEMail());
							empDAO.updEmp(tmpEmpVO);

							int rowNum = i+4;
							rowNum 	   = rowNum + emptyRowCount;

							for (String emptyRow : emptyRowList) {
								if (Integer.toString(rowNum).equals(emptyRow)) {
									emptyRowCount++;
									rowNum++;
								}
							}
							System.out.println("[sendEmpExcel] 중복된 데이터 행번호 >> " + rowNum);
							dupEmpRow.add(Integer.toString(rowNum));
							updCount++;
						}

					} else { // 사번, 이름 중복 체크

						int rowNum = i+4;
						rowNum 	   = rowNum + emptyRowCount;

						for (String emptyRow : emptyRowList) {
							if (Integer.toString(rowNum).equals(emptyRow)) {
								emptyRowCount++;
								rowNum++;
							}
						}
						System.out.println("[sendEmpExcel] 중복된 데이터 행번호 >> " + rowNum);
						checkEmpRow.add(Integer.toString(rowNum));
						checkCount++;
					}

				} else {
					empVO.setEmpNonce(empNonce);
					empDAO.insEmp(empVO);
					if (smsSendType.equals("Y")) {
						String content = "";
						if (bizName == null || "".equals(bizName)) { bizName = "뉴젠피앤피"; }

						content += bizName + "직원들을 위한 모바일 앱 \"Candy\" 설치 안내입니다. \n";
						content += "아래 설치URL에서 Candy앱 설치를 진행해주시기 바랍니다. \n";
						content += " http://app.ieumsign.co.kr \n";
						
						BizTalkKKOVO kkoVO = new BizTalkKKOVO();
						kkoVO.setSubject("[뉴젠피엔피 * Candy]");
						kkoVO.setContent(content);
						kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
						kkoVO.setRecipientNum(empVO.getPhoneNum());
						kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
						kkoVO.setTemplateCode("contract003");
						kkoVO.setKkoBtnType("1");
						kkoVO.setKkoBtnInfo("Candy^WL^^");
						bizTalkDAO.insBizTalk(kkoVO);
					}
					insCount++;
				}
			}
			result.setEmpNonce(Integer.toString(insCount + updCount + checkCount + dupCount));
			result.setInsCount(Integer.toString(insCount));
			result.setUpdCount(Integer.toString(updCount));
			result.setDupCount(Integer.toString(dupCount));
			result.setCheckCount(Integer.toString(checkCount));
			result.setDuplicateEmpRow(dupEmpRow);
			result.setCheckEmpRow(checkEmpRow);
			workbook.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}


	@Override
	public EmpVO sendPayCareEmpExcel(String bizId, String xlsPath, String smsSendType) throws Exception {

		List<EmpVO> empList = new ArrayList<EmpVO>();
		EmpVO result 		= new EmpVO();
		System.out.println("[sendPayCareEmpExcel]  기업id -> " + bizId);
		
		try {
			File file = new File(xlsPath);
			if (!file.exists()) {
				System.out.println("[sendPayCareEmpExcel] XLS파일이 존재하지 않습니다.");
				return null;
			}
			
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet[] sheets 	  = workbook.getSheets();
			
			for (int s=0; s < sheets.length; s++) {
				Sheet sheet = sheets[s];
				int colNum  = sheet.getColumns();
				int rowNum  = sheet.getRows();
				
				System.out.println("sheet=>" + sheet.getName() + "_" + colNum + "_" + rowNum);
				
				// paycare 엑셀 컬럼명 맵핑
				for (int r=1; r < rowNum; r++) {
					String empNo 		= ExcelUtil.getCellValue(sheet.getCell(10, r));
					String deptName 	= ExcelUtil.getCellValue(sheet.getCell(3, r));
					String empName 		= ExcelUtil.getCellValue(sheet.getCell(11, r));
					String userDate 	= StringUtil.substring(ExcelUtil.getCellValue(sheet.getCell(14, r)), 0, 6);
					String joinDate 	= ExcelUtil.getCellValue(sheet.getCell(8, r));
					String leaveDate 	= ExcelUtil.getCellValue(sheet.getCell(9, r));
					String addr1 		= ExcelUtil.getCellValue(sheet.getCell(21, r));
					String positionName = ExcelUtil.getCellValue(sheet.getCell(7, r));
					String telNum 		= ExcelUtil.getCellValue(sheet.getCell(17, r));
					String phoneNum 	= ExcelUtil.getCellValue(sheet.getCell(18, r));
					String eMail 		= ExcelUtil.getCellValue(sheet.getCell(19, r));
					String userDateNum = StringUtil.substring(ExcelUtil.getCellValue(sheet.getCell(14, r)), 7, 8);

					if (userDateNum.equals("1") || userDateNum.equals("2")) { userDate = "19" + userDate; }
					else 													{ userDate = "20" + userDate; }
					userDate  = StringUtil.StringReplace(userDate);
					joinDate  = StringUtil.StringReplace(joinDate);
					leaveDate = StringUtil.StringReplace(leaveDate);
					telNum	  = StringUtil.StringReplace(telNum);
					telNum	  = telNum.replaceAll(" ",""); // 전화번호 공백허용 제거
					phoneNum  = StringUtil.StringReplace(phoneNum);
					phoneNum  = phoneNum.replaceAll(" ",""); // 휴대폰번호 공백허용 제거
					
					if(leaveDate.equals("99999999")) { leaveDate = ""; }
					
					// 사원정보 입력
					EmpVO empVO = new EmpVO();
					empVO.setBizId(bizId);
					empVO.setEmpNo(empNo);
					empVO.setEmpName(empName);
					empVO.setLoginId("");
					empVO.setEmpType("1");
					empVO.setAddr1(addr1);
					empVO.setAddr2("");
					empVO.setTelNum(telNum);
					empVO.setPhoneNum(phoneNum);
					empVO.setUserDate(userDate);
					empVO.setEMail(eMail);
					empVO.setJoinDate(joinDate);
					empVO.setLeaveDate(leaveDate);
					empVO.setEmpNonce("");
					empVO.setDeptName(deptName);
					empVO.setPositionName(positionName);
					empVO.setStepName("");
					empVO.setCountryType("81");
					empVO.setConfirmType("N");
					
					System.out.println("[sendPayCareEmpExcel] 조회->" + empName);

					if (!StringUtil.isNotNull(empNo) ) break;
					
					if (!StringUtil.isNotNull(empName) || !StringUtil.isNotNull(userDate) || !StringUtil.isNotNull(joinDate)) {
						System.out.println(empVO.getEmpName() + "님의 필수입력항목(성명, 입사일자, 생년월일)이 없습니다.");
						result.setEmpName(empVO.getEmpName());
						result.setExcelRow(r);
						result.setExcelSheet(sheet.getName());
						return result;
					}
					
					if (smsSendType.equals("Y") && !StringUtil.isNotNull(phoneNum)) {
						System.out.println(empVO.getEmpName() + "님의 필수입력항목(휴대폰번호)이 없습니다.");
						result.setEmpName(empVO.getEmpName());
						result.setExcelRow(r);
						result.setExcelSheet(sheet.getName());
						return result;
					}
					empList.add(empVO);
				}
			}
			
			int count = 0;
			// 인사정보 등록
			for (int i=0; i < empList.size(); i++) {
				EmpVO empVO = empList.get(i);
				System.out.println("[sendPayCareEmpExcel] 처리->" + empVO.getEmpName());
				
				EmpVO empNoVO = new EmpVO();
				empNoVO.setBizId(empVO.getBizId());
				empNoVO.setEmpNo(empVO.getEmpNo());

				EmpVO checkEmpVO = empDAO.getEmpNo(empNoVO);
				String empNonce  = "";
				
				if ("Y".equals(smsSendType)) { empNonce = StringUtil.getRandom(6); }
				if (checkEmpVO != null) {
					// 인사정보 변경
					System.out.println("[sendPayCareEmpExcel] 이미 존재하는 임직원["+empVO.getEmpName()+"] 정보입니다.");
					if (!StringUtil.isNotNull(checkEmpVO.getLoginId())) {
						empNonce = checkEmpVO.getEmpNonce();

						if ("Y".equals(smsSendType)) {
							
							String content = "";
							content += "직원들을 위한 모바일 앱 \"Candy\" 설치 안내입니다. \n";
							content += "아래 설치URL에서 Candy앱 설치를 진행해주시기 바랍니다. \n";
							content += "설치URL \n";
							content += " https://app.ieumsign.co.kr \n";
							
							BizTalkKKOVO kkoVO = new BizTalkKKOVO();
							kkoVO.setSubject("[뉴젠피엔피 * Candy]");
							kkoVO.setContent(content);
							kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
							kkoVO.setRecipientNum(empVO.getPhoneNum());
							kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
							kkoVO.setTemplateCode("contract003");
							kkoVO.setKkoBtnType("1");
							kkoVO.setKkoBtnInfo("Candy^WL^^");
							bizTalkDAO.insBizTalk(kkoVO);
						}
					}
				} else {
					// 인사정보 입력
					empVO.setEmpNonce(empNonce);
					empDAO.insEmp(empVO);
					if ("Y".equals(smsSendType)) {

						String content = "";
						content += "직원들을 위한 모바일 앱 \"Candy\" 설치 안내입니다. \n";
						content += "아래 설치URL에서 Candy앱 설치를 진행해주시기 바랍니다. \n";
						content += "설치URL \n";
						content += " https://app.ieumsign.co.kr \n";
						
						BizTalkKKOVO kkoVO = new BizTalkKKOVO();
						kkoVO.setSubject("[뉴젠피엔피 * Candy]");
						kkoVO.setContent(content);
						kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
						kkoVO.setRecipientNum(empVO.getPhoneNum());
						kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
						kkoVO.setTemplateCode("contract003");
						kkoVO.setKkoBtnType("1");
						kkoVO.setKkoBtnInfo("Candy^WL^^");
						bizTalkDAO.insBizTalk(kkoVO);
					}
					count++;
				}
			}
			result.setEmpNonce(Integer.toString(count));
			workbook.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Description  : 나눔HR 인사정보 데이터 
	 * @Method Name : sendNanumhrEmpExcel
	 * @return EmpVO
	 * @throws Exception
	 */
	@Override
	public EmpVO sendNanumhrEmpExcel(String bizId, String xlsPath, String metaId, String fileId) throws Exception {
		List<EmpVO> empList = new ArrayList<EmpVO>();
		EmpVO result		= new EmpVO();
		System.out.println("[sendNanumhrEmpExcel]  기업id -> "  + bizId);
		System.out.println("[sendNanumhrEmpExcel]  파일경로 -> " + xlsPath);
		
		try {
			File file = new File(xlsPath);
			if (!file.exists()) {
				result.setMessage("XLS파일이 존재하지 않습니다.");
				System.out.println("[sendNanumhrEmpExcel] XLS파일이 존재하지 않습니다.");
				return result;
			}
			
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet[] sheets 	  = workbook.getSheets();
			
			if(sheets.length > 1){
				//worksheet가 여러개일 경우 파일을 분리해서 올려야 함.
				result.setMessage("엑셀에 시트가 여러개 존재합니다. 파일을 분리해주세요.");
				System.out.println("엑셀에 시트가 여러개 존재합니다. 파일을 분리해주세요.");
				return result;
			}
			
			Sheet sheet = sheets[0];
			int colNum  = sheet.getColumns();
			int rowNum  = sheet.getRows();

			//사번 컬럼
			int empNoCulumn = -1;
			System.out.println("sheet=>" + sheet.getName() + "_" + colNum + "_" + rowNum);
			
			// 사번 컬럼 탐색
			for (int r=0; r < colNum; r++) {
				String empNoHeader = ExcelUtil.getCellValue(sheet.getCell(r, 0));
				System.out.println("header => "+empNoHeader);
				if(StringUtil.StringReplace(empNoHeader.replaceAll(" ", "")).equals("사번")){
					empNoCulumn = r;
					break;
				}
				if(empNoCulumn == -1 && StringUtil.StringReplace(empNoHeader.replaceAll(" ", "")).equals("사원번호")){
					empNoCulumn = r;
					break;
				}
				if(empNoCulumn == -1 && StringUtil.StringReplace(empNoHeader.replaceAll(" ", "")).equals("회사번호")){
					empNoCulumn = r;
					break;
				}
				if(empNoCulumn == -1 && StringUtil.StringReplace(empNoHeader.replaceAll(" ", "")).equals("EMPNO")){
					empNoCulumn = r;
					break;
				}
			}
			
			if(empNoCulumn == -1){
				result.setMessage("사번 컬럼이 존재하지 않습니다.");
				System.out.println("[sendNanumhrEmpExcel] 사번 컬럼이 존재하지 않습니다.");
				return result;
			}
			
			int successCount = 0;
			
			// 나눔HR 엑셀 컬럼명 맵핑
			for(int s=2; s < rowNum; s++){
				Map<MetaDataVO, MetaDataVO> mapList = new HashMap<MetaDataVO, MetaDataVO>();
				List<MetaDataVO> metaDataList = new ArrayList<MetaDataVO>();
				String empNoData = ExcelUtil.getCellValue(sheet.getCell(empNoCulumn, s));
				if(StringUtil.isNull(empNoData)){
					result.setMessage(s+"번 row의 사번 데이터가 존재하지 않습니다.");
					System.out.println("[sendNanumhrEmpExcel] 사번 데이터이 존재하지 않습니다.");
					continue;
				}
				//사번 - dataId
				for(int r=0; r < colNum; r++){
					MetaDataVO headerVO = new MetaDataVO();
					headerVO.setEmpNo(empNoData);
					headerVO.setDataId(ExcelUtil.getCellValue(sheet.getCell(r, 0)));
					if(StringUtil.isNull(ExcelUtil.getCellValue(sheet.getCell(r, 0)))){
						result.setMessage((r+1)+"번의 헤더 데이터가 존재하지 않습니다.");
						System.out.println("[sendNanumhrEmpExcel]("+r+", "+0+")번의 헤더 데이터가 존재하지 않습니다.");
						continue;
					}
					MetaDataVO bodyVO = new MetaDataVO();
					bodyVO.setBizId(bizId);
					bodyVO.setMetaId(metaId);
					bodyVO.setEmpNo(empNoData);
					bodyVO.setDataId(ExcelUtil.getCellValue(sheet.getCell(r, 0)));
					bodyVO.setDataValue(ExcelUtil.getCellValue(sheet.getCell(r, s)));
					mapList.put(headerVO, bodyVO);
				}
				//ROW 별로 insert
				metaDataList=new ArrayList<>(mapList.values());
				if(metaDataList.size() > 0){
					metaDAO.insMetaDataList(metaDataList);
					successCount ++;
				}
			}
			
			//insert된 meta데이터 중 emp에 존재하는 데이터 조회 -> 조회 후 update
			EmpVO empDataVO = new EmpVO();
			empDataVO.setMetaId(metaId);
			empDataVO.setBizId(bizId);
			List<EmpVO> resList = empDAO.getEmpExist(empDataVO);

			System.out.println("#[sendNanumhrEmpExcel] tbl_meta_data -> tbl_emp 총 count : "+successCount);
			System.out.println("#[sendNanumhrEmpExcel] tbl_meta_data -> tbl_emp 업데이트 count : "+resList.size());
			for(int i=0;i<resList.size();i++){
				//이미 emp에 존재하는 데이터일 경우 null이여도 update진행
				empDAO.updEmpList(resList.get(i));
			}
			//emp에 존재하지 않는 meta 데이터 조회 후 insert
			empDAO.insMetaDataToEmp(empDataVO);
			
			System.out.println("#[sendNanumhrEmpExcel] : excel -> tbl_meta_data ["+successCount+"]건 성공");
			result.setEmpNonce("엑셀에 업로드 된 "+(rowNum-2)+"건의 데이터 중"+successCount);
			workbook.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public List<EmpVO> getEmpUserGrpList(EmpVO vo)  throws Exception {
		return empDAO.getEmpUserGrpList(vo);
	}

	@Override
	public int getEmpUserGrpListCount(EmpVO vo)  throws Exception {
		return empDAO.getEmpUserGrpListCount(vo);
	}

	@Override
	public int sendAppInsSms(List<EmpVO> list) {
		int count = 0;

		for (int i=0; i < list.size(); i++) {
			EmpVO selEmpVO = list.get(i);
			EmpVO empVO	   = empDAO.getEmp(selEmpVO);
			
			if (empVO != null) {
				
				if (StringUtil.isNull(empVO.getLoginId())) {
					
					String content = "";
					content += "["+empVO.getBizName() +"] ["+empVO.getEmpName() + "]님 \n";
					content += "이제 모바일로 급여명세서,연말정산,전자계약, 연차등을 쉽게 관리합니다. \n";
					content += "아래 주소로 뉴젠 캔디를 설치해주세요. \n";
					content += " https://app.ieumsign.co.kr \n";
					
					BizTalkKKOVO kkoVO = new BizTalkKKOVO();
					kkoVO.setSubject("[뉴젠피앤피 * Candy]");
					kkoVO.setContent(content);
					kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
					kkoVO.setRecipientNum(empVO.getPhoneNum());
					bizTalkDAO.insBizTalkMms(kkoVO);
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public void insEmpLog(EmpLogVO vo) throws Exception {
		empDAO.insEmpLog(vo);
	}

	/**
	 * Description  : 나눔HR 인사정보 업로드시 압축 후 서버 저장(D:/KFORM/META_FILE/.../file, 암호 : 최우영1!)
	 * @Method Name : getZipFileAddList
	 * @param request  : HttpServletRequest 객체
	 * @return FileVO
	 * @throws Exception
	 */
	
	public FileVO getZipFileAddList(String bizId, HttpServletRequest request, String directory) throws Exception {
		MultipartHttpServletRequest multiRequest = null;
		FileVO resultVO = new FileVO();
		
		try {
			multiRequest = (MultipartHttpServletRequest) request;
		} catch (ClassCastException ex) {
			return null;
		}
		// 파일 추출
		Map<String, MultipartFile> files = multiRequest.getFileMap();
		
		File temp = new File(directory);
		if (!temp.exists()) { temp.mkdirs(); }
		
		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		String filenameToSave;
		String originalFilename;
		
		while (itr.hasNext()) {
			file = itr.next().getValue();
			originalFilename = file.getOriginalFilename();
			
			if (!"".equals(originalFilename)) {

				//파일명 중복 처리
				filenameToSave = MultipartFileUtil.getFilenameToSave(directory, originalFilename);
				
				// 경로 미 존재 시 생성함.
				File temp1 = new File(directory);
				if (!temp1.exists()) { temp1.mkdirs(); }
				
				File realFile = new File(directory + filenameToSave);
				System.out.println("#나눔 HR인사정보 파일 위치(EXCEL) : "+realFile);
				
				//기존에 존재하는 파일이라면 기존파일 제거
				if (realFile.exists()) { realFile.delete(); }
				
				// 임시 파일 저장(업로드한 엑셀 파일)
				file.transferTo(realFile);
				
				String zipFileName = StringUtil.getUUID()+".zip";
				
				/* zip파일 생성 및 암호화 */
	            ZipParameters parameters = new ZipParameters();
	            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
	            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
	            parameters.setEncryptFiles(true);
	            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
	            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
	            parameters.setPassword("chldndud1!");

				/* 업로드 한 엑셀파일 zip 생성*/
	            ZipFile zipFile = new ZipFile(directory + zipFileName);
	            zipFile.addFile(realFile, parameters);
	            
				System.out.println("#나눔 HR인사정보 zip 파일 위치 : "+directory + zipFileName);
				
				//DB처리
				MetaFileVO vo = new MetaFileVO();
				vo.setBizId(bizId);
				vo.setFileName(zipFileName);
				vo.setFilePath(directory);
				String metaId = metaDAO.insMetaFile(vo);

				// 파일 정보를 VO에 설정
				resultVO.setFileStrePath(directory);
				resultVO.setFileStreNm(filenameToSave);
				resultVO.setFileStreOriNm(originalFilename);
				resultVO.setFileSize(file.getSize());
				resultVO.setFileExt("excel");
				resultVO.setFileKey(metaId);
			}
		}
		return resultVO;
	}
	
	public EmpVO selectEmpInfo(EmpVO vo) throws Exception{
		return empDAO.getEmp(vo);
	}

	@Override
	public List<EmpVO> getCustomerList(EmpVO vo, SessionVO loginVO)  throws Exception {
		return empDAO.getCustomerList(vo);
	}

	@Override
	public int getCustomerListCount(EmpVO vo)  throws Exception {
		return empDAO.getCustomerListCount(vo);
	}

	@Override
	public boolean insCustomer(EmpVO vo) throws Exception {
		EmpVO resultVO = new EmpVO();
		resultVO 	   = empDAO.getCustomer(vo);
		if(resultVO==null) {
			empDAO.insCustomer(vo);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int updCustomer(EmpVO vo) throws Exception {
		return empDAO.updCustomer(vo);
	}

	@Override
	public int delCustomer(EmpVO vo) throws Exception {
		int result  = 0;
		EmpVO empVO = empDAO.getCustomer(vo);

		if(empVO==null) {
			System.out.println("[delCustomer] 거래처정보를 찾을수 없습니다."+vo.getBusinessNo());
			return 0;
		}
		result = empDAO.delCustomer(vo);

		return result;
	}

	@Override
	public EmpVO sendCustomerExcel(String bizId, String xlsPath, String insEmpNo) throws Exception {
		List<EmpVO> empList 	  = new ArrayList();
		EmpVO result 			  = new EmpVO();
		List<String> emptyRowList = new ArrayList<String>();
		System.out.println("[sendCustomerExcel]  기업id -> "+bizId);

		try {
			File file = new File(xlsPath);
			if (!file.exists()) {
				System.out.println("[sendCustomerExcel] XLS파일이 존재하지 않습니다.");
				return null;
			}

			Workbook workbook =  Workbook.getWorkbook(file);
			Sheet[] sheets 	  = workbook.getSheets();

			for(int s=0;s<sheets.length;s++) {
				Sheet sheet = sheets[s];
				int colNum  = sheet.getColumns();
				int rowNum  = sheet.getRows();

				System.out.println("sheet=>"+sheet.getName()+"_"+colNum+"_"+rowNum);

				for(int r=3; r < rowNum; r++) {

					String bizBusinessNo	= ExcelUtil.getCellValue(sheet.getCell(2, r));
					String customerName		= ExcelUtil.getCellValue(sheet.getCell(3, r));
					String businessNo		= ExcelUtil.getCellValue(sheet.getCell(4, r));
					String ownerName		= ExcelUtil.getCellValue(sheet.getCell(5, r));
					String addr1			= ExcelUtil.getCellValue(sheet.getCell(6, r));
					String managerName		= ExcelUtil.getCellValue(sheet.getCell(7, r));
					String managerPhoneNum	= ExcelUtil.getCellValue(sheet.getCell(8, r));
					String managerEmail		= ExcelUtil.getCellValue(sheet.getCell(9, r));
					String managerDeptName	= ExcelUtil.getCellValue(sheet.getCell(10, r));

					bizBusinessNo	= StringUtil.StringReplace(bizBusinessNo);
					businessNo		= StringUtil.StringReplace(businessNo);
					managerPhoneNum	= StringUtil.StringReplace(managerPhoneNum);
					managerPhoneNum	= managerPhoneNum.replaceAll(" ",""); // 휴대폰번호 공백허용 제거

					// 사업자번호가 오류인경우 무시하고 다음행을 처리하도록 요청(9/1 설계운영팀 김정인 요청)
					if(businessNo.length() < 10){
						emptyRowList.add(Integer.toString(r+1));
						System.out.println("[sendCustomerExcel] 사업자번호 오류로 pass한 행 >> " + r+1);
						continue;
					}

					BizGrpVO bizGrpVO = new BizGrpVO();
					bizGrpVO.setBusinessNo(bizBusinessNo);
					bizGrpVO.setBizId(bizId);
					BizGrpVO checkBizVO = bizDAO.getBizExsit(bizGrpVO);

					if(checkBizVO==null){
						System.out.println("입력하신 [ "+customerName+" ] 님의 사업자등록번호가 존재하지 않습니다.");
						result.setEmpName(customerName);
						result.setEmpNonce("0");
						return result;
					}

					// 거래처 정보 입력
					EmpVO empVO = new EmpVO();
					empVO.setBizId(checkBizVO.getBizId());
					empVO.setCustomerName(customerName);
					empVO.setBusinessNo(businessNo);
					empVO.setOwnerName(ownerName);
					empVO.setAddr1(addr1);
					empVO.setManagerName(managerName);
					empVO.setManagerPhoneNum(managerPhoneNum);
					empVO.setManagerEmail(managerEmail);
					empVO.setManagerDeptName(managerDeptName);
					empVO.setInsEmpNo(insEmpNo);
					empVO.setUpdEmpNo(insEmpNo);

					if (!StringUtil.isNotNull(businessNo) ) break;

					if (!StringUtil.isNotNull(bizBusinessNo) || !StringUtil.isNotNull(customerName) || !StringUtil.isNotNull(managerPhoneNum) || !StringUtil.isNotNull(managerEmail)) {
						System.out.println(customerName+"님의 필수입력항목이 없습니다.");
						result.setEmpName(customerName);
						return result;
					}
					empList.add(empVO);
				}
			}

			int insCount 	  = 0;
			int updCount 	  = 0;
			int dupCount 	  = 0;
			int emptyRowCount = 0;

			List<String> dupEmpRow = new ArrayList<String>();	// 중복열 체크

			// 거래처 정보 등록
			for (int i=0;i<empList.size();i++) {

				EmpVO empVO   = empList.get(i);
				EmpVO empNoVO = new EmpVO();
				empNoVO.setBizId(empVO.getBizId());
				empNoVO.setBusinessNo(empVO.getBusinessNo());
				EmpVO checkEmpVO = empDAO.getCustomer(empNoVO);

				if(checkEmpVO!=null) {

					System.out.println("[sendCustomerExcel] 이미 존재하는 거래처["+empVO.getCustomerName()+"] 정보입니다.");

					if (areValuesEqual(checkEmpVO.getBusinessNo(),empVO.getBusinessNo())) {
						// 모든 데이터가 같을 시
						if (areValuesEqual(checkEmpVO.getCustomerName(),empVO.getCustomerName()) &&
						areValuesEqual(checkEmpVO.getBizId(),empVO.getBizId()) &&
						areValuesEqual(checkEmpVO.getOwnerName(),empVO.getOwnerName()) &&
						areValuesEqual(checkEmpVO.getAddr1(),empVO.getAddr1()) &&
						areValuesEqual(checkEmpVO.getManagerName(),empVO.getManagerName()) &&
						areValuesEqual(checkEmpVO.getManagerPhoneNum(),empVO.getManagerPhoneNum()) &&
						areValuesEqual(checkEmpVO.getManagerEmail(),empVO.getManagerEmail()) &&
						areValuesEqual(checkEmpVO.getManagerDeptName(),empVO.getManagerDeptName())) {

							int rowNum = i + 4;
							rowNum	   = rowNum + emptyRowCount;

							for (String emptyRow : emptyRowList) {
								if (Integer.toString(rowNum).equals(emptyRow)) {
									emptyRowCount++;
									rowNum++;
								}
							}
							System.out.println("[sendCustomerExcel] 중복된 데이터 행번호 >> " + rowNum);
							dupCount++;

						} else {
							// 기존에 존재하는 사업자 등록번호 데이터의 다른 정보 변동사항이 있을 시
							int rowNum = i+4;
							rowNum 	   = rowNum + emptyRowCount;

							for (String emptyRow : emptyRowList) {
								if (Integer.toString(rowNum).equals(emptyRow)) {
									emptyRowCount++;
									rowNum++;
								}
							}
							System.out.println("[sendCustomerExcel] 중복된 데이터 행번호 >> "+rowNum);
							dupEmpRow.add(Integer.toString(rowNum));
							updCount++;
						}
					}
				} else {
					// 거래처 정보 입력
					empDAO.insCustomer(empVO);
					insCount++;
				}
			}
			result.setEmpNonce(Integer.toString(insCount + updCount + dupCount));
			result.setInsCount(Integer.toString(insCount));
			result.setUpdCount(Integer.toString(updCount));
			result.setDupCount(Integer.toString(dupCount));
			result.setDuplicateEmpRow(dupEmpRow);
			workbook.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public EmpVO getCustomer(EmpVO vo)  throws Exception { return empDAO.getCustomer(vo); }


	// 인사 목록 엑셀 생성
	@Override
	public int downUserDataExcel(String filePath, String fileName, EmpVO vo) throws Exception {

		String excelFile = filePath + fileName;
		int result 		 = 0;
		HSSFWorkbook wb  = new HSSFWorkbook ();
		HSSFSheet sheet  = wb.createSheet(fileName);

		if (sheet == null) { return -1; }

		sheet.setColumnWidth(0, 3 * _oneByte);
		sheet.setColumnWidth(1, 10 * _oneByte);
		sheet.setColumnWidth(2, 3 * _oneByte);

		// 테두리 스타일
		CellStyle headStyle = wb.createCellStyle();
		Font redFont 		= wb.createFont();
		redFont.setColor(IndexedColors.RED.getIndex());
		headStyle.setFont(redFont);

		// 엑셀 헤더 생성
		Row rowHeader = sheet.createRow(0);
		Cell cell 	  = rowHeader.createCell(1);
		cell.setCellValue("* 빨간색 표시는 필수 입력항목입니다. 신규로 등록할 임직원정보를 [작성예]를 참고하셔서 작성하여 주시기 바랍니다.");
		cell.setCellStyle(headStyle);

		// 노란 배경색 스타일
		CellStyle yellowBackgroundStyle = wb.createCellStyle();
		yellowBackgroundStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		yellowBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		yellowBackgroundStyle.setBorderTop(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderBottom(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderLeft(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderRight(BorderStyle.THIN);

		Row rowHeader1 = sheet.createRow(1);

		// 해더 상태
		String[] headerTitles = {
				"소속기업명",
				"사업자등록번호",
				"사원번호",
				"부서",
				"성명",
				"생년월일",
				"입사일자",
				"퇴사일자",
				"주소",
				"직급",
				"호봉",
				"전화번호",
				"휴대폰번호",
				"이메일"
		};

		for (int i = 1; i <= headerTitles.length; i++) {
			Cell cell1 = rowHeader1.createCell(i);
			cell1.setCellValue(headerTitles[i - 1]);
			cell1.setCellStyle(yellowBackgroundStyle);
			sheet.setColumnWidth(i, (headerTitles[i - 1].length() + 15) * 256);
		}

		Font blueFont  = wb.createFont();
		Row rowHeader2 = sheet.createRow(2);

		String[] cellValues2 = {
			"작성예",
			"종사업장1",
			"1231212345",
			"1001",
			"영업팀",
			"이영희",
			"19900101",
			"20220101",
			"20221231",
			"서울시 영등포구 영등포동 1234 OO아파트",
			"대리",
			"2",
			"0212345678",
			"01012345678",
			"sample@newzenpnp.com"
		};

		for (int j = 0; j < cellValues2.length; j++) {
			Cell cell2 = rowHeader2.createCell(j);
			cell2.setCellValue(cellValues2[j]);

			if (j == 0 || j == 1 || j == 2 || j == 3 || j == 5 || j == 13 || j == 14) {
				CellStyle grayBackgroundStyle = wb.createCellStyle();
				grayBackgroundStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				grayBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				grayBackgroundStyle.setBorderTop(BorderStyle.THIN);
				grayBackgroundStyle.setBorderBottom(BorderStyle.THIN);
				grayBackgroundStyle.setBorderLeft(BorderStyle.THIN);
				grayBackgroundStyle.setBorderRight(BorderStyle.THIN);
				grayBackgroundStyle.setFont(redFont);
				cell2.setCellStyle(grayBackgroundStyle);
			} else {
				CellStyle grayBackgroundStyle = wb.createCellStyle();
				grayBackgroundStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				grayBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				grayBackgroundStyle.setBorderTop(BorderStyle.THIN);
				grayBackgroundStyle.setBorderBottom(BorderStyle.THIN);
				grayBackgroundStyle.setBorderLeft(BorderStyle.THIN);
				grayBackgroundStyle.setBorderRight(BorderStyle.THIN);
				blueFont.setColor(IndexedColors.BLUE.getIndex());
				grayBackgroundStyle.setFont(blueFont);
				cell2.setCellStyle(grayBackgroundStyle);
			}
		}

		List<EmpVO> empList = empDAO.getExcelUserDown(vo);// 엑셀데이터 생성

		for(int i = 0; i < empList.size(); i++) {

			Row rowData = sheet.createRow(3 + i);
			EmpVO emp   = empList.get(i);

			String[] cellValues = {
				emp.getBizName(),		// 소속기업명
				emp.getBusinessNo(),	// 사업자등록번호
				emp.getEmpNo(),			// 사원번호
				emp.getDeptName(),		// 부서
				emp.getEmpName(),		// 성명
				emp.getUserDate(),		// 생년월일
				emp.getJoinDate(),		// 입사일자
				emp.getLeaveDate(),		// 퇴사일자
				emp.getAddr1(),			// 주소
				emp.getPositionName(),	// 직급
				emp.getStepName(),		// 호봉
				emp.getTelNum(),		// 전화번호
				emp.getPhoneNum(),		// 휴대폰번호
				emp.getEMail()			// 이메일
			};

			for (int j = 1; j <= cellValues.length; j++) {
				Cell cellData = rowData.createCell(j);
				// 데이터 값이 0일시 빈칸으로 변환
				String cellValue = cellValues[j - 1];
				if (cellValue != null && cellValue.equals("0")) { cellValue = ""; }
				
				cellData.setCellValue(cellValue);

			}
		}
		try {
			FileOutputStream file = new FileOutputStream(excelFile);
			wb.write(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 인사 목록 엑셀 생성 ( 부문관리자)
	@Override
	public int downUserGrpDataExcel(String filePath, String fileName, EmpVO vo) throws Exception {

		String excelFile = filePath + fileName;
		int result 		 = 0;
		HSSFWorkbook wb  = new HSSFWorkbook ();
		HSSFSheet sheet  = wb.createSheet(fileName);

		if (sheet == null) { return -1; }

		sheet.setColumnWidth(0, 3 * _oneByte);
		sheet.setColumnWidth(1, 10 * _oneByte);
		sheet.setColumnWidth(2, 3 * _oneByte);

		// 테두리 스타일
		CellStyle headStyle = wb.createCellStyle();
		Font redFont 		= wb.createFont();
		redFont.setColor(IndexedColors.RED.getIndex());
		headStyle.setFont(redFont);

		// 엑셀 헤더 생성
		Row rowHeader = sheet.createRow(0);
		Cell cell 	  = rowHeader.createCell(1);
		cell.setCellValue("* 빨간색 표시는 필수 입력항목입니다. 신규로 등록할 임직원정보를 [작성예]를 참고하셔서 작성하여 주시기 바랍니다.");
		cell.setCellStyle(headStyle);

		// 노란 배경색 스타일
		CellStyle yellowBackgroundStyle = wb.createCellStyle();
		yellowBackgroundStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		yellowBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		yellowBackgroundStyle.setBorderTop(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderBottom(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderLeft(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderRight(BorderStyle.THIN);

		Row rowHeader1 = sheet.createRow(1);
		// 해더 상태
		String[] headerTitles = {
				"소속기업명",
				"사업자등록번호",
				"사원번호",
				"부서",
				"성명",
				"생년월일",
				"입사일자",
				"퇴사일자",
				"주소",
				"직급",
				"호봉",
				"전화번호",
				"휴대폰번호",
				"이메일"
		};

		for (int i = 1; i <= headerTitles.length; i++) {
			Cell cell1 = rowHeader1.createCell(i);
			cell1.setCellValue(headerTitles[i - 1]);
			cell1.setCellStyle(yellowBackgroundStyle);
			sheet.setColumnWidth(i, (headerTitles[i - 1].length() + 15) * 256);
		}

		Font blueFont  = wb.createFont();
		Row rowHeader2 = sheet.createRow(2);

		String[] cellValues2 = {
				"작성예",
				"종사업장1",
				"1231212345",
				"1001",
				"영업팀",
				"이영희",
				"19900101",
				"20220101",
				"20221231",
				"서울시 영등포구 영등포동 1234 OO아파트",
				"대리",
				"2",
				"0212345678",
				"01012345678",
				"sample@newzenpnp.com"
		};

		for (int j = 0; j < cellValues2.length; j++) {
			Cell cell2 = rowHeader2.createCell(j);
			cell2.setCellValue(cellValues2[j]);

			if (j == 0 || j == 1 || j == 2 || j == 3 || j == 5 || j == 13 || j== 14) {
				CellStyle grayBackgroundStyle = wb.createCellStyle();
				grayBackgroundStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				grayBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				grayBackgroundStyle.setBorderTop(BorderStyle.THIN);
				grayBackgroundStyle.setBorderBottom(BorderStyle.THIN);
				grayBackgroundStyle.setBorderLeft(BorderStyle.THIN);
				grayBackgroundStyle.setBorderRight(BorderStyle.THIN);
				grayBackgroundStyle.setFont(redFont);
				cell2.setCellStyle(grayBackgroundStyle);
			} else {
				CellStyle grayBackgroundStyle = wb.createCellStyle();
				grayBackgroundStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				grayBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				grayBackgroundStyle.setBorderTop(BorderStyle.THIN);
				grayBackgroundStyle.setBorderBottom(BorderStyle.THIN);
				grayBackgroundStyle.setBorderLeft(BorderStyle.THIN);
				grayBackgroundStyle.setBorderRight(BorderStyle.THIN);
				blueFont.setColor(IndexedColors.BLUE.getIndex());
				grayBackgroundStyle.setFont(blueFont);
				cell2.setCellStyle(grayBackgroundStyle);
			}
		}
		List<EmpVO> empList = empDAO.getExcelUserGrpDown(vo);// 엑셀데이터 생성

		for(int i = 0; i < empList.size(); i++) {

			Row rowData = sheet.createRow(3 + i);
			EmpVO emp   = empList.get(i);

			String[] cellValues = {
					emp.getBizName(),		// 소속기업명
					emp.getBusinessNo(),	// 사업자등록번호
					emp.getEmpNo(),			// 사원번호
					emp.getDeptName(),		// 부서
					emp.getEmpName(),		// 성명
					emp.getUserDate(),		// 생년월일
					emp.getJoinDate(),		// 입사일자
					emp.getLeaveDate(),		// 퇴사일자
					emp.getAddr1(),			// 주소
					emp.getPositionName(),	// 직급
					emp.getStepName(),		// 호봉
					emp.getTelNum(),		// 전화번호
					emp.getPhoneNum(),		// 휴대폰번호
					emp.getEMail()			// 이메일
			};

			for (int j = 1; j <= cellValues.length; j++) {
				Cell cellData = rowData.createCell(j);
				cellData.setCellValue(cellValues[j - 1]);
			}
		}
		try {
			FileOutputStream file = new FileOutputStream(excelFile);
			wb.write(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 거래처 목록 엑셀 생성
	@Override
	public int downCustomerDataExcel(String filePath, String fileName, EmpVO vo) throws Exception {

		String excelFile = filePath + fileName;
		int result 		 = 0;
		HSSFWorkbook wb  = new HSSFWorkbook ();
		HSSFSheet sheet  = wb.createSheet(fileName);
		if (sheet == null) { return -1; }

		sheet.setColumnWidth(0, 3 * _oneByte);
		sheet.setColumnWidth(1, 10 * _oneByte);
		sheet.setColumnWidth(2, 3 * _oneByte);

		// 테두리 스타일
		CellStyle headStyle = wb.createCellStyle();
		Font redFont 		= wb.createFont();
		redFont.setColor(IndexedColors.RED.getIndex());
		headStyle.setFont(redFont);

		// 엑셀 헤더 생성
		Row rowHeader = sheet.createRow(0);
		Cell cell 	  = rowHeader.createCell(1);
		cell.setCellValue("* 빨간색 표시는 필수 입력항목입니다. 신규로 등록할 거래처정보를 [작성예]를 참고하셔서 작성하여 주시기 바랍니다.");
		cell.setCellStyle(headStyle);

		// 노란 배경색 스타일
		CellStyle yellowBackgroundStyle = wb.createCellStyle();
		yellowBackgroundStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		yellowBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		yellowBackgroundStyle.setBorderTop(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderBottom(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderLeft(BorderStyle.THIN);
		yellowBackgroundStyle.setBorderRight(BorderStyle.THIN);

		Row rowHeader1 = sheet.createRow(1);
		// 해더 상태
		String[] headerTitles = {
				"소속기업명",
				"사업자등록번호",
				"거래처명",
				"거래처 사업자등록번호",
				"거래처 대표자명",
				"거래처 주소",
				"거래처 담당자명",
				"거래처 담당자 휴대폰번호",
				"거래처 담당자 이메일",
				"거래처 담당자 부서명"
		};

		for (int i = 1; i <= headerTitles.length; i++) {
			Cell cell1 = rowHeader1.createCell(i);
			cell1.setCellValue(headerTitles[i - 1]);
			cell1.setCellStyle(yellowBackgroundStyle);
			sheet.setColumnWidth(i, (headerTitles[i - 1].length() + 15) * 256);
		}

		Font blueFont  = wb.createFont();
		Row rowHeader2 = sheet.createRow(2);

		String[] cellValues2 = {
				"작성예",
				"뉴젠피앤피",
				"1112233333",
				"거래처1",
				"9876543210",
				"홍길동",
				"서울시 영등포구 영등포동 1234 OO아파트",
				"박길동",
				"01012345678",
				"sample@newzenpnp.com",
				"구매팀"
		};

		for (int j = 0; j < cellValues2.length; j++) {
			Cell cell2 = rowHeader2.createCell(j);
			cell2.setCellValue(cellValues2[j]);

			if (j == 0 || j == 1 || j == 2 || j == 3 || j == 4  || j == 8 || j == 9 ) {
				CellStyle grayBackgroundStyle = wb.createCellStyle();
				grayBackgroundStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				grayBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				grayBackgroundStyle.setBorderTop(BorderStyle.THIN);
				grayBackgroundStyle.setBorderBottom(BorderStyle.THIN);
				grayBackgroundStyle.setBorderLeft(BorderStyle.THIN);
				grayBackgroundStyle.setBorderRight(BorderStyle.THIN);
				grayBackgroundStyle.setFont(redFont);
				cell2.setCellStyle(grayBackgroundStyle);
			} else {
				CellStyle grayBackgroundStyle = wb.createCellStyle();
				grayBackgroundStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				grayBackgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				grayBackgroundStyle.setBorderTop(BorderStyle.THIN);
				grayBackgroundStyle.setBorderBottom(BorderStyle.THIN);
				grayBackgroundStyle.setBorderLeft(BorderStyle.THIN);
				grayBackgroundStyle.setBorderRight(BorderStyle.THIN);
				blueFont.setColor(IndexedColors.BLUE.getIndex());
				grayBackgroundStyle.setFont(blueFont);
				cell2.setCellStyle(grayBackgroundStyle);
			}
		}

		List<EmpVO> empList = empDAO.getExcelCustomerDown(vo);// 엑셀데이터 생성

		for(int i = 0; i < empList.size(); i++) {

			Row rowData = sheet.createRow(3 + i);
			EmpVO emp   = empList.get(i);

			String[] cellValues = {
					emp.getBizName(),			// 소속기업명
					emp.getBizBusinessNo(),		// 소속기업 사업자등록번호
					emp.getCustomerName(),		// 거래처명
					emp.getBusinessNo(),		// 거래처 사업자 등록번호
					emp.getOwnerName(),			// 거래처 대표자명
					emp.getAddr1(),				// 거래처 주소
					emp.getManagerName(),		// 거래처 담당자 명
					emp.getManagerPhoneNum(),	// 거래처 담당자 휴대폰번호
					emp.getManagerEmail(),		// 거래처 담당자 이메일
					emp.getManagerDeptName()	// 거래처 담당자 부서명
			};

			for (int j = 1; j <= cellValues.length; j++) {
				Cell cellData = rowData.createCell(j);
				cellData.setCellValue(cellValues[j - 1]);
			}
		}
		try {
			FileOutputStream file = new FileOutputStream(excelFile);
			wb.write(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	// 일괄등록 엑셀 데이터와 DB 데이터 비교 (NULL과 ''공백 같은 값 처리)
	private boolean areValuesEqual(String value1, String value2) {
		if (value1 == null) value1 = "";
		if (value2 == null) value2 = "";
		return value1.equals(value2);
	}

}


