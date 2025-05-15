<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="/WEB-INF/views/templates/header.jsp"%>

<fmt:setLocale value="vi_VN" />
<h3 class="title_unpaid">Hóa đơn chưa thanh toán</h3>
<div class="list-unpaid">
	<c:forEach var="order" items="${orders}">
		<div class="order-card-unpaid">
			<div class="order-header">
				<div class="order-date">
					Ngày đặt hàng:
					<fmt:formatDate value="${order.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm" />
				</div>
			</div>

			<table class="table">
				<thead>
					<tr>
						<th style="text-align: left;">Sản phẩm</th>
						<th style="text-align: center;">Số lượng</th>
						<th style="text-align: right;">Giá</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${order.items}">
						<tr>
							<td class="product-name">${item.product.name}</td>
							<td class="product-quantity"><span>${item.quantity}</span></td>
							<td class="product-price"><fmt:formatNumber
									value="${item.quantity * item.price}" type="number"
									maxFractionDigits="0" groupingUsed="true" /> đ</td>
						</tr>
					</c:forEach>

					<!-- Tổng giá của order này -->
					<tr class="order-subtotal-row">
						<td colspan="2" class="order-subtotal-label">Tổng giá đơn
							hàng:</td>
						<td class="order-subtotal-amount"><fmt:formatNumber
								value="${order.totalPrice}" type="number" maxFractionDigits="0"
								groupingUsed="true" /> đ</td>
					</tr>
				</tbody>
			</table>
		</div>
	</c:forEach>

	<!-- Tổng cộng tất cả đơn hàng chưa thanh toán -->
	<div class="total-all-orders">
		<div class="total-all-label">Tổng cộng tất cả đơn hàng:</div>
		<div class="total-all-amount">
			<fmt:formatNumber value="${totalPrice}" type="number"
				maxFractionDigits="0" groupingUsed="true" />
			đ
		</div>
	</div>
</div>

<%@ include file="/WEB-INF/views/templates/footer.jsp"%>