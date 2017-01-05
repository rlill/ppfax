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

<h3>Articles</h3>
<table class="grid">

	<tr>
		<th>Title</th>
		<th>Created</th>
		<th>Modified</th>
		<th>Status</th>
	</tr>

<c:if test="${not empty articles}">
<c:forEach items="${articles}" var="article">

	<tr>
		<td><a href="/article/${article.id}">${article.title}</a></td>
		<td>${article.dateCreated}<br>${article.authorId}</td>
		<td>${article.dateModified}</td>
		<td>${article.status}</td>
	</tr>
	
</c:forEach>
</c:if>

	<form action="/article" method="POST">
	<input type="hidden" name="action" value="addArticle"/>
	<tr>
		<td><input type="text" name="title"/></td>
		<td></td>
		<td></td>
		<td></td>
		<td><input type="submit" value="add"/></td>
	</tr>
	</form>

</table>

<c:if test="${not empty article}">

	<h3>${article.title}</h3>
	<h2>${article.headline}</h2>
	<p><b>${article.teasertext}</b></p>
	
	<c:forEach var="paragraph" items="${paragraphs}">
		<u>${paragraph.headline}</u><br>
		<p>${paragraph.bodyText}</p>
	</c:forEach>

	<i>${article.authorId}</i><br>
</c:if>

</body>
</html>
