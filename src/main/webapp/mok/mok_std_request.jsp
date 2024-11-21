<%@ page import="java.util.UUID" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.logging.SimpleFormatter" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.dreamsecurity.mobileOK.mobileOKKeyManager" %>
<%@ page import="com.dreamsecurity.mobileOK.MobileOKException" %>
<%@ page import="com.dreamsecurity.json.JSONObject" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String id = request.getParameter("id");
%>
<%!
    public String mobileOK_std_request(mobileOKKeyManager mobileOK, HttpSession session, String clientPrefix) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        /* 1. 본인확인-표준창 거래요청정보 생성  */

        /* 1.1 이용기관 거래ID 생성, 20자 이상 40자 이내 이용기관 고유 트랜잭션ID (예시) 이용기관식별자+UUID, ...  */
        // - 본인확인-표준창 거래ID 는 유일한 값이어야 하며 기 사용한 거래ID가 있는 경우 오류 발생
        // - 이용기관이 고유식별 ID로 유일성을 보장할 경우 고객이 이용하는 ID사용 가능
        String clientTxId = clientPrefix + UUID.randomUUID().toString().replaceAll("-", "");

        /* 1.2 인증 결과 검증을 위한 거래 ID 세션 저장 (권고) */
        // 동일한 세션내 요청과 결과가 동일한지 확인 및 인증결과 재사용 방지처리, "MOKResult" 응답결과 처리시 필수 구현
        session.setAttribute("sessionClientTxId", clientTxId);

        /* 1.3 거래 ID, 인증 시간을 통한 본인확인 거래 요청 정보 생성  */
        String reqClientInfo = clientTxId + "|" + formatter.format(cal.getTime());

        /* 1.4 생성된 거래정보 암호화 */
        String encryptReqClientInfo;
        try                         { encryptReqClientInfo = mobileOK.RSAEncrypt(reqClientInfo); }
        catch (MobileOKException e) { return e.getErrorCode() + "|" + e.getMessage(); }

        /* 1.5 거래 요청 정보 JSON 생성 */
        JSONObject jsonObject = new JSONObject();

        /* 본인확인 서비스 용도 */
        /* 01001 : 회원가입, 01002 : 정보변경, 01003 : ID찾기, 01004 : 비밀번호찾기, 01005 : 본인확인용, 01006 : 성인인증, 01007 : 상품구매/결제, 01999 : 기타 */
        jsonObject.put("usageCode", "01005");

        /* 본인확인 이용기관 서비스 ID (키파일에 serviceId 포함 됨) */
        jsonObject.put("serviceId", mobileOK.getServiceId());

        /* 암호화된 본인확인 거래 요청 정보 */
        jsonObject.put("encryptReqClientInfo", encryptReqClientInfo);

        /* 이용상품 코드 */
        /* 이용상품 코드, telcoAuth : 휴대폰본인확인 (SMS인증시 인증번호 발송 방식 “SMS”)*/
        /* 이용상품 코드, telcoAuth-LMS : 휴대폰본인확인 (SMS인증시 인증번호 발송 방식 “LMS”)*/
        jsonObject.put("serviceType", "telcoAuth");

        /* 본인확인 결과 타입 */
        /* 본인확인 결과 타입, "MOKToken"  : 개인정보 응답결과를 이용기관 서버에서 본인확인 서버에 요청하여 수신 후 처리 */
        jsonObject.put("retTransferType", "MOKToken");

        /* 본인확인 결과 수신 URL "https://" 포함한 URL 입력 */
        // 개발 서버
        // String url = "https://ieumsign.newzenpnp.co.kr/mok/mok_std_result.jsp";
        // 운영 서버
        String url = "https://ieumsign.co.kr/mok/mok_std_result.jsp";



        jsonObject.put("returnUrl", url); // 결과 수신 후 전달 URL 설정
        /* 1.6 거래 요청 정보 JSON 반환 */
        return jsonObject.toString();
    }
%>
<%
    /* 2. 본인확인 서비스 API 설정 */
    try {
        mobileOKKeyManager mobileOK = new mobileOKKeyManager();
        /* 키파일은 반드시 서버의 안전한 로컬경로에 별도 저장. 웹URL 경로에 파일이 있을경우 키파일이 외부에 노출될 수 있음 주의 */
        mobileOK.keyInit("E:\\dreamKey/mok_keyInfo.dat", "dream");

        // 이용기관 거래ID생성시 이용기관별 유일성 보장을 위해 설정, 이용기관식별자는 이용기관코드 영문자로 반드시 수정
        String clientPrefix =  "NEWZEN";  // 8자이내 영대소문자,숫자 (예) MOK, TESTCOKR

        String jsonResult = mobileOK_std_request(mobileOK, session, clientPrefix);

        JSONObject resultJson = new JSONObject(jsonResult);

        /* 3. 거래 정보 응답 */
        out.write(resultJson.toString());
    } catch (MobileOKException e) {
        out.write(e.getErrorCode() + "|" + e.getMessage());
    }
%>