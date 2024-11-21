package com.ezsign.biz.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import com.ezsign.biz.vo.BizGrpVO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biz.vo.CandycashBizVO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.user.vo.UserGrpVO;

@Repository("bizDAO")
public class BizDAO extends EgovAbstractDAO {
	
	public int getCheckService(BizVO vo) {
		return (Integer)selectByPk("bizDAO.getCheckService", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<BizVO> getBizList(BizVO vo) {
		return (List<BizVO>)list("bizDAO.getBizList", vo);
	}

	public List<BizVO> getCsBizList(BizVO vo) {
		return (List<BizVO>)list("bizDAO.getCsBizList", vo);
	}
	
	public BizVO getBiz(BizVO vo) {
		return (BizVO)selectByPk("bizDAO.getBiz", vo);
	}

	public int getBizListCount(BizVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("bizDAO.getBizListCount", vo);
	}

	public int getCsBizListCount(BizVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("bizDAO.getCsBizListCount", vo);
	}
	
	public void insBiz(BizVO vo) {
		insert("bizDAO.insBiz", vo);
	}
	
	public int updBiz(BizVO vo) {
		return update("bizDAO.updBiz", vo);
	}

	public int updBizTax(BizVO vo) {
		return update("bizDAO.updBizTax", vo);
	}

	public int delBiz(BizVO vo) {
		return delete("bizDAO.delBiz", vo);
	}
	
	public void insBizService(BizVO vo) {
		insert("bizDAO.insBizService", vo);
	}
	
	public int delBizService(BizVO vo) {
		return delete("bizDAO.delBizService", vo);
	}
	

	public void insBizGrp(BizGrpVO vo) {
		insert("bizDAO.insBizGrp", vo);
	}

	public void delBizGrp(BizGrpVO vo) {
		delete("bizDAO.delBizGrp", vo);
	}

	@SuppressWarnings("unchecked")
	public List<BizGrpVO> getBizGrp(BizGrpVO vo) {
		return (List<BizGrpVO>)list("bizDAO.getBizGrp", vo);
	}

	@SuppressWarnings("unchecked")
	public List<BizGrpVO> getBizGrpList(BizGrpVO vo) {
		return (List<BizGrpVO>)list("bizDAO.getBizGrpList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<BizGrpVO> getBizGrpCombo(BizGrpVO vo) {
		return (List<BizGrpVO>)list("bizDAO.getBizGrpCombo", vo);
	}

	@SuppressWarnings("unchecked")
	public List<BizGrpVO> getBizGrpSubAdminList(BizGrpVO vo) {
		return (List<BizGrpVO>)list("bizDAO.getBizGrpSubAdminList", vo);
	}
	
	// 기업 서비스 추가
	public void insBizServiceAdd(BizVO vo) {
		insert("bizDAO.insBizServiceAdd", vo);
	}
	
	// 서비스 조회
	@SuppressWarnings("unchecked")
	public List<BizVO> getServiceList(BizVO vo) {
		return (List<BizVO>) list("bizDAO.getServiceList", vo);
	}
	
	// 접속한 사용자의 기업권한
	@SuppressWarnings("unchecked")
	public List<BizGrpVO> getBizGrpNameList(BizGrpVO vo) {
		return (List<BizGrpVO>)list("bizDAO.getBizGrpNameList", vo);
	}

	public void updateOpenVoucherContractEnd() {
		update("bizDAO.updateOpenVoucherContractEnd");
	}
	
	public void updatePointContractEnd(BizVO vo) {
		update("bizDAO.updatePointContractEnd", vo);
	}
	
	public void updatePointLogContractEnd(PointLogVO vo) {
		insert("bizDAO.updatePointLogContractEnd", vo);
	}
	
	// 서비스 조회
	@SuppressWarnings("unchecked")
	public List<BizVO> getupdateOpenVoucher(BizVO vo) {
		return (List<BizVO>) list("bizDAO.getupdateOpenVoucher", vo);
	}
	
	// 서비스 조회
	@SuppressWarnings("unchecked")
	public List<BizVO> getContractEndList(BizVO vo) {
		return (List<BizVO>) list("bizDAO.getContractEndList", vo);
	}
	
	// 서비스 조회
	@SuppressWarnings("unchecked")
	public List<BizVO> getContractD30List(BizVO vo) {
		return (List<BizVO>) list("bizDAO.getContractD30List", vo);
	}
	
	// 서비스 조회
	@SuppressWarnings("unchecked")
	public List<BizVO> getDownloadExpireList(BizVO vo) {
		return (List<BizVO>) list("bizDAO.getDownloadExpireList", vo);
	}
	
	public void insertBizRequst(BizVO vo) {
		insert("bizDAO.insertBizRequst", vo);
	}

	public void updCandyCashBizInfo(CandycashBizVO vo) {
		update("bizDAO.updCandyCashBizInfo", vo);
	}
	
	// 접속한 사용자의 기업권한
	@SuppressWarnings("unchecked")
	public BizGrpVO getBizExsit(BizGrpVO vo) {
		return (BizGrpVO)selectByPk("bizDAO.getBizExsit", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<CandycashBizVO> getCandyCashBizList(CandycashBizVO vo) {
		return (List<CandycashBizVO>)list("bizDAO.getCandyCashBizList", vo);
	}

	public void insDefaultContractForm(BizVO bizVO) {
		insert("bizDAO.insDefaultContractForm", bizVO);
	}

	public List<BizVO> getBizEmail(BizVO vo) {
		return (List<BizVO>)list("bizDAO.getBizEmail", vo);
	}
}
