<%@ tag %>

<%@ attribute name="type" required="true" type="java.lang.String" %>



<div class="listmenu">

<div class="listmenuitem">
<form action="/${type}" method="get">
	<input type="submit" value="back"/>
</form>
</div>

<div class="listmenuitem">
<form action="/${type}/new" method="get">
	<input type="submit" value="new"/>
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
