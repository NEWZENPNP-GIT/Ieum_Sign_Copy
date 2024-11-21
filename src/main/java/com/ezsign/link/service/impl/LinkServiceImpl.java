package com.ezsign.link.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biztalk.dao.BizTalkDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.dao.YS010DAO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.system.dao.YS031DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.util.XmlNewzenParser;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.link.service.LinkService;
import com.ezsign.paystub.dao.PaystubDAO;
import com.ezsign.user.dao.UserDAO;
import com.jarvis.common.util.DateUtil;
import com.jarvis.common.util.FileUtil;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("linkService")
public class LinkServiceImpl extends AbstractServiceImpl implements LinkService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Resource(name="empDAO")
	private EmpDAO empDAO;
	
	@Resource(name="userDAO")
	private UserDAO userDAO;
	
	@Resource(name="contentService")
	private ContentService contentService;

	@Resource(name="bizTalkDAO")
	private BizTalkDAO bizTalkDAO;

	@Resource(name="paystubDAO")
	private PaystubDAO paystubDAO;
	
	@Resource(name = "ys000DAO")
	private YS000DAO ys000DAO;

	@Resource(name = "ys010DAO")
	private YS010DAO ys010DAO;

	@Resource(name = "ys030DAO")
	private YS030DAO ys030DAO;

	@Resource(name = "ys031DAO")
	private YS031DAO ys031DAO;
	
	@Resource(name = "ye000DAO")
	private YE000DAO ye000DAO;
	
	@Resource(name = "ye001DAO")
	private YE001DAO ye001DAO;

	@Override
	public boolean setLinkXML(BizVO bizNoVO, FileVO vo) throws Exception {
		
		List<String> talkList = new ArrayList<String>();
		boolean result = false;
		
		String bizId = "";
		String febYear = bizNoVO.getFebYear();
		String 계약ID = "";
		String 사업장ID = "";
		String 부서ID = "";
		
		// 서비스가입여부
		BizVO bizVO = new BizVO();
		bizVO.setBusinessNo(bizNoVO.getBusinessNo());
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		if(bizList.size()==0) {
			System.out.println("[setLinkXML] 기업의 가입 정보가 존재하지 않습니다.");
			bizNoVO.setMessage("[setLinkXML] 기업의 가입 정보가 존재하지 않습니다.");
			return result;
		} else {
			BizVO resultBizVO = bizList.get(0);
			bizId = resultBizVO.getBizId();
			
			if(bizNoVO.getServiceType().equals("003")) {
				// 연말정산 계약ID 체크 없으면 생성
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(bizId);
				ys000VO.setFebYear(febYear);					
				List<YS000VO> ys000List = ys000DAO.getYS000List(ys000VO);
				if(ys000List.size()==0) {
					계약ID = SecurityUtil.bitEncryption(febYear+bizId);
					ys000VO.set계약ID(계약ID);
					ys000DAO.insYS000(ys000VO);
										
					// 사업장관리
					YS030VO ys030VO = new YS030VO();
					ys030VO.set계약ID(계약ID);
					ys030VO.set지점여부("0");
					ys030VO.set사업장명(resultBizVO.getBizName());
					ys030VO.set대표자명(resultBizVO.getOwnerName());
					ys030VO.set사업자등록번호(resultBizVO.getBusinessNo());
					ys030VO.set회사주소1(resultBizVO.getAddr1());
					ys030VO.set회사주소2(resultBizVO.getAddr2());
					ys030VO.set회사전화1(resultBizVO.getCompanyTelNum());
					ys030VO.set단위과세자여부("0");
					ys030VO.set종사업자일련번호("0000");
					ys030VO.set사용여부("1");
					ys030VO.set신고대상("2");
					ys030DAO.insYS030(ys030VO);
					
					사업장ID = ys030VO.get사업장ID();
					
					// 부서관리
					YS031VO ys031VO = new YS031VO();
					ys031VO.set계약ID(계약ID);
					ys031VO.set사업장ID(사업장ID);
					ys031VO.set부서명("기본부서");
					ys031DAO.insYS031(ys031VO);
					
					부서ID = ys031VO.get부서ID();
					
				} else {
					계약ID = ys000List.get(0).get계약ID();

					YS030VO ys030VO = new YS030VO();
					ys030VO.set계약ID(계약ID);
					ys030VO.set지점여부("0");
					ys030VO.setStartPage(0);
					ys030VO.setEndPage(1);
					List<YS030VO> ys030VOList = ys030DAO.getYS030List(ys030VO);
					if(ys030VOList!=null&&ys030VOList.size()>0) {
						사업장ID = ys030VOList.get(0).get사업장ID();
					}
					

					YS031VO ys031VO = new YS031VO();
					ys031VO.set계약ID(계약ID);
					ys031VO.set사업장ID(사업장ID);
					List<YS031VO> ys031VOList = ys031DAO.getYS031List(ys031VO);
					if(ys031VOList!=null&&ys031VOList.size()>0) {
						부서ID = ys031VOList.get(0).get부서ID();
					}
				}
			}
		}
		
		ContentVO contentVO = new ContentVO();
		contentVO.setFileType(vo.getFileExt());
		contentVO.setFileName(vo.getFileStreNm());
		contentVO.setOrgFileName(vo.getFileStreOriNm());
		contentVO.setFileSize(Long.toString(vo.getFileSize()));
		contentVO.setFilePath(vo.getFileStrePath()+MultipartFileUtil.SEPERATOR);
		contentVO.setFileTitle(DateUtil.getTimeStamp(7));
		contentVO.setClassId(LinkService.NewzenXML);
		contentVO.setFileVersion("0");
		contentVO.setParFileId("");
		contentVO.setEtcDesc("");
		contentVO.setCheckInOut("N");
		contentVO.setCheckUserId("");
		contentVO.setCheckCount("0");
		contentVO.setUseYn("Y");
		contentVO.setDelYn("N");
		contentVO.setInsEmpNo("SYSTEM");
		
		int nResult = contentService.transContent(contentVO);
		System.out.println("[setLinkXML] transContent["+contentVO.getFileId()+"] result code :" + nResult);
		if(contentService.COMPLETED==nResult) {
			String xmlPath = vo.getFileStrePath()+MultipartFileUtil.SEPERATOR+vo.getFileStreNm();
			
			String filedata = FileUtil.readFile(xmlPath);
			
			// 인사정보 입력
			List<EmpVO> empList = XmlNewzenParser.XMLtoEmployee(filedata);
			if(empList.size()>0) {
				for(int i=0;i<empList.size();i++) {
					EmpVO empVO = empList.get(i);
					
					if(StringUtil.isNull(empVO.getEmpNo())||StringUtil.isNull(empVO.getEmpName())) {
						System.out.println("[setLinkXML] 근로자 정보의 사번 또는 성명이 존재하지 않습니다.");
						bizNoVO.setMessage("[setLinkXML] 근로자 정보의 사번 또는 성명이 존재하지 않습니다.");
						return result;
					}
					/*
					if(StringUtil.isNull(empVO.getPhoneNum())||StringUtil.isNull(empVO.getEMail())) {
						System.out.println("[setLinkXML] 근로자 정보의 필수항목(전화번호, 이메일)이 존재하지 않습니다.");
						bizNoVO.setMessage("[setLinkXML] 근로자 정보의 필수항목(전화번호, 이메일)이 존재하지 않습니다.");
						return result;
					}
					*/

					if(StringUtil.isNull(empVO.getUserDate())||StringUtil.isNull(empVO.getJoinDate())) {
						System.out.println("[setLinkXML] 근로자 정보의 필수항목(생년월일, 입사일자)이 존재하지 않습니다.");
						bizNoVO.setMessage("[setLinkXML] 근로자 정보의 필수항목(생년월일, 입사일자)이 존재하지 않습니다.");
						return result;
					}
					
					EmpVO paramEmpVO = new EmpVO();
					paramEmpVO.setBizId(bizId);
					paramEmpVO.setEmpNo(empVO.getEmpNo());
					EmpVO resultEmpVO = empDAO.getEmpNo(paramEmpVO);
					
					EmpVO insEmpVO = new EmpVO();
					String userId = "";
					insEmpVO.setBizId(bizId);
					insEmpVO.setEmpNo(empVO.getEmpNo());
					insEmpVO.setEmpName(empVO.getEmpName());
					insEmpVO.setLoginId("");
					insEmpVO.setUserPwd("");
					insEmpVO.setEmpType(empVO.getEmpType());
					insEmpVO.setAddr1(empVO.getAddr1());
					insEmpVO.setAddr2(empVO.getAddr2());
					insEmpVO.setTelNum(empVO.getTelNum());
					insEmpVO.setPhoneNum(empVO.getPhoneNum());
					insEmpVO.setUserDate(empVO.getUserDate());
					insEmpVO.setEMail(empVO.getEMail());
					insEmpVO.setJoinDate(empVO.getJoinDate());
					insEmpVO.setLeaveDate(empVO.getLeaveDate());
					insEmpVO.setDeptName(empVO.getDeptName());
					insEmpVO.setPositionName(empVO.getPositionName());
					insEmpVO.setStepName(empVO.getStepName());
					insEmpVO.setCountryType("81");
					insEmpVO.setUseYn("Y");
					insEmpVO.setConfirmType("N");
					insEmpVO.setLastConnTime("");
					
					if(resultEmpVO!=null) {
						insEmpVO.setUserId(resultEmpVO.getUserId());
						empDAO.updEmp(insEmpVO);
						userId = resultEmpVO.getUserId();
					} else {
						empDAO.insEmp(insEmpVO);
						userId = insEmpVO.getUserId();
					}
					
					// 연말정산 대상정보 변경
					if(bizNoVO.getServiceType().equals("003")) {
						// 개인식별번호, 내외국인, 거주구분 필수값 체크
						if(StringUtil.isNull(empVO.getUserData()) 
							|| StringUtil.isNull(empVO.getCountryType())
							|| StringUtil.isNull(empVO.getLiveType())) {
							System.out.println("[setLinkXML] 근로자<"+empVO.getEmpName()+"> 정보의 필수값(개인식별번호, 내외국인, 거주구분)이 존재하지 않습니다.");
							bizNoVO.setMessage("[setLinkXML] 근로자<"+empVO.getEmpName()+"> 정보의 필수값(개인식별번호, 내외국인, 거주구분)이 존재하지 않습니다.");
							return result;
						}
						
						YE000VO ye000VO = new YE000VO();
						ye000VO.setBizId(bizId);
						ye000VO.set계약ID(계약ID);
						ye000VO.set사용자ID(userId);
						
		                int 나이 = 0;
		                switch (empVO.getUserData().substring(6, 7)) {
		                    case "1":
		                    case "2":
		                    case "5":
		                    case "6":
		                        나이 = Integer.parseInt(bizNoVO.getFebYear()) - Integer.parseInt("19" + empVO.getUserData().substring(0, 2));
		                        break;
		                    case "3":
		                    case "4":
		                    case "7":
		                    case "8":
		                        나이 = Integer.parseInt(bizNoVO.getFebYear()) - Integer.parseInt("20" + empVO.getUserData().substring(0, 2));
		                        break;
		                    case "9":
		                    case "0":
		                        나이 = Integer.parseInt(bizNoVO.getFebYear()) - Integer.parseInt("18" + empVO.getUserData().substring(0, 2));
		                        break;
		                }
						String age = String.valueOf(나이);
						String identityCode = SecurityUtil.getinstance().aesEncode(empVO.getUserData());						
						ye000VO.set개인식별번호(identityCode);
						
						ye000VO.set내외국인구분(empVO.getCountryType());
						ye000VO.set거주구분(empVO.getLiveType());
						
						if(empVO.getCountryType().equals("1")) {
							// 내국인인경우 국적코드 KR 설정
							ye000VO.set국가코드("KR");
						}
						if(empVO.getLiveType().equals("1")) {
							// 거주구분이 국내거주자 인경우 거주지국코드 KR설정
							ye000VO.set거주지국코드("KR");
						}
						
						// 연말정산 대상존재여부
						YE000VO resultYE000VO = new YE000VO();
						resultYE000VO = ye000DAO.getYE000(ye000VO);
						if(resultYE000VO==null) {
							System.out.println("[setLinkXML] 인사정보<"+empVO.getEmpName()+">가 존재하지 않습니다.");
							bizNoVO.setMessage("[setLinkXML] 인사정보<"+empVO.getEmpName()+">가  존재하지 않습니다.");
							return result;
						} else {
							if(StringUtil.isNull(resultYE000VO.get계약ID())) {
								ye000VO.set사업장ID(사업장ID);
								ye000VO.set부서ID(부서ID);
								ye000VO.set진행상태코드("1");
								ye000VO.set부서표시여부("1");
								ye000VO.set전년도총급여("0");
								ye000VO.set사용여부("1");	// 연말정산 대상 지정
								ye000DAO.insYE000(ye000VO);
							} else {
								ye000DAO.updYE000(ye000VO);
							}
						}
						
						// 부양가족 본인 등록
						YE001VO ye001ID = new YE001VO();
						ye001ID.set계약ID(계약ID);
						ye001ID.set사용자ID(userId);
						ye001ID.set개인식별번호(identityCode);
						YE001VO resultye001ID = ye001DAO.getYE001ID(ye001ID);
						
						if(resultye001ID==null) {
							YE001VO insYE001VO = new YE001VO();
							insYE001VO.set계약ID(계약ID);
							insYE001VO.set사용자ID(userId);
							insYE001VO.set부양가족ID(userId);
							insYE001VO.set가족관계("0");
							insYE001VO.set내외국인(empVO.getCountryType());
							insYE001VO.set성명(empVO.getEmpName());
							insYE001VO.set개인식별번호(identityCode);
							insYE001VO.set나이(age);
							insYE001VO.set기본공제("1");
							insYE001VO.set부녀자("2");
							insYE001VO.set한부모("2");
							insYE001VO.set경로우대("2");
							insYE001VO.set장애인("4");
							insYE001VO.set자녀("2");
							insYE001VO.set출산입양("4");
							ye001DAO.insYE001(insYE001VO);
						}
					}
				}
			}
			
			// 부양가족 정보 등록
			List<YE001VO> ye001List = XmlNewzenParser.XMLtoDependent(filedata);
			
			if(ye001List.size()>0) {
				for(int i=0;i<ye001List.size();i++) {
					YE001VO ye001VO = ye001List.get(i);

					EmpVO paramEmpVO = new EmpVO();
					paramEmpVO.setBizId(bizId);
					paramEmpVO.setEmpNo(ye001VO.getEmpNo());
					EmpVO resultEmpVO = empDAO.getEmpNo(paramEmpVO);

					if(resultEmpVO != null) {
		                int 나이 = 0;
		                switch (ye001VO.get개인식별번호().substring(6, 7)) {
		                    case "1":
		                    case "2":
		                    case "5":
		                    case "6":
		                        나이 = Integer.parseInt(bizNoVO.getFebYear()) - Integer.parseInt("19" + ye001VO.get개인식별번호().substring(0, 2));
		                        break;
		                    case "3":
		                    case "4":
		                    case "7":
		                    case "8":
		                        나이 = Integer.parseInt(bizNoVO.getFebYear()) - Integer.parseInt("20" + ye001VO.get개인식별번호().substring(0, 2));
		                        break;
		                    case "9":
		                    case "0":
		                        나이 = Integer.parseInt(bizNoVO.getFebYear()) - Integer.parseInt("18" + ye001VO.get개인식별번호().substring(0, 2));
		                        break;
		                }
						String age = String.valueOf(나이);
						
						String identityCode = "";
						// 개인식별코드 암호화						
						identityCode = SecurityUtil.getinstance().aesEncode(ye001VO.get개인식별번호());
						
						// 연말정산 부양가족 정보 조회
						YE001VO ye001ID = new YE001VO();
						ye001ID.set계약ID(계약ID);
						ye001ID.set사용자ID(resultEmpVO.getUserId());
						ye001ID.set개인식별번호(identityCode);
						YE001VO resultye001ID = ye001DAO.getYE001ID(ye001ID);
						
						YE001VO insYE001VO = new YE001VO();
						insYE001VO.set계약ID(계약ID);
						insYE001VO.set사용자ID(resultEmpVO.getUserId());						
						insYE001VO.set내외국인(ye001VO.get내외국인());
						insYE001VO.set성명(ye001VO.get성명());
						insYE001VO.set개인식별번호(identityCode);
						insYE001VO.set나이(age);
						insYE001VO.set가족관계(ye001VO.get가족관계());
						// 기본공제
						if("0".equals(ye001VO.get기본공제())) {
							insYE001VO.set기본공제("2");
						} else {
							insYE001VO.set기본공제("1");
						}

						// 부녀자
						if("0".equals(ye001VO.get부녀자())) {
							insYE001VO.set부녀자("2");
						} else {
							insYE001VO.set부녀자("1");
						}
						
						// 한부모
						if("0".equals(ye001VO.get한부모())) {
							insYE001VO.set한부모("2");
						} else {
							insYE001VO.set한부모("1");
						}
						
						// 경로우대
						if("0".equals(ye001VO.get경로우대())) {
							insYE001VO.set경로우대("2");
						} else {
							insYE001VO.set경로우대("1");
						}
						
						// 장애인
						if("0".equals(ye001VO.get장애인())) {
							insYE001VO.set장애인("4");
						} else {
							insYE001VO.set장애인(ye001VO.get장애인());
						}
						
						// 자녀
						if("0".equals(ye001VO.get자녀())) {
							insYE001VO.set자녀("2");
						} else {
							insYE001VO.set자녀("1");
						}
						
						// 출산입양
						if("0".equals(ye001VO.get출산입양())) {
							insYE001VO.set출산입양("4");
						} else {
							insYE001VO.set출산입양(ye001VO.get출산입양());
						}
						
						
						// 부양가족이 없는경우에만 입력처리
						if(resultye001ID==null) {
							ye001DAO.insYE001(insYE001VO);
						} else {
							insYE001VO.set부양가족ID(resultye001ID.get부양가족ID());
							ye001DAO.updYE001(insYE001VO);
						}
					}
				}
			}
			
			System.out.println("[setLinkXML] TempFile Delete : "+xmlPath);
			FileUtil.deleteFile(xmlPath);
			result = true;
		}
		
		return result;
	}

	// 기업 연말정산 가입여부
	@Override
	public int checkService(BizVO vo) {
		int result = 0;
		String bizId = "";
		
		BizVO bizVO = new BizVO();
		bizVO.setBusinessNo(vo.getBusinessNo());
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		if(bizList!=null&&bizList.size()>0) {
			bizId = bizList.get(0).getBizId();
		} else {
			System.out.println("[checkService] 기업의 가입 정보가 존재하지 않습니다.");
			vo.setMessage("[checkService] 기업의 가입 정보가 존재하지 않습니다.");
			return result;
		}
		
		// 서비스가입 여부 체크
		bizVO.setBizId(bizId);
		bizVO.setServiceType(vo.getServiceType()); 
		
		result = bizDAO.getCheckService(bizVO);
		
		return result;
	}
}
