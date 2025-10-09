<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty param.css}">
    <c:set var="tempStyles" value="${requestScope.pageStyles}" />
    <c:if test="${empty tempStyles}">
        <c:set var="tempStyles" value="" scope="request" />
    </c:if>
    <c:set var="tempStyles" value="${tempStyles}${param.css}," scope="request" />
    <c:set var="pageStyles" value="${tempStyles}" scope="request" />
</c:if>

<c:if test="${not empty param.js}">
    <c:set var="tempScripts" value="${requestScope.pageScripts}" />
    <c:if test="${empty tempScripts}">
        <c:set var="tempScripts" value="" scope="request" />
    </c:if>
    <c:set var="tempScripts" value="${tempScripts}${param.js}," scope="request" />
    <c:set var="pageScripts" value="${tempScripts}" scope="request" />
</c:if>

<c:if test="${not empty param.inline}">
    <c:set var="tempInline" value="${requestScope.inlineScripts}" />
    <c:if test="${empty tempInline}">
        <c:set var="tempInline" value="" scope="request" />
    </c:if>
    <c:set var="tempInline" value="${tempInline}${param.inline}" scope="request" />
    <c:set var="inlineScripts" value="${tempInline}" scope="request" />
</c:if>