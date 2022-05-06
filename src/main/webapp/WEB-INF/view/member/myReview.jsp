<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<div class="default_width container-fluid">
	<div class="row">

		<div class="col-sm-2" id="memberinfo_left_nav">
			<a href="${pageContext.request.contextPath}/reservation/reservationList">
				예약내역<br> <br>
			</a>
			<a href="${pageContext.request.contextPath}/member/memberInfo">
				내정보관리<br> <br>
			</a>
			<a href="${pageContext.request.contextPath}/member/myReview">
				내가 작성한 리뷰<br> <br>
			</a>
		</div>

		<div class="col-sm-10">
			<c:forEach var="review" items="${reviewList}">
				<div id="review-box" style="padding: 20px 60px 20px 60px;">
					<div style="color: #c8c8c8; font-size: 14px">
						<div class="row">
							<div class="col-sm-6 review_star">
								<c:choose>
									<c:when test="${review.score == '1' }">⭐</c:when>
									<c:when test="${review.score == '2' }">⭐ ⭐</c:when>
									<c:when test="${review.score == '3' }">⭐ ⭐ ⭐</c:when>
									<c:when test="${review.score == '4' }">⭐ ⭐ ⭐ ⭐</c:when>
									<c:when test="${review.score == '5' }">⭐ ⭐ ⭐ ⭐ ⭐</c:when>
								</c:choose>
							</div>
							<div class="col-sm-6 review_email" style="text-align: right;">
								<span> ${fn:substring(review.review_date,0,4)} - ${fn:substring(review.review_date,4,6)} - ${fn:substring(review.review_date,6,8)}</span>
							</div>
						</div>

						<div class="review_email">
							<span>${review.bu_title}</span> - <span>${review.ro_name}</span> <br>
							<span>
								${fn:substring(review.checkin,0,4)}년  
								${fn:substring(review.checkin,4,6)}월  
								${fn:substring(review.checkin,6,8)}일 ~ 
								${fn:substring(review.checkout,0,4)}년 
								${fn:substring(review.checkout,4,6)}월 
								${fn:substring(review.checkout,6,8)}일 
							</span>
						</div>
					</div>

					<div class="review_content mt-1">
						<span>${review.content}</span>
					</div>
				</div>
				<hr style="border: 1px solid #dadada; margin: 0 40px 0 40px;">
				<!-- 리뷰 끝 -->
			</c:forEach>
		</div>
	</div>
</div>

</body>
</html>