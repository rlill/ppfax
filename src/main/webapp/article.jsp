<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ppf" tagdir="/WEB-INF/tags" %>

<ppf:header type="article"/>

<c:choose>
<c:when test="${not empty article}">

<ppf:listmenu type="article"/>

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
	
	<c:set var="seq" value="${0}"/>
	
<c:forEach var="paragraph" items="${paragraphs}">
	<c:set var="seq" value="${seq+1}"/>
	<tbody id="paragraphEditor_${seq}" class="paragraphEditor existingParagraph">
		<input type="hidden" name="sequence_${seq}" class="sequence" value="${seq}"/>
		<input type="hidden" name="paragraph_${seq}" value="${paragraph.id}"/>
		<tr>
			<td colspan="2"><hr/></td>
		<tr>
		<tr>
			<th>Headline</th>
			<td><input type="text" name="headline_${seq}" value="${paragraph.headline}"/></td>
		</tr>
		<tr>
			<th>Paragraph</th>
			<td><textarea name="bodyText_${seq}" rows="10"><c:out value="${paragraph.bodyText}"/></textarea></td>
		</tr>
		<tr>
			<th>Image</th>
			<td>
				<c:if test="${not empty paragraph.imageId && paragraph.imageId gt 0}">
					<img src="${paragraph.image.imageUrl}"/><br>${image.title}
				</c:if>
			</td>
		</tr>
		<tr>
			<th>Style</th>
			<td>
				<select name="style_${seq}" size="1">
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
				<input type="button" value="remove" class="removeParagraph"/>
				<input type="button" value="&darr;" class="moveDown"/>
				<input type="button" value="&uarr;" class="moveUp"/>
			</td>
		</tr>
		<tr>
			<td colspan="2"><hr/></td>
		</tr>
	</tbody>
</c:forEach>
	
	<tbody id="paragraphTemplate" style="display:none">
		<input type="hidden" name="sequence" class="sequence" value="0"/>
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
				<input type="button" value="remove" class="removeParagraph"/>
				<input type="button" value="&uarr;" class="moveUp"/>
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
				<input type="hidden" name="pcount" id="pcount" value="${seq}"/>
				<input type="submit" value="save"/>
				<input type="button" value="new paragraph" id="addParagraph"/>
			</td>
		</tr>
	</tbody>
</table>
</form>


<script type="text/javascript" >

var topParagraphIndex = ${seq};

moveParagraphUp = function(editor){
	console.log("Up " + editor.find('input.sequence').val() + " = " + editor.attr('id'));
	switchElements(editor, editor.prev('.paragraphEditor'));
};

moveParagraphDown = function(editor){
	console.log("Down " + editor.find('input.sequence').val() + " = " + editor.attr('id'));
	switchElements(editor, editor.next('.paragraphEditor'));
};

switchElements = function(el, sw){
	var iel = el.find('input.sequence').val();
	var isw = sw.find('input.sequence').val();
	console.log("switch " + iel + " - " + isw);
	if (iel > 0 && isw > 0) {
	    var tmp = $('<span>').hide();
	    el.before(tmp);
	    sw.before(el);
	    tmp.replaceWith(sw);

	    el.find('input.sequence').val(isw);
		sw.find('input.sequence').val(iel);
	}
}

$(document).ready(function(){
	
	$('input#addParagraph').click(function(){
		
		topParagraphIndex++;
		$('input#pcount').val(topParagraphIndex);
		var editor = $('tbody#paragraphTemplate')
			.clone()
			.attr('id', 'paragraphEditor_' + topParagraphIndex)
			.addClass('paragraphEditor')
			.show()

		editor.find('input[name=sequence]').val(topParagraphIndex);

		editor.find('input,textarea,select').each(function(){
			$(this).attr('name', $(this).attr('name') + '_' + topParagraphIndex);
		});
		
		editor.find('input.removeParagraph').click(function(){
			editor.remove();
		});
			
		editor.insertBefore($('tbody#controlRow'));
	});
	
	$('tbody.existingParagraph').each(function(){
		var editor = $(this);
		editor.find('input.removeParagraph').click(function(){
			editor.remove();
		});
		editor.find('input.moveUp').click(function(){
			moveParagraphUp($(this).parentsUntil('table.editor').last());
		});
		editor.find('input.moveDown').click(function(){
			moveParagraphDown($(this).parentsUntil('table.editor').last());
		});
	});
	
});

</script>



</c:when>
<c:when test="${not empty newArticle}">

<ppf:listmenu type="article"/>

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

<ppf:listmenu type="article"/>

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

</c:otherwise>
</c:choose>


</body>
</html>
