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
                    <h3><i class="fas fa-plus me-2"></i>Thêm mới sản phẩm</h3>
                </div>
                <div class="card-body">
                    <div class="form-container">
                        <form:form
						    action="${pageContext.request.contextPath}/admin/stores/${storeId}/products/save"
						    method="post"
						    modelAttribute="product"
						    enctype="multipart/form-data">

                            <!-- Tên sản phẩm -->
                            <div class="form-group mb-3">
                                <label for="name" class="form-label required-field">Tên sản phẩm</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-box"></i></span>
                                    <form:input path="name" cssClass="form-control" id="name" placeholder="Nhập tên sản phẩm" />
                                </div>
                                <form:errors path="name" cssClass="text-danger small" />
                            </div>

                            <!-- Giá sản phẩm -->
                            <div class="form-group mb-3">
                                <label for="price" class="form-label required-field">Giá</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-dollar-sign"></i></span>
                                    <form:input path="price" cssClass="form-control" id="price" placeholder="Nhập giá sản phẩm" />
                                </div>
                                <form:errors path="price" cssClass="text-danger small" />
                            </div>

                            <!-- Mô tả sản phẩm -->
                            <div class="form-group mb-3">
                                <label for="description" class="form-label">Mô tả</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-align-left"></i></span>
                                    <form:textarea path="description" cssClass="form-control" id="description" placeholder="Nhập mô tả sản phẩm" rows="4" />
                                </div>
                                <form:errors path="description" cssClass="text-danger small" />
                            </div>

                            <!-- Trạng thái -->
                            <div class="form-group mb-3">
                                <label for="status" class="form-label required-field">Trạng thái</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-toggle-on"></i></span>
                                    <form:select path="status" cssClass="form-select" id="status" required="true">
                                        <form:option value="1" label="Còn hàng" />
                                        <form:option value="2" label="Hết hàng" />
                                    </form:select>
                                </div>
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


                            <!-- Buttons -->
                            <div class="form-group d-flex justify-content-between mt-4">
                                <a href="${pageContext.request.contextPath}/admin/stores/${storeId}/products" class="btn btn-secondary">
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
