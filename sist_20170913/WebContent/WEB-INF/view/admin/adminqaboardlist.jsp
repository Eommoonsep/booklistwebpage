<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 커스텀 태그 파일(pre.tag) 사용을 위한 준비 --%>
<%@ taglib prefix="ct" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>SIST v1.0</title>

<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<style>
div.panel {
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.19);
}
</style>

<script>
	$(document).ready(function() {

		//툴팁 설정
		$('[data-toggle="tooltip"]').tooltip();

		//답변쓰기 버튼에 대한 이벤트 등록
		$(".btnReplyAdd").click(function() {
			
			//문제) 기존 글제목, 기존 글내용을 모달창의 입력폼에 출력하는 액션 추가
			var title = $(this).parents("tr").find("a").text();
	        var content_ = $(this).parents("tr").find("p:nth-child(2)").text();
	        $("#replyQABoardInsertForm #title").val(title);
	        $("#replyQABoardInsertForm #content_").text(content_);
	         
			//부모 글번호를 hidden 태그에 저장하는 액션 추가
			//replyqaboardinsert.it
			var qid = $(this).val();
			$("#replyQABoardInsertForm #qid").val(qid);
			//text(), html()이 아닌 val() 사용할 것.
			$("#replyQABoardInsertForm #replyContent").val("");
			$("#replyQABoardInsertForm").modal();
		});
	
		//답변글 보기 버튼에 대한 popover 등록
		//답변글에 대한 답변 동적 출력 액션 코드
		$("button.btnReply").popover({
			title:"관리자 답변",
			placement:"left",
			trigger:"focus",
			content:function() {
				
				//Ajax 요청 코드 추가
				//->동기 방식 요청
				var qid = $(this).val();
				var result;
				$.ajax({
					url : "qaboardajaxreply.it",
					data : {qid : qid},
					success : function(data) {
						result = data;
					},
					async : false
				});				
				//console.log("result:"+result);
				return result;
			}
		});

	});
</script>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header" style="padding: 10px;">
				<a href="${pageContext.request.contextPath}/adminmain.it"><img
					src="${pageContext.request.contextPath}/img/sist_logo.png"
					alt="sist_logo.png" style="vertical-align: bottom;"></a>
			</div>
			<div>
			
				<ul class="nav navbar-nav">
					<li><a
						href="${pageContext.request.contextPath}/adminmain.it">Home</a></li>
					<li><a href="#">과정</a></li>
					<li><a href="#">공지사항</a></li>
					<li class="active"><a
						href="${pageContext.request.contextPath}/adminqaboardlist.it">문의게시판</a></li>
					<li><a href="#">회원</a></li>
					<li><a href="#">상담</a></li>
					<li><a href="#">성적</a></li>
					<li><a href="#">교재</a></li>

					<!-- EL 표현을 이용해서 세션객체에 저장된 정보를 확인할 수 있다 -->
					<li><a href="${pageContext.request.contextPath}/logout.it">관리자(${sessionScope.logininfo.id_})
							로그아웃</a></li>

				</ul>

			</div>
		</div>
	</nav>

	<div class="container">
		<div class="panel-group">

			<div class="panel panel-default">
				<div class="panel-heading">
					교육 과정 및 교육원 관련 문의
					<button type="button" class="btn btn-default btn-xs"
						id="QABoardInsert" data-toggle="tooltip" data-placement="top"
						title="로그인 과정 없이 글쓰기가 가능합니다." disabled="disabled">글쓰기</button>
				</div>
				<div class="panel-body">
					<table class="table">
						<thead>
							<tr>
								<th>번호</th>
								<th class="col-md-8">제목</th>
								<th>작성일</th>
								<th>답변보기</th>
								<th>답변쓰기</th>
								<th>블라인드</th>
							</tr>
						</thead>
						<tbody>

							<c:forEach var="q" items="${qaboardlist}">
							<tr>
								<td>${q.qid}</td>
								<td>
									<%-- collapse 기능을 위해서 href="", data-toggle="" 속성 추가 --%>
									<a href="#demo${q.qid}" data-toggle="collapse">${q.title}</a>
								
									<%-- 비밀글인 경우 (비밀글) 표시 추가 --%>
									<c:if test="${q.privacy == 1}">
									<span class="text-danger small">(비밀글)</span>								
									</c:if>
								
									<%-- collapse 기능을 위해서 id="", class="" 속성 추가 --%>
									<div id="demo${q.qid}" class="collapse"
										style="width: 80%; margin: 10px;">

										<p>${q.clientip}</p>
										
										<%-- 글 내용 출력시 줄바꿈 처리 추가 필요 - 커스텀 태그 --%>
										<p><ct:pre value="${q.content_}" /></p>
										
									</div></td>
								<td>${q.writeday}</td>
								<td>

								<%-- 답변이 없는 경우는 버튼 비활성 상태로 지정, 답변이 있는 경우는 버튼 활성 상태로 지정 --%>
								<button type="button" 
										class="btn btn-default btn-xs btnReply ${(empty q.replyqid)?'disabled':''}"  ${(empty q.replyqid)?'disabled':''}  value="${q.replyqid}">답변보기</button>								
								
								</td>
								<td><button type="button"
										class="btn btn-default btn-xs btnReplyAdd" value="${q.qid}">답변쓰기</button></td>
								<td><input type="checkbox" class="blindCB"></td>
							</tr>
							</c:forEach>
							
						</tbody>
					</table>
					<div>
						<button type="button" class="btn btn-default btn-sm btnPicture">
							Total <span class="badge totalCount">1</span>
						</button>
						<button type="button" class="btn btn-default btn-sm btnPicture">
							Count <span class="badge totalCount">1</span>
						</button>
						<button type="button" class="btn btn-default btn-sm btnPicture">
							Blind <span class="badge blindCount">0</span>
						</button>
						<button type="button" class="btn btn-default btn-sm btnTotalPages">
							Pages <span class="badge totalPages">1</span>
						</button>
						<button type="button"
							class="btn btn-default btn-sm btnCurrentPage">
							Page <span class="badge currentPage">1</span>
						</button>
						<button type="button" class="btn btn-default btn-sm btnPrevious">Previous</button>
						<button type="button" class="btn btn-default btn-sm btnNext">Next</button>
					</div>
				</div>
			</div>


		</div>


		<!-- Modal -->
		<div id="replyQABoardInsertForm" class="modal fade" role="dialog">
			<div class="modal-dialog modal-lg">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">교육 과정 및 교육원 관련 문의에 대한 답변</h4>
					</div>
					<div class="modal-body">

						<div class="form-group">
							<label for="title">제목:</label>
							<input type="text" class="form-control" id="title"
								name="title" disabled="disabled" value="제목">
						</div>
						<div class="form-group">
							<label for="content">내용:</label>
							<textarea class="form-control" rows="10" id="content_"
								name="content_" disabled="disabled">글내용</textarea>
						</div>
						<form role="form"
							action="${pageContext.request.contextPath}/adminqaboardinsert.it"
							method="post">
							<input type="hidden" id="qid" name="qid" value="">
							<div class="form-group">
								<label for="replyContent">답변내용:</label>
								<textarea class="form-control" rows="10" id="replyContent"
									name="replyContent" placeholder="내용 (1000자 이내)" maxlength="1000"
									required="required"></textarea>
							</div>
							<button type="submit" class="btn btn-default btn-sm">Submit</button>
						</form>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default btn-sm"
							data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>


	</div>

	<footer class="container-fluid bg-4 text-center">
		<p>www.sist.co.kr 쌍용교육센터 since 1985</p>
	</footer>

</body>
</html>