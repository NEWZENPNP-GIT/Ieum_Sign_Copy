package com.ezsign.popup.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.framework.exception.AngullarProcessException;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;

import com.ezsign.popup.dao.ExcelUploadDAO;
import com.ezsign.popup.service.ExcelUploadService;
import com.ezsign.popup.vo.ExcelUploadRequest;
import com.ezsign.popup.vo.ExcelUploadVO;
import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.service.ContractService;
import com.ezsign.contract.vo.ContractFormVO;
import com.ezsign.contract.vo.ContractPersonNewFormVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;

@Service("excelUploadService")
public class ExcelUploadServiceImpl implements ExcelUploadService {

	private Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "excelUploadDAO")
    private ExcelUploadDAO excelUploadDAO;

    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "contractService")
    private ContractService contractService;
    
    @Resource(name = "contractDAO")
    private ContractDAO contractDAO;
    

    
    @Override
    public List<ExcelUploadVO> getExcelMappingList(ExcelUploadVO excelUploadVO) {
        List<ExcelUploadVO> list = excelUploadDAO.getExcelMappingList(excelUploadVO);

        CodeVO codeVO = new CodeVO();

        for (ExcelUploadVO vo : list) {
            if ("5".equals(vo.getColumnType())) {
                if (vo.getGrpCommCode() == null) {
                    if (vo.getCodeValue() != null) {
                        vo.setCodes(Arrays.asList(vo.getCodeValue().split(",")));
                    }
                } else {
                    vo.setCodes(new ArrayList<>());

                    codeVO.setGrpCommCode(vo.getGrpCommCode());
                    List<CodeVO> resultCode = codeDAO.getCodeList(codeVO);

                    for (CodeVO item : resultCode) {
                        vo.getCodes().add(item.getCommCode());
                    }
                }
            }
        }

        return list;
    }


    @Override
    public List<ExcelUploadVO> getExcelMappingContractList(ExcelUploadVO excelUploadVO) {
        List<ExcelUploadVO> list = new ArrayList<ExcelUploadVO>();
    	
        ContractPersonNewFormVO formVO = new ContractPersonNewFormVO();
        formVO.setBizId(excelUploadVO.getBizId());
        formVO.setFileId(excelUploadVO.getId());
    	List<ContractPersonNewFormVO> formList = contractDAO.getContractPersonNewFormList(formVO);

    	ExcelUploadVO excelFormVO = new ExcelUploadVO();
    	int row = 0;

        excelFormVO = new ExcelUploadVO();
    	excelFormVO.setBizId(excelUploadVO.getBizId());
    	excelFormVO.setExcelType(excelUploadVO.getExcelType());
    	excelFormVO.setColumnId("TXT_00000");
    	excelFormVO.setColumnType("1"); // 기본값(text)
    	excelFormVO.setRequired("1");
    	excelFormVO.setColumnName("TXT_00000");
    	excelFormVO.setDisplayName("사원번호");
    	excelFormVO.setDefaultOrder(row);
        list.add(excelFormVO);
        row++;
        
        for (ContractPersonNewFormVO vo : formList) {
        	excelFormVO = new ExcelUploadVO();
        	excelFormVO.setBizId(excelUploadVO.getBizId());
        	excelFormVO.setExcelType(excelUploadVO.getExcelType());
        	excelFormVO.setColumnId(vo.getFormDataId());
        	excelFormVO.setColumnType("1"); // 기본값(text)
        	excelFormVO.setRequired("1");
        	excelFormVO.setColumnName(vo.getFormDataId());
        	excelFormVO.setDisplayName(vo.getFormDataName());
        	excelFormVO.setDefaultOrder(row);
        	
            list.add(excelFormVO);
            row++;
        }
        
        return list;
    }
    
    @Override
    public List<ExcelUploadVO> getExcelMapMasterList(ExcelUploadVO excelUploadVO) {
        List<ExcelUploadVO> list = excelUploadDAO.getExcelMapMasterList(excelUploadVO);
        return list;
    }
    
    @Override
    public List<ExcelUploadVO> getExcelMapDetailList(ExcelUploadVO excelUploadVO) {
        List<ExcelUploadVO> list = excelUploadDAO.getExcelMapDetailList(excelUploadVO);
        return list;
    }
    
    @Override
    public int selectExcelMappingCnt(ExcelUploadVO excelUploadVO) throws Exception{
    	return excelUploadDAO.selectExcelMappingCnt(excelUploadVO);
    }

    @Override
    public void deleteExcelMap(ExcelUploadVO excelUploadVO) throws Exception{
    	excelUploadDAO.deleteExcelMap(excelUploadVO);
    	
    	excelUploadDAO.deleteExcelMapDetail(excelUploadVO);
    	
    }
    
    @Override
    public void insertExcelMap(List<ExcelUploadVO> excelUploadList) throws Exception{    	
    	for (int i=0;i<excelUploadList.size();i++) {
    		ExcelUploadVO vo = excelUploadList.get(i);
    		if(i==0) excelUploadDAO.insertExcelMap(vo);
        	excelUploadDAO.insertExcelMapDetail(vo);
    	}
    }
    
    /**
     * <pre>
     * 1. 개요 : 근로자 엑셀 파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType1
     * @date : 2020. 6. 9.
     * @author : 
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2020. 6. 9.					개발자				최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @param bizId
     *  @param request
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType1(String bizId, ExcelUploadRequest<EmpVO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
    		
    		int dataCnt = 0;
	        if (request.data != null) {

	        	EmpVO empVO = new EmpVO();
	        	empVO.setBizId(bizId);
	            
	            logger.debug("# 근로자 엑셀 파일 총 건수 : " + request.data.size() );
	            
	            for (EmpVO vo : request.data) {
	            	
	            	logger.debug("# 근로자 request : " + vo.toString());
	            	
					empVO.setBizId(bizId);
					empVO.setEmpNo(vo.getEmpNo());
					empVO.setEmpName(vo.getEmpName());
					empVO.setLoginId("");
					empVO.setEmpType("1");
					empVO.setAddr1(vo.getAddr1());
					empVO.setAddr2(vo.getAddr2());
					empVO.setTelNum(vo.getTelNum());
					empVO.setPhoneNum(vo.getPhoneNum());
					empVO.setUserDate(vo.getUserDate());
					empVO.setEMail(vo.getEMail());
					empVO.setJoinDate(vo.getJoinDate());
					empVO.setLeaveDate(vo.getLeaveDate());
					empVO.setEmpNonce("");
					empVO.setDeptName(vo.getDeptName());
					empVO.setPositionName(vo.getPositionName());
					empVO.setStepName(vo.getStepName());
					empVO.setCountryType("81");
					empVO.setConfirmType("N");

	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO != null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 이미 존재합니다.");
	                } else {
	                	if(StringUtil.isNull(vo.getEmpNo()) || StringUtil.isNull(vo.getEmpName()) ||
	                		StringUtil.isNull(vo.getPhoneNum()) || StringUtil.isNull(vo.getEMail()) ||
	                		StringUtil.isNull(vo.getUserDate())) {
	                		logger.info("필수항목누락:"+vo.getEmpNo()+","+vo.getEmpName()+","+vo.getPhoneNum()+","+vo.getEMail()+","+vo.getUserDate());
		                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ", " + vo.getEmpName() + ")의 "+
		                    									" 필수항목(사번, 성명, 휴대폰번호, 이메일, 생년월일, 입사일)중 누락된 항목이 존재합니다.");
	                	} else {
			                empDAO.insEmp(empVO);
			                dataCnt++;
	                	}
	                }
	            }
	        }
	        
	        resultMap.put("result", "1");
	        resultMap.put("message", "[ " + dataCnt + " ] 건이 처리되었습니다.");
	        
    	}catch(AngullarProcessException ex){
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
        
        return resultMap;
    }

    /**
     * <pre>
     * 1. 개요 : 계약조건 엑셀 파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelTypeContract
     * @date : 2020. 6. 10.
     * @author : 
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2020. 6. 10.					개발자				최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @param bizId
     *  @param request
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelTypeC(String bizId, String fileId, List<HashMap<String, String>> data) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
    		int dataCnt = 0;
	        if (data != null) {
	        	String empNo = "";
	    		String formData = "";
	    		
	    		ContractFormVO contractVO = new ContractFormVO();
	    		contractVO.setBizId(bizId);
	    		contractVO.setContractDate(DateUtil.getTimeStamp(3));
	    		contractVO.setContractFileId(fileId);
	    		
	            logger.debug("# 계약조건 엑셀 파일 총 건수 : " + data.size() );
	            
	        	ContractPersonNewFormVO newFormVO = new ContractPersonNewFormVO();
	        	newFormVO.setBizId(bizId);
	        	newFormVO.setFileId(fileId);
	        	List<ContractPersonNewFormVO> formList = contractDAO.getContractPersonNewFormList(newFormVO);
	        	
	        	for(ContractPersonNewFormVO formVO : formList) {
	        		String formDataId = formVO.getFormDataId();
	        		String formDataName = formVO.getFormDataName();
	        		
		            for (HashMap<String, String> vo : data) {
		            	String formDataValue = vo.get(formDataId);
		            	if(StringUtil.isNotNull(formDataValue)) {
		            		formData += formDataId + "^" + formDataName + "^" + formDataValue;
			            	logger.debug("# 계약조건 request : " + formDataId + "," + formDataValue);
		            		break;
		            	}
		            	
		            }
		            formData += "|";
	        	}
	        	

	            for (HashMap<String, String> vo : data) {
	            	String findEmpNo = vo.get("emp_no");
	            	if(StringUtil.isNull(findEmpNo)) {
	            		findEmpNo = vo.get("TXT_00000");
	            		if(StringUtil.isNull(findEmpNo)) {
	            			findEmpNo = vo.get("사원번호");
		            		if(StringUtil.isNull(findEmpNo)) {
		            			findEmpNo = vo.get("사번");
		            		}
	            		}
	            	}
	            	if(StringUtil.isNotNull(findEmpNo)) {
	            		empNo = findEmpNo;
	            	}
	            }
	            
	            if(StringUtil.isNotNull(empNo)) {
		        	EmpVO empVO = new EmpVO();
		        	empVO.setBizId(bizId);
	                empVO.setEmpNo(empNo);
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if(resultEmpVO==null) {
	                    throw new AngullarProcessException("사번(" + empNo + ")이 존재하지 않습니다.");
	                } else {
	    	    		contractVO.setUserId(resultEmpVO.getUserId());
	    	        	contractVO.setFormDataValue(formData);
	            		int count= contractService.createContractForm(contractVO);
		                dataCnt++;
	                }
	            } else {
                    throw new AngullarProcessException("사번 항목이 존재하지 않습니다.");
	            }
	        	
	        }
	        
	        resultMap.put("result", "1");
	        resultMap.put("message", "[ " + dataCnt + " ] 건이 처리되었습니다.");
	        
    	}catch(AngullarProcessException ex){
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
        
        return resultMap;
    }

    
    
}
