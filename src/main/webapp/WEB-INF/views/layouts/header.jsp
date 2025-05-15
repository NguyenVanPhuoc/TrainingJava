<header class="flex items-center justify-between mb-4">
    <h2 class="text-2xl font-semibold">${pageTitle != null ? pageTitle : 'Dashboard'}</h2>
    <div class="relative">
	    <img 
	        src="${not empty principal.avatar ? '/uploads/' += principal.avatar : 'https://picsum.photos/40'}"
	        alt="User Avatar" 
	        class="rounded-full cursor-pointer"
	        onclick="toggleMenu()"
	        id="avatar"
	    />
	
	    <div id="logoutMenu" class="absolute right-0 mt-2 w-32 bg-white border border-gray-200 rounded shadow-lg hidden z-50">
	        <sec:authorize access="isAuthenticated()">
	            <a class="block px-4 py-2 text-gray-700 hover:bg-gray-100" 
	               href="${pageContext.request.contextPath}/admin/logout">Logout</a>
	        </sec:authorize>
	    </div>
	</div>
    
</header>
