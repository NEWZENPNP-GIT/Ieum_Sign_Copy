<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <link rel="stylesheet" href="/K-Form/pki/js/bs-3.3.5/css/bootstrap.css" type="text/css">
  <link rel="stylesheet" href="/K-Form/pki/js/bs-3.3.5/css/bootstrap-theme.css" type="text/css">
  <link rel="stylesheet" href="/K-Form/pki/openweb/css/common.css" type="text/css">
  <script src="/K-Form/pki/openweb/js/jquery/jquery-1.11.3.js"></script>
   <script src="/K-Form/pki/js/bs-3.3.5/js/bootstrap.js"></script>
  <script src="/K-Form/js/nxts/nxts.min.js"></script>
    <script src="/K-Form/js/nxts/nxtspki_config.js"></script>
  <script src="/K-Form/js/nxts/nxtspki.js"></script>
<link rel="shortcut icon" href="http://newzenpnp.co.kr/favicon.ico" />

</head>
<body>
<script>
	//초기화 함수 필수
    $(document).ready(function(){
        nxTSPKI.onInit(function(){ 
			// nxTSPKI.init 함수 완료 후 실행해야 하는 함수나 기능 작성 위치
			//alert("Init 완료");
		});

		nxTSPKI.init(true);
    });	
</script>
</body>
</html>
