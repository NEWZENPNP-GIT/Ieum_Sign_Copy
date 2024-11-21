package com.ezsign.push.service;

import java.util.List;

import com.ezsign.push.vo.PushVO;

public interface PushService {
	
	/**
	 * 읽지 않은 푸시 리스트
	 * @param vo
	 * @return List<PushVO>
	 * @throws Exception
	 */
	public List<PushVO> getPushList(PushVO vo) throws Exception;
	
	/**
	 * 푸시 읽지 않음 갯수 가져오기 
	 * @param vo
	 * @return PushVO
	 * @throws Exception
	 */
	public PushVO getReadCnt(PushVO vo) throws Exception;
	
	/**
	 * 푸시 메시지 가져오기 
	 * @param vo
	 * @return PushVO
	 * @throws Exception
	 */
	public PushVO getPushMessage(PushVO vo) throws Exception;
	
	
	/**
	 * 푸시 읽음 처리
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	public int insPushRead(PushVO vo)  throws Exception;
	
	
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
	public PushVO insPushSendList(PushVO vo)  throws Exception;
	
	/**
	 * 푸시 날려야 할 리스트 가져오기 - 스케쥴러
	 * @param vo
	 * @return List<PushVO>
	 * @throws Exception
	 */
	public List<PushVO> getPushSendList(PushVO vo) throws Exception;
	
	/**
	 * 푸시 전송 완료 날짜 등록 - 스케쥴러
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	public int updPushSendList(PushVO vo) throws Exception;
	
	
}
