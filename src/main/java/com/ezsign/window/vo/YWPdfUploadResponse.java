package com.ezsign.window.vo;

import com.ezsign.feb.worker.vo.YE001VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YWPdfUploadResponse {

    public boolean success;
    public String message;

    public WorkMonth 근무년월;

    public String xmlPath;
    public String 계약ID;
    public String 사용자ID;
    public List<YE001VO> 부양가족;
    public List<YE001VO> 추가부양가족;
    public List<ManItem> 인별항목;

    public static class ManItem {
        public String 이름;
        public String 개인식별번호 = null;

        public int 건강보험_보수월액 = 0;
        public int 건강보험_소득월액 = 0;
        public int 장기요양보험_보수월액 = 0;
        public int 장기요양보험_소득월액 = 0;
        public int 국민연금_직장 = 0;
        public int 국민연금_지역 = 0;
        public int 국민연금외_공적연금 = 0;        
        public int 신용카드_일반 = 0;
        public int 신용카드_전통시장 = 0;
        public int 신용카드_대중교통 = 0;
        public int 신용카드_도서공연 = 0;
        public int 직불카드_일반 = 0;
        public int 직불카드_전통시장 = 0;
        public int 직불카드_대중교통 = 0;
        public int 직불카드_도서공연 = 0;
        public int 현금영수증_일반 = 0;
        public int 현금영수증_전통시장 = 0;
        public int 현금영수증_대중교통 = 0;
        public int 현금영수증_도서공연 = 0;
        public int 보험료_일반보장성 = 0;
        public int 보험료_장애인보장성 = 0;
        public int 의료비_의료비 = 0;
        public int 의료비_안경구입비 = 0;
        public int 교육비_교육비_직업훈련비 = 0;
        public int 교육비_교복구입비 = 0;
        public int 교육비_체험학습비 = 0;
        public int 교육비_장애인특수 = 0;
        public int 기부금_공제대상 = 0;
        public int 기부금_기부장려 = 0;
        public int 개인연금저축_연금저축_퇴직연금 = 0;
        public int 소기업소상공인공제부금 = 0;
        public int 장기집합투자증권저축 = 0;
        public int 주택자금 = 0;
        public int 주택마련저축 = 0;
        public int 지출액합계 = 0;
    }
}
