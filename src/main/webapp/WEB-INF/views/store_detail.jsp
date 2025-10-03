<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/views/templates/header.jsp"%>
<!-- Lấy user từ Spring Security -->
<sec:authentication property="principal" var="userPrincipal"/>
<c:set var="currentUserId" value="${userPrincipal.user.id}" />

<h2 class="page-title">${store.name}</h2>
<p>Địa chỉ: ${store.address}</p>
<p id="distance"></p>

<div class="flex-between mt-4">
	<div class="product-list">
		<h3 class="title">Sản phẩm</h3>
		<p class="order-time">Thời gian đặt hàng: ${store.orderStartTime}
			- ${store.orderEndTime}</p>

		<c:forEach var="product" items="${products}">
			<div class="product-item ${product.status == 2 ? 'status-disabled' : ''}"
				id="product-${product.id}" data-id="${product.id}"
				data-name="${product.name}" data-price="${product.price}">
				<img
					src="${pageContext.request.contextPath}/uploads/${not empty product.image ? product.image : '../images/food.jpg'}"
					alt="${product.name}" class="product-image">
				<div class="product-info">
					<div class="name-note">
						<div class="product-name">${product.name}</div>
						<p class="font-small">${product.description}</p>
					</div>
					<div class="product-price">${product.displayPrice}đ</div>
				</div>
				<button class="btn btn-add">+</button>
			</div>
		</c:forEach>
	</div>

	<div class="cart">
		<div class="detail-cart">
			<h3 class="title">Giỏ hàng</h3>
			<div id="cart-items">
				<div class="empty-cart">Giỏ hàng trống</div>
			</div>
			<div class="total-price">
				Tổng tiền: <span id="total">0</span> đ
			</div>
			<div class="d-flex justify-content-end">
				<button id="confirm-btn" class="confirm-btn">Xác nhận</button>
			</div>
		</div>
		<div class="ordered-products mt-4">
			<h3 class="title">Sản Phẩm Đã Đặt</h3>

			<table class="order-table">
				<thead>
					<tr>
						<th>Sản Phẩm</th>
						<th>Số Lượng</th>
						<th>Ghi Chú</th>
						<th>Người Đặt</th>
						<th>Thao tác</th>
					</tr>
				</thead>
				<tbody id="ordered-products-table">
					<c:set var="totalQuantity" value="0" />
					<c:forEach var="orderedProduct" items="${orderedProducts}">
						<tr>
							<td data-label="Sản Phẩm" class="product-name">${orderedProduct.productName}</td>
							<td data-label="Số Lượng" class="quantity"><span
								class="quantity-badge">${orderedProduct.quantity}</span></td>
							<td data-label="Ghi chú" class="note"><span class="note">${orderedProduct.note}</span></td>
							<td data-label="Người Đặt" class="customer-name">${orderedProduct.customerName}</td>
							<td data-label="Thao tác">
								<!-- Chỉ hiển thị nút xóa nếu user_id trùng khớp -->
								<c:if test="${orderedProduct.userId == currentUserId}">
									<button type="button" class="btn btn-danger btn-sm ms-1 btn-delete"
										data-id="${orderedProduct.id}">
										<i class="fas fa-trash"></i>
									</button>
								</c:if>
								<c:if test="${orderedProduct.userId != currentUserId}">
									<button type="button" class="btn btn-warning btn-sm ms-1 btn-ban">
										<i class="fa fa-ban" aria-hidden="true"></i>
									</button>
								</c:if>
							</td>
						</tr>
						<c:set var="totalQuantity"
							value="${totalQuantity + orderedProduct.quantity}" />
					</c:forEach>
					<tr class="font-bold bg-gray-100">
						<td colspan="1">Tổng cộng</td>
						<td colspan="1"><span class="quantity-badge">${totalQuantity}</span></td>
						<td colspan="3"></td>
					</tr>
				</tbody>
			</table>
		</div>

	</div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

  let cart = {};
  const currentUserId = ${userPrincipal.user.id};
  
  function updateCart() {
    const $cartItems = $('#cart-items');
    $cartItems.empty();
    let total = 0;

    if (Object.keys(cart).length === 0) {
      $cartItems.html('<div class="empty-cart">Giỏ hàng trống</div>');
    } else {
      $.each(cart, function(id, item) {
        const itemTotal = item.price * item.quantity;
        total += itemTotal;
        const priceFormatted = (item.price * item.quantity).toLocaleString('vi-VN');

        $cartItems.append(
     		'<div class="cart-item" data-id="' + id + '">' +
     		  '<div class="cart-info">' +
     		    '<div class="product-details">' +
     		      '<div class="product-name">' + item.name + '</div>' +
     		      '<div class="product-quality">Số lượng: ' + item.quantity + '</div>' +
     		      '<div class="product-price">' + priceFormatted + ' đ</div>' +
     		    '</div>' +
     		    '<textarea class="note-input" placeholder="Ghi chú...">' + (item.note || '') + '</textarea>' +
     		  '</div>' +
     		  '<button class="btn-minus">-</button>' +
     		'</div>'
     		);
      });
    }

    $('#total').text(total.toLocaleString('vi-VN'));
  }

  $(document).ready(function() {
	  if (navigator.geolocation) {
		    navigator.geolocation.getCurrentPosition(function(position) {
		        var latitude = position.coords.latitude;
		        var longitude = position.coords.longitude;
		        const storeId = "${store.id}";
		        console.log("Latitude: " + latitude + ", Longitude: " + longitude, storeId);

		        fetch('/api/distance', {
		            method: 'POST',
		            headers: {
		                'Content-Type': 'application/json'
		            },
		            body: JSON.stringify({
		                latCurrent: latitude,
		                lonCurrent: longitude,
		                storeId: storeId 
		            })
		        })
		        .then(response => response.json())
		        .then(data => {
		        	const distanceElement = document.getElementById("distance");
		            if (data.distance) {
		                distanceElement.textContent = "Khoảng cách từ vị trí hiện tại: " + data.distance + " km";
		            } else {
		                distanceElement.textContent = "Có lỗi khi tính khoảng cách.";
		            }
		        })
		        .catch(error => console.error('Error:', error));
		    }, function(error) {
		    	document.getElementById("distance").textContent = "Vui lòng bật định vị vị trí hiện tại để xem khoảng cách.";
		    });
		} else {
			document.getElementById("distance").textContent = "Trình duyệt của bạn không hỗ trợ định vị.";
		}


	  function isInOrderTime(start, end) {
		  const now = new Date();
		  const currentTime = now.getHours() * 60 + now.getMinutes();

		  const [startHour, startMinute] = start.split(':').map(Number);
		  const [endHour, endMinute] = end.split(':').map(Number);

		  const startTime = startHour * 60 + startMinute;
		  const endTime = endHour * 60 + endMinute;

		  return currentTime >= startTime && currentTime <= endTime;
		}

		const orderStart = "${store.orderStartTime}";
		const orderEnd = "${store.orderEndTime}";

		// Khai báo biến interval ở ngoài để có thể truy cập từ mọi nơi
		let orderTimeInterval;

		// Hàm kiểm tra và cập nhật trạng thái đặt hàng
		function checkOrderTime() {
			console.log('Checking order time...');
			if (!isInOrderTime(orderStart, orderEnd)) {
				console.log('Outside order time');
				$('.btn-add, .btn-minus').prop('disabled', true);
				if ($('.order-time-alert').length === 0) {
					$('.detail-cart .title').after('<div class="order-time-alert">Hiện tại ngoài giờ đặt hàng</div>');
				}
				// CHỈ thêm class cho sản phẩm KHÔNG bị disabled do status
				$('.product-item:not(.status-disabled)').addClass('time-disabled');
				// ẨN nút xóa và HIỆN nút ban cho tất cả các order
				$('.btn-delete').each(function() {
					const $deleteBtn = $(this);
					const $banBtn = $('<button type="button" class="btn btn-warning btn-sm ms-1 btn-ban"><i class="fa fa-ban" aria-hidden="true"></i></button>');
					$deleteBtn.replaceWith($banBtn);
				});
				if ($('#confirm-btn').length > 0) {
					$('#confirm-btn').remove();
				}
				if (orderTimeInterval) {
					clearInterval(orderTimeInterval);
					orderTimeInterval = null;
					console.log('Interval stopped - outside order time');
				}
			} else {
				console.log('Inside order time');
				$('.btn-add, .btn-minus').prop('disabled', false);
				$('.order-time-alert').remove();
				// CHỈ remove class time-disabled, giữ nguyên status-disabled
				$('.product-item').removeClass('time-disabled');
				$('.btn-delete').removeClass('time-disabled');
				if ($('#confirm-btn').length === 0 && Object.keys(cart).length > 0) {
					$('.detail-cart .d-flex').append('<button id="confirm-btn" class="confirm-btn">Xác nhận</button>');
				}
			}
		}

		// Kiểm tra lần đầu khi trang load
		checkOrderTime();

		// Chỉ bắt đầu interval nếu đang trong giờ đặt hàng
		if (isInOrderTime(orderStart, orderEnd)) {
			orderTimeInterval = setInterval(checkOrderTime, 60000);
			console.log('Interval started - inside order time');
		}

		// Kiểm tra khi người dùng quay lại tab
		$(window).on('focus', function() {
			console.log('Window focused, checking order time');
			checkOrderTime();
			// Nếu đang trong giờ đặt hàng và interval chưa chạy, bắt đầu lại
			if (isInOrderTime(orderStart, orderEnd) && !orderTimeInterval) {
				orderTimeInterval = setInterval(checkOrderTime, 60000);
				console.log('Interval restarted - inside order time');
			}
		});

		// Dọn dẹp interval khi rời trang
		$(window).on('beforeunload', function() {
			console.log('Cleaning up interval');
			if (orderTimeInterval) {
				clearInterval(orderTimeInterval);
				orderTimeInterval = null;
			}
		});

	  $('#cart-items').on('input', '.note-input', function () {
		  const id = $(this).closest('.cart-item').data('id');
		  const note = $(this).val();
		  if (cart[id]) {
		    cart[id].note = note;
		  }
		});

    $('.btn-add').on('click', function() {
      const $item = $(this).closest('.product-item');
      const id = $item.data('id');
      const name = $item.data('name');
      const price = parseInt($item.data('price'));
      const note = $item.find('.note-input').val() || '';
      console.log(note);

      if (cart[id]) {
        cart[id].quantity += 1;
      } else {
        cart[id] = { name, price, quantity: 1, note };
      }

      updateCart();
    });

    $('#cart-items').on('click', '.btn-minus', function() {
      const id = $(this).closest('.cart-item').data('id');
      if (cart[id]) {
        cart[id].quantity -= 1;
        if (cart[id].quantity <= 0) {
          delete cart[id];
        }
        updateCart();
      }
    });

    // Nút xác nhận
    $('#confirm-btn').on('click', function () {
   	  if (Object.keys(cart).length === 0) {
   	    alert('Giỏ hàng trống!');
   	    return;
   	  }

   	  const storeIdFromUrl = parseInt(window.location.pathname.split('/').pop());

   	  let total = 0;
   	  const items = [];

   	  for (let id in cart) {
   	    const item = {
   	      productId: parseInt(id),
   	      quantity: cart[id].quantity,
   	      price: cart[id].price,
   	   	  note: cart[id].note || ''
   	    };
   	    items.push(item);
   	    total += item.price * item.quantity;
   	  }

   	  const orderData = {
   	    items: items,
   	    total: total,
   	    storeId: storeIdFromUrl
   	  };

   	  console.log(orderData);

   	  $.ajax({
   	    url: '/order/create',
   	    type: 'POST',
   	    contentType: 'application/json',
   	    data: JSON.stringify(orderData),
   	    success: function (response) {
   	      cart = {};
   	      updateCart();
	   	  $('#ordered-products-table').empty();
	   	  let totalQuantity = 0;
	   	    response.forEach(function (item) {
	   	    	totalQuantity += item.quantity;
				// Kiểm tra quyền xóa dựa trên user_id
                const canDelete = item.userId === currentUserId;
                const deleteButton = canDelete 
                    ? '<button type="button" class="btn btn-danger btn-sm ms-1 btn-delete" data-id="' + item.id + '"><i class="fas fa-trash"></i></button>'
                    : '<button type="button" class="btn btn-warning btn-sm ms-1 btn-ban"> <i class="fa fa-ban" aria-hidden="true"></i></button>';
	   	    	var row = 
	   	    	    '<tr>' +
	   	    	        '<td data-label="Sản Phẩm" class="product-name">' + item.productName + '</td>' +
	   	    	        '<td data-label="Số Lượng" class="quantity"><span class="quantity-badge">' + item.quantity + '</span></td>' +
	   	    	     	'<td data-label="Ghi chú" class="note"><span class="note">' + (item.note || '') + '</span></td>' +
	   	    	        '<td data-label="Người Đặt" class="customer-name">' + item.customerName + '</td>' +
	   	    	        '<td data-label="Thao tác">' + deleteButton + '</td>' +
	   	    	    '</tr>';
	   	    	$('#ordered-products-table').append(row);
	   	    });
		   	 const totalRow = 
		         '<tr class="font-bold bg-gray-100">' +
		             '<td colspan="1">Tổng cộng</td>' +
		             '<td data-label="Số Lượng" colspan="1"><span class="quantity-badge">' + totalQuantity + '</span></td>' +
		             '<td colspan="3"></td>' +
		         '</tr>';
	
		     $('#ordered-products-table').append(totalRow);
   	    },
   	    error: function (error) {
   	      alert('Đặt hàng thất bại!');
   	      console.error(error);
   	    }
   	  });
   	});

    $('#ordered-products-table').on('click', '.btn-delete', function() {
        const id = $(this).data('id');
        if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
            $.ajax({
                url: '/orderItem/delete/' + id,
                type: 'POST',
                success: function(response) {
                    location.reload();
                },
                error: function(error) {
                    alert('Xóa sản phẩm thất bại!');
                    console.error(error);
                }
            });
        }
    });
  });
</script>
<%@ include file="/WEB-INF/views/templates/footer.jsp"%>