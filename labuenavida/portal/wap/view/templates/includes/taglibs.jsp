<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/portal.tld" prefix="p" %>

<c:set var="applicationName" value="${pageContext.request.contextPath}" />
<c:set var="contextPath" value="${pageContext.request.contextPath}/" scope="session" />
<c:set var="imagePath" value="${applicationName}/portal/wap/view/templates/resources/images/" />
<c:set var="resourcesPath" value="${applicationName}/portal/wap/view/templates/resources/" />