<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ppf" tagdir="/WEB-INF/tags" %>

<ppf:header type="${type}"/>

<h3>${title}</h3>

${question}

<form action="/${type}" method="POST">
<input type="hidden" name="action" value="${action}"/>
<input type="hidden" name="id" value="${id}"/>

<c:choose>
<c:when test="${dialog eq 'yesno'}">
<input type="submit" name="answer" value="Yes"/>
<input type="submit" name="answer" value="No"/>
</c:when>
<c:when test="${dialog eq 'continueabort'}">
<input type="submit" name="answer" value="Continue"/>
<input type="submit" name="answer" value="Abort"/>
</c:when>
<c:when test="${dialog eq 'okcancel'}">
<input type="submit" name="answer" value="OK"/>
<input type="submit" name="answer" value="Cancel"/>
</c:when>
<c:when test="${dialog eq 'ok'}">
<input type="submit" name="answer" value="OK"/>
</c:when>

</c:choose>

</form>

</body>
</html>
