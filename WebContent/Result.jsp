<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File was uploaded</title>
</head>
<body>
	<c:set var = "mType" scope = "session" value = "${mediaType}"/>
	<c:choose>
	<c:when test = "${mType == 'image'}">
		<img src="${filePath}" width="250"><br>
	</c:when>
	<c:when test = "${mType == 'video'||mType == 'application' }">
		<video>
			<source src="${filePath}">
		</video><br>
	</c:when>
	<c:otherwise>
      Unknown media type...<br>
    </c:otherwise>
	</c:choose>	
	${description}<br>
	<a href ="Home.jsp">Back</a><br>
</body>
</html>