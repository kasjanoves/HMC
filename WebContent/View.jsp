<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="formTags" uri="FormsTags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Media details</title>
</head>
<body>
	<a href ="Home.jsp">Back</a><br>
	<formTags:MediaView id='${param.id}' />
	<br>
	<formTags:MediaTags id='${param.id}' />
	<br>
	Select tag
	<form action="AddTag.do" method="post">
		<formTags:TagSelector id='${param.id}' />
		<input type="submit">
		<input name="id" type="hidden" value = '${param.id}'>
	</form>
	<br>
	Add new tag
	<form action="AddTag.do" method="post">
		<input name="tag" type="text">
		<input type="submit">
		<input name="id" type="hidden" value = '${param.id}'>
	</form>
	<form action="Delete.do" method="post">
		<input type="submit">
		<input name="id" type="hidden" value = '${param.id}'>
	</form>	
</body>
</html>