package com.ezsign.contract.dao;


import java.util.List;

import com.ezsign.contract.vo.*;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("contractDAO")
public class ContractDAO extends EgovAbstractDAO {
	
	public ContractPersonMainVO getContractPersonMain(ContractPersonVO vo) {
		return (ContractPersonMainVO)select("contractDAO.getContractPersonMain", vo);
	}
	
	public String insContractPerson(ContractPersonVO vo) {
		return (String)insert("contractDAO.insContractPerson", vo);
	}
	
	public int updContractPerson(ContractPersonVO vo) {
		return update("contractDAO.updContractPerson", vo);
	}

	public int delContractPerson(ContractPersonVO vo) {
		return delete("contractDAO.delContractPerson", vo);
	}

	public int delContractPersonTemp(ContractPersonVO vo) {
		return delete("contractDAO.delContractPersonTemp", vo);
	}

	public void moveContractPersonDel(ContractPersonVO vo) {
		insert("contractDAO.moveContractPersonDel", vo);
	}
	
	public ContractPersonVO getContractPerson(ContractPersonVO vo) {
		return (ContractPersonVO)selectByPk("contractDAO.getContractPerson", vo);
	}

	public ContractPersonVO getContractPersonNonce(ContractPersonVO vo) {
		return (ContractPersonVO)selectByPk("contractDAO.getContractPersonNonce", vo);
	}

