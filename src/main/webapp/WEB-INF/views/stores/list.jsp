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
                                <a href="#" class="btn btn-warning craw-data" title="Crawl dữ liệu" data-store-id="${store.id}">
                                    <i class="fas fa-spider"></i>
                                </a>
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

        <!-- Modal Crawl Data -->
        <div class="modal fade" id="crawlModal" tabindex="-1" aria-labelledby="crawlModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="crawlModalLabel">Crawl Dữ Liệu GrabFood</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="crawlForm">
                            <input type="hidden" id="storeIdInput">
                            <div class="mb-3">
                                <label for="urlInput" class="form-label">URL GrabFood:</label>
                                <input type="url" class="form-control" id="urlInput" 
                                    placeholder="https://food.grab.com/vn/vi/restaurant/..." required>
                            </div>
                        </form>
                        <div id="crawlResult" class="mt-3" style="display: none;"></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="button" class="btn btn-primary" id="startCrawlBtn">Bắt đầu Crawl</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$(document).ready(function($) {
    let currentStoreId = null;
    
    // Xử lý khi click vào nút crawl data
    $('.craw-data').click(function(e) {
        e.preventDefault();
        currentStoreId = $(this).data('store-id');
        $('#storeIdInput').val(currentStoreId);
        $('#urlInput').val('');
        $('#crawlResult').hide().empty();
        $('#crawlModal').modal('show');
    });
    
    // Xử lý khi click nút bắt đầu crawl
    $('#startCrawlBtn').click(function() {
        const url = $('#urlInput').val().trim();
        
        if (!url) {
            alert('Vui lòng nhập URL GrabFood');
            return;
        }
        
        // Hiển thị loading
        $('#startCrawlBtn').prop('disabled', true).html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Đang xử lý...');
        
        // Gọi API crawl
        $.ajax({
            url: '${pageContext.request.contextPath}/crawl/store',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                storeId: currentStoreId,
                url: url
            }),
            success: function(response) {
                const message = response.message || 'Crawl dữ liệu thành công';
                const count = response.productsCount || 0;
                $('#crawlResult').show().html(
                    '<div class="alert alert-success">' +
                    '<h6>' + message + '</h6>' +
                    '<p>Đã thêm ' + count + ' sản phẩm vào cửa hàng.</p>' +
                    '</div>'
                );
                
                $('#startCrawlBtn').prop('disabled', false).html('Bắt đầu Crawl');
                
                // Tự động đóng modal sau 5 giây
                setTimeout(function() {
                    $('#crawlModal').modal('hide');
                }, 5000);
            },
            error: function(xhr) {
                let errorMessage = 'Có lỗi xảy ra khi crawl dữ liệu';
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                } else if (xhr.status === 502) {
                    errorMessage = 'Không thể kết nối đến trang GrabFood. Vui lòng kiểm tra lại URL.';
                }
                
                $('#crawlResult').show().html(`
                    <div class="alert alert-danger">
                        <h6>Crawl thất bại!</h6>
                        <p>${errorMessage}</p>
                    </div>
                `);
                $('#startCrawlBtn').prop('disabled', false).html('Bắt đầu Crawl');
            }
        });
    });
    
    // Reset form khi modal đóng
    $('#crawlModal').on('hidden.bs.modal', function() {
        $('#crawlForm')[0].reset();
        $('#crawlResult').hide().empty();
        $('#startCrawlBtn').prop('disabled', false).html('Bắt đầu Crawl');
    });
});
</script>
