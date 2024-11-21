package com.ezsign.push.vo;

public class ReturnResult {
	
	public static class Result {
		/** 애플리케이션에 전달할 성공 메세지 명 */
		public static final boolean SUCCESS_CODE   = true;
		
		/** 애플리케이션에 전달할 실패 메세지 명 */
		public static final boolean FAILURE_CODE   = false;
		
	}
	
	public static class ResultCode {
		
		/** 에러없음 */
		public static final int NOERROR   = 0;		
		
		/** 사용자 ID가 없음 */
		public static final int USERID_NOTFOUND   = 1;
		
		/** Biz ID가 없음 */
		public static final int BIZID_NOTFOUND   = 2;
		
		/** 푸시 호출 실패 */
		public static final int PUSH_CALL_FAIL  = 3;
		
		/** PUSHTOKEN 없음 */
		public static final int PUSHTOKEN_NOTFOUND  = 4;

		/** DB 조회 에러 */
		public static final int DB_SELECT_ERROR   = 5;
		
		/** DB 저장 에러 */
		public static final int DB_INSERT_ERROR   = 6;
		
		/** DB 수정 에러 */
		public static final int DB_UPDATE_ERROR   = 7;
		
		/** DB 삭제 에러 */
		public static final int DB_DELETE_ERROR   = 8;
		
		/** PUSH - PUSH대상이 없음 */
		public static final int PUSH_TO_NOTFOUND   = 7;
		
		/** PUSH - PUSH타입이 없음 */
		public static final int PUSH_TO_TYPE_NOTFOUND   = 10;
		
		/** ARGUMENTS가 없음 */
		public static final int ARGUMENTS_NOTFOUND   = 11;
		
		/** PARAMETER가 없음 */
		public static final int PARAMETER_NOTFOUND   = 12;
		
		/** PUSH ID가 없음 */
		public static final int PUSHID_NOTFOUND   = 13;
		
		/** DEVICE ID가 없음 */
		public static final int DEVICEID_NOTFOUND   = 14;
		
		/** 권한 없음 */
		public static final int NO_PERMISSION   = 15;
		
		/** 검색 결과 없음*/
		public static final int SELECT_NOTFOUND   = 16;
		
	}
	
	public static String ToString(int ERR)
    {
        switch (ERR)
        {
            case 0: return "에러가 없음";
            case 1: return "사용자 ID가 없음";
            case 2: return "Biz ID가 없음";
            case 3: return "푸시 호출 실패";
            case 4: return "PUSHTOKEN 없음";
            case 5: return "DB 조회 에러";
            case 6: return "DB 저장 에러";
            case 7: return "DB 수정 에러";
            case 8: return "DB 삭제 에러";
            case 9: return "PUSH대상이 없음";
            case 10: return "PUSH타입이 없음";
            case 11: return "ARGUMENTS가 없음";
            case 12: return "PARAMETER가 없음";
            case 13: return "PUSH ID가 없음";
            case 14: return "DEVICE ID가 없음";
            case 15: return "권한 없음";
            case 16: return "검색 결과 없음";

            default: return "unknown";
        }
    }
}
