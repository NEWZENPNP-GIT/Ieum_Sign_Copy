package com.ezsign.fcm.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.fcm.service.PushSendService;
import com.ezsign.user.dao.DeviceDAO;
import com.ezsign.user.vo.DeviceVO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONObject;

@Service("pushSendService")
public class PushSendServiceImpl extends EgovAbstractServiceImpl implements PushSendService {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="deviceDAO")
	private DeviceDAO deviceDAO;
	
	@Resource(name="empDAO")
	private EmpDAO empDAO;
	
	public Boolean candySendPushMessage(String bizId, String userId, String message) throws Exception {
		//데이터조회 후 PUSH KEY 가져온다.
		try {
			
			//해당 근로자 정보를 가지고 온다.
			EmpVO searchEmpVO = new EmpVO();
			searchEmpVO.setBizId(bizId);
			searchEmpVO.setUserId(userId);
			
			EmpVO resultEmpVO = empDAO.getEmp(searchEmpVO);
			
			if (resultEmpVO == null || resultEmpVO.getLoginId() == null || resultEmpVO.getLoginId().isEmpty()) {
				return false;
			}
			
			//메시지 변경
			message = message.replace("{0}", resultEmpVO.getEmpName());
			
			//근로자의 디바이스 정보 조회
			DeviceVO vo = new DeviceVO();
			vo.setUserId(resultEmpVO.getLoginId());
			
			List<DeviceVO> resultList = deviceDAO.getDeviceList(vo);
			
			//조회된 푸쉬(로그인아이디별 여러개만 데이터 가지고 있음.
			if (resultList != null && resultList.size() > 0) {
				for(int i = 0; i < resultList.size(); i++) {
					DeviceVO resultVO = resultList.get(i);
					String pushToken = resultVO.getPushToken();
					
					if (!pushToken.isEmpty()) {
						
						JSONObject jsonobj = new JSONObject();
						JSONObject jsonData = new JSONObject();
						JSONObject jsonNoti = new JSONObject();
						
						jsonobj.put("to", pushToken);
						jsonobj.put("priority", "high");
						
						jsonData.put("title", PUSH_TITLE);
						jsonData.put("message", message);
						
						jsonobj.put("data", jsonData);
						
						jsonNoti.put("title", PUSH_TITLE);
						jsonNoti.put("body", message);
						
						jsonobj.put("notification", jsonNoti);
						
						System.out.println("보내는 메시지 "+jsonobj.toString());
						try {
							//보내기 처리
							URL url = new URL(SERVER_URL);
							HttpURLConnection conn = (HttpURLConnection)url.openConnection();
							
							conn.setRequestMethod("POST");
							conn.setRequestProperty("Content-Type", CLIENT_CONT_TYPE);
							conn.setRequestProperty("Authorization", SERVER_AUTH);
							
							conn.setDoOutput(true);
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
							bw.write(jsonobj.toString());
							bw.flush();
							bw.close();
							
							//서버에서 보낸 응답 데이터 수신 받기
							BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
							String returnMsg = in.readLine();
							
							System.out.println("서버에서 받은 메시지: "+returnMsg);
							
							int responseCode = conn.getResponseCode();
							
							System.out.println(responseCode +":응답 코드 받음");
						
						}
						catch (IOException ex) {
							ex.printStackTrace();
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}	
			} else {
				return true;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return true;
	}

}
