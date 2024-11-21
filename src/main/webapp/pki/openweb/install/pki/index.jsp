<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
    <title>SCORE PKI for OpenWeb 설치</title>
    <script src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script src="/pki/openweb/js/nxts/nxts.min.js"></script>
    <script src="/pki/openweb/js/nxts/nxtspki_config.js"></script>
    <script src="/pki/openweb/js/nxts/nxtspki.js"></script>
    <script src="/pki/openweb/js/demo.js"></script>
    <script>nxTSConfig.disableInstallConfirmMessage = true;</script>
	<link rel="stylesheet" href="/pki/openweb/css/nxtsdemo.css" type="text/css">
	<link rel="shortcut icon" href="http://newzenpnp.co.kr/favicon.ico" />

</head>
<body>
<ul id="nxtsdemoinstall">
  <li> SCORE PKI for OpenWeb 프로그램 설치 </li>
  <li> <img src="../../img/install.png"></li>
  <li>
    <dl>
      <dt>설정정보 </dt>
      <dd>
        <div class="panel-body">
          <div id="configInfo"> </div>
          </div>
      </dd>
    </dl>
  </li>
  <li>
    <dl>
      <dt>브라우저 정보 </dt>
      <dd>
        <div class="panel-body">
          <div id="browserInfo"> </div>
  </div>
      </dd>
    </dl>
  <li> <a class="btn01" onClick="download_setup();" >설치파일 다운로드</a> </li>
  <li> <a class="btn02" href="/contract_main.html">처음으로</a> <!-- <a class="btn02" href="history.back(-1);" >뒤로</a>  --></li>
</ul>


<script>
	var checkTimer;
	$(document).ready(function(){
		function installComplete(res,data) {
			$("#nxVersion").html(res.data.nx);
			$("#pkiVersion").html(res.data.tstoolkit);

			if(res.code == nxTSError.res_success) {
				checkTimer.stop();
				alert("SCORE PKI for OpenWeb 프로그램이 설치 되었습니다.")
				//window.location.href = "../../pki/";
				history.back(-1);
			}
		}
		checkTimer = new nxTSUtil.timer(3000,function(){
			nxTSCommon.installCheck(false,{ajaxto:2500,success:installComplete,versionCheck:[nxTSConfig.TSTOOLKIT]});
		});
		checkTimer.start();

		updateConfigInfo("configInfo");
		updateBrowserInfo("browserInfo");
	});
  
	function download_setup() {
		window.location.href = "../../down/nxtspkisetup.exe";
	}

	function updateConfigInfo(target) {
		var ul = $("<ul></ul>");
		ul.append($('<li><span class="key">NX 권장 버전 </span><span class="val uninstall">'+nxTSPKIConfig.version.nx+' ( 현재 버전 : <span id="nxVersion" class=""></span> )</span></li>'));
		ul.append($('<li><span class="key">TSPKI 권장 버전</span><span class="val uninstall">'+nxTSPKIConfig.version.tstoolkit+' ( 현재 버전 : <span id="pkiVersion" class=""></span> )</span></li>'));
		ul.append($('<li><span class="key">Service URL</span><span class="val">'+nxTSConfig.getServiceUrl()+'</span></li>'));
		$("#"+target).append(ul);
	}

	function updateBrowserInfo(target) {
		var ul = $("<ul></ul>");
		ul.append($('<li><span class="key">Platform</span><span class="val">'+navigator.platform+'</span></li>'));
		ul.append($('<li><span class="key">AppVersion</span><span class="val">'+navigator.appVersion+'</span></li>'));
		ul.append($('<li><span class="key">UserAgent</span><span class="val">'+navigator.userAgent+'</span></li>'));
		$("#"+target).append(ul);
	}

</script>
</body>
</html>
