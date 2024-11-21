package com.ezsign.push.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.push.dao.PushDAO;
import com.ezsign.push.service.PushService;
import com.ezsign.push.util.FCM;
import com.ezsign.push.vo.PushVO;
import com.ezsign.push.vo.ReturnResult;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("pushService")
public class PushServiceImpl extends AbstractServiceImpl implements PushService {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="codeDAO")
	private CodeDAO codeDAO;
	
	@Resource(name="pushDAO")
	private PushDAO pushDAO;
	
	@Override
	public List<PushVO> getPushList(PushVO vo) throws Exception {
		List<PushVO> result = new ArrayList();
		
		result = pushDAO.getPushList(vo);
		
		return result;
	}
	
	@Override
	public PushVO getReadCnt(PushVO vo) throws Exception {
		
		PushVO pushVo = pushDAO.getReadCnt(vo);
		
		return pushVo;
	}
	
	
	@Override
	public PushVO getPushMessage(PushVO vo) throws Exception {
		
		PushVO pushVo = pushDAO.getPushMessage(vo);
		return pushVo;
	}
	
	@Override
	public int insPushRead(PushVO vo)  throws Exception {
			int existYN = pushDAO.insPushReadYn(vo);
			if(existYN == 0) {
				pushDAO.insPushRead(vo);
			}
		return 1; 
	}
	
	@Override
	public int updPushSendList(PushVO vo) {
		return pushDAO.updPushSendList(vo);
	}
	
	@Override
	public List<PushVO> getPushSendList(PushVO vo) throws Exception {
		List<PushVO> result = new ArrayList();
		
		result = pushDAO.getPushSendList(vo);
		
		return result;
	}
	
	/**
	 * 푸시 날려야 할 리스트 넣기
	 * PUSH대상 : 사용자 - toType(003 - 코드표 참고(보내는 대상코드)), pushType(코드표 참고(푸쉬 메세지 코드)), userId(사용자ID-LoginId 아님), bookDate(예약발송일자 - 필요시), body(내용)
	 * PUSH대상 : 회사 - toType(002 - 코드표 참고(보내는 대상코드)), pushType(코드표 참고(푸쉬 메세지 코드)), bizId(회사ID), bookDate(예약발송일자 - 필요시), body(내용)
	 * PUSH대상 : 앱 전체 유저 - toType(001 - 코드표 참고(보내는 대상코드)), pushType(코드표 참고(푸쉬 메세지 코드)), bookDate(예약발송일자 - 필요시), body(내용)
	 * 
	 * TO_TYPE : 
	 * 001 - CANDY
	 * 002 - TOPIC
	 * 003 - TOKEN
	 * 
	 * PUSH_TYPE
	 * 001 - 공지사항
	 * 002 - 문의글 답변
	 * 003 - 켈린더 내용 등록
	 * 004 - 연말정산 종료일 임박
	 * 005 - 연말정산 1차 마감
	 */
	@Override
	public PushVO insPushSendList(PushVO vo) {
		
		boolean checkFlag = false;
		
		if(null == vo) {
			vo.setResult(ReturnResult.Result.FAILURE_CODE);
			vo.setResultCode(ReturnResult.ResultCode.ARGUMENTS_NOTFOUND);
			return vo;
		}
		
		if(null == vo.getToType() || ("").equals(vo.getToType())) {
			vo.setResult(ReturnResult.Result.FAILURE_CODE);
			vo.setResultCode(ReturnResult.ResultCode.PUSH_TO_TYPE_NOTFOUND);
			return vo;
		} else {
			if((FCM.PushType.TOKEN).equals(vo.getToType())) {
				if(null == vo.getUserId() || ("").equals(vo.getUserId())) {
					vo.setResult(ReturnResult.Result.FAILURE_CODE);
					vo.setResultCode(ReturnResult.ResultCode.USERID_NOTFOUND);
					return vo;
				} else {
					checkFlag = true;
				}
			} else if((FCM.PushType.TOPIC).equals(vo.getToType())){
				if(null == vo.getBizId() || ("").equals(vo.getBizId())) {
					vo.setResult(ReturnResult.Result.FAILURE_CODE);
					vo.setResultCode(ReturnResult.ResultCode.BIZID_NOTFOUND);
					return vo;
				} else {
					checkFlag = true;
				}
			} else {
				checkFlag = true;
			}
		}
		
		if(checkFlag) {
			try {
					vo.setTitle("CANDY");
					if((FCM.PushType.TOKEN).equals(vo.getToType())) {
						pushDAO.insPushSendUser(vo);
					} else if((FCM.PushType.TOPIC).equals(vo.getToType())) {
						pushDAO.insPushSendTopic(vo);
					} else if((FCM.PushType.CANDY).equals(vo.getToType())) {
						vo.setBizId("CANDY");
						pushDAO.insPushSendTopic(vo);
					}
					
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("에러 발생");
				vo.setResult(ReturnResult.Result.FAILURE_CODE);
				vo.setResultCode(ReturnResult.ResultCode.DB_INSERT_ERROR);
			} finally {
				return vo;
			}
		}
		
		vo.setResult(ReturnResult.Result.SUCCESS_CODE);
		vo.setResultCode(ReturnResult.ResultCode.NOERROR);
		return vo;
		
	}
	
}
