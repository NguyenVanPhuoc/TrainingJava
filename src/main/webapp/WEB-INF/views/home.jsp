<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/views/templates/header.jsp"%>
<h2 class="page-title">Danh sách cửa hàng</h2>

<div class="restaurant-grid">
	<c:forEach var="store" items="${stores}">
		<a href="${pageContext.request.contextPath}/store/detail/${store.id}"
			class="restaurant-card-link">
			<div class="restaurant-card" data-store-id="${store.id}">
				<div class="restaurant-image">
					<img
						src="${pageContext.request.contextPath}/uploads/${store.image}"
						alt="${store.name}">
					<div class="restaurant-overlay">
						<span class="view-details">Xem chi tiết</span>
					</div>
				</div>
				<div class="restaurant-info">
					<h3 class="restaurant-name">${store.name}</h3>
					<p class="store-rating">
						Đánh giá: <span style="color: gold;"> <c:set
								var="fullStars" value="${store.rating - (store.rating % 1)}" />
							<c:set var="hasHalfStar"
								value="${store.rating % 1 >= 0.25 && store.rating % 1 < 0.75}" />
							<c:set var="emptyStars"
								value="${5 - fullStars - (hasHalfStar ? 1 : 0)}" />
							<c:forEach begin="1" end="${fullStars}" var="i">
								<i class="fas fa-star"></i>
							</c:forEach><c:if test="${hasHalfStar}">
								<i class="fas fa-star-half-alt"></i>
							</c:if><c:forEach begin="1" end="${emptyStars}"
								var="i">
								<i class="far fa-star"></i>
							</c:forEach>
						</span>
						<c:if test="${store.rating != null}">
						    (${store.rating})
						</c:if>

					</p>
					<p class="store-distance" id="distance-${store.id}">Đang lấy vị trí...</p>
				</div>
			</div>
		</a>
	</c:forEach>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
document.addEventListener("DOMContentLoaded", function () {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                const latCurrent = position.coords.latitude;
                const lonCurrent = position.coords.longitude;

                // Lấy tất cả storeId
                const storeCards = document.querySelectorAll('.restaurant-card');
                const storeIds = Array.from(storeCards).map(card => card.getAttribute('data-store-id'));

                fetch('/api/list/distances', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        latCurrent: latCurrent,
                        lonCurrent: lonCurrent,
                        storeIds: storeIds
                    })
                })
                .then(response => response.json())
                .then(data => {
                    for (const storeId in data.distances) {
                        const dist = data.distances[storeId];
                        const el = document.getElementById("distance-" + storeId);
                        if (el) {
                            el.textContent = "Khoản cách: " + dist.toFixed(2) + " km";
                        }
                    }
                })
                .catch(() => {
                    storeCards.forEach(card => {
                        const storeId = card.getAttribute('data-store-id');
                        document.getElementById("distance-" + storeId).textContent = "Không thể tính.";
                    });
                });
            },
            function () {
                document.querySelectorAll('.store-distance').forEach(el => {
                    el.textContent = "Vui lòng bật định vị.";
                });
            }
        );
    } else {
        document.querySelectorAll('.store-distance').forEach(el => {
            el.textContent = "Trình duyệt không hỗ trợ định vị.";
        });
    }
});
</script>

<%@ include file="/WEB-INF/views/templates/footer.jsp"%>