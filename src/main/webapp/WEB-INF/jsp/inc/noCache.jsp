<%   
	response.setHeader("Cache-Control","no-store");   
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader("Expires",0); //prevents caching at the proxy server
	if (request.getProtocol().equals("HTTP/1.1")) response.setHeader("Cache-Control", "no-cache"); 
%>