package com.ezsign.schedule.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.schedule.dao.ScheduleDAO;
import com.ezsign.schedule.service.ScheduleService;
import com.ezsign.schedule.vo.ScheduleVO;
import com.ezsign.push.vo.PushVO;
import com.ezsign.push.dao.PushDAO;
import com.ezsign.push.service.PushService;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("scheduleService")
public class ScheduleServiceImpl extends AbstractServiceImpl implements ScheduleService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="scheduleDAO")
	private ScheduleDAO scheduleDAO;
	
	@Resource(name="pushDAO")
	private PushDAO pushDAO;
	
	@Resource(name = "pushService")
	private PushService pushService;
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void insSchedule(ScheduleVO vo, String bizname) throws Exception {
		scheduleDAO.insSchedule(vo);
		if ( vo.getOpenType().equals("3")) {

			PushVO pushVo = new PushVO();
			pushVo.setToType("002");
			pushVo.setPushType("003");
			pushVo.setBizId(vo.getBizId());
			pushVo.setValue1(bizname);
			pushVo.setValue2(vo.getSubject());
			// pushVo.setBookDate("20181207141500");

			PushVO pushVo1 = new PushVO();
			pushVo1 =  pushDAO.getPushMessage(pushVo);
			
			pushVo.setBody(pushVo1.getMessageContents());
			pushService.insPushSendList(pushVo);
		}
	}

	@Override
	public void delSchedule(ScheduleVO vo) throws Exception {
		scheduleDAO.delSchedule(vo);
	}

	@Override
	public int updSchedule(ScheduleVO vo) throws Exception {
		return scheduleDAO.updSchedule(vo);
	}
	
	@Override
	public List<ScheduleVO> getScheduleList(ScheduleVO vo) throws Exception {
		List<ScheduleVO> dataList = new ArrayList<ScheduleVO>();
		dataList = scheduleDAO.getScheduleList(vo);
		
		return dataList;		
	}
	
}
