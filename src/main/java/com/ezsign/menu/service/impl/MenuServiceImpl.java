package com.ezsign.menu.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.menu.dao.MenuDAO;
import com.ezsign.menu.service.MenuService;
import com.ezsign.menu.vo.MenuPermVO;
import com.ezsign.menu.vo.MenuUserTypeVO;
import com.ezsign.menu.vo.MenuVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("menuService")
public class MenuServiceImpl extends AbstractServiceImpl implements MenuService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="menuDAO")
	private MenuDAO menuDAO;

	public static void main(String[] args) {

	}

	@Override
	public void insMenu(MenuVO vo) throws Exception {
		menuDAO.insMenu(vo);
	}

	@Override
	public void delMenu(MenuVO vo) throws Exception {
		menuDAO.delMenu(vo);
	}

	@Override
	public List<MenuVO> getMenuMainList(MenuVO vo) throws Exception {
		List<MenuVO> dataList = new ArrayList<MenuVO>();
		dataList = menuDAO.getMenuMainList(vo);

		return dataList;
	}

	@Override
	public List<MenuVO> getMenuList(MenuVO vo) throws Exception {
		List<MenuVO> dataList = new ArrayList<MenuVO>();

		dataList = menuDAO.getMenuList(vo);

		return dataList;
	}

	@Override
	public int updMenu(MenuVO vo) throws Exception {
		int reuslt = menuDAO.updMenu(vo);
		return reuslt;
	}

	@Override
	public void insMenuPerm(MenuPermVO vo) throws Exception {
		menuDAO.insMenuPerm(vo);
	}

	@Override
	public void delMenuPerm(MenuPermVO vo) throws Exception {
		menuDAO.delMenuPerm(vo);
	}

	@Override
	public int updMenuPerm(MenuPermVO vo) throws Exception {
		return menuDAO.updMenuPerm(vo);
	}

	@Override
	public List<MenuPermVO> getMenuPermList(MenuPermVO vo) throws Exception {
		List<MenuPermVO> dataList = new ArrayList<MenuPermVO>();
		dataList = menuDAO.getMenuPermList(vo);

		return dataList;
	}

	@Override
	public void insMenuUserType(MenuUserTypeVO vo) throws Exception {
		menuDAO.insMenuUserType(vo);
	}

	@Override
	public void delMenuUserType(MenuUserTypeVO vo) throws Exception {
		menuDAO.delMenuUserType(vo);
	}

	@Override
	public List<MenuUserTypeVO> getMenuUserTypeList(MenuUserTypeVO vo) throws Exception {
		List<MenuUserTypeVO> dataList = new ArrayList<MenuUserTypeVO>();
		dataList = menuDAO.getMenuUserTypeList(vo);

		return dataList;
	}

}
