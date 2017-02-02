<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ppf" tagdir="/WEB-INF/tags" %>

<ppf:header type="user"/>

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
  Admin: ${admin}<br>
</c:if>

<h3>Users</h3>
<table class="grid">

	<tr>
		<th>Email</th>
		<th>Name</th>
		<th>Role</th>
		<th>Status</th>
	</tr>

<c:if test="${not empty users}">
<c:forEach items="${users}" var="user">

	<form action="/admin" method="POST">
	<input type="hidden" name="id" value="${user.id}"/>
	<input type="hidden" name="action" value="updateUser"/>
	<tr>
		<td>${user.email}</td>
		<td><input type="text" name="name" value="${user.name}"/></td>
		<td><select name="role" size="1">
			<c:forEach var="role" items="${userRoles}">
				<c:set var="chk" value="${user.role.name eq role.name ? 'selected' : ''}" />
				<option value="${role}" ${chk}>${role.name}</option>
			</c:forEach>
		</select></td>
		<td><select name="status" size="1">
			<c:forEach var="stat" items="${userStatus}">
				<c:set var="chk" value="${user.status.name eq stat.name ? 'selected' : ''}" />
				<option value="${stat}" ${chk}>${stat.name}</option>
			</c:forEach>
		</select></td>
		<td><input type="submit" value="update"/></td>
	</tr>
	</form>
	
</c:forEach>
</c:if>

	<form action="/admin" method="POST">
	<input type="hidden" name="action" value="addUser"/>
	<tr>
		<td><input type="text" name="email"/></td>
		<td><input type="text" name="name" value="${user.name}"/></td>
		<td><select name="role" size="1">
			<c:forEach var="role" items="${userRoles}">
				<option value="${role}">${role.name}</option>
			</c:forEach>
		</select></td>
		<td><select name="status" size="1">
			<c:forEach var="stat" items="${userStatus}">
				<option value="${stat}">${stat.name}</option>
			</c:forEach>
		</select></td>
		<td><input type="submit" value="add"/></td>
	</tr>
	</form>

</table>


</body>
</html>
