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
                    <h3><i class="fas fa-user-plus me-2"></i>Thêm mới thành viên</h3>
                </div>
                <div class="card-body">
                    <div class="form-container">
                        <form:form action="${pageContext.request.contextPath}/admin/users/create" modelAttribute="user" method="post" enctype="multipart/form-data">

                            <!-- Username -->
                            <div class="form-group">
                                <label for="username" class="form-label required-field">Username</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <form:input path="username" cssClass="form-control" id="username" placeholder="Nhập username"/>
                                </div>
                                <form:errors path="username" cssClass="text-red-500 error-validate" />
                            </div>

                            <!-- Email -->
                            <div class="form-group">
                                <label for="email" class="form-label required-field">Email</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                    <form:input path="email" type="email" cssClass="form-control" id="email" placeholder="example@domain.com"/>
                                </div>
                                <form:errors path="email" cssClass="text-red-500 error-validate" />
                            </div>

                            <!-- Full Name -->
                            <div class="form-group">
                                <label for="name" class="form-label required-field">Họ và tên</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                                    <form:input path="name" cssClass="form-control" id="name" placeholder="Nhập họ và tên đầy đủ"/>
                                </div>
                                <form:errors path="name" cssClass="text-red-500 error-validate" />
                            </div>

                            <!-- Role -->
                            <div class="form-group">
                                <label for="role" class="form-label required-field">Vai trò</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user-shield"></i></span>
                                    <form:select path="role" cssClass="form-select" id="role" required="true">
                                        <form:option value="1" label="Admin"/>
                                        <form:option value="2" label="User"/>
                                    </form:select>
                                </div>
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

                            <!-- Address -->
                            <div class="form-group">
                                <label for="address" class="form-label">Địa chỉ</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-map-marker-alt"></i></span>
                                    <form:input path="address" cssClass="form-control" id="address" placeholder="Nhập địa chỉ" />
                                </div>
                                <form:errors path="address" cssClass="text-red-500 error-validate" />
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

                            <!-- Password -->
                            <div class="form-group">
                                <label for="password" class="form-label required-field">Mật khẩu</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                    <form:password path="password" cssClass="form-control" id="password" placeholder="Nhập mật khẩu"
                                                   onkeyup="checkPasswordStrength(this.value)" />
                                    <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </div>
                                <form:errors path="password" cssClass="text-red-500 error-validate" />                               
                            </div>

                            <!-- Buttons -->
                            <div class="form-group d-flex justify-content-between mt-4">
                                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-back">
                                    <i class="fas fa-arrow-left me-2"></i>Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
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
