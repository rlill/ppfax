<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ppf" tagdir="/WEB-INF/tags" %>

<ppf:header type="category"/>

<c:choose>
<c:when test="${not empty category}">

<h3>Category</h3>

<ppf:listmenu type="category"/>

<form action="/category" method="POST">
<input type="hidden" name="action" value="updateCategory"/>
<input type="hidden" name="id" value="${category.id}"/>
<table class="editor">
	<tr>
		<th>Name</th>
		<td><input type="text" name="name" value="${category.name}"/></td>
	</tr>
	<tr>
		<th>Path</th>
		<td><input type="text" name="path" value="${category.path}"/></td>
	</tr>
	<tr>
		<th>Sidebar</th>
		<td><textarea name="sidebarcontent" rows="10"><c:out value="${category.sidebarcontent}"/></textarea></td>
	</tr>
	<tr>
		<th>Keywords</th>
		<td><input type="text" name="keywords" value="${category.keywords}"/></td>
	</tr>
	<tr>
		<th>Parent</th>
		<td>
			<select name="parentId" size="5">
				<option value="0">ROOT</option>
				<c:forEach var="cat" items="${categories}">
					<c:if test="${cat.id != category.id}">
						<c:set var="chk" value="${cat.id == category.parentId ? 'selected' : ''}"/>
						<option value="${cat.id}" ${chk}>${cat.path}</option>
					</c:if>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<th>Status</th>
		<td>
			<select name="status" size="1">
				<c:forEach var="status" items="${publicationStatus}">
					<c:set var="chk" value="${status.name eq category.status.name ? 'selected' : ''}"/>
					<option value="${status}" ${chk}>${status.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<th></th>
		<td><input type="submit" value="save"/></td>
	</tr>
</table>
</form>

</c:when>
<c:when test="${not empty newCategory}">

<h3>New Category</h3>

<ppf:listmenu type="category"/>

<form action="/category" method="POST">
<input type="hidden" name="action" value="addCategory"/>
<table class="editor">
	<tr>
		<th>Name</th>
		<td><input type="text" name="name"/></td>
	</tr>
	<tr>
		<th>Path</th>
		<td><input type="text" name="path"/></td>
	</tr>
	<tr>
		<th>Sidebar</th>
		<td><textarea name="sidebarcontent" rows="10"></textarea></td>
	</tr>
	<tr>
		<th>Keywords</th>
		<td><input type="text" name="keywords"></td>
	</tr>
	<tr>
		<th>Parent</th>
		<td>
			<select name="parentId" size="5">
				<option value="0">ROOT</option>
				<c:forEach var="cat" items="${categories}">
					<option value="${cat.id}">${cat.path}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<th>Status</th>
		<td>
			<select name="status" size="1">
				<c:forEach var="status" items="${publicationStatus}">
					<option value="${status}">${status.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<th></th>
		<td><input type="submit" value="save"/></td>
	</tr>
</table>
</form>

</c:when>
<c:otherwise>


<h3>Categories</h3>

<ppf:listmenu type="category"/>

<table class="grid">

	<tr>
		<th>Name</th>
		<th>Path</th>
		<th>Created</th>
		<th>Status</th>
	</tr>

<c:if test="${not empty categories}">
<c:forEach items="${categories}" var="category">

	<tr>
		<td><a href="/category/${category.id}">${category.name}</a></td>
		<td>${category.path}</td>
		<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${category.dateCreated}" timeZone="GMT" /><br>
			${not empty category.author ? category.author.name : ''}</td>
		<td>${category.status}</td>
	</tr>
	
</c:forEach>
</c:if>

</table>

<form action="/category/new" method="GET">
	<input type="submit" value="new"/>
</form>

</c:otherwise>
</c:choose>

</body>
</html>
