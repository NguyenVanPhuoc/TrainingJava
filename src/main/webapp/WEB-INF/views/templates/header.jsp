<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><c:out value="${pageTitle}" /></title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
	<header class="header bg-white shadow-sm py-3">
	  <div class="container">
	    <div class="d-flex justify-content-between align-items-center">
	      <!-- Logo section with Bầu Food text -->
	      <div class="logo-container">
		      <a href="/" class="nav-link px-3">
		        <h1 class="m-0 d-flex align-items-center">
		          <!-- You can replace this with an actual logo image -->
		          <span class="logo-text fw-bold" style="color: #28a745; font-size: 28px;">
		            <span style="color: #218838;">Bầu</span> Food
		          </span>
		        </h1>
		      </a>
	      </div>
	
	      <!-- Navigation menu - you can add this if needed -->
	      <nav class="d-none d-lg-flex">
	        <ul class="nav-menu d-flex list-unstyled m-0">
	          <li class="nav-item"><a href="/" class="nav-link px-3">Thực Đơn</a></li>
	          <li class="nav-item"><a href="/user/orders/unpaid" class="nav-link px-3">Hóa Đơn</a></li>
	          <li class="nav-item"><a href="https://www.chatwork.com/#!rid261092654" class="nav-link px-3">Liên Hệ</a></li>
	        </ul>
	      </nav>
	
	      <!-- User authentication section -->
	      <div class="header-right">
	        <sec:authorize access="isAuthenticated()">
	          <div class="position-relative user-profile">
	            <img src="${avatarUrl}" 
	              alt="User Avatar"
	              class="rounded-circle cursor-pointer user-avatar"
	              onclick="toggleMenu()"
	              id="avatar" />
	            
	
	            <div id="logoutMenu" class="position-absolute end-0 mt-2 bg-white border rounded shadow-lg d-none" style="width: 150px; z-index: 1000;">
	              <a class="d-block px-4 py-2 text-decoration-none text-dark hover-bg-light" href="${pageContext.request.contextPath}/logout">Đăng Xuất</a>
	            </div>
	          </div>
	        </sec:authorize>
	
	        <sec:authorize access="isAnonymous()">
	          <a class="btn btn-outline-success me-2" href="${pageContext.request.contextPath}/login">Đăng Nhập</a>
	          <a class="btn btn-success" href="${pageContext.request.contextPath}/login">Đăng Ký</a>
	        </sec:authorize>
	      </div>
	    </div>
	  </div>
	</header>
	<div class="content container">
