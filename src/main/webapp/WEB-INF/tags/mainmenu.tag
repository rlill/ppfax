<%@ tag %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<div class="mainmenu">

[<a href="/">Home</a>]
<c:forEach var="cat" items="${rootCategories}">
	[<a href="${cat.path}"><c:out value="${cat.name}"/></a>]
</c:forEach>

</div>
