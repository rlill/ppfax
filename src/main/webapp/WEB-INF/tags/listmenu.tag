<%@ tag %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<%@ attribute name="type" required="true" type="java.lang.String" %>
<%@ attribute name="back" required="false" type="java.lang.Boolean" %>



<div class="listmenu">

<c:if test="${back}">
<div class="listmenuitem">
<form action="/${type}" method="get">
	<input type="submit" value="back"/>
</form>
</div>
</c:if>

<div class="listmenuitem">
<form action="/${type}/new" method="get">
	<input type="submit" value="new ${type}"/>
</form>
</div>

<div class="searchform">

<form action="/${type}" method="get">
<table class="searchform">
  <tr><th>
    <input type="submit" value="search"/>
  </th><td>
    <input type="text" name="searchstring"/>
  </td></tr>
</table>
</form>

</div>

<div class="clear"></div>

</div>
