<%@page contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Top Artists from Last.fm</title>
</head>
<body>
 <p>
   <a href="<c:url value="/refresh.html"/>">Refresh from Last.fm</a>
 </p>
<h2>Top artists</h2>
<p>
  <c:if test="${fn:length(topArtists) == 0}">
    <h3>No artists found in database. Refresh from Last.fm</h3>
  </c:if>
  <table>
    <tr>
      <th>Name</th>
      <th>Play count</th>
      <th>Listeners</th>
    </tr>
    <c:forEach items="${topArtists}" var="artist">
      <tr>
        <td>${artist.name}</td>
        <td>${artist.playCount}</td>
        <td>${artist.listeners}</td>
      </tr>
    </c:forEach>
  </table>
</p>
</body>
</html>
