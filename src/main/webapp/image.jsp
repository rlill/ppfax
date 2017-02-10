<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ppf" tagdir="/WEB-INF/tags" %>

<ppf:header type="image"/>

<c:choose>
<c:when test="${not empty image}">

<h3>Image ${image.id}</h3>

<ppf:listmenu type="image" back="true"/>

<form action="/image" method="POST">
<input type="hidden" name="action" value="updateImage"/>
<input type="hidden" name="id" value="${image.id}"/>
<table class="editor">
	<tr>
		<th>Title</th>
		<td><input type="text" name="title" value="${image.title}"/></td>
	</tr>
	<tr>
		<th>Status</th>
		<td>
			<select name="status" size="1">
				<c:forEach var="status" items="${publicationStatus}">
					<c:set var="chk" value="${status.name eq image.status.name ? 'selected' : ''}"/>
					<option value="${status}" ${chk}>${status.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>

	<tr>
		<th></th>
		<td>
			<img src="/img/3/${image.id}" />
		</td>
	</tr>

	<tr>
		<th></th>
		<td>
			<input type="submit" value="save"/>
			
<c:if test="${admin}">
			<a href="/image/delete/${image.id}">
				<input type="button" value="delete"/>
			</a>
</c:if>

		</td>
	</tr>

</table>
</form>

</c:when>
<c:otherwise>

<h3>Images</h3>

<ppf:listmenu type="image"/>

<table class="grid">

	<tr>
		<th>Title</th>
		<th>Created</th>
		<th>Status</th>
		<th>Live URL</th>
	</tr>

<c:if test="${not empty images}">
<c:forEach items="${images}" var="image">

	<tr>
		<td><a href="/image/${image.id}">${image.title}</a></td>
		<td>${image.author.name}<br><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${image.dateCreated}" timeZone="GMT" /></td>
		<td>${image.status}</td>
		<td>${image.storagePath}</td>
		<td><img src="/img/1/${image.id}" /></td>
	</tr>
	
</c:forEach>
</c:if>

	<form action="/image" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="action" value="addImage"/>
	<tr>
		<td><input type="text" name="title"></td>
		<td></td>
		<td>
			<select name="status" size="1">
				<c:forEach var="status" items="${publicationStatus}">
					<c:set var="chk" value="${status.name eq image.status.name ? 'selected' : ''}"/>
					<option value="${status}" ${chk}>${status.name}</option>
				</c:forEach>
			</select>
		</td>
		<td><input type="file" name="file"/></td>
		<td><input type="submit" value="upload"/></td>
	</tr>
	</form> 


</table>




</c:otherwise>
</c:choose>


</body>
</html>
