<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<% Exception ex = (Exception)request.getAttribute("authException"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<title>Error!</title>
</head>
<body>
	<%=ex.getMessage() %>
</body>
</html>