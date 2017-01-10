<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="header.jsp"%>

<c:choose>
<c:when test="${not empty article}">

<h3>Article ${article.id}</h3>

<form action="/article" method="POST">
<input type="hidden" name="action" value="updateArticle"/>
<input type="hidden" name="id" value="${article.id}"/>
<table class="editor">
	<tr>
		<th>Title</th>
		<td><input type="text" name="title" value="${article.title}"/></td>
	</tr>
	<tr>
		<th>Headline</th>
		<td><input type="text" name="headline" value="${article.headline}"/></td>
	</tr>
	<tr>
		<th>Teasertext</th>
		<td><textarea name="teasertext" rows="10"><c:out value="${article.teasertext}"/></textarea></td>
	</tr>
	<tr>
		<th>Keywords</th>
		<td><input type="text" name="keywords" value="${article.keywords}"/></td>
	</tr>
	<tr>
		<th>Publish date</th>
		<fmt:formatDate pattern="yyyy-MM-dd" value="${article.dateCreated}" timeZone="GMT" var="pubDate" />
		<td><input type="text" name="datePublished" value="${pubDate}"/></td>
	</tr>
	<tr>
		<th>Category</th>
		<td>
			<select name="categoryId" size="5">
				<c:forEach var="cat" items="${categories}">
					<c:set var="chk" value="${cat.id == article.categoryId ? 'selected' : ''}"/>
					<option value="${cat.id}" ${chk}>${cat.path}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<th>Status</th>
		<td>
			<select name="status" size="1">
				<c:forEach var="status" items="${publicationStatus}">
					<c:set var="chk" value="${status.name eq article.status.name ? 'selected' : ''}"/>
					<option value="${status}" ${chk}>${status.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	
<c:forEach var="paragraph" items="${paragraphs}">
	<tr>
		<td colspan="2"><hr/></td>
	<tr>
	<tr>
		<th>Headline</th>
		<td><input type="text" name="headline" value="${paragraph.headline}"/></td>
	</tr>
	<tr>
		<th>Paragraph</th>
		<td><textarea name="bodyText" rows="10"><c:out value="${'paragraph.bodyText'}"/></textarea></td>
	</tr>
	<tr>
		<th>Image</th>
		<td><img src="${paragraph.image.imageUrl}"/><br>${image.title}</td>
	</tr>
	<tr>
		<th>Style</th>
		<td>
			<select name="style" size="1">
				<c:forEach var="style" items="${ParagraphStyles}">
					<c:set var="chk" value="${style.name eq paragraph.style.name ? 'selected' : ''}"/>
					<option value="${style}" ${chk}>${style.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<th></th>
		<td>
			<input type="button" value="remove" id="removeParagraph"/>
			<input type="button" value="&darr;" id="moveDown"/>
			<input type="button" value="&uarr;" id="moveUp"/>
		</td>
	</tr>
	<tr>
		<td colspan="2"><hr/></td>
	<tr>
</c:forEach>
	
	<tbody id="paragraphTemplate" style="display:none">
	<tr>
		<td colspan="2"><hr/></td>
	<tr>
		<th>Headline</th>
		<td><input type="text" name="headline"/></td>
	</tr>
	<tr>
		<th>Paragraph</th>
		<td><textarea name="bodyText" rows="10"></textarea></td>
	</tr>
	<tr>
		<th>Image title</th>
		<td><input type="text" name="title"></td>
	</tr>
	<tr>
		<th>Image file</th>
		<td><input type="file" name="image"></td>
	</tr>
	<tr>
		<th>Style</th>
		<td>
			<select name="style" size="1">
				<c:forEach var="style" items="${ParagraphStyles}">
					<option value="${style}">${style.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<th></th>
		<td>
			<input type="button" value="remove" id="removeParagraph"/>
			<input type="button" value="&uarr;" id="moveUp"/>
		</td>
	</tr>
	<tr>
		<td colspan="2"><hr/></td>
	<tr>
	</tbody>
	
	<tbody id="controlRow">
	<tr>
		<th></th>
		<td>
			<input type="submit" value="save"/>
			<input type="button" value="new paragraph" id="addParagraph"/>
		</td>
	</tr>
	</tbody>
</table>
</form>


<script type="text/javascript" >

$(document).ready(function(){
	
	$('input#addParagraph').click(function(){
		
		$('tbody#paragraphTemplate')
			.clone()
			.attr('id', '')
			.show()
			.insertBefore($('tbody#controlRow'));
	});
	
});

</script>



</c:when>
<c:when test="${not empty newArticle}">

<h3>New Article</h3>

<form action="/article" method="POST">
<input type="hidden" name="action" value="addArticle"/>
<table class="editor">
	<tr>
		<th>Title</th>
		<td><input type="text" name="title"/></td>
	</tr>
	<tr>
		<th>Headline</th>
		<td><input type="text" name="headline"/></td>
	</tr>
	<tr>
		<th>Teasertext</th>
		<td><textarea name="teasertext" rows="10"></textarea></td>
	</tr>
	<tr>
		<th>Keywords</th>
		<td><input type="text" name="keywords"/></td>
	</tr>
	<tr>
		<th>Category</th>
		<td>
			<select name="categoryId" size="5">
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

<h3>Articles</h3>
<table class="grid">

	<tr>
		<th>Title</th>
		<th>Created</th>
		<th>Status</th>
		<th>Live URL</th>
	</tr>

<c:if test="${not empty articles}">
<c:forEach items="${articles}" var="article">

	<tr>
		<td><a href="/article/${article.id}">${article.title}</a><br>${article.headline}</td>
		<td>${article.author.name}<br><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${article.dateCreated}" timeZone="GMT" /></td>
		<td>${article.status}</td>
		<td><a href="${article.publicUri}">${article.publicUri}</a></td>
	</tr>
	
</c:forEach>
</c:if>

</table>

<form action="/article/new" method="GET">
	<input type="submit" value="new"/>
</form>

</c:otherwise>
</c:choose>


</body>
</html>
