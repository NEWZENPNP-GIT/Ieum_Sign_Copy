package com.ezsign.feb.easyFeb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YE001VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("attachFileDao")
public class AttachFileDao extends EgovAbstractDAO {

	
	@SuppressWarnings("unchecked")
	public List<Map> getYE402List(Map params) {
		return (List<Map>) list("easyFeb.getYE402", params);
	}

	public List<Map> getYE401List(Map params) {
		return (List<Map>) list("easyFeb.getYE401", params);
	}

	public List<Map> getYE403List(Map params) {
		return (List<Map>) list("easyFeb.getYE403", params);
	}

	public List<Map> getYE108List(Map params) {
		return (List<Map>) list("easyFeb.getYE108", params);
	}

	public List<Map> getYE404List(Map params) {
		return (List<Map>) list("easyFeb.getYE404", params);
	}

	public List<Map> getYE105List(Map params) {
		return (List<Map>) list("easyFeb.getYE105", params);
	}

	public List<Map> getYE001List(Map params) {
		return (List<Map>) list("easyFeb.getYE001", params);
	}
	
	public Map getFileCount(Map params) {
		return (Map)select("easyFeb.getFileCount", params);
	}
}