	@SuppressWarnings("unchecked")
	public List<ContractPersonVO> delContractPersonList(ContractPersonVO vo) {
		return (List<ContractPersonVO>)list("contractDAO.delContractPersonList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContractPersonVO> getContractPersonList(ContractPersonVO vo) {
		return (List<ContractPersonVO>)list("contractDAO.getContractPersonList", vo);
	}
	
	public int getContractPersonListCount(ContractPersonVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("contractDAO.getContractPersonListCount", vo);
	}

	public int getContractPersonWriteCount(ContractPersonVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("contractDAO.getContractPersonWriteCount", vo);
	}
	
	public int getContractPersonDownloadCount(ContractPersonVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("contractDAO.getContractPersonDownloadCount", vo);
	}

	public int getContractPersonUserGrpDownloadCount(ContractPersonVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("contractDAO.getContractPersonUserGrpDownloadCount", vo);
	}

	public void insContractPersonLog(ContractPersonLogVO vo) {
		insert("contractDAO.insContractPersonLog", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContractPersonLogVO> getContractPersonLogList(ContractPersonLogVO vo) {
		return (List<ContractPersonLogVO>)list("contractDAO.getContractPersonLogList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContractFormVO> getContractFormList(ContractFormVO vo) {
		return (List<ContractFormVO>)list("contractDAO.getContractFormList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<ContractFormValueVO> getContractFormListExcel(ContractFormVO vo) {
		return (List<ContractFormValueVO>)list("contractDAO.getContractFormListExcel", vo);
	}
	
	public void insContractForm(ContractFormVO vo) {
		insert("contractDAO.insContractForm", vo);
	}

	public void insContractFormList(List<ContractFormVO> vo) {
		insert("contractDAO.insContractFormList", vo);
	}
	
	public void updContractForm(ContractFormVO vo) {
		update("contractDAO.updContractForm", vo);
	}
	
	public int delContractForm(ContractFormVO vo) {
		return delete("contractDAO.delContractForm", vo);
	}
	
	public void insContractNew(ContractPersonNewVO vo) {
		insert("contractDAO.insContractNew", vo);
	}
	
	public void updContractNew(ContractPersonNewVO vo) {
		update("contractDAO.updContractNew", vo);
	}

	public void updContractNewSign(ContractPersonNewVO vo) {
		update("contractDAO.updContractNewSign", vo);
	}

	public void updContractNewStatus(ContractPersonNewVO vo) {
		update("contractDAO.updContractNewStatus", vo);
	}
	
	public void delContractNew(ContractPersonNewVO vo) {
		delete("contractDAO.delContractNew", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContractPersonNewVO> getContractPersonNewList(ContractPersonNewVO vo) {
		return (List<ContractPersonNewVO>)list("contractDAO.getContractPersonNewList", vo);
	}

	public List<ContractPersonNewVO> getContractTransformList(ContractPersonNewVO vo) {
		return (List<ContractPersonNewVO>)list("contractDAO.getContractTransformList", vo);
	}

	public ContractPersonNewVO getContractTransformListCount(ContractPersonNewVO vo) {
		return (ContractPersonNewVO)getSqlMapClientTemplate().queryForObject("contractDAO.getContractTransformListCount", vo);
	}

	public ContractPersonNewVO getContractTransformStatusListCount(ContractPersonNewVO vo) {
		return (ContractPersonNewVO)getSqlMapClientTemplate().queryForObject("contractDAO.getContractTransformStatusListCount", vo);
	}
	
	public void insContractNewForm(ContractPersonNewFormVO vo) {
		insert("contractDAO.insContractNewForm", vo);
	}

	public void delContractNewForm(ContractPersonNewFormVO vo) {
		delete("contractDAO.delContractNewForm", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContractPersonNewFormVO> getContractPersonNewFormList(ContractPersonNewFormVO vo) {
		return (List<ContractPersonNewFormVO>)list("contractDAO.getContractPersonNewFormList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<ContractPersonVO> getContractPersonUserGrpList(ContractPersonVO vo) {
		return (List<ContractPersonVO>)list("contractDAO.getContractPersonUserGrpList", vo);
	}
	
	public int getContractPersonUserGrpListCount(ContractPersonVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("contractDAO.getContractPersonUserGrpListCount", vo);
	}
	
	public ContractPersonNewVO getContractPersonSign(ContractPersonVO vo) {
		return (ContractPersonNewVO)select("contractDAO.getContractPersonSign", vo);
	}

	public int getDownloadCount(ContractPersonLogVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("contractDAO.getDownloadCount", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContractPersonNewFormCommentVO> getContractPersonNewFormCommentList(ContractPersonNewFormCommentVO vo) {
		return (List<ContractPersonNewFormCommentVO>)list("contractDAO.getContractPersonNewFormCommentList", vo);
	}
	
	public void insContractPersonNewFormComment(ContractPersonNewFormCommentVO vo) {
		insert("contractDAO.insContractPersonNewFormComment", vo);
	}
	
	public void delContractPersonNewFormComment(ContractPersonNewFormCommentVO vo) {
		delete("contractDAO.delContractPersonNewFormComment", vo);
	}

	public void updContractNewFinal(ContractPersonNewVO vo) {
		update("contractDAO.updContractNewFinal", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContractPersonVO> getRemoveContractList(ContractPersonVO vo) {
		return (List<ContractPersonVO>)list("contractDAO.getRemoveContractList", vo);
	}
	
	public void insMetaDataToContractFormList(ContractFormVO vo){
		insert("contractDAO.insMetaDataToContractFormList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<ContractPersonVO> getMetaDataToContractList(ContractPersonVO vo){
		return (List<ContractPersonVO>) list ("contractDAO.getMetaDataToContractList", vo);
	}

	public void insContractLog() {
		insert("contractDAO.insContractLog");
	}
	
	public String getEncodeYn(ContractFormVO vo) {
        return (String) select("contractDAO.getEncodeYn", vo);
    }

	public void insContrectSetting(ContractSettingVO vo) {
		insert("contractDAO.insContractSetting", vo);
	}

	public void delContrectSetting(ContractSettingVO vo) {
		delete("contractDAO.delContractSetting", vo);
	}

	// 재전송시 전송일자 업데이트
	public void updContractExpirdDate(ContractPersonVO vo) {
		update("contractDAO.updContractExpirdDate", vo);
	}

	// 문서 상태 업데이트
	public void updContractNewRe(ContractPersonNewVO vo) {
		update("contractDAO.updContractNewRe", vo);
	}

	// 문서 상태 업데이트 시 엑셀 데이터 삭제
	public void delContractNewFormRe(ContractPersonNewVO vo) {
		delete("contractDAO.delContractNewFormRe", vo);
	}

	// 미리보기 계약서 조회
	public ContractPersonVO getContractPersonPreview(ContractPersonVO vo) {
		return (ContractPersonVO)selectByPk("contractDAO.getContractPersonPreview", vo);
	}

	// 미리보기 계약서 엑셀 데이터 조회
	public List<ContractFormVO> getContractPreviewFormList(ContractFormVO vo) {
		return (List<ContractFormVO>)list("contractDAO.getContractPreviewFormList", vo);
	}

	public void delPreview() {
		delete("contractDAO.delPreview");
	}

}
