
package com.ezsign.welmoney.service.impl;


import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biztalk.dao.BizTalkDAO;
import com.ezsign.biztalk.vo.BizTalkKKOVO;
import com.ezsign.content.service.ContentService;
import com.ezsign.contract.service.ContractService;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.dao.DeviceDAO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.service.UserService;
import com.ezsign.welmoney.dao.WelMoneyUserDAO;
import com.ezsign.welmoney.service.WelMoneyUserService;
import com.ezsign.welmoney.vo.WelMoneyUserVO;
import com.ezsign.user.vo.*;
import com.jarvis.common.util.FileUtil;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("welMoneyUserService")
public class WelMoneyUserServiceImpl extends AbstractServiceImpl implements WelMoneyUserService {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "contentService")
    private ContentService contentService;

    @Resource(name = "contractService")
    private ContractService contractService;

    @Resource(name = "bizDAO")
    private BizDAO bizDAO;

    @Resource(name = "userDAO")
    private UserDAO userDAO;

    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "bizTalkDAO")
    private BizTalkDAO bizTalkDAO;

    @Resource(name = "pointDAO")
    private PointDAO pointDAO;

    @Resource(name = "ys000DAO")
    private YS000DAO ys000DAO;

    @Resource(name = "deviceDAO")
    private DeviceDAO deviceDAO;
    
    @Resource(name = "welMoneyUserDAO")
    private WelMoneyUserDAO welMoneyUserDAO;

    @Override
    public SessionVO loginProcess(WelMoneyUserVO vo) throws Exception {
        SessionVO loginVO = new SessionVO();
        WelMoneyUserVO userVO = new WelMoneyUserVO();

        userVO = welMoneyUserDAO.loginProcess(vo);

        if (userVO == null) return null;
        System.out.println("login result : " + userVO.getUserId());

        String userPwd = userVO.getUserPwd();
        
        String encPwd = SecurityUtil.encrypt(vo.getUserPwd());

        //System.out.println(userPwd+"^"+encPwd+"^"+vo.getUserPwd());
        if (userPwd.equals(encPwd)) {
            System.out.println(userVO.getUserId() + "패스워드 일치");
            loginVO.setLoginId(userVO.getUserId());

        } else {
        
            loginVO = null;
        }


        return loginVO;
    }

    @Override
	public List<WelMoneyUserVO> getMemberList(WelMoneyUserVO vo)  throws Exception {
		List<WelMoneyUserVO> list = null;
		
		list = welMoneyUserDAO.getMemberList(vo);
		
		return list;
	}

	@Override
	public int getMemberListCount(WelMoneyUserVO vo) throws Exception {
		return welMoneyUserDAO.getMemberListCount(vo);
	}
	
	@Override
	public WelMoneyUserVO getMember(WelMoneyUserVO vo) throws Exception {
		return welMoneyUserDAO.getMember(vo);
	}
	
	@Override
	public boolean insMember(WelMoneyUserVO vo) throws Exception {

		WelMoneyUserVO resultVO = new WelMoneyUserVO();
		resultVO = welMoneyUserDAO.getMember(vo);
		if(resultVO==null) {
			welMoneyUserDAO.insMember(vo);
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
	public int updMember(WelMoneyUserVO vo) throws Exception {
		return welMoneyUserDAO.updMember(vo);
	}
	
	@Override
	public void delMember(WelMoneyUserVO vo) throws Exception {
		welMoneyUserDAO.delMember(vo);
	}
}
