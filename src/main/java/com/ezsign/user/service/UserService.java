package com.ezsign.user.service;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.vo.*;

import java.util.List;

public interface UserService {

    SessionVO loginMobileProcess(DeviceVO vo) throws Exception;

    SessionVO loginProcess(UserVO vo) throws Exception;

    SessionVO XloginXProcess(UserVO vo) throws Exception;

    void logOutProcess(SessionVO vo) throws Exception;

    int updUserPwd(UserVO vo) throws Exception;

    int resetUserPwd(UserVO vo) throws Exception;
    
    int updMobileUserPwd(UserVO vo) throws Exception;

    int insUser(UserVO vo) throws Exception;

    int insUserAdmin(UserVO vo) throws Exception;

    int updUser(UserVO vo) throws Exception;

    List<UserVO> getUserList(UserVO vo) throws Exception;

    List<UserVO> findUser(UserVO vo) throws Exception;

    void insUserCert(UserCertVO vo) throws Exception;

    void insUserMok(UserMokVO vo) throws Exception;

    void insUserJoinCert(UserJoinCertVO vo) throws Exception;

    int getUserJoinCert(UserJoinCertVO vo) throws Exception;

    int insUserGrpBiz(BizVO vo) throws Exception;

    void insUserGrp(UserGrpVO vo) throws Exception;

    void delUserGrp(UserGrpVO vo) throws Exception;

    List<UserGrpVO> getUserGrp(UserGrpVO vo) throws Exception;

    List<UserGrpVO> getUserGrpList(UserGrpVO vo) throws Exception;

    String getUserNonce();

	int resetUserPwdByDate(UserVO vo) throws Exception;

    int delUserLoginInfo(UserVO vo) throws Exception;
    
    public Boolean isUserPwd(UserVO vo) throws Exception;
    
    
}
