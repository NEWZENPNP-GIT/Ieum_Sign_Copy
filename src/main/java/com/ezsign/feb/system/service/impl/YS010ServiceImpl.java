package com.ezsign.feb.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.system.dao.YS010DAO;
import com.ezsign.feb.system.service.YS010Service;
import com.ezsign.feb.system.vo.YS010VO;
import com.ezsign.push.dao.PushDAO;
import com.ezsign.push.service.PushService;
import com.ezsign.push.vo.PushVO;

@Service("ys010Service")
public class YS010ServiceImpl implements YS010Service {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	@Resource(name="ys010DAO")
	private YS010DAO ys010DAO;
	
	@Resource(name="pushDAO")
	private PushDAO pushDAO;
	
	@Resource(name = "pushService")
	private PushService pushService;
	
	// 연말정산_기초설정 조회
	@Override
	public List<YS010VO> getYS010List(YS010VO vo)  throws Exception {
		List<YS010VO> list =  ys010DAO.getYS010List(vo);
		
		return list;
	}

	// 연말정산_기초설정 입력
	@Override
	public void insYS010(YS010VO vo) throws Exception {
		
		// 데이터 존재여부확인 (계약ID)
		System.out.println("계약ID:"+vo.get계약ID());
		List<YS010VO> list =  ys010DAO.getYS010List(vo);
		
		if(list.size()==0) {
			// 미존재시 데이터 입력
			ys010DAO.insYS010(vo);	
		} else {
			// 데이터 존재시 수정으로 처리
			ys010DAO.updYS010(vo);
		}
	}

	// 연말정산_기초설정 수정
	@Override
	public int updYS010(YS010VO vo, String bizId, String febYear) throws Exception {
		
		// 근로자 입력기간 마지막
		PushVO pushVo = new PushVO();
		pushVo.setToType("002");
		pushVo.setPushType("004");
		pushVo.setBizId(bizId);
		pushVo.setValue1(febYear);
		pushVo.setValue2("");
		pushVo.setBookDate(vo.get근로자접속기간_TO()+"090000");

		PushVO pushVo1 = new PushVO();
		pushVo1 =  pushDAO.getPushMessage(pushVo);
		
		pushVo.setBody(pushVo1.getMessageContents());
		pushService.insPushSendList(pushVo);
		
		// 관리자 입력기간 마지막
		PushVO pushVo01 = new PushVO();
		pushVo01.setToType("002");
		pushVo01.setPushType("005");
		pushVo01.setBizId(bizId);
		pushVo01.setValue1(febYear);
		pushVo01.setValue2("");
		pushVo01.setBookDate(vo.get관리자접속기간_TO()+"090000");

		PushVO pushVo12 = new PushVO();
		pushVo12 =  pushDAO.getPushMessage(pushVo01);
		
		pushVo01.setBody(pushVo12.getMessageContents());
		pushService.insPushSendList(pushVo01); 
		
		
		return ys010DAO.updYS010(vo);		
	}

	// 연말정산 근로자 접속기간 체크
	@Override
	public int getWorkerDateCheck(YS010VO vo) {
		
		return ys010DAO.getWorkerDateCheck(vo);
	}
}
