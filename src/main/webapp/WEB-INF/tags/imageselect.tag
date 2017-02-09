<%@ tag %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<%@ attribute name="index" required="true" type="java.lang.String" %>
<%@ attribute name="value" required="false" type="java.lang.String" %>

<jsp:useBean id="random" class="farm.chaos.ppfax.utils.RandomBean" scope="application" />
<c:set var="iid" value="${random.nextId}" />

<div class="imgdlg" id="${iid}">
	<input type="hidden" name="imageId${index}" value="${value}" />
	<div class="imgdlghead">
		<ul>
			<li title="search" class="active"><a class="tabhead" href="#">Search</a></li>
		    <li title="upload"><a class="tabhead" href="#">Upload</a></li>
		    <li title="download"><a class="tabhead" href="#">Download</a></li>
		</ul>
		<div class="clear"></div>
	</div>
	<div class="imgdlgbody search">
		<table><tr><th>&nbsp;</th></tr>
		<tr><th>
			<input type="button" value="&#x1F50D;"/>
		</th><td>
			<input type="text" value="*" />
		</td></tr></table>
	</div>
	<div class="imgdlgbody searchresult" style="display:none">
		<div class="vscroll"></div>
	</div>
	<div class="imgdlgbody upload" style="display:none">
		<table>
		<tr><td>
			Title: <input type="text"/>
		</td><th>
			<input type="button" value="upload"/>
		</th></tr>
		<tr><td>
			<input type="file" value="file"/>
		</td></tr></table>
	</div>
	<div class="imgdlgbody download" style="display:none">
		<table><tr><td>
			URL: <input type="text"/>
		</td><th>
			<input type="button" value="download"/>
		</th></tr></table>
	</div>
</div>

<script type="text/javascript">

$(document).ready(function(){
	$('#${iid} div.imgdlghead ul a').click(function(){
		$('#${iid} div.imgdlghead ul li').each(function(){
			$(this).removeClass('active');
		});
		$(this).parent().addClass('active');
		var title = $(this).parent().attr('title');
		$('#${iid} div.imgdlgbody').each(function(){
			if ($(this).hasClass(title))
				$(this).show();
			else
				$(this).hide();
		});
		return false;
	});

	$('#${iid} div.imgdlgbody.search input[type=button]').click(function(){

		var searchterm = $('#${iid} div.imgdlgbody.search input[type=text]').val();
		console.log("searchterm: " + searchterm);
		
		// search button
	    $.getJSON('/api/v1/image/search/' + encodeURIComponent(searchterm), function (data) {

	    	var ul = $('#${iid} div.searchresult div.vscroll').empty();

	    	$.each(data, function(key, value) {
	    	    
	    	    var img = $('<img src="/img/2/' + value.id + '">');
	    	    var tit = $('<p>' + value.title + '</p>');
	    	    
	    	    var li = $('<div class="imglistitem"></div>');
	    	    li.append(img).append(tit);
	    	    
	    	    ul.append(li);
	    	    
	    	    // choose image
	    	    img.click(function(){
	    	    	$('#${iid} input[type=hidden]').val(value.id);
	    	    	$('#${iid} div.imgdlgbody.searchresult').find('img').css('border', 0);
	    	    	$(this).css('border', '2px solid red');
	    	    });

	    	});
	    	
	    	$('#${iid} div.imgdlgbody.search').hide();
	    	$('#${iid} div.imgdlgbody.searchresult').show();
		
		});
		
	});
	
	$('#${iid} div.imgdlgbody.search input[type=button]').click();

});

</script>
