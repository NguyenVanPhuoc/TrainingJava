<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<div id="contact-section" class="contact-section">
    <h3 class="page-title-contact">Liên hệ với tôi</h3>
    
    <div class="contact-container">
        <div class="contact-info">
            <div class="contact-item">
                <div class="contact-icon">
                    <i class="fas fa-map-marker-alt"></i>
                </div>
                <div class="contact-details">
                    <h3>Địa chỉ</h3>
                    <p>79 Đ. Dũng Sĩ Thanh KhêThanh Khê Tây, Thanh Khê, Đà Nẵng</p>
                </div>
            </div>
            
            <div class="contact-item">
                <div class="contact-icon">
                    <i class="fas fa-phone"></i>
                </div>
                <div class="contact-details">
                    <h3>Điện thoại</h3>
                    <a href="tel:0363906700">0363906700</a>
                </div>
            </div>
            
            <div class="contact-item">
                <div class="contact-icon">
                    <i class="fas fa-envelope"></i>
                </div>
                <div class="contact-details">
                    <h3>Email</h3>
                    <p>phuocnv.nta@gmail.com</p>
                    <p>vanphuoc260797@gmail.com</p>
                </div>
            </div>
            <div class="contact-item">
                <div class="contact-icon">
                    <i class="fas fa-share-alt"></i>
                </div>
                <div class="contact-details">
                    <h3>Mạng xã hội</h3>
                    <p>Kết nối với tôi qua</p>
                    <div class="social-web">
                        <a href="#" class="social-link facebook">
                            <i class="fab fa-facebook-f"></i>
                        </a>
                        <a href="#" class="social-link zalo">
                            <i class="fab fa-facebook-messenger"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <div class="contact-social">
            
            <div class="contact-item">
                <div class="contact-details">
                    <h3>Thông tin ngân hàng</h3>
                    <p>Quét mã QR để chuyển khoản</p>
                    <div class="bank-info">
                        <div class="qr-code">
                           <img src="${pageContext.request.contextPath}/images/qr.JPG" alt="QR">
                        </div>
                        <div class="bank-details">
                            <p><strong>Ngân hàng: </strong>ACB</p>
                            <p><strong>Số tài khoản: </strong>21675957</p>
                            <p><strong>Chủ tài khoản: </strong>NGUYỄN VĂN PHƯỚC</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
