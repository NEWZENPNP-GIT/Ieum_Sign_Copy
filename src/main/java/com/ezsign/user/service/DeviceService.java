package com.ezsign.user.service;

import com.ezsign.user.vo.DeviceVO;

import java.util.List;

public interface DeviceService {

    List<DeviceVO> getDeviceList(DeviceVO vo);

    void insDevice(DeviceVO vo);

    int updDevice(DeviceVO vo);
}
