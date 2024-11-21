package com.ezsign.user.dao;

import com.ezsign.user.vo.DeviceVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("deviceDAO")
public class DeviceDAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public List<DeviceVO> getDeviceList(DeviceVO vo) {
        return (List<DeviceVO>) list("deviceDAO.getDeviceList", vo);
    }

    public void insDevice(DeviceVO vo) {
        insert("deviceDAO.insDevice", vo);
    }

    public int updDevice(DeviceVO vo) {
        return update("deviceDAO.updDevice", vo);
    }
    
    public String getCandyCashVersion() {
		return (String)getSqlMapClientTemplate().queryForObject("deviceDAO.getCandyCashVersion");
	}
}
