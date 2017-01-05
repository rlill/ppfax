<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="header.jsp"%>

<a href="${loginUrl}">login</a><br>
<a href="${logoutUrl}">logout</a><br>

<c:if test="${not empty googleUser}">
  <b>Google</b><br>
  User: ${googleUser.userId}<br>
  Email: ${googleUser.email}<br>
</c:if>

<c:if test="${not empty ppUser}">
  <b>Local</b><br>
  ${ppUser}<br>
  User: ${ppUser.id}<br>
  Role: ${ppUser.role}<br>
  Status: ${ppUser.status}<br>
</c:if>

<h1>Error</h1>

</body>
</html>
