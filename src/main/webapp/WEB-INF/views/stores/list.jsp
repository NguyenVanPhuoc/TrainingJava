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
        <h2><i class="fas fa-users me-2"></i>Danh sách cửa hàng</h2>
        <div>
            <button type="button" class="btn btn-danger btn-sm me-2" id="deleteSelectedBtn" disabled>
                <i class="fas fa-trash"></i> Xóa đã chọn
            </button>
            <a href="${pageContext.request.contextPath}/admin/stores/create" class="btn btn-primary btn-sm">
                <i class="fas fa-plus me-1"></i>Thêm mới
            </a>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/admin/stores" method="GET" class="search-bar">
         <input type="text" class="input-key" name="keyword" placeholder="Tìm kiếm" value="${keyword}" />
         <select name="status" class="select-status">
            <option value="">Tất cả</option>
            <option value="1" ${status == 1 ? 'selected' : ''}>Hoạt động</option>
            <option value="2" ${status == 2 ? 'selected' : ''}>Ngừng hoạt động</option>
        </select>
         <button type="submit" class="btn btn-primary">Search</button>
     </form>

    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover user-table">
                <thead>
                    <tr>
                        <th width="50"><input type="checkbox" class="form-check-input" id="selectAll"></th>
                        <th>ID</th>
                        <th>Tên</th>
                        <th>Số điện thoại</th>
                        <th>Địa chỉ</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="store" items="${stores}">
                        <tr>
                            <td><input type="checkbox" class="form-check-input item-checkbox" value="${store.id}"></td>
                            <td>${store.id}</td>
                            <td>${store.name}</td>
                            <td>${store.phone}</td>
                            <td>${store.address}</td>
                            <td>${store.statusName}</td>
                            <td class="action-buttons">
                                <a href="${pageContext.request.contextPath}/admin/stores/${store.id}/products" class="btn btn-info" title="Xem sản phẩm">
                                    <i class="fas fa-box-open"></i>
                                </a>        
                                <a href="${pageContext.request.contextPath}/admin/stores/edit/${store.id}" class="btn-edit btn btn-primary" title="Sửa">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm ms-1 delete-btn" data-item-id="${store.id}">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty stores}">
                        <tr>
                            <td colspan="7" style="text-align: center;">Không có dữ liệu</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- Set variables for delete modal -->
        <c:set var="itemName" value="cửa hàng" />
        <c:set var="deleteUrl" value="/admin/stores/delete/" />
        <c:set var="deleteMultipleUrl" value="/admin/stores/delete-multiple" />
        <%@ include file="/WEB-INF/views/common/delete-modals.jsp"%>
        <input type="hidden" id="selectedItemIds" name="selectedItemIds" value="">

        <%@ include file="/WEB-INF/views/common/pagination.jsp" %>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/list-actions.js"></script>
