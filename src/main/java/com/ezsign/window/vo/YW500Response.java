package com.ezsign.window.vo;

@SuppressWarnings("NonAsciiCharacters")
public class YW500Response {

    public boolean success;

    public WorkMonth 근무년월;

    public boolean 설문지_주택마련저축_입력여부 = false;
    public boolean 설문지_주택마련저축_적용여부 = false;
    public boolean 설문지_주택임차차입금_입력여부 = false;
    public boolean 설문지_주택임차차입금_적용여부 = false;
    public boolean 설문지_장기주택저당차입금_입력여부 = false;
    public boolean 설문지_장기주택저당차입금_적용여부 = false;
    public boolean 설문지_월세액공제_입력여부 = false;
    public boolean 설문지_월세액공제_적용여부 = false;

    public final MapItem 근로자_국세청자료 = new MapItem();
    public final MapItem 근로자_기타자료 = new MapItem();
    public final MapItem 근로자_합계 = new MapItem();

    public final MapItem 관리자_국세청자료 = new MapItem();
    public final MapItem 관리자_기타자료 = new MapItem();
    public final MapItem 관리자_합계 = new MapItem();

    public final MapComment 수정내용 = new MapComment();

    public static class MapItem {
        public int 연금보험료공제 = 0;
        public int 연금보험료공제_국민연금보험료 = 0;
        public int 연금보험료공제_공적연금보험료 = 0;

        public int 특별소득공제 = 0;
        public int 특별소득공제_국민건강보험료 = 0;
        public int 특별소득공제_장기요양보험료 = 0;
        public int 특별소득공제_고용보험료 = 0;
        public int 특별소득공제_주택임차차입금원리금상환액 = 0;
        public int 특별소득공제_주택임차차입금원리금상환액_대출기관 = 0;
        public int 특별소득공제_주택임차차입금원리금상환액_거주자 = 0;
        public int 특별소득공제_장기주택저당차입금이자상환액 = 0;
        public int 특별소득공제_기부금이월분 = 0;

        public int 개인연금저축 = 0;
        public int 주택마련저축 = 0;
        public int 신용카드 = 0;
        public int 장기집합투자증권저축 = 0;

        public int 그밖의소득공제 = 0;
        public int 그밖의소득공제_소기업소상공인공제부금 = 0;
        public int 그밖의소득공제_투자조합출자등 = 0;
        public int 그밖의소득공제_우리사주조합출연금 = 0;
        public int 그밖의소득공제_고용유지중소기업근로자 = 0;

        public int 연금계좌 = 0;
        public int 연금계좌_과학기술인공제 = 0;
        public int 연금계좌_퇴직연금 = 0;
        public int 연금계좌_연금저축 = 0;

        public int 특별세액공제 = 0;
        public int 특별세액공제_보험료 = 0;
        public int 특별세액공제_보험료_보장성 = 0;
        public int 특별세액공제_보험료_장애인전용보장성 = 0;
        public int 특별세액공제_의료비안경구입비 = 0;
        public int 특별세액공제_의료비안경구입비_본인65세이상자장애인 = 0;
        public int 특별세액공제_의료비안경구입비_난임시술비 = 0;
        public int 특별세액공제_의료비안경구입비_그밖의공제대상자 = 0;
        public int 특별세액공제_교육비교복구입비체험학습비 = 0;
        public int 특별세액공제_교육비교복구입비체험학습비_본인 = 0;
        public int 특별세액공제_교육비교복구입비체험학습비_취학전아동 = 0;
        public int 특별세액공제_교육비교복구입비체험학습비_초중고등학교 = 0;
        public int 특별세액공제_교육비교복구입비체험학습비_대학생 = 0;
        public int 특별세액공제_교육비교복구입비체험학습비_장애인 = 0;
        public int 특별세액공제_기부금 = 0;
        public int 특별세액공제_기부금_정치자금기부금 = 0;
        public int 특별세액공제_기부금_법정기부금 = 0;
        public int 특별세액공제_기부금_우리사주기부금 = 0;
        public int 특별세액공제_기부금_종교단체외지정기부금 = 0;
        public int 특별세액공제_기부금_종교단체지정기부금 = 0;

        public int 월세액 = 0;
        public int 주택차입금 = 0;
        public int 세액감면 = 0;
        public int 외국납부세액 = 0;
    }

    public static class MapComment {
        public String 연금보험료공제 = "";
        public String 연금보험료공제_국민연금보험료 = "";
        public String 연금보험료공제_공적연금보험료 = "";

        public String 특별소득공제 = "";
        public String 특별소득공제_국민건강보험료 = "";
        public String 특별소득공제_장기요양보험료 = "";
        public String 특별소득공제_고용보험료 = "";
        public String 특별소득공제_주택임차차입금원리금상환액 = "";
        public String 특별소득공제_주택임차차입금원리금상환액_대출기관 = "";
        public String 특별소득공제_주택임차차입금원리금상환액_거주자 = "";
        public String 특별소득공제_주택임차차입금원리금상환액_소득공제명세 = "";
        public String 특별소득공제_장기주택저당차입금이자상환액 = "";
        public String 특별소득공제_기부금이월분 = "";

        public String 개인연금저축 = "";
        public String 주택마련저축 = "";
        public String 신용카드 = "";
        public String 장기집합투자증권저축 = "";

        public String 그밖의소득공제 = "";
        public String 그밖의소득공제_소기업소상공인공제부금 = "";
        public String 그밖의소득공제_투자조합출자등 = "";
        public String 그밖의소득공제_우리사주조합출연금 = "";
        public String 그밖의소득공제_고용유지중소기업근로자 = "";

        public String 연금계좌 = "";
        public String 연금계좌_과학기술인공제 = "";
        public String 연금계좌_퇴직연금 = "";
        public String 연금계좌_연금저축 = "";

        public String 특별세액공제 = "";
        public String 특별세액공제_보험료 = "";
        public String 특별세액공제_보험료_보장성 = "";
        public String 특별세액공제_보험료_장애인전용보장성 = "";
        public String 특별세액공제_의료비안경구입비 = "";
        public String 특별세액공제_의료비안경구입비_본인65세이상자장애인 = "";
        public String 특별세액공제_의료비안경구입비_난임시술비 = "";
        public String 특별세액공제_의료비안경구입비_그밖의공제대상자 = "";
        public String 특별세액공제_교육비교복구입비체험학습비 = "";
        public String 특별세액공제_교육비교복구입비체험학습비_본인 = "";
        public String 특별세액공제_교육비교복구입비체험학습비_취학전아동 = "";
        public String 특별세액공제_교육비교복구입비체험학습비_초중고등학교 = "";
        public String 특별세액공제_교육비교복구입비체험학습비_대학생 = "";
        public String 특별세액공제_교육비교복구입비체험학습비_장애인 = "";
        public String 특별세액공제_기부금 = "";
        public String 특별세액공제_기부금_정치자금기부금 = "";
        public String 특별세액공제_기부금_법정기부금 = "";
        public String 특별세액공제_기부금_우리사주기부금 = "";
        public String 특별세액공제_기부금_종교단체외지정기부금 = "";
        public String 특별세액공제_기부금_종교단체지정기부금 = "";

        public String 월세액 = "";
        public String 주택차입금 = "";
        public String 세액감면 = "";
        public String 외국납부세액 = "";
    }
}
