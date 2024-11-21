package com.ezsign.user.service.impl;

import com.ezsign.user.dao.DeviceDAO;
import com.ezsign.user.service.DeviceService;
import com.ezsign.user.vo.DeviceVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceService")
public class DeviceServiceImpl extends AbstractServiceImpl implements DeviceService {

    @Resource(name = "deviceDAO")
    private DeviceDAO deviceDAO;

    @Override
    public List<DeviceVO> getDeviceList(DeviceVO vo) {
        return deviceDAO.getDeviceList(vo);
    }

    @Override
    public void insDevice(DeviceVO vo) {
        deviceDAO.insDevice(vo);
    }

    @Override
    public int updDevice(DeviceVO vo) {
        return deviceDAO.updDevice(vo);
    }
}
