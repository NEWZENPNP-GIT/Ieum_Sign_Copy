<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="./common.jsp" %>
    <title>전자봉투 데이터 서명/검증</title>
</head>
<body>

<div class="container">
    <%@include file="header.jsp" %>
    <div class="col-md-9">
        <div><h3>Envelop / DecryptEnvelop </h3></div>

        <div class="panel panel-primary">
            <div class="panel-heading">Envelop될 원문 데이터</div>
            <div class="panel-body">
                <form onsubmit="return false" class="form-horizontal">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="testCert">인증서</label>
                        <div class="col-sm-9">
                            <textarea class="form-control" id="testCert" rows="5" cols="80"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="testData">데이터</label>
                        <div class="col-sm-9">
                            <textarea id="testData" rows="3" cols="80">Envelop용 샘플 데이터 입니다.</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="testData"></label>
                        <div class="col-sm-9">
                            <button  onclick="envelopDataTest();"> Envelop</button>
                        </div>
                    </div>

                </form>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">Envelop 결과</div>
            <div class="panel-body">
                <textarea id="envelopedData" rows="5" cols="80"></textarea>
                <form class="form-inline" onsubmit="return false;">
                    <button onclick="decryptEnvelopDataTest();">서명 데이터 검증.</button>
                </form>
            </div>
        </div>
        <div class="panel panel-success">
            <div class="panel-heading">서명 데이터 검증결과.</div>
            <div class="panel-body">
                <textarea id="decryptEnvelopedData" rows="5" cols="80"></textarea>
                <h5>키정보</h5>
                <div id="ecvelopKeyInfo" class="scrollable-data"></div>
            </div>
        </div>

        <hr>
        <%@include file="footer.jsp" %>

    </div>
</div>



