package com.ezsign.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.bbs.dao.BbsDAO;
import com.ezsign.bbs.service.BbsService;
import com.ezsign.bbs.vo.BbsCommentVO;
import com.ezsign.bbs.vo.BbsContentsVO;
import com.ezsign.bbs.vo.BbsFileVO;
import com.ezsign.bbs.vo.BbsInfoVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.push.dao.PushDAO;
import com.ezsign.push.service.PushService;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("bbsService")
public class BbsServiceImpl extends AbstractServiceImpl implements BbsService{

private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "bbsDAO")
	private BbsDAO bbsDAO;

	@Resource(name="pushDAO")
	private PushDAO pushDAO;
	
	@Resource(name = "pushService")
	private PushService pushService;

	@Resource(name="empDAO")
	private EmpDAO empDAO;

	@Override
	public void insBbsInfo(BbsInfoVO vo) throws Exception {
		bbsDAO.insBbsInfo(vo);
	}

	@Override
	public List<BbsInfoVO> getBbsInfoList(BbsInfoVO vo) throws Exception {
		List<BbsInfoVO> list = bbsDAO.getBbsInfoList(vo);
		
		return list;
	}

	@Override
	public int getBbsInfoListCount(BbsInfoVO vo) throws Exception {
		return bbsDAO.getBbsInfoListCount(vo);
	}

	@Override
	public int updBbsInfo(BbsInfoVO vo) throws Exception {
		return bbsDAO.updBbsInfo(vo);
	}

	@Override
	public int delBbsInfo(BbsInfoVO vo) throws Exception {
		return bbsDAO.delBbsInfo(vo);
	}

	@Override
	public int insBbsContents(BbsContentsVO vo, List<FileVO> bbsFileList, int userType) throws Exception {
		
		int result = 0;
		String bbsId = vo.getBbsId();
		
		// FAQ 등록
		if(bbsId.equals(BbsService.FAQ)) {
			if(userType == 9) {
				bbsDAO.insBbsContents(vo);
			} else {
				return -100;
			}
		}
		
		// 공지사항 등록
		if(bbsId.equals(BbsService.NOTICE)) {
			if(userType > 5) {
				
				//공지글로 게시 일 경우 최대갯수 체크 
				if(StringUtils.equals(vo.getContentsType(), "1")){
					BbsContentsVO paramCntsVO = new BbsContentsVO();
					paramCntsVO.setBbsId(vo.getBbsId());
					paramCntsVO.setContentsType(vo.getContentsType());
					
					int bbsCnt = bbsDAO.getBbsContentsListCount(paramCntsVO);
					
					// 공지 게시글이 5개일경우 이전 정보 한개를 공지 게시글에서 제거한다.
					if(bbsCnt == 5){						
						paramCntsVO.setSortName("A.INS_DATE");
						paramCntsVO.setSortOrder("ASC");
						paramCntsVO.setStartPage(0);
						paramCntsVO.setEndPage(5);
						
						List<BbsContentsVO> bbsList = bbsDAO.getBbsContentsList(paramCntsVO);
						
						if(bbsList != null && bbsList.size() > 0){
							BbsContentsVO updCntsVO = new BbsContentsVO();
							updCntsVO.setContentsType("0");
							updCntsVO.setBbsId(bbsList.get(0).getBbsId());
							updCntsVO.setBbsNo(bbsList.get(0).getBbsNo());
							
							bbsDAO.updBbsContents(updCntsVO);
						}						
					}
					
				}
								
				// 공지사항등록
				bbsDAO.insBbsContents(vo);
				
				/*
				// push 메시지 처리 				
				PushVO pushVo = new PushVO();
				pushVo.setToType("002");
				pushVo.setPushType("001");
				pushVo.setBizId(vo.getBizId());
				pushVo.setValue1("");
				pushVo.setValue2("");
				// pushVo.setBookDate("20181207141500");

				PushVO pushVo1 = new PushVO();
				pushVo1 =  pushDAO.getPushMessage(pushVo);
				
				pushVo.setBody(pushVo1.getMessageContents());
				pushService.insPushSendList(pushVo);
				*/
				
			} else {
				return -100;
			}
		}
		
		// 문의 게시판 등록
		if(!bbsId.equals(BbsService.FAQ) && !bbsId.equals(BbsService.NOTICE)) {
			bbsDAO.insBbsContents(vo);
		}
		
		// 첨부파일
		for (int i=0; i < bbsFileList.size(); i++) {
			FileVO fileVO = new FileVO();
			fileVO = bbsFileList.get(i);
			BbsFileVO bbsFileVO = new BbsFileVO();
			
			bbsFileVO.setBizId(vo.getBizId());
			bbsFileVO.setBbsId(vo.getBbsId());
			bbsFileVO.setBbsNo(vo.getBbsNo());
			bbsFileVO.setFilePath(fileVO.getFileStrePath());
			bbsFileVO.setOrgFileName(fileVO.getFileStreOriNm());
			bbsFileVO.setFileName(fileVO.getFileStreNm());
			bbsFileVO.setFileSize(fileVO.getFileSize());
			bbsFileVO.setDownCnt(0);
			bbsFileVO.setUseYn("Y");
			bbsFileVO.setUserId(vo.getUserId());
			
			bbsDAO.insBbsFile(bbsFileVO);
			
		}
		result++;
		
		return result;
	}

	@Override
	public List<BbsContentsVO> getBbsContentsList(BbsContentsVO vo) throws Exception {
		
		List<BbsContentsVO> list = bbsDAO.getBbsContentsList(vo); 
		
		return list;
	}

	@Override
	public int getBbsContentsListCount(BbsContentsVO vo) throws Exception {
		
		return bbsDAO.getBbsContentsListCount(vo);
	}

	@Override
	public BbsContentsVO getBbsContents(BbsContentsVO vo) throws Exception {
		BbsContentsVO bbsVO = bbsDAO.getBbsContents(vo);
		if(bbsVO!=null) {
			// 조회수 업데이트
			BbsContentsVO updBbsVO = new BbsContentsVO();
			updBbsVO.setBbsId(vo.getBbsId());
			updBbsVO.setBbsNo(vo.getBbsNo());
			updBbsVO.setHitCnt(Integer.toString(Integer.parseInt(bbsVO.getHitCnt())+1));
			if(vo.getUserType() != null){
				if(vo.getUserType().equals("9")){
					updBbsVO.setAdminCheck("Y");
				}
			}
			bbsDAO.updBbsContents(updBbsVO);
		}
		return bbsVO;
	}
	
	@Override
	public int updBbsContents(BbsContentsVO vo, List<FileVO> bbsFileList, int userType) throws Exception {
		int result = 0;
		String bbsId = vo.getBbsId();
		
		// FAQ 수정
		if(bbsId.equals(BbsService.FAQ)) {
			if(userType == 9) {
				result = bbsDAO.updBbsContents(vo);
			} else {
				return -100;
			}
		}
		
		// 공지사항 수정
		if(bbsId.equals(BbsService.NOTICE)) {
			if(userType > 5) {
				result = bbsDAO.updBbsContents(vo);
			} else {
				return -100;
			}
		}
		
		// 문의게시판 수정
		if(!bbsId.equals(BbsService.FAQ) && !bbsId.equals(BbsService.NOTICE)) {
			result = bbsDAO.updBbsContents(vo);
		}
		
		// 첨부파일
		for (int i=0; i < bbsFileList.size(); i++) {
			FileVO fileVO = new FileVO();
			fileVO = bbsFileList.get(i);
			BbsFileVO bbsFileVO = new BbsFileVO();
			
			bbsFileVO.setBizId(vo.getBizId());
			bbsFileVO.setBbsId(vo.getBbsId());
			bbsFileVO.setBbsNo(vo.getBbsNo());
			bbsFileVO.setFileNo(vo.getFileNo());
			bbsFileVO.setFilePath(fileVO.getFileStrePath());
			bbsFileVO.setOrgFileName(fileVO.getFileStreOriNm());
			bbsFileVO.setFileName(fileVO.getFileStreNm());
			bbsFileVO.setFileSize(fileVO.getFileSize());
			bbsFileVO.setDownCnt(0);
			bbsFileVO.setUserId(vo.getUserId());
			
			// 첨부파일이 있을때 수정 없으면 등록
			if(StringUtil.isNotNull(vo.getFileNo())) {
				bbsDAO.updBbsFile(bbsFileVO);
			} else {
				bbsDAO.insBbsFile(bbsFileVO);
			}
			
		}
		
		return result;
	}

	@Override
	public int delBbsContents(BbsContentsVO vo, int userType) throws Exception {
		int result = 0;
		String bbsId = vo.getBbsId();
		
		// FAQ 삭제
		if(bbsId.equals(BbsService.FAQ)) {
			if(userType == 9) {
				result = bbsDAO.delBbsContents(vo);
			} else {
				return -100;
			}
		}
		
		// 공지사항 삭제
		if(bbsId.equals(BbsService.NOTICE)) {
			if(userType > 5) {
				result = bbsDAO.delBbsContents(vo);
			} else {
				return -100;
			}
		}
		
		// 문의게시판 삭제
		if(!bbsId.equals(BbsService.FAQ) && !bbsId.equals(BbsService.NOTICE)) {
			result = bbsDAO.delBbsContents(vo);
		}
		
		return result;
	}

	@Override
	public int insBbsFile(BbsFileVO vo) throws Exception {
		bbsDAO.insBbsFile(vo);
		return 0;
	}

	@Override
	public List<BbsFileVO> getBbsFileList(BbsFileVO vo) throws Exception {
		List<BbsFileVO> list = null;
		
		list = bbsDAO.getBbsFileList(vo);
		
		return list;
	}

	@Override
	public int updBbsFile(BbsFileVO vo) throws Exception {
		return bbsDAO.updBbsFile(vo);
	}

	@Override
	public int delBbsFile(BbsFileVO vo) throws Exception {
		
		List<BbsFileVO> bbsFileList = bbsDAO.getBbsFileList(vo);
		
		// 첨부파일 삭제
		for (int i=0; i < bbsFileList.size(); i++) {
			BbsFileVO bbsFileVO = new BbsFileVO();
			bbsFileVO = bbsFileList.get(i);
			
			String originFilePath = MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE") + bbsFileVO.getFilePath() + bbsFileVO.getFileName();
			
			logger.info("게시판 첨부파일 : " + originFilePath);
			
			boolean deleteResult = FileUtil.deleteFile(originFilePath);
			if(!deleteResult) {
				logger.error("[delBbsFile] 파일 삭제에 실패하였습니다.");
				return 0;
			}
		}
		
		return bbsDAO.delBbsFile(vo);
	}

	@Override
	public int insBbsComment(BbsCommentVO vo, int userType) throws Exception {
		bbsDAO.insBbsComment(vo);

		if(userType > 5) {
			// 컨텐츠를 가져와서 사용자 ID를 찾기
			BbsContentsVO bbsInfo = new BbsContentsVO();
			
			bbsInfo.setBizId(vo.getBizId());
			bbsInfo.setBbsId(vo.getBbsId());
			bbsInfo.setBbsNo(vo.getBbsNo());

			BbsContentsVO bbsVO = bbsDAO.getBbsContents(bbsInfo);
			
			/*
			// push 메시지 처리 			
			PushVO pushVo = new PushVO();
			pushVo.setToType("003");
			pushVo.setPushType("002");
			pushVo.setUserId(bbsVO.getUserId());
			pushVo.setValue1(bbsVO.getEmpName());
			pushVo.setValue2(bbsVO.getSubject()); //subject
			// pushVo.setBookDate("20181207141500");

			PushVO pushVo1 = new PushVO();
			pushVo1 =  pushDAO.getPushMessage(pushVo);
			
			pushVo.setBody(pushVo1.getMessageContents());
			pushService.insPushSendList(pushVo);
			*/
			
		} else {
			return -100;
		}
		return 0;
	}

	@Override
	public List<BbsCommentVO> getBbsCommentList(BbsCommentVO vo) throws Exception {
		List<BbsCommentVO> list = null;
		
		list = bbsDAO.getBbsCommentList(vo);
		
		return list;
	}

	@Override
	public int getBbsCommentListCount(BbsCommentVO vo) throws Exception {
		return bbsDAO.getBbsCommentListCount(vo);
	}

	@Override
	public int updBbsComment(BbsCommentVO vo) throws Exception {
		return bbsDAO.updBbsComment(vo);
	}

	@Override
	public int delBbsComment(BbsCommentVO vo) throws Exception {
		return bbsDAO.delBbsComment(vo);
	}
	
	//노무 SOS 게시글의 답변일 경우 기업담당자 이메일로 알림 발송.
	@Override
	public void sendCommentEmail(BbsCommentVO vo) throws Exception {
		
		EmpVO empVo = new EmpVO();
		empVo.setBizId(vo.getBizId());
		empVo.setEmpType("6");
		empVo.setStartPage(-1);
		empVo.setEndPage(-1);
		List<EmpVO> result = empDAO.getEmpList(empVo);
		
		BbsContentsVO contVO = new BbsContentsVO();
		contVO.setBizId(vo.getBizId());
		contVO.setBbsNo(vo.getBbsNo());
		contVO.setBbsId(vo.getBbsId());
		contVO.setStartPage(0);
		contVO.setEndPage(1);
		BbsContentsVO bbsVO = bbsDAO.getBbsContents(contVO);
		
		for(int i=0;i<result.size();i++){
			EmpVO empVO = result.get(i);
			
			if(bbsVO != null && !empVO.getUserId().equals(vo.getUserId())){
				String content = "";
			
				content +="<!DOCTYPE html> ";
				content +="<html> ";
				content +="<head> ";
				content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
				content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
				content +="<title>전자문서</title> ";
				content +="</head> ";
				content +="<body> ";
				content +="	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
				content +="		<div style=\" padding: 37px 0 0 0;\"> ";
				content +="			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
				content +="				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
				content +="			</div> ";
				content +="			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
				content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">새로운 댓글 내용</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br>발송이메일입니다.</span>";
				content +="			</div> ";
				content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
				content +="				<span >"+empVO.getEmpName()+"</span><span>님!<br> ";
				content +="				["+bbsVO.getSubject()+"] 글에 답변이 등록되었으니 확인 바랍니다.<br> ";
				content +="				</span> ";
				content +="			</div> ";
				content +="			<div style=\"margin-top: 74px; text-align: center;\"> ";
				content +="				<div style=\"width: 255px; height: 43px; padding-top: 15px; margin-bottom: 32px; background-color: #00a9e9; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> ";
				content +="					<a href=\""+System.getProperty("system.feb.url")+"\" style=\"color: #fff; font-size: 18px; font-weight: bold; font-family: 'tahoma'; text-decoration: none;\">로그인하기  </a> ";
				content +="				</div> ";
				content +="			</div> ";
				content +="		</div> ";
				content +="		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
				content +="			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
				content +="				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
				content +="				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
				content +="				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
				content +="			</div> ";
				content +="		</div> ";
				content +="	</div> ";
				content +="</body> ";
				content +="</html> ";
				
				MultiPartEmail email = new MultiPartEmail();
				MailVO mailVO = new MailVO();
				mailVO.setFrom("no_reply@newzenpnp.com");
				mailVO.setTo(empVO.getEMail());
				mailVO.setCc("");
				mailVO.setBcc("");
				mailVO.setSubject("[뉴젠피앤피] "+bbsVO.getSubject()+" 글에 답변이 등록되었으니 확인바랍니다.");
				mailVO.setText(content);
				
				logger.info("IeumSign 전자문서 이메일을 발송. email : " + empVO.getEMail());
				
				email.send(mailVO);
			}
		}
	}
	
}
