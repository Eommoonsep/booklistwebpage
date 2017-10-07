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
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<!-- 외부 스크립트 파일 연결 -> 주의) 연결 순서 중요 -->
<script src="${pageContext.request.contextPath}/script/util.js"></script>

<script src="http://maps.googleapis.com/maps/api/js"></script>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<style>
div.panel {
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.19);
}
</style>

<script>
	$(document).ready(function() {
				
				
				//글쓰기 버튼에 대한 툴팁 액션
				$('[data-toggle="tooltip"]').tooltip();

				//글쓰기 버튼에 대한 클릭 이벤트
				$("#QBBoardInsert").on("click", function() {
					$("#myQABoardInsertForm").modal();
				});
				
				//------------------------------------
				//글쓰기 성공(100), 실패 메시지(101) 반환
				if ("${param.code}" == "100") {
					$(".insert-success").css("display", "inline");
				} else if ("${param.code}" == "101") {
					$(".insert-fail").css("display", "inline");
				} 
				//------------------------------------


				//패스워드를 선택적으로 입력받기 위한 체크박스 액션
				//체크박스에 change 이벤트 등록
				$("#undercover").on("change", function() {
					//체크박스가 선택된 상태(checked)인지 확인하는 방법
					if ($(this).is(":checked")) {
						///show() 메소드 특정 영역이 화면에 나타나도록 설정
						$("#myQABoardInsertForm").find("#pwForm").show();
						//removeAttr() 메소드 기존 속성 제거
						$("#myQABoardInsertForm").find("#pwForm").find("#pw").removeAttr("disabled");
					} else {
						///hide() 메소드 특정 영역이 화면에 보이지 않도록 설정
						$("#myQABoardInsertForm").find("#pwForm").hide();
						$("#myQABoardInsertForm").find("#pwForm").find("#pw").attr("disabled", "disabled");
					}
				});
				
				
				//비밀글 확인을 위한 패스워드 전송 버튼에 대한 클릭 이벤트
				$(".btnSubmit").on("click", function() {
					
					//문제) 
					//글번호 확인
					//패스워드 확인 -> 현재 버튼의 부모 폼 하위 요소
					//-> Ajax 요청시 데이터 전송
					//-> $.get()-비동기 or $.ajax()-비동기, 동기
					//-> Ajax 응답 결과(두 가지)를 화면에 출력
					
					
					var qid = $(this).val();
					var pw = $(this).parents("form").find("#pw").val();
					var btnSubmit = $(this);
					
					$.get("qaboardajaxcontent.it", {qid:qid, pw:pw}, function(data) {
						
						var result = data;
						
						if (result == "ERROR") {
							//에러 메시지 출력
							 $(btnSubmit).parents("form").find(".fail").css("display", "inline");
						} else {
							//서버로부터 응답받은 결과를 출력
							$(btnSubmit).parents("div.collapse").html("<p>"+result+"</p>");
							$(btnSubmit).parents("form").remove();
						}
					
					});
					
					
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
				
				

	}); //ready 이벤트
</script>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header" style="padding:10px;">
				<a href="${pageContext.request.contextPath}/main.it"><img src="${pageContext.request.contextPath}/img/sist_logo.png" alt="sist_logo.png" style="vertical-align: bottom;"></a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li><a
						href="${pageContext.request.contextPath}/main.it">Home</a></li>
					<li><a
						href="${pageContext.request.contextPath}/sistintro.it">센터소개</a></li>
					<li><a
						href="${pageContext.request.contextPath}/sisteducation.it">교육과정
							소개</a></li>
					<li class="active"><a
						href="${pageContext.request.contextPath}/qaboardlist.it">문의게시판</a></li>
					<li><a href="#" data-toggle="modal"
						data-target="#myLoginFormModal">회원가입/로그인</a></li>
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
						id="QBBoardInsert" data-toggle="tooltip" data-placement="top"
						title="로그인 과정 없이 글쓰기가 가능합니다.">글쓰기</button>
						
						<span class="alert alert-success insert-success"
							style="padding: 8px; display: none;"> <strong>Success!</strong>
							글쓰기가 성공했습니다.
						</span> <span class="alert alert-danger insert-fail"
							style="padding: 8px; display: none;"> <strong>Fail!</strong>
							글쓰기가 실패했습니다.
						</span>
						
				</div>
				<div class="panel-body">
					<table class="table">
						<thead>
							<tr>
								<th>번호</th>
								<th style="width: 70%">제목</th>
								<th>작성일</th>
								<th>답변보기</th>
							</tr>
						</thead>
						<tbody>
						
						
						<%-- 
							<tr>
								<td>137</td>
								<td><a href="#demo137" data-toggle="collapse"
									data-target="#demo137">연수신청문의</a><span
									class="text-danger small">(비밀글)</span>
									<div id="demo137" class="collapse"
										style="width: 80%; margin: 10px;">
										<form role="form" method="post">
											<div class="form-group">
												<input type="password" class="form-control input-sm" id="pw"
													name="pw" placeholder="패스워드 (4자리)" maxlength="4"
													required="required">
											</div>
											<button type="submit" class="btn btn-default btn-xs">Submit</button>
										</form>

									</div></td>
								<td>2017-11-03</td>
								<td><button type="button"
										class="btn btn-default btn-xs btnReply" disabled="disabled">답변보기</button></td>
							</tr>
							<tr>
								<td>136</td>
								<td><a href="#demo136" data-toggle="collapse">취업 교육 과정
										문의 드려요</a>
									<div id="demo136" class="collapse"
										style="width: 80%; margin: 10px;">

										<p>
											지금 4년제 대학 3학년2학기에 재학중인 학생입니다.<br> 취업교육과정을 보니 2018년 2월
											졸업예정자까지만 수강을 할 수 있다고 써있더라구요.<br> 그러면 현재 4학년인 학생들만 수강할 수
											있는 건가요?<br> 그렇다면 내년(4학년)에 교육을 받는다고 가정할 때<br> 월~금에
											강의를 한다고 써있는데,<br> 재학중인 상태에서는 교육을 받지 못하고,<br> 휴학을 하고
											교육을 받아야하는 건가요?<br> 다른 학생들은 어떤 상태(휴학을 한다던지, 막학기를 남겨두고
											한다던지 등)에서 교육을 받는지 궁금하여 문의글 올립니다.
										</p>
									</div></td>
								<td>2017-11-03</td>
								<td><button type="button"
										class="btn btn-default btn-xs btnReply" disabled="disabled">답변보기</button></td>
							</tr>
							<tr>
								<td>135</td>
								<td><a href="#demo135" data-toggle="collapse">노동부에서 인증을
										받으려면 얼마나 걸리나요?</a>
									<div id="demo135" class="collapse"
										style="width: 80%; margin: 10px;">
										<p>..</p>
									</div></td>
								<td>2017-10-30</td>
								<td><button type="button"
										class="btn btn-default btn-xs btnReply" title="관리자 답변"
										data-toggle="popover" data-placement="left"
										data-content="안녕하세요? 현재는 노동부에서 상담과 카드발급하는 기간이 4~6주정도 걸린다고 보시면 됩니다. 감사합니다.">답변보기</button></td>
							</tr>
							<tr>
								<td>133</td>
								<td><a href="#demo133" data-toggle="collapse">12월 17일
										과정 문의 드립니다.</a>
									<div id="demo133" class="collapse"
										style="width: 80%; margin: 10px;">
										<p>안녕하세요 2018년 2월 졸업예정인 비전공자 학생입니다. 강남센터의12월 17일 개강 예정인
											Framework을 활용한 빅데이터개발 강좌를 듣고 싶은데요 제가 비전공자이다보니 아무래도 이해하는데
											전공자보다는 어려울꺼라 생각이 듭니다. 웹프로그래머 과정보다 자바시간이 적고 빅데이터에대한 교육이 있던데
											이경우라면 웹프로그래밍 과정을 배우는게 나을지 아니면 배우고싶은 빅데이터과정을 배우는것이 더 나을지
											고민입니다. 어떤과정을 배우는 것이 더 나을까요? 그리고 신청하고 면접이 있는것으로 봐서 혹시 따로 보는 날이
											있는것인지 내일 배움카드를 발급을 받는데 오래걸린다고해서 그런데 발급이 된후 면접이 가능한건지도
											문의드리겠습니다. 바쁘신데 확인해주셔서 감사합니다</p>
									</div></td>
								<td>2017-10-30</td>
								<td><button type="button"
										class="btn btn-default btn-xs btnReply" title="관리자 답변"
										data-toggle="popover" data-placement="left"
										data-content="안녕하세요? 교육과정에대한 상세한 문의는 방문이나 유선으로 해주시면 좀 더 상세하게 설명을 들으실 수가 있습니다. 불편하시더라도 유선이나 방문상담을 이용해주시기 바랍니다. 서류접수 후 면접을 보신 후에 최종합격이 되시면 카드발급을 받으시면 됩니다. 감사합니다.">답변보기</button></td>
							</tr>
 							--%>
							
							
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
										
										<%-- 비밀글인 경우 내용을 출력하지 않고, 패스워드 입력폼이 나오도록 추가 --%>
										<c:choose>
											<c:when test="${q.privacy == 0}">
											
										<%-- 글 내용 출력시 줄바꿈 처리 추가 필요 - 커스텀 태그 --%>
										<p><ct:pre value="${q.content_}" /></p>
										
											</c:when>
											<c:otherwise>
										<form role="form" method="post">
											<div class="form-group">
												<input type="password" class="form-control input-sm" id="pw"
													name="pw" placeholder="패스워드 (4자리)" maxlength="4"
													required="required">
											</div>
											<!-- Ajax 요청을 위해서 submit이 아니라 button으로 변경 -->
											<button type="button" class="btn btn-default btn-xs btnSubmit" value="${q.qid}">Submit</button>
											
											<span class="alert alert-danger fail"
												style="padding: 8px; display: none;"> <strong>Fail!</strong>
												패스워드가 틀렸습니다.
											</span>
											
										</form>											
											</c:otherwise>
										</c:choose>
										
										
									</div></td>
								<td>${q.writeday}</td>
								<td>
								
								<%-- 답변이 없는 경우는 버튼 비활성 상태로 지정, 답변이 있는 경우는 버튼 활성 상태로 지정 --%>
								<button type="button" 
										class="btn btn-default btn-xs btnReply ${(empty q.replyqid)?'disabled':''}"  ${(empty q.replyqid)?'disabled':''} value="${q.replyqid}">답변보기</button>
										
								</td>
							</tr>
							</c:forEach>
							
						</tbody>
					</table>
					<ul class="pager">
						<li><button type="button" class="btn btn-default btn-sm btnPrevious disabled">Previous</button></li>
						<li><button type="button" class="btn btn-default btn-sm btnNext disabled">Next</button></li>
					</ul>
				</div>
			</div>


		</div>


		<!-- Modal -->
		<div id="myQABoardInsertForm" class="modal fade" role="dialog">
			<div class="modal-dialog modal-lg">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">교육 과정 및 교육원 관련 문의</h4>
						<label>최대한 빠른 답변을 해드리겠습니다.</label>
					</div>
					<div class="modal-body">

						<form role="form" action="${pageContext.request.contextPath}/qaboardinsert.it" method="post">
							<div class="form-group">
								<label for="title">제목:</label> <input type="text"
									class="form-control" id="title" name="title"
									placeholder="제목 (100자 이내)" maxlength="100" required="required"
									autofocus="autofocus">
							</div>
							<div class="form-group">
								<label for="content_">내용:</label>
								<textarea class="form-control" rows="10" id="content_" name="content_"
									placeholder="내용 (1000자 이내)" maxlength="1000"
									required="required"></textarea>
							</div>
							<div class="checkbox">
								<label><input type="checkbox" value="" id="undercover">비밀글
									<span class="small">(비밀글을 체크하면 글내용 보기를 할 때 패스워드를 확인하는
										게시물이 됩니다.)</span></label> <label class="text-danger small">* 수강 신청 확인
									등 신원 확인이 필요한 문의는 이름, 전화번호, 이메일을 적어주시고, 비밀글 체크를 해주시기 바랍니다.</label>
							</div>
							<div class="form-group" id="pwForm" style="display: none;">
								<input type="password" class="form-control " id="pw" name="pw"
									placeholder="패스워드 (4자리)" maxlength="4" required="required"
									disabled="disabled">
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

		<!-- Modal -->
		<div id="myLoginFormModal" class="modal fade" role="dialog">

			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header" style="padding: 35px 50px;">
						<h4>
							<span class="glyphicon glyphicon-lock"></span> 로그인
						</h4>
						<label>정상적인 서비스 사용을 위해서 로그인해야 합니다.</label>
					</div>
					<div class="modal-body" style="padding: 40px 50px;">
						<form role="form" method="post" action="${pageContext.request.contextPath}/login.it">
							<div class="form-group">
								<label for="id"> ID :</label> <input type="text"
									class="form-control" id="id_" name="id_" placeholder="Enter ID"
									required="required">
							</div>
							<div class="form-group">
								<label for="pw"> Password :</label> <input type="password"
									class="form-control" id="pw" name="pw"
									placeholder="Enter Password" required="required">
							</div>
							<button type="submit" class="btn btn-default btn-block">
								Login</button>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default btn-sm" id="memberInsertForm">회원가입</button>
						<button type="button" class="btn btn-default btn-sm"
							data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>

		</div>

	</div>
</body>
</html>
