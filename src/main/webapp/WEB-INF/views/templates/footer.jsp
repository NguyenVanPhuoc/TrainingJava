<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
</div>
<footer class="footer bg-success bg-opacity-10 py-4 mt-5 border-top border-success border-opacity-25">
  <div class="container">
    <div class="row">
      <!-- Logo and description -->
      <div class="col-lg-4 mb-4 mb-lg-0">
        <div class="footer-brand">
          <h3 class="mb-3">
            <span class="text-success fw-bold">Bầu</span> 
            <span class="text-dark fw-bold">Food</span>
          </h3>
          <p class="text-secondary">Mang đến cho bạn trải nghiệm ẩm thực tuyệt vời với những món ăn đậm đà hương vị Việt Nam.</p>
        </div>
      </div>
      
      <!-- Quick links -->
      <div class="col-6 col-lg-2 mb-4 mb-lg-0">
        <h6 class="text-uppercase mb-3 text-success">Liên kết</h6>
        <ul class="list-unstyled mb-0">
          <li class="mb-2"><a href="#" class="text-secondary text-decoration-none">Thực đơn</a></li>
          <li class="mb-2"><a href="/user/orders/unpaid" class="text-secondary text-decoration-none">Hóa đơn</a></li>
          <li class="mb-2"><a href="https://www.chatwork.com/#!rid261092654" class="text-secondary text-decoration-none" target="_blank">Liên hệ</a></li>
          <li class="mb-2"><a href="/user/orders/unpaid" class="text-secondary text-decoration-none">Hồ sơ</a></li>
        </ul>
      </div>
      
      <!-- Contact information -->
      <div class="col-6 col-lg-3 mb-4 mb-lg-0">
        <h6 class="text-uppercase mb-3 text-success">Liên hệ</h6>
        <ul class="list-unstyled mb-0">
          <li class="mb-2 d-flex align-items-center text-left">
            <i class="bi bi-geo-alt-fill me-2 text-success"></i>
            <span class="text-secondary">79 Đ. Dũng Sĩ Thanh KhêThanh Khê Tây, Thanh Khê, Đà Nẵng</span>
          </li>
          <li class="mb-2 d-flex align-items-center">
            <i class="bi bi-telephone-fill me-2 text-success"></i>
            <a href="tel:+0363906700" class="text-secondary">0363906700</a>
          </li>
          <li class="mb-2 d-flex align-items-center">
            <i class="bi bi-envelope-fill me-2 text-success"></i>
            <span class="text-secondary">phuocnv.nta@gmail.com</span>
          </li>
        </ul>
      </div>
      
      <!-- Newsletter -->
      <div class="col-lg-3">
        <h6 class="text-uppercase mb-3 text-success">Đăng ký nhận tin</h6>
        <p class="text-secondary mb-3">Đăng ký để nhận thông tin khuyến mãi mới nhất</p>
        <div class="input-group mb-3">
          <input type="email" class="form-control" placeholder="Email của bạn" aria-label="Email của bạn">
          <button class="btn btn-success" type="button">Đăng ký</button>
        </div>
        
        <!-- Social links -->
        <div class="social-links mt-3">
          <a href="#" class="text-success me-3" title="Facebook">
            <i class="bi bi-facebook fs-5"></i>
          </a>
          <a href="#" class="text-success me-3" title="Instagram">
            <i class="bi bi-instagram fs-5"></i>
          </a>
          <a href="#" class="text-success" title="Twitter">
            <i class="bi bi-twitter-x fs-5"></i>
          </a>
        </div>
      </div>
    </div>
    
    <hr class="my-4 border-success border-opacity-25">
    
    <!-- Copyright -->
    <div class="row">
      <div class="col-md-6 text-center text-md-start">
        <p class="small text-secondary mb-md-0">&copy; 2025 Bầu Food. Tất cả quyền được bảo lưu.</p>
      </div>
      <div class="col-md-6 text-center text-md-end">
        <ul class="list-inline mb-0 small">
          <li class="list-inline-item"><a href="#" class="text-secondary">Điều khoản sử dụng</a></li>
          <li class="list-inline-item"><span class="text-secondary mx-2">|</span></li>
          <li class="list-inline-item"><a href="#" class="text-secondary">Chính sách bảo mật</a></li>
        </ul>
      </div>
    </div>
  </div>
</footer>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script>
  function toggleMenu() {
    const menu = document.getElementById('logoutMenu');
    menu.classList.toggle('d-none');
  }

  document.addEventListener('click', function(event) {
    const avatar = document.getElementById('avatar');
    const menu = document.getElementById('logoutMenu');
    if (avatar && menu && !avatar.contains(event.target) && !menu.contains(event.target)) {
      menu.classList.add('d-none');
    }
  });
</script>
</body>
</html>