package com.ezsign.menu.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;

import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.framework.vo.TreeVO;
import com.ezsign.menu.service.MenuService;
import com.ezsign.menu.vo.MenuPermVO;
import com.ezsign.menu.vo.MenuVO;
import com.ezsign.user.service.UserSnsService;


@Controller
@RequestMapping("/menu/")
public class MenuController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "menuService")
	private MenuService menuService;

	@Resource(name = "bizService")
	private BizService bizService;

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoin.do")
	public String goMemberJoinCtrl() {
		logger.info(":::::::::::::::::::: goMemberJoin :::::::::::::::::::");

		String redirectUri = "member/member_Join_step01";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoinUser.do")
	public String goMemberJoinUserCtrl() {
		logger.info(":::::::::::::::::::: goMemberJoinUser :::::::::::::::::::");

		String redirectUri = "member/member_Join_step01_user";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoin2.do")
	public ModelAndView goMemberJoin2Ctrl(HttpServletRequest request, @ModelAttribute("MenuVO") MenuVO vo) {
		logger.info(":::::::::::::::::::: goMemberJoin2 :::::::::::::::::::");
		String redirectUri = "member/member_Join_step02_user";
		ModelAndView mv = new ModelAndView();

		if(vo.getMenuType().equals("6")) {
			// 기업관리자
			redirectUri = "member/member_Join_step02_biz";
		} else if(vo.getMenuType().equals("8")) {
			// 노무사 또는 세무사, 중간 관리자
			redirectUri = "member/member_Join_step02_work";
		}

		Object snsType = request.getSession().getAttribute("snsType");
		Object snsEmail = request.getSession().getAttribute("snsEmail");
		Object snsName = request.getSession().getAttribute("snsName");

		mv.addObject("snsType", snsType);
		mv.addObject("snsEmail", snsEmail);
		mv.addObject("snsName", snsName);
		mv.setViewName(redirectUri);

		return mv;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoin3.do")
	public String goMemberJoin3Ctrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goMemberJoin3 :::::::::::::::::::");

		String redirectUri = "member/member_Join_step03";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goUserPwd.do")
	public String goUserPwdCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goUserPwd :::::::::::::::::::");
		logger.info("empNonce=>"+vo.getId());

		String redirectUri = "user/user_pwd_reset_step01";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goUserPwd2.do")
	public String goUserPwd2Ctrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goUserPwd :::::::::::::::::::");

		String redirectUri = "user/user_pwd_reset_step02";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMainMenu.do")
	public String goMainMenuCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goMainMenu :::::::::::::::::::");

		String redirectUri = "main/main";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMyPage.do")
	public String goMyPageCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goMyPage :::::::::::::::::::");

		String redirectUri = "user/myPage";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoinCallback.do")
	public String goMemberJoinCallbackCtrl() {
		logger.info(":::::::::::::::::::: goMemberJoinCallback :::::::::::::::::::");

		String redirectUri = "member/member_Join_callback";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoinLogin.do")
	public String goMemberJoinLoginCtrl(HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goMemberJoinLogin :::::::::::::::::::");

		String redirectUri = "member/member_Join_login";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoinLoginType.do")
	public ModelAndView goMemberJoinLoginTypeCtrl(HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goMemberJoinLoginType :::::::::::::::::::");

		ModelAndView mv = new ModelAndView();

		String uuid = UUID.randomUUID().toString();
		String redirectUri = "member/member_Join_step01";

		String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?";
		naverLoginUrl += "response_type=code";
		naverLoginUrl += "&client_id=" + UserSnsService.NAVER_CLIENT_ID;
		naverLoginUrl += "&redirect_uri=" + UserSnsService.NAVER_REDIRECT_URI;
		naverLoginUrl += "&state=" + uuid;

		String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?";
		kakaoLoginUrl += "&client_id=" + UserSnsService.KAKAO_CLIENT_ID;
		kakaoLoginUrl += "&redirect_uri=" + UserSnsService.KAKAO_REDIRECT_URI;
		kakaoLoginUrl += "&response_type=code";

		String googleLoginUrl = "";
		googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth?";
		googleLoginUrl += "client_id=" + UserSnsService.GOOGLE_CLIENT_ID;
		googleLoginUrl += "&nonce=" + uuid;
		googleLoginUrl += "&response_type=code";
		googleLoginUrl += "&redirect_uri="+UserSnsService.GOOGLE_REDIRECT_URI;
		googleLoginUrl += "&scope=openid%20profile%20email";
		googleLoginUrl += "&access_type=offline";
		googleLoginUrl += "&prompt=consent";

		request.getSession().removeAttribute("uuid");
		request.getSession().setAttribute("uuid", uuid);
		mv.addObject("naverUrl", naverLoginUrl);
		mv.addObject("kakaoUrl", kakaoLoginUrl);
		mv.addObject("googleUrl", googleLoginUrl);
		mv.setViewName("member/member_Join_login_type");

		return mv;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoinUserType.do")
	public String goMemberJoinUserTypeCtrl(HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goMemberJoinUserType :::::::::::::::::::");

		request.getSession().removeAttribute("accessToken");
		request.getSession().removeAttribute("refreshToken");
		request.getSession().removeAttribute("snsEmail");
		request.getSession().removeAttribute("snsName");
		request.getSession().removeAttribute("idToken");
		request.getSession().removeAttribute("snsType");

		String redirectUri = "member/member_Join_user_type";

		return redirectUri;
	}

	@RequestMapping(method=RequestMethod.GET, value="goMemberJoinUserType1.do")
	public String goMemberJoinUserType1Ctrl(HttpServletRequest request) {
		logger.info(":::::::::::::::::::: goMemberJoinUserType :::::::::::::::::::");

		//이음싸인 회원가입이기때문에 세션제거(sns로그인)
		/*request.getSession().removeAttribute("accessToken");
		request.getSession().removeAttribute("refreshToken");
		request.getSession().removeAttribute("idToken");
		request.getSession().removeAttribute("snsType");*/

		String redirectUri = "member/member_Join_user_type";

		return redirectUri;
	}


	@RequestMapping(method = RequestMethod.GET , value = "goMenu.do")
	@ResponseBody
	public ResponseEntity<JSONObject> goMenuCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: goMenu :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setUserType(loginVO.getUserType());

				List<MenuVO> result= menuService.getMenuMainList(vo);
				if(result.size()>0) {
					jsonObject.put("data", result.get(0));
					success = true;
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method=RequestMethod.GET, value="goMenuMain.do")
	public ModelAndView goMenuMain(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) {
		String redirectUri = "login/login";
		ModelAndView view = new ModelAndView();
		logger.info(":::::::::::::::::::: menu mainPage loading :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				redirectUri = "login/login";
			} else {
				logger.info("사용자명=>"+loginVO.getBizId() + " - " + loginVO.getUserName());
				if(StringUtil.isNotNull(vo.getMenuUrl())) {
					redirectUri = vo.getMenuUrl();
				} else {
					List<MenuVO> result= menuService.getMenuList(vo);
					if(result.size()>0) {
						redirectUri = "menu/main";
						view.addObject("data", result);
						view.setViewName(redirectUri);

					}
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		return view;
	}

	@RequestMapping(method = RequestMethod.POST , value = "insMenu.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insMenuCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: insMenu :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				menuService.insMenu(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "delMenu.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delMenuCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: delMenu :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				menuService.delMenu(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updMenu.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updMenuCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: updMenu :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				menuService.updMenu(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "insMenuPerm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insMenuPermCtrl(@ModelAttribute("MenuPermVO") MenuPermVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: insMenuPerm :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				menuService.insMenuPerm(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "delMenuPerm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delMenuPermCtrl(@ModelAttribute("MenuPermVO") MenuPermVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: delMenuPerm :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				menuService.delMenuPerm(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updMenuPerm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updMenuPermCtrl(@ModelAttribute("MenuPermVO") MenuPermVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: updMenuPerm :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				menuService.updMenuPerm(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getMenuMainList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMenuMainListCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 메인메뉴리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		boolean isOpenVoucher = false;

		try {
			logger.info(":::::::::::::::::::: getMenuMainList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setUserType(loginVO.getUserType());
				vo.setUseYn("Y");

				List<MenuVO> result= menuService.getMenuMainList(vo);

				BizVO getBizVo = new BizVO();
				getBizVo.setBizId(loginVO.getBizId());

				List<BizVO> resultBiz = bizService.getBizList(getBizVo);

				if (resultBiz != null && resultBiz.size() != 0) {
					BizVO resultBizVo = resultBiz.get(0);
					//바우쳐
					isOpenVoucher = (resultBizVo.getOpenVoucherType() == "1");
				}

				//result에서 근태, 급여를 삭제한다.
				if (isOpenVoucher) {
					//근태 급여 메뉴ID 리스트
					String[] menuHideCode = {"181016114856182","181016114850181","190105163114159","190105163034157"};

					for(int i = 0; i < result.size(); i++) {
						if (Arrays.asList(menuHideCode).contains(result.get(i).getMenuCode())) {
							result.remove(i);
						}
					}
				}

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getMenuMngList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMenuMngListCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 메뉴관리 리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getMenuMngList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				List<MenuVO> result= menuService.getMenuList(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getMenuTreeList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMenuTreeListCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 메뉴관리 treeview 조회용
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getMenuTreeList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				List<TreeVO> result = new ArrayList<TreeVO>();
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setPmenuCode("ROOT");
				vo.setMenuLvl("0");
				List<MenuVO> resultLvl0= menuService.getMenuList(vo);
				// 서비스 메뉴 조회
				for(int i=0;i<resultLvl0.size();i++) {
					MenuVO menuLvl0VO = resultLvl0.get(i);
					TreeVO treeLvl0VO = new TreeVO();
					treeLvl0VO.setLabel(menuLvl0VO.getMenuName());
					treeLvl0VO.setItemId(menuLvl0VO.getMenuCode());
					treeLvl0VO.setItemLv("Lvl - "+menuLvl0VO.getMenuLvl());
					treeLvl0VO.setParentId("0");
					treeLvl0VO.setUrl(menuLvl0VO.getMenuCode());

					// 하위메뉴 존재시 조회
					if(menuLvl0VO.getChildCnt()>0) {

						// Level 1 조회
						vo.setPmenuCode(menuLvl0VO.getMenuCode());
						vo.setMenuLvl("1");
						List<MenuVO> resultLvl1= menuService.getMenuList(vo);
						List<TreeVO> result1 = new ArrayList<TreeVO>();
						for(int j=0;j<resultLvl1.size();j++) {
							MenuVO menuLvl1VO = resultLvl1.get(j);
							TreeVO treeLvl1VO = new TreeVO();
							treeLvl1VO.setLabel(menuLvl1VO.getMenuName());
							treeLvl1VO.setItemId(menuLvl1VO.getMenuCode());
							treeLvl1VO.setItemLv("Lvl - "+menuLvl1VO.getMenuLvl());
							treeLvl1VO.setParentId(menuLvl1VO.getPmenuCode());
							treeLvl1VO.setUrl(menuLvl1VO.getMenuCode());

							// 하위메뉴 존재시 조회
							if(menuLvl1VO.getChildCnt()>0) {

								vo.setPmenuCode(menuLvl1VO.getMenuCode());
								vo.setMenuLvl("2");
								List<MenuVO> resultLvl2= menuService.getMenuList(vo);
								List<TreeVO> result2 = new ArrayList<TreeVO>();

								for(int k=0;k<resultLvl2.size();k++) {
									MenuVO menuLvl2VO = resultLvl2.get(k);
									TreeVO treeLvl2VO = new TreeVO();
									treeLvl2VO.setLabel(menuLvl2VO.getMenuName());
									treeLvl2VO.setItemId(menuLvl2VO.getMenuCode());
									treeLvl2VO.setItemLv("Lvl - "+menuLvl2VO.getMenuLvl());
									treeLvl2VO.setParentId(menuLvl2VO.getPmenuCode());
									treeLvl2VO.setUrl(menuLvl2VO.getMenuCode());

									result2.add(treeLvl2VO);
								}
								treeLvl1VO.setChildren(result2);
							}

							result1.add(treeLvl1VO);
						}
						treeLvl0VO.setChildren(result1);
					}
					result.add(treeLvl0VO);
				}

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getMenuTreeUserList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMenuTreeUserListCtrl(@ModelAttribute("MenuVO") MenuVO vo, HttpServletRequest request) throws Exception
	{
		// 메뉴관리 treeview 조회용
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getMenuTreeUserList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				List<TreeVO> result = new ArrayList<TreeVO>();
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setPmenuCode("ROOT");
				vo.setMenuLvl("0");
				List<MenuVO> resultLvl0= menuService.getMenuMainList(vo);
				// 서비스 메뉴 조회
				for(int i=0;i<resultLvl0.size();i++) {
					MenuVO menuLvl0VO = resultLvl0.get(i);
					TreeVO treeLvl0VO = new TreeVO();
					treeLvl0VO.setLabel(menuLvl0VO.getMenuName());
					treeLvl0VO.setItemId(menuLvl0VO.getMenuCode());
					treeLvl0VO.setItemLv("Lvl - "+menuLvl0VO.getMenuLvl());
					treeLvl0VO.setParentId("0");
					treeLvl0VO.setUrl(menuLvl0VO.getMenuCode());

					// 하위메뉴 존재시 조회
					if(menuLvl0VO.getChildCnt()>0) {

						// Level 1 조회
						vo.setPmenuCode(menuLvl0VO.getMenuCode());
						vo.setMenuLvl("1");
						List<MenuVO> resultLvl1= menuService.getMenuMainList(vo);
						List<TreeVO> result1 = new ArrayList<TreeVO>();
						for(int j=0;j<resultLvl1.size();j++) {
							MenuVO menuLvl1VO = resultLvl1.get(j);
							TreeVO treeLvl1VO = new TreeVO();
							treeLvl1VO.setLabel(menuLvl1VO.getMenuName());
							treeLvl1VO.setItemId(menuLvl1VO.getMenuCode());
							treeLvl1VO.setItemLv("Lvl - "+menuLvl1VO.getMenuLvl());
							treeLvl1VO.setParentId(menuLvl1VO.getPmenuCode());
							treeLvl1VO.setUrl(menuLvl1VO.getMenuCode());

							// 하위메뉴 존재시 조회
							if(menuLvl1VO.getChildCnt()>0) {

								vo.setPmenuCode(menuLvl1VO.getMenuCode());
								vo.setMenuLvl("2");
								List<MenuVO> resultLvl2= menuService.getMenuMainList(vo);
								List<TreeVO> result2 = new ArrayList<TreeVO>();

								for(int k=0;k<resultLvl2.size();k++) {
									MenuVO menuLvl2VO = resultLvl2.get(k);
									TreeVO treeLvl2VO = new TreeVO();
									treeLvl2VO.setLabel(menuLvl2VO.getMenuName());
									treeLvl2VO.setItemId(menuLvl2VO.getMenuCode());
									treeLvl2VO.setItemLv("Lvl - "+menuLvl2VO.getMenuLvl());
									treeLvl2VO.setParentId(menuLvl2VO.getPmenuCode());
									treeLvl2VO.setUrl(menuLvl2VO.getMenuCode());

									result2.add(treeLvl2VO);
								}
								treeLvl1VO.setChildren(result2);
							}

							result1.add(treeLvl1VO);
						}
						treeLvl0VO.setChildren(result1);
					}
					result.add(treeLvl0VO);
				}

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getMenuPermList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMenuPermListCtrl(@ModelAttribute("MenuPermVO") MenuPermVO vo, HttpServletRequest request) throws Exception
	{
		// 메뉴관리 리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getMenuPermList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				List<MenuPermVO> result= menuService.getMenuPermList(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

}
