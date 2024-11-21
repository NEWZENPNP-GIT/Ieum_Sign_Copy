package com.ezsign.account.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.account.dao.AccountDAO;
import com.ezsign.account.service.AccountService;
import com.ezsign.account.vo.AccountVO;
import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.user.dao.UserSnsDAO;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="contentService")
	private ContentService contentService;
	
	@Resource(name="bizDAO")
	private BizDAO bizDAO;

	@Resource(name="accountDAO")
	private AccountDAO accountDAO;

	@Resource(name="userSnsDAO")
	private UserSnsDAO userSnsDAO;
	
	@Override
	public int leaveAccount(AccountVO vo) throws Exception {
		logger.info(":::: leaveAccount ["+vo.getBizId()+"] :::: ");
		int result = 0;
		List<ContentVO> delList = new ArrayList<ContentVO>();
		
		// 기업정보 자료조회
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setStartPage(0);
		bizVO.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		
		if(bizList.size()==0) return result;
		
		bizVO = bizList.get(0);
		
		/////////////////////////////////////////////////////////////////
		// 파일 삭제처리 START
		/////////////////////////////////////////////////////////////////
		
		// 콘텐트 등록 파일 일괄 삭제		
		List<AccountVO> delContentList = accountDAO.getDelContentList(vo);
		
		for(int i=0;i<delContentList.size();i++) {
			String delFileId = delContentList.get(i).getFileId();
			if(StringUtil.isNotNull(delFileId)) {
				ContentVO delContentVO = new ContentVO();
				delContentVO.setFileId(delFileId);
				delList.add(delContentVO);
			}
		}
		
		// 콘텐츠 파일삭제
		result = contentService.deleteContent(delList);

		logger.info("[leaveAccount] deleteContent ==>"+result);
		
		// 콘텐츠외 등록 파일 일괄 삭제		
		vo.setFilePath(MultipartFileUtil.getSystemPath()+"images/sign");
		List<AccountVO> delFileList = accountDAO.getDelFileList(vo);
		
		for(int i=0;i<delFileList.size();i++) {
			String delFilePath = delFileList.get(i).getFilePath();
			if(StringUtil.isNotNull(delFilePath)) {
				FileUtil.deleteFile(delFilePath);
			}
		}
		
		/////////////////////////////////////////////////////////////////
		// 파일 삭제처리 END
		/////////////////////////////////////////////////////////////////
		
		// 콘텐츠 메타정보 삭제처리
		for(int i=0;i<delContentList.size();i++) {
			AccountVO delContentMetaVO = delContentList.get(i);
			accountDAO.delContentMeta(delContentMetaVO);
		}
		
		// 프로시져 활용한 데이터 일괄삭제
		//accountDAO.callProcDelData(vo);
		accountDAO.delAccountData1(vo);
		accountDAO.delAccountData2(vo);
		accountDAO.delAccountData3(vo);
		accountDAO.delAccountData4(vo);
		accountDAO.delAccountData5(vo);
		accountDAO.delAccountData6(vo);
		accountDAO.delAccountData7(vo);
		accountDAO.delAccountData8(vo);
		accountDAO.delAccountData9(vo);
		accountDAO.delAccountData10(vo);
		accountDAO.delAccountData11(vo);
		accountDAO.delAccountData12(vo);
		accountDAO.delAccountData13(vo);
		accountDAO.delAccountData14(vo);
		accountDAO.delAccountData15(vo);
		accountDAO.delAccountData16(vo);
		accountDAO.delAccountData17(vo);
		accountDAO.delAccountData18(vo);
		accountDAO.delAccountData19(vo);
		accountDAO.delAccountData20(vo);
		accountDAO.delAccountData21(vo);
		accountDAO.delAccountData22(vo);
		accountDAO.delAccountData23(vo);
		accountDAO.delAccountData24(vo);
		accountDAO.delAccountData25(vo);
		accountDAO.delAccountData26(vo);
		accountDAO.delAccountData27(vo);
		accountDAO.delAccountData28(vo);
		accountDAO.delAccountData29(vo);
		accountDAO.delAccountData30(vo);
		accountDAO.delAccountData31(vo);
		accountDAO.delAccountData32(vo);
		accountDAO.delAccountData33(vo);
		accountDAO.delAccountData34(vo);
		accountDAO.delAccountData35(vo);
		accountDAO.delAccountData36(vo);
		accountDAO.delAccountData37(vo);
		accountDAO.delAccountData38(vo);
		accountDAO.delAccountData39(vo);
		accountDAO.delAccountData40(vo);
		accountDAO.delAccountData41(vo);
		accountDAO.delAccountData42(vo);
		accountDAO.delAccountData43(vo);
		accountDAO.delAccountData44(vo);
		accountDAO.delAccountData45(vo);
		accountDAO.delAccountData46(vo);
		accountDAO.delAccountData47(vo);
		accountDAO.delAccountData48(vo);
		accountDAO.delAccountData49(vo);
		accountDAO.delAccountData50(vo);
		accountDAO.delAccountData51(vo);
		accountDAO.delAccountData52(vo);
		accountDAO.delAccountData53(vo);
		accountDAO.delAccountData54(vo);
		accountDAO.delAccountData55(vo);
		accountDAO.delAccountData56(vo);
		accountDAO.delAccountData57(vo);
		accountDAO.delAccountData58(vo);
		accountDAO.delAccountData59(vo);
		accountDAO.delAccountData60(vo);
		accountDAO.delAccountData61(vo);
		accountDAO.delAccountData62(vo);
		accountDAO.delAccountData63(vo);
		accountDAO.delAccountData64(vo);
		accountDAO.delAccountData65(vo);
		accountDAO.delAccountData66(vo);
		accountDAO.delAccountData67(vo);
		accountDAO.delAccountData68(vo);
		accountDAO.delAccountData69(vo);
		accountDAO.delAccountData70(vo);
		accountDAO.delAccountData71(vo);
		accountDAO.delAccountData72(vo);
		accountDAO.delAccountData73(vo);
		accountDAO.delAccountData74(vo);
		accountDAO.delAccountData75(vo);
		accountDAO.delAccountData76(vo);
		accountDAO.delAccountData77(vo);
		accountDAO.delAccountData78(vo);
		accountDAO.delAccountData79(vo);
		accountDAO.delAccountData80(vo);
		accountDAO.delAccountData81(vo);
		accountDAO.delAccountData82(vo);
		accountDAO.delAccountData83(vo);
		accountDAO.delAccountData84(vo);
		accountDAO.delAccountData85(vo);
		accountDAO.delAccountData86(vo);
		accountDAO.delAccountData87(vo);
		accountDAO.delAccountData88(vo);
		accountDAO.delAccountData89(vo);
		accountDAO.delAccountData90(vo);
		accountDAO.delAccountData91(vo);
		accountDAO.delAccountData92(vo);
		accountDAO.delAccountData93(vo);
		accountDAO.delAccountData94(vo);
		accountDAO.delAccountData95(vo);
		accountDAO.delAccountData96(vo);
		accountDAO.delAccountData97(vo);
		accountDAO.delAccountData98(vo);
		accountDAO.delAccountData99(vo);
		accountDAO.delAccountData100(vo);
		accountDAO.delAccountData101(vo);
		accountDAO.delAccountData102(vo);
		accountDAO.delAccountData103(vo);
		accountDAO.delAccountData104(vo);
		accountDAO.delAccountData105(vo);
		accountDAO.delAccountData106(vo);
		accountDAO.delAccountData107(vo);
		accountDAO.delAccountData108(vo);
		accountDAO.delAccountData109(vo);
		accountDAO.delAccountData110(vo);
		accountDAO.delAccountData111(vo);
		accountDAO.delAccountData112(vo);
		
		return result;
	}

	@Override
	public int leaveUser(AccountVO vo) throws Exception {
		logger.info(":::: leaveUser ["+vo.getBizId()+", "+vo.getUserId()+"] :::: ");
		int result = 0;
		List<ContentVO> delList = new ArrayList<ContentVO>();
		
		// 기업정보 자료조회
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setStartPage(0);
		bizVO.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		
		if(bizList.size()==0) return result;
		
		bizVO = bizList.get(0);
		
		/////////////////////////////////////////////////////////////////
		// 파일 삭제처리 START
		/////////////////////////////////////////////////////////////////
		
		// 콘텐트 등록 파일 일괄 삭제		
		List<AccountVO> delContentList = accountDAO.getDelUserContentList(vo);

		for(int i=0;i<delContentList.size();i++) {
			String delFileId = delContentList.get(i).getFileId();
			if(StringUtil.isNotNull(delFileId)) {
				ContentVO delContentVO = new ContentVO();
				delContentVO.setFileId(delFileId);
				delList.add(delContentVO);
			}
		}

		List<ContentVO> resultList = delList.parallelStream().distinct().collect(Collectors.toList());

		// 콘텐츠 파일삭제
		result = contentService.deleteContent(resultList);
		
		logger.info("[leaveUser] deleteContent ==>"+result);
		
		/////////////////////////////////////////////////////////////////
		// 파일 삭제처리 END
		/////////////////////////////////////////////////////////////////
		
		// 콘텐츠 메타정보 삭제처리
		for(int i=0;i<delContentList.size();i++) {
			AccountVO delContentMetaVO = delContentList.get(i);
			accountDAO.delContentMeta(delContentMetaVO);
		}
		
		// 프로시져 활용한 데이터 일괄삭제
		if(StringUtils.isNotEmpty(vo.getUserId())) {
		
			accountDAO.delAccountData3(vo);
			accountDAO.delAccountData4(vo);
			accountDAO.delAccountData5(vo);
			accountDAO.delAccountData6(vo);
			accountDAO.delAccountData7(vo);
			accountDAO.delAccountData8(vo);
			accountDAO.delAccountData9(vo);
			accountDAO.delAccountData10(vo);
			
			accountDAO.delAccountData12(vo);
			
			accountDAO.delAccountData15(vo);
			accountDAO.delAccountData16(vo);
			
			accountDAO.delAccountData19(vo);
			
			accountDAO.delAccountData32(vo);
			
			accountDAO.delAccountData35(vo);
			
			accountDAO.delAccountData37(vo);
			accountDAO.delAccountData38(vo);
			accountDAO.delAccountData39(vo);
			accountDAO.delAccountData40(vo);
	
			accountDAO.delAccountData111(vo);
			accountDAO.delAccountData113(vo);
			accountDAO.delAccountData112(vo);
			
		}
		
		return result;
	}

	@Override
	public int leaveSelectedUser(List<AccountVO> accountList) throws Exception {
		int totalCnt = 0;
		for(AccountVO vo : accountList){
			logger.info(":::: leaveUser ["+vo.getBizId()+", "+vo.getUserId()+"] :::: ");
			int result = 0;
			List<ContentVO> delList = new ArrayList<ContentVO>();
			
			// 기업정보 자료조회
			/*BizVO bizVO = new BizVO();
			bizVO.setBizId(vo.getBizId());
			bizVO.setStartPage(0);
			bizVO.setEndPage(10);
			List<BizVO> bizList = bizDAO.getBizList(bizVO);
			
			if(bizList.size()==0) return result;
			
			bizVO = bizList.get(0);*/
			
			/////////////////////////////////////////////////////////////////
			// 파일 삭제처리 START
			/////////////////////////////////////////////////////////////////
			
			// 콘텐트 등록 파일 일괄 삭제		
			List<AccountVO> delContentList = accountDAO.getDelUserContentList(vo);
			
			for(int i=0;i<delContentList.size();i++) {
				String delFileId = delContentList.get(i).getFileId();
				if(StringUtil.isNotNull(delFileId)) {
					ContentVO delContentVO = new ContentVO();
					delContentVO.setFileId(delFileId);
					delList.add(delContentVO);
				}
			}

			// 콘텐츠 파일삭제
			result = contentService.deleteContent(delList);
			
			logger.info("[leaveUser] deleteContent ==>"+result);
			
			/////////////////////////////////////////////////////////////////
			// 파일 삭제처리 END
			/////////////////////////////////////////////////////////////////
			
			// 콘텐츠 메타정보 삭제처리
			for(int i=0;i<delContentList.size();i++) {
				AccountVO delContentMetaVO = delContentList.get(i);
				accountDAO.delContentMeta(delContentMetaVO);
			}
			
			// 프로시져 활용한 데이터 일괄삭제
			accountDAO.delAccountData3(vo);
			accountDAO.delAccountData4(vo);
			accountDAO.delAccountData5(vo);
			accountDAO.delAccountData6(vo);
			accountDAO.delAccountData7(vo);
			accountDAO.delAccountData8(vo);
			accountDAO.delAccountData9(vo);
			accountDAO.delAccountData10(vo);
			
			accountDAO.delAccountData12(vo);
			
			accountDAO.delAccountData15(vo);
			accountDAO.delAccountData16(vo);
			
			accountDAO.delAccountData19(vo);
			
			accountDAO.delAccountData32(vo);
			
			accountDAO.delAccountData35(vo);
			
			accountDAO.delAccountData37(vo);
			accountDAO.delAccountData38(vo);
			accountDAO.delAccountData39(vo);
			accountDAO.delAccountData40(vo);
	
			accountDAO.delAccountData111(vo);
			accountDAO.delAccountData113(vo);
			accountDAO.delAccountData112(vo);
			totalCnt ++;
		}
		return totalCnt;
	}

}
