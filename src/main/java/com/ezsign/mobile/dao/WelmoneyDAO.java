package com.ezsign.mobile.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;



@Repository("welmoneyDAO")
public class WelmoneyDAO extends EgovAbstractDAO {
	
	public void insWelmoneyLoginLog(String loginId, String ipAddr) {
		HashMap<String, String> obj = new HashMap<>();
		obj.put("loginId", loginId);
		obj.put("ipAddr", ipAddr);

		insert("welmoneyDAO.insWelmoneyLoginLog", obj);
	}
	
}
