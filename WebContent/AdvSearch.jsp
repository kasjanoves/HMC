<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="formTags" uri="FormsTags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	Advanced search<br>
	<form action="AdvSearch.do" method="post">
		<formTags:MetadataFilters />
		<input type="submit" value="Search" >
	</form>
</body>
</html>