package com.ezsign.menu.service;

import java.util.List;

import com.ezsign.menu.vo.MenuPermVO;
import com.ezsign.menu.vo.MenuUserTypeVO;
import com.ezsign.menu.vo.MenuVO;

public interface MenuService {

	public void insMenu(MenuVO vo) throws Exception;

	public void delMenu(MenuVO vo) throws Exception;

	public int updMenu(MenuVO vo) throws Exception;

	public List<MenuVO> getMenuMainList(MenuVO vo) throws Exception;

	public List<MenuVO> getMenuList(MenuVO vo) throws Exception;

	public void insMenuPerm(MenuPermVO vo) throws Exception;

	public void delMenuPerm(MenuPermVO vo) throws Exception;

	public int updMenuPerm(MenuPermVO vo) throws Exception;

	public List<MenuPermVO> getMenuPermList(MenuPermVO vo) throws Exception;

	public void insMenuUserType(MenuUserTypeVO vo) throws Exception;

	public void delMenuUserType(MenuUserTypeVO vo) throws Exception;

	public List<MenuUserTypeVO> getMenuUserTypeList(MenuUserTypeVO vo) throws Exception;
	
}
