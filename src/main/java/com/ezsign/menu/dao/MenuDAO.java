package com.ezsign.menu.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.framework.util.StringUtil;
import com.ezsign.menu.vo.MenuPermVO;
import com.ezsign.menu.vo.MenuUserTypeVO;
import com.ezsign.menu.vo.MenuVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("menuDAO")
public class MenuDAO extends EgovAbstractDAO {
	
	public void insMenu(MenuVO vo) {
		insert("menuDAO.insMenu", vo);
	}
	
	public void delMenu(MenuVO vo) {
		update("menuDAO.delMenu", vo);
	}

	@SuppressWarnings("unchecked")
	public List<MenuVO> getMenuMainList(MenuVO vo) {
		return (List<MenuVO>)list("menuDAO.getMenuMainList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<MenuVO> getMenuList(MenuVO vo) {
		
		return (List<MenuVO>)list("menuDAO.getMenuList", vo);
	}
	
	public int updMenu(MenuVO vo) {
		return update("menuDAO.updMenu", vo);
	}
	
	public void insMenuPerm(MenuPermVO vo) {
		insert("menuDAO.insMenuPerm", vo);
	}
	
	public int updMenuPerm(MenuPermVO vo) {
		return update("menuDAO.updMenuPerm", vo);
	}
	
	
	public void delMenuPerm(MenuPermVO vo) {
		delete("menuDAO.delMenuPerm", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<MenuPermVO> getMenuPermList(MenuPermVO vo) {
		return (List<MenuPermVO>)list("menuDAO.getMenuPermList", vo);
	}
	
	public void insMenuUserType(MenuUserTypeVO vo) {
		insert("menuDAO.insMenuUserType", vo);
	}
	
	public void delMenuUserType(MenuUserTypeVO vo) {
		delete("menuDAO.delMenuUserType", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<MenuUserTypeVO> getMenuUserTypeList(MenuUserTypeVO vo) {
		return (List<MenuUserTypeVO>)list("menuDAO.getMenuUserTypeList", vo);
	}
	
}
