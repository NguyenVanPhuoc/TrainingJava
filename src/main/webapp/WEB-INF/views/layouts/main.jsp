<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.springframework.security.core.Authentication"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle != null ? pageTitle : 'Admin Dashboard'}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/users.css">
</head>
<body class="flex">
    <!-- Sidebar cố định -->
    <div class="fixed top-0 left-0 h-screen w-64 bg-gray-800 text-white flex flex-col z-10">
        <%@ include file="/WEB-INF/views/layouts/sidebar.jsp" %>
    </div>

    <!-- Phần nội dung dịch sang bên phải -->
    <div class="ml-64 flex-1 p-6 overflow-y-auto h-screen">
        <%@ include file="/WEB-INF/views/layouts/header.jsp" %>

        <main>
            <jsp:include page="${contentPage}" />
        </main>
    </div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
<script>
    function toggleMenu() {
        const menu = document.getElementById("logoutMenu");
        menu.classList.toggle("hidden");
    }
    document.addEventListener("click", function (e) {
        const avatar = document.getElementById("avatar");
        const menu = document.getElementById("logoutMenu");
        if (!avatar.contains(e.target) && !menu.contains(e.target)) {
            menu.classList.add("hidden");
        }
    });
    function toggleSubmenu(id) {
        const submenu = document.getElementById(id);
        if (submenu.style.display === "block") {
            submenu.style.display = "none";
        } else {
            submenu.style.display = "block";
        }
    }
    
    document.addEventListener("DOMContentLoaded", function () {
        flatpickr("#orderStartTime, #orderEndTime", {
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            time_24hr: true,
            minuteIncrement: 5,
            defaultHour: 8 
        });
    });
</script>
</body>
</html>
