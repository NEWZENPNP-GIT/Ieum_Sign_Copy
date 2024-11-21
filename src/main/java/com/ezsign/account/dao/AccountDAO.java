package com.ezsign.account.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.account.vo.AccountVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("accountDAO")
public class AccountDAO extends EgovAbstractDAO {
	
	
	@SuppressWarnings("unchecked")
	public List<AccountVO> getDelContentList(AccountVO vo) {
		return (List<AccountVO>)list("accountDAO.getDelContentList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<AccountVO> getDelUserContentList(AccountVO vo) {
		return (List<AccountVO>)list("accountDAO.getDelUserContentList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<AccountVO> getDelFileList(AccountVO vo) {
		return (List<AccountVO>)list("accountDAO.getDelFileList", vo);
	}

	public int delContentMeta(AccountVO vo) {
		return delete("accountDAO.delContentMeta", vo);
	}

	public int delAccountData1(AccountVO vo) {
		return delete("accountDAO.delAccountData1", vo);
	}

	public int delAccountData2(AccountVO vo) {
		return delete("accountDAO.delAccountData2", vo);
	}

	public int delAccountData3(AccountVO vo) {
		return delete("accountDAO.delAccountData3", vo);
	}

	public int delAccountData4(AccountVO vo) {
		return delete("accountDAO.delAccountData4", vo);
	}

	public int delAccountData5(AccountVO vo) {
		return delete("accountDAO.delAccountData5", vo);
	}

	public int delAccountData6(AccountVO vo) {
		return delete("accountDAO.delAccountData6", vo);
	}

	public int delAccountData7(AccountVO vo) {
		return delete("accountDAO.delAccountData7", vo);
	}

	public int delAccountData8(AccountVO vo) {
		return delete("accountDAO.delAccountData8", vo);
	}

	public int delAccountData9(AccountVO vo) {
		return delete("accountDAO.delAccountData9", vo);
	}

	public int delAccountData10(AccountVO vo) {
		return delete("accountDAO.delAccountData10", vo);
	}

	public int delAccountData11(AccountVO vo) {
		return delete("accountDAO.delAccountData11", vo);
	}

	public int delAccountData12(AccountVO vo) {
		return delete("accountDAO.delAccountData12", vo);
	}

	public int delAccountData13(AccountVO vo) {
		return delete("accountDAO.delAccountData13", vo);
	}

	public int delAccountData14(AccountVO vo) {
		return delete("accountDAO.delAccountData14", vo);
	}

	public int delAccountData15(AccountVO vo) {
		return delete("accountDAO.delAccountData15", vo);
	}

	public int delAccountData16(AccountVO vo) {
		return delete("accountDAO.delAccountData16", vo);
	}

	public int delAccountData17(AccountVO vo) {
		return delete("accountDAO.delAccountData17", vo);
	}

	public int delAccountData18(AccountVO vo) {
		return delete("accountDAO.delAccountData18", vo);
	}

	public int delAccountData19(AccountVO vo) {
		return delete("accountDAO.delAccountData19", vo);
	}

	public int delAccountData20(AccountVO vo) {
		return delete("accountDAO.delAccountData20", vo);
	}

	public int delAccountData21(AccountVO vo) {
		return delete("accountDAO.delAccountData21", vo);
	}

	public int delAccountData22(AccountVO vo) {
		return delete("accountDAO.delAccountData22", vo);
	}

	public int delAccountData23(AccountVO vo) {
		return delete("accountDAO.delAccountData23", vo);
	}

	public int delAccountData24(AccountVO vo) {
		return delete("accountDAO.delAccountData24", vo);
	}

	public int delAccountData25(AccountVO vo) {
		return delete("accountDAO.delAccountData25", vo);
	}

	public int delAccountData26(AccountVO vo) {
		return delete("accountDAO.delAccountData26", vo);
	}

	public int delAccountData27(AccountVO vo) {
		return delete("accountDAO.delAccountData27", vo);
	}

	public int delAccountData28(AccountVO vo) {
		return delete("accountDAO.delAccountData28", vo);
	}

	public int delAccountData29(AccountVO vo) {
		return delete("accountDAO.delAccountData29", vo);
	}

	public int delAccountData30(AccountVO vo) {
		return delete("accountDAO.delAccountData30", vo);
	}

	public int delAccountData31(AccountVO vo) {
		return delete("accountDAO.delAccountData31", vo);
	}

	public int delAccountData32(AccountVO vo) {
		return delete("accountDAO.delAccountData32", vo);
	}

	public int delAccountData33(AccountVO vo) {
		return delete("accountDAO.delAccountData33", vo);
	}

	public int delAccountData34(AccountVO vo) {
		return delete("accountDAO.delAccountData34", vo);
	}

	public int delAccountData35(AccountVO vo) {
		return delete("accountDAO.delAccountData35", vo);
	}

	public int delAccountData36(AccountVO vo) {
		return delete("accountDAO.delAccountData36", vo);
	}

	public int delAccountData37(AccountVO vo) {
		return delete("accountDAO.delAccountData37", vo);
	}

	public int delAccountData38(AccountVO vo) {
		return delete("accountDAO.delAccountData38", vo);
	}

	public int delAccountData39(AccountVO vo) {
		return delete("accountDAO.delAccountData39", vo);
	}

	public int delAccountData40(AccountVO vo) {
		return delete("accountDAO.delAccountData40", vo);
	}

	public int delAccountData41(AccountVO vo) {
		return delete("accountDAO.delAccountData41", vo);
	}

	public int delAccountData42(AccountVO vo) {
		return delete("accountDAO.delAccountData42", vo);
	}

	public int delAccountData43(AccountVO vo) {
		return delete("accountDAO.delAccountData43", vo);
	}

	public int delAccountData44(AccountVO vo) {
		return delete("accountDAO.delAccountData44", vo);
	}

	public int delAccountData45(AccountVO vo) {
		return delete("accountDAO.delAccountData45", vo);
	}

	public int delAccountData46(AccountVO vo) {
		return delete("accountDAO.delAccountData46", vo);
	}

	public int delAccountData47(AccountVO vo) {
		return delete("accountDAO.delAccountData47", vo);
	}

	public int delAccountData48(AccountVO vo) {
		return delete("accountDAO.delAccountData48", vo);
	}

	public int delAccountData49(AccountVO vo) {
		return delete("accountDAO.delAccountData49", vo);
	}

	public int delAccountData50(AccountVO vo) {
		return delete("accountDAO.delAccountData50", vo);
	}

	public int delAccountData51(AccountVO vo) {
		return delete("accountDAO.delAccountData51", vo);
	}

	public int delAccountData52(AccountVO vo) {
		return delete("accountDAO.delAccountData52", vo);
	}

	public int delAccountData53(AccountVO vo) {
		return delete("accountDAO.delAccountData53", vo);
	}

	public int delAccountData54(AccountVO vo) {
		return delete("accountDAO.delAccountData54", vo);
	}

	public int delAccountData55(AccountVO vo) {
		return delete("accountDAO.delAccountData55", vo);
	}

	public int delAccountData56(AccountVO vo) {
		return delete("accountDAO.delAccountData56", vo);
	}

	public int delAccountData57(AccountVO vo) {
		return delete("accountDAO.delAccountData57", vo);
	}

	public int delAccountData58(AccountVO vo) {
		return delete("accountDAO.delAccountData58", vo);
	}

	public int delAccountData59(AccountVO vo) {
		return delete("accountDAO.delAccountData59", vo);
	}

	public int delAccountData60(AccountVO vo) {
		return delete("accountDAO.delAccountData60", vo);
	}

	public int delAccountData61(AccountVO vo) {
		return delete("accountDAO.delAccountData61", vo);
	}

	public int delAccountData62(AccountVO vo) {
		return delete("accountDAO.delAccountData62", vo);
	}

	public int delAccountData63(AccountVO vo) {
		return delete("accountDAO.delAccountData63", vo);
	}

	public int delAccountData64(AccountVO vo) {
		return delete("accountDAO.delAccountData64", vo);
	}

	public int delAccountData65(AccountVO vo) {
		return delete("accountDAO.delAccountData65", vo);
	}

	public int delAccountData66(AccountVO vo) {
		return delete("accountDAO.delAccountData66", vo);
	}

	public int delAccountData67(AccountVO vo) {
		return delete("accountDAO.delAccountData67", vo);
	}

	public int delAccountData68(AccountVO vo) {
		return delete("accountDAO.delAccountData68", vo);
	}

	public int delAccountData69(AccountVO vo) {
		return delete("accountDAO.delAccountData69", vo);
	}

	public int delAccountData70(AccountVO vo) {
		return delete("accountDAO.delAccountData70", vo);
	}

	public int delAccountData71(AccountVO vo) {
		return delete("accountDAO.delAccountData71", vo);
	}

	public int delAccountData72(AccountVO vo) {
		return delete("accountDAO.delAccountData72", vo);
	}

	public int delAccountData73(AccountVO vo) {
		return delete("accountDAO.delAccountData73", vo);
	}

	public int delAccountData74(AccountVO vo) {
		return delete("accountDAO.delAccountData74", vo);
	}

	public int delAccountData75(AccountVO vo) {
		return delete("accountDAO.delAccountData75", vo);
	}

	public int delAccountData76(AccountVO vo) {
		return delete("accountDAO.delAccountData76", vo);
	}

	public int delAccountData77(AccountVO vo) {
		return delete("accountDAO.delAccountData77", vo);
	}

	public int delAccountData78(AccountVO vo) {
		return delete("accountDAO.delAccountData78", vo);
	}

	public int delAccountData79(AccountVO vo) {
		return delete("accountDAO.delAccountData79", vo);
	}

	public int delAccountData80(AccountVO vo) {
		return delete("accountDAO.delAccountData80", vo);
	}

	public int delAccountData81(AccountVO vo) {
		return delete("accountDAO.delAccountData81", vo);
	}

	public int delAccountData82(AccountVO vo) {
		return delete("accountDAO.delAccountData82", vo);
	}

	public int delAccountData83(AccountVO vo) {
		return delete("accountDAO.delAccountData83", vo);
	}

	public int delAccountData84(AccountVO vo) {
		return delete("accountDAO.delAccountData84", vo);
	}

	public int delAccountData85(AccountVO vo) {
		return delete("accountDAO.delAccountData85", vo);
	}

	public int delAccountData86(AccountVO vo) {
		return delete("accountDAO.delAccountData86", vo);
	}

	public int delAccountData87(AccountVO vo) {
		return delete("accountDAO.delAccountData87", vo);
	}

	public int delAccountData88(AccountVO vo) {
		return delete("accountDAO.delAccountData88", vo);
	}

	public int delAccountData89(AccountVO vo) {
		return delete("accountDAO.delAccountData89", vo);
	}
	
	public int delAccountData90(AccountVO vo) {
		return delete("accountDAO.delAccountData90", vo);
	}

	public int delAccountData91(AccountVO vo) {
		return delete("accountDAO.delAccountData91", vo);
	}

	public int delAccountData92(AccountVO vo) {
		return delete("accountDAO.delAccountData92", vo);
	}

	public int delAccountData93(AccountVO vo) {
		return delete("accountDAO.delAccountData93", vo);
	}

	public int delAccountData94(AccountVO vo) {
		return delete("accountDAO.delAccountData94", vo);
	}

	public int delAccountData95(AccountVO vo) {
		return delete("accountDAO.delAccountData95", vo);
	}

	public int delAccountData96(AccountVO vo) {
		return delete("accountDAO.delAccountData96", vo);
	}

	public int delAccountData97(AccountVO vo) {
		return delete("accountDAO.delAccountData97", vo);
	}

	public int delAccountData98(AccountVO vo) {
		return delete("accountDAO.delAccountData98", vo);
	}

	public int delAccountData99(AccountVO vo) {
		return delete("accountDAO.delAccountData99", vo);
	}

	public int delAccountData100(AccountVO vo) {
		return delete("accountDAO.delAccountData100", vo);
	}

	public int delAccountData101(AccountVO vo) {
		return delete("accountDAO.delAccountData101", vo);
	}

	public int delAccountData102(AccountVO vo) {
		return delete("accountDAO.delAccountData102", vo);
	}

	public int delAccountData103(AccountVO vo) {
		return delete("accountDAO.delAccountData103", vo);
	}

	public int delAccountData104(AccountVO vo) {
		return delete("accountDAO.delAccountData104", vo);
	}

	public int delAccountData105(AccountVO vo) {
		return delete("accountDAO.delAccountData105", vo);
	}

	public int delAccountData106(AccountVO vo) {
		return delete("accountDAO.delAccountData106", vo);
	}

	public int delAccountData107(AccountVO vo) {
		return delete("accountDAO.delAccountData107", vo);
	}

	public int delAccountData108(AccountVO vo) {
		return delete("accountDAO.delAccountData108", vo);
	}

	public int delAccountData109(AccountVO vo) {
		return delete("accountDAO.delAccountData109", vo);
	}

	public int delAccountData110(AccountVO vo) {
		return delete("accountDAO.delAccountData110", vo);
	}

	public int delAccountData111(AccountVO vo) {
		return delete("accountDAO.delAccountData111", vo);
	}

	public int delAccountData112(AccountVO vo) {
		return delete("accountDAO.delAccountData112", vo);
	}

	public int delAccountData113(AccountVO vo) {
		return delete("accountDAO.delAccountData113", vo);
	}
	
}
