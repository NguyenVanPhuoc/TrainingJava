<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card">
                <div class="card-header d-flex align-items-center">
                    <h3><i class="fas fa-user-plus me-2"></i>Thêm mới cửa hàng</h3>
                </div>
                <div class="card-body">
                    <div class="form-container">
                        <form:form action="${pageContext.request.contextPath}/admin/stores/save" modelAttribute="store" method="post" enctype="multipart/form-data">

                            <!-- name -->
                            <div class="form-group">
                                <label for="name" class="form-label required-field">Tên cửa hàng</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <form:input path="name" cssClass="form-control" id="name" placeholder="Nhập tên cửa hàng"/>
                                </div>
                                <form:errors path="name" cssClass="text-red-500 error-validate" />
                            </div>

                            <!-- slug -->
                            <div class="form-group">
                                <label for="slug" class="form-label required-field">Slug</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-link"></i></span>
                                    <form:input path="slug" cssClass="form-control" id="slug" placeholder="Nhập slug"/>
                                </div>
                                <form:errors path="slug" cssClass="text-red-500 error-validate" />
                            </div>
                            
                            <!-- Address -->
                            <div class="form-group">
                                <label for="address" class="form-label required-field">Địa chỉ</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-map-marker-alt"></i></span>
                                    <form:input path="address" cssClass="form-control" id="address" placeholder="Nhập địa chỉ" />
                                </div>
                                <form:errors path="address" cssClass="text-red-500 error-validate" />
                            </div>
                            
                            <!-- Thời gian bắt đầu đặt hàng -->
							<div class="form-group">
							    <label for="orderStartTime" class="form-label required-field">Giờ bắt đầu đặt hàng</label>
							    <div class="input-group">
							        <span class="input-group-text"><i class="fas fa-clock"></i></span>
							        <form:input path="orderStartTime" cssClass="form-control" id="orderStartTime" type="text" />
							    </div>
							    <form:errors path="orderStartTime" cssClass="text-red-500 error-validate" />
							</div>
							
							<!-- Thời gian kết thúc đặt hàng -->
							<div class="form-group">
							    <label for="orderEndTime" class="form-label required-field">Giờ kết thúc đặt hàng</label>
							    <div class="input-group">
							        <span class="input-group-text"><i class="fas fa-clock"></i></span>
							        <form:input path="orderEndTime" cssClass="form-control" id="orderEndTime" type="time" />
							    </div>
							    <form:errors path="orderEndTime" cssClass="text-red-500 error-validate" />
							</div>
                            
                            <!-- Phone -->
                            <div class="form-group">
                                <label for="phone" class="form-label">Số điện thoại</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                    <form:input path="phone" cssClass="form-control" id="phone" placeholder="Nhập số điện thoại" />
                                </div>
                                <form:errors path="phone" cssClass="text-red-500 error-validate" />
                            </div>
                            
                            <!-- Rating -->
							<div class="form-group">
							    <label for="rating" class="form-label">Đánh giá cửa hàng (0 - 5)</label>
							    <div class="input-group">
							        <span class="input-group-text"><i class="fas fa-star"></i></span>
							        <form:input path="rating" type="number" step="0.1" min="0" max="5" cssClass="form-control" id="rating" placeholder="Nhập điểm đánh giá ví dụ 4.5" />
							    </div>
							    <form:errors path="rating" cssClass="text-red-500 error-validate" />
							</div>
                            
                            <!-- Upload hình ảnh -->
							<div class="form-group mb-2">
							    <label for="image" class="form-label">Hình ảnh sản phẩm</label>
							    <div class="input-group">
							        <span class="input-group-text"><i class="fas fa-image"></i></span>
							        <input type="file" class="form-control" name="imageFile" id="image" accept="image/*" />
							    </div>
							
							    <!-- Hình ảnh preview -->
							    <div class="form-group mb-4" id="previewContainer" style="display: none;">
							        <img id="previewImage" 
							             src=""
							             alt="Ảnh sản phẩm" 
							             class="img-thumbnail"
							             style="max-width: 150px; margin-top: 10px;"
							        />
							    </div>
							</div>

                            <!-- Trạng thái -->
                            <div class="form-group">
                                <label for="status" class="form-label required-field">Trạng thái</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-toggle-on"></i></span>
                                    <form:select path="status" cssClass="form-select" id="status" required="true">
                                        <form:option value="1" label="Hoạt động"/>
                                        <form:option value="2" label="Ngừng hoạt động"/>
                                    </form:select>
                                </div>
                            </div>
                            
                            <!-- Buttons -->
                            <div class="form-group d-flex justify-content-between mt-4">
                                <a href="${pageContext.request.contextPath}/admin/stores" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Quay lại
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save me-2"></i>Lưu
                                </button>
                            </div>

                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
