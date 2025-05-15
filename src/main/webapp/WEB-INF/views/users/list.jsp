<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<c:if test="${not empty successMessage}">
    <div class="alert alert-success alert-dismissible fade show success-message" role="alert">
        ${successMessage}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>
<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h2><i class="fas fa-users me-2"></i>Danh sách thành viên</h2>
        <div>
            <button type="button" class="btn btn-danger btn-sm me-2" id="deleteSelectedBtn" disabled>
                <i class="fas fa-trash me-1"></i>Xóa đã chọn
            </button>
            <a href="${pageContext.request.contextPath}/admin/users/create" class="btn btn-primary btn-sm">
                <i class="fas fa-plus me-1"></i>Thêm mới
            </a>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/admin/users" method="GET" class="search-bar">
         <input type="text" class="input-key" name="keyword" placeholder="Tìm kiếm" value="${keyword}" />
         <select name="status" class="select-status">
	        <option value="">Tất cả</option>
	        <option value="1" ${status == 1 ? 'selected' : ''}>Kích hoạt</option>
	        <option value="2" ${status == 2 ? 'selected' : ''}>Không kích hoạt</option>
	    </select>
         <button type="submit" class="btn btn-primary">Search</button>
     </form>

    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover user-table">
                <thead>
                    <tr>
                        <th>
                            <input type="checkbox" id="selectAll" class="form-check-input">
                        </th>
                        <th>ID</th>
                        <th>Họ và Tên</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Địa chỉ</th>
                        <th>Vai trò</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>
                                <input type="checkbox" class="form-check-input item-checkbox" value="${user.id}">
                            </td>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.phone}</td>
                            <td>${user.address}</td>
                            <td>${user.roleName}</td>
                            <td>${user.statusName}</td>
                            <td class="action-buttons">
                                <a href="${pageContext.request.contextPath}/admin/users/edit/${user.id}" class="btn-edit btn btn-primary" title="Sửa">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm ms-1 delete-btn" data-item-id="${user.id}" title="Xóa">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty users}">
	                    <tr>
	                        <td colspan="10" style="text-align: center;">Không có dữ liệu</td>
	                    </tr>
	                </c:if>
                </tbody>
            </table>
        </div>
        <%@ include file="/WEB-INF/views/common/pagination.jsp" %>
    </div>
</div>

<c:set var="itemName" value="thành viên" />
<c:set var="deleteUrl" value="/admin/users/delete/" />
<c:set var="deleteMultipleUrl" value="/admin/users/delete-multiple" />
<%@ include file="/WEB-INF/views/common/delete-modals.jsp" %>

<script src="${pageContext.request.contextPath}/js/list-actions.js"></script>
