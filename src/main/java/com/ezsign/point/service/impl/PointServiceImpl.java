package com.ezsign.point.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.service.PointService;
import com.ezsign.point.vo.DistPointVO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;


@Service("pointService")
public class PointServiceImpl extends AbstractServiceImpl implements PointService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="pointDAO")
	private PointDAO pointDAO;

	@Override
	public PointVO insPoint(PointVO vo) throws Exception {
		PointVO pointVO = new PointVO();
		pointVO.setBizId(vo.getBizId());
		pointVO.setCurPoint(vo.getCurPoint());
		PointVO resultPointVO = pointDAO.getPoint(pointVO);

		if(resultPointVO==null) {
			pointVO.setUserId(vo.getUserId());
			pointDAO.insPoint(pointVO);

			int paymentAmt = 0;
			if(vo.getCurPoint()>0) {
				paymentAmt = vo.getCurPoint() * 100;
			}

			PointLogVO pointLogVO = new PointLogVO();
			pointLogVO.setBizId(vo.getBizId());
			pointLogVO.setUserId(vo.getUserId());
			pointLogVO.setPointType("1");
			pointLogVO.setPointPrice(vo.getCurPoint());
			pointLogVO.setPaymentAmt(paymentAmt);
			pointLogVO.setEtcDesc("포인트를 조정하였습니다.");

			pointDAO.insPointLog(pointLogVO);

		}
		return pointVO;
	}

	@Override
	public PointVO getPoint(PointVO vo)  throws Exception {

		return pointDAO.getPoint(vo);
	}

	@Override
	public List<PointLogVO> getPointLogList(PointLogVO vo)  throws Exception {
		List<PointLogVO> list = null;

		list = pointDAO.getPointLogList(vo);

		return list;
	}

	@Override
	public int getPointLogListCount(PointLogVO vo) throws Exception {
		return pointDAO.getPointLogListCount(vo);
	}

	@Override
	public int updPoint(PointVO vo)  throws Exception {
		int result = 0;

		// 포인트생성이 되지 않은경우 생성후 조정
		PointVO pVO = pointDAO.getPoint(vo);
		if(pVO==null) {
			pointDAO.insPoint(vo);
			result=1;
		} else {
			result = pointDAO.updPoint(vo);
		}

		PointLogVO logVO = new PointLogVO();
		logVO.setBizId(vo.getBizId());
		logVO.setUserId(vo.getUserId());
		logVO.setPointType("3");
		logVO.setPointPrice(vo.getCurPoint());
		logVO.setPaymentAmt(0);
		logVO.setEtcDesc("시스템관리자가 포인트를 조정하였습니다.("+vo.getComment()+")");
		pointDAO.insPointLog(logVO);


		return result;
	}

	@Override
	public int updPointNew(PointVO vo)  throws Exception {
		int result = 0;
		PointLogVO logVO = new PointLogVO();

		if(vo.getDeduPoint() > 0){
			vo.setCurPoint(vo.getCurPoint()-vo.getDeduPoint());
			logVO.setPointType("3");
			logVO.setPointPrice(vo.getDeduPoint());
		}else{
			vo.setCurPoint(vo.getCurPoint()+vo.getChargePoint());
			logVO.setPointType("1");
			logVO.setPointPrice(vo.getChargePoint());
		}
		// 포인트생성이 되지 않은경우 생성후 조정
		PointVO pVO = pointDAO.getPoint(vo);
		if(pVO==null) {
			pointDAO.insPoint(vo);
			result=1;
		} else {
			result = pointDAO.updPoint(vo);
		}


		logVO.setBizId(vo.getBizId());
		logVO.setUserId(vo.getUserId());
		//logVO.setPointType("3");
		//logVO.setPointPrice(vo.getCurPoint());
		logVO.setPointResult(vo.getCurPoint());
		logVO.setPaymentAmt(0);
		logVO.setEtcDesc(vo.getComment());
		pointDAO.insPointLog(logVO);


		return result;
	}

	@Override
	public int updPointDist(DistPointVO vo)  throws Exception {
		int result = 0;
		PointLogVO logVO = new PointLogVO();
		PointVO pVO = new PointVO();
		pVO.setBizId(vo.getBizId());

		// 포인트생성이 되지 않은경우 생성후 조정
		PointVO rVO = pointDAO.getPoint(pVO);
		if(rVO==null) {
			//pointDAO.insPoint(vo);
			result=1;
		} else {
			result = pointDAO.updPointDist(vo);
			logVO.setBizId(vo.getBizId());
			logVO.setUserId(vo.getUserId());
			if(vo.getDeduPoint() > 0){
				logVO.setPointType("3");
				logVO.setPointPrice(vo.getDeduPoint());
				logVO.setPointResult(rVO.getCurPoint() - logVO.getPointPrice());
				logVO.setEtcDesc(vo.getComment());
			}else if(vo.getChargePoint() > 0){
				logVO.setPointType("1");
				logVO.setPointPrice(vo.getChargePoint());
				logVO.setPointResult(rVO.getCurPoint() + logVO.getPointPrice());
				logVO.setEtcDesc(vo.getComment());
			}
			logVO.setPaymentAmt(0);
			pointDAO.insPointLog(logVO);
		}

		return result;
	}

}
