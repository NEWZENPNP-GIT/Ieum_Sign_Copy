package com.ezsign.framework.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.contract.service.ContractService;
import com.ezsign.contract.vo.ContractFormVO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubDataVO;


public class XmlNewzenParser {
	
	public static void main(String[] args) {
		
    	String XMLPath = "D:\\document\\K-Form\\뉴젠솔루션\\인터페이스_XML\\sample.xml";
    	
	    try {
			String filedata = FileUtil.readFile(XMLPath);
			List<EmpVO> empData = XMLtoEmployee(filedata);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<BizVO> XMLtoCompany(String xmldata) throws Exception {				
		List<BizVO> result = new ArrayList();
		
		SAXBuilder builder = new SAXBuilder();
		
	    //Document doc= builder.build(new InputSource(new StringReader(xmldata)));
		InputStream inputStream = new ByteArrayInputStream(xmldata.getBytes());
		UnicodeBOMInputStream cleanInputStream = new UnicodeBOMInputStream(inputStream);
		cleanInputStream.skipBOM();
		
		Document doc = builder.build(cleanInputStream);
	    
	    Element root= doc.getRootElement();
	    List<Element> InfoItems = root.getChildren("company_info");
	    
	    for(int i = 0 ; i < InfoItems.size() ; i++ ){
	    	Element childItem = InfoItems.get(i);
	    	
	    	List<Element> Items = childItem.getChildren("company");
	    	
		    for(int j = 0 ; j < Items.size() ; j++ ){
		    	BizVO dataVO = new BizVO();
		    	Element Item = Items.get(j);
		    	
		    	String businessNo = Item.getChild("business_no").getValue();
		    	businessNo = StringUtil.ReplaceAll(businessNo, "-", "");
		    	
		    	String bizName = Item.getChild("biz_name").getValue();
		    	String ownerName = Item.getChild("owner_name").getValue();
		    	String companyAddr1 = Item.getChild("company_addr1").getValue();
		    	String companyAddr2 = Item.getChild("company_addr2").getValue();
		    	String companyTelNum = StringUtil.ReplaceAll(Item.getChild("company_tel_num").getValue(), "-", "");
		    	String companyFaxNum = StringUtil.ReplaceAll(Item.getChild("company_fax_num").getValue(), "-", "");
		    	
		    	dataVO.setBusinessNo(businessNo);
		    	dataVO.setBizName(bizName);
		    	dataVO.setOwnerName(ownerName);
		    	dataVO.setAddr1(companyAddr1);
		    	dataVO.setAddr2(companyAddr2);
		    	dataVO.setCompanyTelNum(companyTelNum);
		    	dataVO.setCompanyFaxNum(companyFaxNum);
		    	
		    	result.add(dataVO);
		    }
	    }
	    
		return result;
	}

	public static List<EmpVO> XMLtoEmployee(String xmldata) throws Exception {				
		List<EmpVO> result = new ArrayList<EmpVO>();
		
		SAXBuilder builder = new SAXBuilder();		

	    //Document doc= builder.build(new InputSource(new StringReader(xmldata)));
		InputStream inputStream = new ByteArrayInputStream(xmldata.getBytes());
		UnicodeBOMInputStream cleanInputStream = new UnicodeBOMInputStream(inputStream);
		cleanInputStream.skipBOM();
		
		Document doc = builder.build(cleanInputStream);
		
	    Element root= doc.getRootElement();
	    List<Element> InfoItems = root.getChildren("employee_info");
	    
	    for(int i = 0 ; i < InfoItems.size() ; i++ ){
	    	Element childItem = InfoItems.get(i);
	    	
	    	List<Element> Items = childItem.getChildren("employee");
	    	
		    for(int j = 0 ; j < Items.size() ; j++ ){
		    	EmpVO dataVO = new EmpVO();
		    	Element Item = Items.get(j);
		    	
		    	String empNo =Item.getChild("emp_no").getValue();
		    	String empName = Item.getChild("emp_name").getValue();
		    	String addr1 = Item.getChild("user_addr1").getValue();
		    	String userDate = Item.getChild("user_date").getValue();
		    	String eMail = Item.getChild("e_mail").getValue();
		    	String phoneNum = StringUtil.ReplaceAll(Item.getChild("phone_num").getValue(), "-", "");
		    	String joinDate = Item.getChild("join_date").getValue();
		    	String leaveDate = Item.getChild("leave_date").getValue();
		    	String deptName = Item.getChild("dept_name").getValue();
		    	String positionName = "";
		    	String stepName = "";
		    	String userData = "";
		    	String countryType = "";
		    	String liveType = "";
		    	if(!Item.getChildren("position_name").isEmpty()) positionName = Item.getChild("position_name").getValue();
		    	if(!Item.getChildren("step_name").isEmpty()) stepName = Item.getChild("step_name").getValue();
		    	if(!Item.getChildren("user_data").isEmpty()) userData = Item.getChild("user_data").getValue();  // 개인식별번호
		    	if(!Item.getChildren("country_type").isEmpty()) countryType = Item.getChild("country_type").getValue();  // 내외국인코드
		    	if(!Item.getChildren("live_type").isEmpty()) liveType = Item.getChild("live_type").getValue();  // 거주구분 (연말정산용:liveType)
		    	
		    	
		    	dataVO.setEmpNo(empNo);
		    	dataVO.setEmpType("1");
		    	dataVO.setEmpName(empName);
		    	dataVO.setCountryType("82");
		    	dataVO.setAddr1(addr1);
		    	dataVO.setAddr2("");
		    	dataVO.setTelNum("");
		    	dataVO.setPhoneNum(phoneNum);
		    	dataVO.setUserDate(userDate);
		    	dataVO.setEMail(eMail);
		    	dataVO.setJoinDate(joinDate);
		    	dataVO.setLeaveDate(leaveDate);
		    	dataVO.setDeptName(deptName);
		    	dataVO.setPositionName(positionName);
		    	dataVO.setStepName(stepName);
		    	dataVO.setUserData(userData);
		    	dataVO.setCountryType(countryType);
		    	dataVO.setLiveType(liveType);
		    	
		    	result.add(dataVO);
		    }
	    	
	    }
	    
		return result;
	}

	public static List<ContractPersonVO> XMLtoContract(String xmldata) throws Exception {				
		List<ContractPersonVO> result = new ArrayList<ContractPersonVO>();
		
		SAXBuilder builder = new SAXBuilder();		

	    //Document doc= builder.build(new InputSource(new StringReader(xmldata)));
		InputStream inputStream = new ByteArrayInputStream(xmldata.getBytes());
		UnicodeBOMInputStream cleanInputStream = new UnicodeBOMInputStream(inputStream);
		cleanInputStream.skipBOM();
		
		Document doc = builder.build(cleanInputStream);
		
	    Element root= doc.getRootElement();
	    List<Element> InfoItems = root.getChildren("contract_info");
	    
	    for(int i = 0 ; i < InfoItems.size() ; i++ ){
	    	Element childItem = InfoItems.get(i);
	    	
	    	String empNo = childItem.getChild("emp_no").getValue();
	    	String contractDate = childItem.getChild("contract_date").getValue();
	    	String contract_file_id = childItem.getChild("contract_file_id").getValue();
	    	
			ContractPersonVO contractVO = new ContractPersonVO();
			contractVO.setEmpNo(empNo);
			contractVO.setContractDate(contractDate);
			contractVO.setContractFileId(contract_file_id);
	    	
	    	List<Element> Items = childItem.getChildren("contract");
	    	
			int txtrownum = 1;
			int chkrownum = 1;
		    for(int j = 0 ; j < Items.size() ; j++ ){
		    	ContractFormVO formVO = new ContractFormVO();
		    	Element Item = Items.get(j);

		    	String itemType =Item.getChild("item_type").getValue();
		    	String itemName =Item.getChild("item_name").getValue();
		    	String itemValue = Item.getChild("item_value").getValue();
		    	if(StringUtil.isNotNull(itemType)) {
		    		if (itemType.equals("0")) {
			    		formVO.setFormDataId("TXT_"+StringUtil.lpad(Integer.toString(txtrownum), 5, "0"));
			    		txtrownum++;
		    		} else if (itemType.equals("1")) {
			    		formVO.setFormDataId("CHK_"+StringUtil.lpad(Integer.toString(chkrownum), 5, "0"));
			    		chkrownum++;
		    		}
		    	} else {
		    		formVO.setFormDataId("TXT_"+StringUtil.lpad(Integer.toString(txtrownum), 5, "0"));
		    		txtrownum++;
		    	}
		    	formVO.setFormDataType(itemType);
		    	formVO.setFormDataName(itemName);
		    	formVO.setFormDataValue(itemValue);

				contractVO.addForm(formVO);
		    }
		    result.add(contractVO);
	    }
	    
		return result;
	}

	public static List<PaystubDataVO> XMLtoPaystub(String xmldata) throws Exception {				
		List<PaystubDataVO> result = new ArrayList();
		
		SAXBuilder builder = new SAXBuilder();		
	    //Document doc= builder.build(new InputSource(new StringReader(xmldata)));
		InputStream inputStream = new ByteArrayInputStream(xmldata.getBytes());
		UnicodeBOMInputStream cleanInputStream = new UnicodeBOMInputStream(inputStream);
		cleanInputStream.skipBOM();
		
		Document doc = builder.build(cleanInputStream);
		
		
	    
	    Element root= doc.getRootElement();
	    List<Element> InfoItems = root.getChildren("payment_info");
	    
	    for(int i = 0 ; i < InfoItems.size() ; i++ ){
	    	Element childItem = InfoItems.get(i);
	    	
	    	List<Element> Items = childItem.getChildren("payment");
	    	
		    for(int j = 0 ; j < Items.size() ; j++ ){
		    	PaystubDataVO dataVO = new PaystubDataVO();
		    	Element Item = Items.get(j);
		    	
		    	String empNo = Item.getChild("emp_no").getValue();
		    	String payMonth = Item.getChild("pay_month").getValue();
		    	String payDate = Item.getChild("pay_date").getValue();
		    	String payType = Item.getChild("pay_type").getValue();
		    	String taxType = Item.getChild("pay_tax_type").getValue();
		    	String taxCode = Item.getChild("pay_tax_code").getValue();
		    	String taxName = Item.getChild("pay_tax_name").getValue();
		    	String taxAmt = XmlDataUtil.priceToString(Item.getChild("pay_tax_amt").getValue());
		    	
		    	dataVO.setServiceId(PaystubService.SERVICE_ID);
		    	dataVO.setUserId(empNo);
		    	dataVO.setPayMonth(payMonth);
		    	dataVO.setPayDate(payDate);
		    	dataVO.setPayType(payType);
		    	dataVO.setPayTaxType(taxType);
		    	dataVO.setPayTaxCode(taxCode);
		    	dataVO.setPayTaxName(taxName);
		    	dataVO.setPayTaxAmt(taxAmt);
		    	
		    	result.add(dataVO);
		    }
	    	
	    }
	    
		return result;
	}
	
	// 부양가족 정보
	public static List<YE001VO> XMLtoDependent(String xmldata) throws Exception {				
		List<YE001VO> result = new ArrayList<YE001VO>();
		
		SAXBuilder builder = new SAXBuilder();		

	    //Document doc= builder.build(new InputSource(new StringReader(xmldata)));
		InputStream inputStream = new ByteArrayInputStream(xmldata.getBytes());
		UnicodeBOMInputStream cleanInputStream = new UnicodeBOMInputStream(inputStream);
		cleanInputStream.skipBOM();
		
		Document doc = builder.build(cleanInputStream);
		
	    Element root= doc.getRootElement();
	    List<Element> InfoItems = root.getChildren("family_info");
	    
	    for(int i = 0 ; i < InfoItems.size() ; i++ ){
	    	Element childItem = InfoItems.get(i);
	    	
	    	List<Element> Items = childItem.getChildren("family");
	    	
		    for(int j = 0 ; j < Items.size() ; j++ ){
		    	YE001VO dataVO = new YE001VO();
		    	Element Item = Items.get(j);
		    	
		    	String empNo = Item.getChild("emp_no").getValue();
		    	String familyType =Item.getChild("family_type").getValue();
		    	String familyName = Item.getChild("family_name").getValue();
		    	String userData = Item.getChild("user_data").getValue();
		    	String age = Item.getChild("age").getValue();
		    	String defaultType = Item.getChild("default_type").getValue();
		    	String womenType = Item.getChild("women_type").getValue();
		    	String parentType = Item.getChild("parent_type").getValue();
		    	String oldType = Item.getChild("old_type").getValue();
		    	String disabledType = Item.getChild("disabled_type").getValue();
		    	String childrenType = Item.getChild("children_type").getValue();
		    	String childbirthType = Item.getChild("childbirth_type").getValue();
		    	
		    	String countryType = "1";
		    	if(!Item.getChildren("country_type").isEmpty()) countryType = Item.getChild("country_type").getValue();  // 내외국인코드
		    	
		    	dataVO.setEmpNo(empNo);
		    	dataVO.set가족관계(familyType);
		    	dataVO.set성명(familyName);
		    	dataVO.set개인식별번호(userData);
		    	dataVO.set나이(age);
		    	dataVO.set기본공제(defaultType);
		    	dataVO.set부녀자(womenType);
		    	dataVO.set한부모(parentType);
		    	dataVO.set경로우대(oldType);
		    	dataVO.set장애인(disabledType);
		    	dataVO.set자녀(childrenType);
		    	dataVO.set출산입양(childbirthType);

		    	dataVO.set내외국인(countryType);
		    	
		    	result.add(dataVO);
		    }
	    	
	    }
	    
		return result;
	}

}

