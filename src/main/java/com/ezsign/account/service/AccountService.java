package com.ezsign.account.service;

import java.util.List;

import com.ezsign.account.vo.AccountVO;

public interface AccountService {

	public int leaveAccount(AccountVO vo) throws Exception;

	public int leaveUser(AccountVO vo) throws Exception;

	public int leaveSelectedUser(List<AccountVO> accountList) throws Exception;
	
}
