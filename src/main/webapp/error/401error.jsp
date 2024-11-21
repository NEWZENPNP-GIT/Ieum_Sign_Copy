<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<%
    response.setStatus(401);
%>
<script>
	location.href = "/";
</script>