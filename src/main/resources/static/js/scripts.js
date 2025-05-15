(function() {
	setTimeout(function () {
        const alertEl = document.querySelector(".success-message");
        if (alertEl) {
            alertEl.classList.remove("show");
            alertEl.classList.add("fade");
            setTimeout(() => alertEl.remove(), 300);
        }
    }, 10000);
	
		
    'use strict';
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();

document.addEventListener("DOMContentLoaded", function() {
    const fileInput = document.querySelector('input[type="file"][name="imageFile"]');
    const previewImage = document.getElementById('previewImage');
    const previewContainer = document.getElementById('previewContainer');

    if (fileInput && previewImage && previewContainer) {
        fileInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    previewImage.src = e.target.result;
                    previewContainer.style.display = "block";
                };
                reader.readAsDataURL(file);
            }
        });
    }
});


document.addEventListener("DOMContentLoaded", function () {
    const table = document.querySelector("table");
    if (!table) return;

    const headers = table.querySelectorAll("th");
    let currentSort = {
        field: null,
        direction: 1
    };

    headers.forEach(header => {
        const icon = header.querySelector("i"); // Lấy icon trong tiêu đề cột
        if (icon) {
            icon.classList.add("fa-sort"); // Đảm bảo icon mặc định là fa-sort
        }

        header.addEventListener("click", function () {
            const field = this.getAttribute("data-sort");
            const rows = Array.from(table.querySelectorAll("tbody tr"));
            const colIndex = Array.from(this.parentNode.parentNode.children).indexOf(this.parentNode);

            if (currentSort.field === field) {
                currentSort.direction *= -1; // Chuyển hướng sắp xếp nếu đã sort cùng cột
            } else {
                currentSort.field = field;
                currentSort.direction = 1; // Mặc định sort tăng dần
            }

            rows.sort((a, b) => {
                let cellA = a.children[colIndex].textContent.trim();
                let cellB = b.children[colIndex].textContent.trim();

                // Kiểm tra xem giá trị có phải là số hay không
                const isNumeric = !isNaN(cellA) && !isNaN(cellB);
                if (isNumeric) {
                    cellA = parseFloat(cellA);
                    cellB = parseFloat(cellB);
                }

                if (cellA < cellB) return -1 * currentSort.direction;
                if (cellA > cellB) return 1 * currentSort.direction;
                return 0;
            });

            // Cập nhật lại nội dung tbody với các dòng đã được sắp xếp
            const tbody = table.querySelector("tbody");
            tbody.innerHTML = "";
            rows.forEach(row => tbody.appendChild(row));

            // Thêm icon cho cột đã được sort
            headers.forEach(header => {
                const icon = header.querySelector("i");
                if (icon) {
                    if (header === this) {
                        // Thêm/loại bỏ các icon phù hợp
                        if (currentSort.direction === 1) {
                            icon.classList.remove("fa-sort", "fa-sort-desc");
                            icon.classList.add("fa-sort-asc");
                        } else {
                            icon.classList.remove("fa-sort", "fa-sort-asc");
                            icon.classList.add("fa-sort-desc");
                        }
                    } else {
                        icon.classList.remove("fa-sort-asc", "fa-sort-desc");
                        icon.classList.add("fa-sort");
                    }
                }
            });
        });
    });
});


// Password toggle visibility
const togglePassword = document.getElementById('togglePassword');
if (togglePassword) {
    togglePassword.addEventListener('click', function() {
        const passwordInput = document.getElementById('password');
        const icon = this.querySelector('i');
        
        if (passwordInput && icon) {
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        }
    });
}