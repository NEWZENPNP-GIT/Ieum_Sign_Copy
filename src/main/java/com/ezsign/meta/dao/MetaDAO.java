package com.ezsign.meta.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.content.vo.CabinetVO;
import com.ezsign.meta.vo.MetaDataVO;
import com.ezsign.meta.vo.MetaFileVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("metaDAO")
public class MetaDAO extends EgovAbstractDAO {
	
	public String insMetaFile(MetaFileVO vo) {
		return (String)insert("metaDAO.insMetaFile", vo);
	}
	
	public void insMetaData(MetaDataVO vo) {
		insert("metaDAO.insMetaData", vo);
	}
	
	public void insMetaDataList(List<MetaDataVO> vo) {
		insert("metaDAO.insMetaDataList", vo);
	}
}