<script>
    function decrypt_complete_callback(res) {
        if (res.code == 0) {
            $("#decryptEnvelopedData").val(res.data.data);
            $("#ecvelopKeyInfo").append(objectToTable("",res.data.keyInfo));
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }
    function envelop_complete_callback(res) {
        if (res.code == 0) {
            $("#envelopedData").val(res.data);
        }
        else {
            alert("error code = " + res.code + ", message = " + res.errorMessage);
        }
    }

    function envelopDataTest() {
        var data = $("#testData").val();
        var cert = $("#testCert").val();
        $("#envelopedData").val("");
        nxTSPKI.envelopData(data,cert,{}, envelop_complete_callback);
    }

    function decryptEnvelopDataTest() {
        var envelopedData = $("#envelopedData").val();
        $("#verifyResult").val("");
        $("#ecvelopKeyInfo").empty();

        var certValidation = $("#certValidation").is(":checked");
        nxTSPKI.decryptEnvelopedData(envelopedData, {"certValidation": certValidation}, decrypt_complete_callback);
    }

    $(document).ready(function(){
        var kmCert        = "-----BEGIN CERTIFICATE-----MIIFnjCCBIagAwIBAgIEWZHJYTANBgkqhkiG9w0BAQsFADBPMQswCQYDVQQGEwJLUjESMBAGA1UECgwJVHJhZGVTaWduMRUwEwYDVQQLDAxBY2NyZWRpdGVkQ0ExFTATBgNVBAMMDFRyYWRlU2lnbkNBMjAeFw0xNTA1MTEwOTI1NDlaFw0xNjA2MDkwMjE4MzlaMG0xCzAJBgNVBAYTAktSMRIwEAYDVQQKDAlUcmFkZVNpZ24xFTATBgNVBAsMDEFjY3JlZGl0ZWRDQTENMAsGA1UECwwEVEVTVDEkMCIGA1UEAwwb7YWM7Iqk7Yq4MV9LVE5FVF8wMDAwMDY4NzI1MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxMlPX/bXTMChBEZmhZcCV3pTQ1hQXNYi4ccYDU5yOjLO0jVkH3RFhJqHuxHcrAi5wSn4K85TwGWNM9xDUGbRCS8x2vPgCvgnt32sBGhvASExJ3vUFfy2L/nP69g4pf0qNdl4wwbQ+6p9qSjpMCtNdLfgGhp/D/e7MVCYuJVVcgcs1r6MrG9/Pw2N5xt/X0aIwQV8gniWsZoE9vg+Dh97O7veieZluo0PbQDMBbTt2FEu2GEk1dbiGDVUF54UGGo2mN9MNT5QN0WpU0njIr7Qjh1t+iI91sMTGZT8DkTvMhXqqiUqz/UIrUg/GAomNo0YAV+l46marYJDsnEIK7MaCQIDAQABo4ICYjCCAl4wgY8GA1UdIwSBhzCBhIAUTV1WCgcD34PK89Vtjxn8EqyQooqhaKRmMGQxCzAJBgNVBAYTAktSMQ0wCwYDVQQKDARLSVNBMS4wLAYDVQQLDCVLb3JlYSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eSBDZW50cmFsMRYwFAYDVQQDDA1LSVNBIFJvb3RDQSA0ggIQCTAdBgNVHQ4EFgQUCtRrJK2rQLkqHCA1n/OcTKUeWKAwDgYDVR0PAQH/BAQDAgbAMHoGA1UdIAEB/wRwMG4wbAYJKoMajJpMAQEDMF8wLgYIKwYBBQUHAgIwIh4gx3QAIMd4yZ3BHLKUACCs9cd4x3jJncEcACDHhbLIsuQwLQYIKwYBBQUHAgEWIWh0dHA6Ly93d3cudHJhZGVzaWduLm5ldC9jcHMuaHRtbDBvBgNVHREEaDBmoGQGCSqDGoyaRAoBAaBXMFUMEO2FjOyKpO2KuDFfS1RORVQwQTA/BgoqgxqMmkQKAQEBMDEwCwYJYIZIAWUDBAIBoCIEICbtJ2uzRKnAoHO/WR3+tCkRJ/qIPdSjHRHTHrJ7pt2/MGYGA1UdHwRfMF0wW6BZoFeGVWxkYXA6Ly9sZGFwLnRyYWRlc2lnbi5uZXQ6Mzg5L2NuPWNybDFkcDU4MyxvdT1jcmxkcDIsb3U9QWNjcmVkaXRlZENBLG89VHJhZGVTaWduLGM9S1IwRgYIKwYBBQUHAQEEOjA4MDYGCCsGAQUFBzABhipodHRwOi8vb2NzcC50cmFkZXNpZ24ubmV0OjE4MDAwL09DU1BTZXJ2ZXIwDQYJKoZIhvcNAQELBQADggEBAIE6DplwEq9PqwW/2M3uEnPOt70LS7aRFVSM+uDO/OeUjRdQ//0CKWnldZ3/qbsT8dwUIRl9A1E/GwDsJJIQ7/QLcFA8swk5EiXkCDkZyPT6JFuS45ibS0xKwbp1xDYXyX2iYEXsrFw/2In2hG4QjGDunMuA7N1YSBTiEMbaDh5LxXrJPoXlo3BOJziVUFbFeneX/8GiusBkuvWwqOxSjkY82GJIDX51zF9l629iUxGVURpJN6cV3RBdDk3r6FXXGPGnfhswruL8uGoFIp4/BX6lE5ZTLc8hZ1GA1oLdrTC3yC9qNsPACnP+6Y5OQk2UomxZHzEMYlHQrar0lAd4/ls=-----END CERTIFICATE-----";
        $("#testCert").val(kmCert);
    });
		
	//초기화 함수 필수
    $(document).ready(function(){
        nxTSPKI.onInit(function(){ 
			//nxTSPKI.init 함수 완료 후 실행해야 하는 함수나 기능 작성 위치
			//alert("Init 완료");
		});

		nxTSPKI.init(true);
    });

</script>


</body>
</html>
