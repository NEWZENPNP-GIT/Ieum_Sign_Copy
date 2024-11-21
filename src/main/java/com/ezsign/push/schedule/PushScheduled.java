/**
 * @author bkj 2016.06.13
 *
 */
package com.ezsign.push.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezsign.push.service.PushService;
import com.ezsign.push.util.FCM;
import com.ezsign.push.vo.PushVO;

@Component
public class PushScheduled {
    
	@Resource(name = "pushService")
	private PushService pushService;
	
	/**
	 * 푸시 메시지 스케쥴러
	 * @param 
	 * @return ModelAndView - 리턴 URL
	 * @throws Exception
	 * 0 0/5 * * * * 5분마다
	 */
	//@Scheduled(cron = "0 */5 * * * *")
	public void PushScheduled() throws Exception{

		PushVO pushVo = new PushVO();
		List<PushVO> pushSendList = pushService.getPushSendList(pushVo);//푸시 데이터 불러오기
		
		System.out.println("푸쉬 갯수 : " + pushSendList.size());

		if(null != pushSendList && 0 < pushSendList.size()){
			
			for (int i = 0; i < pushSendList.size(); i++) {
				pushVo = pushSendList.get(i);
				
				int resultCode = FCM.send_FCM_Notification(pushVo.getTo(), pushVo.getTitle(), pushVo.getBody());
				
				System.out.println("resultCode : " + resultCode);
				
				if(200 == resultCode) {
					pushService.updPushSendList(pushVo); //푸시 전송 완료 날짜 등록
				}
			}
						
		}
		
	}
}


