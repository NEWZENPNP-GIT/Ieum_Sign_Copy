package com.ezsign.point.service;

import java.util.List;

import com.ezsign.point.vo.DistPointVO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;


public interface PointService {

	public PointVO insPoint(PointVO vo) throws Exception;
	
	public PointVO getPoint(PointVO vo)  throws Exception;

	public List<PointLogVO> getPointLogList(PointLogVO vo)  throws Exception;

	public int getPointLogListCount(PointLogVO vo)  throws Exception;

	public int updPoint(PointVO vo)  throws Exception;
	
	public int updPointNew(PointVO vo)  throws Exception;
	
	public int updPointDist(DistPointVO vo)  throws Exception;

}
