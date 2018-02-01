<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib prefix="formTags" uri="FormsTags" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Media details</title>
</head>
<body>
	<a href ="Home.do">Back</a><br>
	<formTags:MediaView mediaInfo='${mediaInfo}' />
	<br>
	<formTags:MediaTags mediaTags='${mediaTags}' mode='byMedia'/>
	Select tag
	<form action="AddTag.do" method="post">
		<formTags:TagSelector mediaTags='${unselectedTags}' />
		<input type="submit" value="add tag">
		<input name="id" type="hidden" value = '${param.id}'>
	</form>
	Add new tag
	<form action="AddTag.do" method="post">
		<input name="tag" type="text">
		<input type="submit" value="add tag">
		<input name="id" type="hidden" value = '${param.id}'>
	</form>
	<form action="Delete.do" method="post">
		<input type="submit" value="Delete item" >
		<input name="id" type="hidden" value = '${param.id}'>
	</form>	
</body>
</html>