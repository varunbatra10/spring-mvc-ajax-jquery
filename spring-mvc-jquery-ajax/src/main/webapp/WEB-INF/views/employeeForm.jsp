<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Form</title>

	<style>
			.content {
				background: #d6f0ff;
			}
			.content .main {
				width: 512px;
				margin: 10px auto;
				background: #e9f7ff;
				padding: 10px;
				height:100%;
				color:#077bbe;
				border: 5px solid #bebebe;
			}
			.heading {
				font-size:20px;
				font-weight:bold;
				font-family: Droid Sans, serif;
				margin-top:20px;
			}
			h1 {
				color:black;
			}
			.input-style {
				width:95%;
				border: 1px solid #e8e8e8;
				height:30px;
				padding:5px;
				margin-top:5px;
			}
			.submit-button {
				width:300px;
				height:50px;
				color: white;
				font-size:20px;
				font-weight:bold;
				background: #01598b;
			}
		</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
$(function() {
	/*  Submit form using Ajax */
	$('#uploadFileBtn').click(
			function(e) {


				var form = $('#employeeForm')[0];

				var data = new FormData(form);

				$.ajax({
					type : "POST",
					enctype : 'multipart/form-data',
					url : 'saveEmployee',
					data : data,
					processData : false,
					contentType : false,
					cache : false,
					timeout : 600000,
					success : function(res) {
							$('#file1').val(res[0]);
							$('#file2').val(res[1]);

					}
				})
			});
});
	$(function() {
		/*  Submit form using Ajax */
		$('#dnldFileBtn').click(
				function(e) {
					var employee = $('#submitForm').serialize();
					//var data = new FormData(form);
					//var employee = JSON.parse(JSON.stringify(jQuery('#submitForm').serializeArray()))
					$.ajax({
						type : "POST",
						//enctype : 'multipart/form-data',
						url : 'getFile',
						data :employee,
						//processData : false,
						//contentType : false,
						//cache : false,
						//timeout : 600000,
						success : function(res) {
							   alert(res);
							   window.location=res;
								//alert("Your file has been downloaded you can download it from here,..."+res)

						}
					})
				});
	});

</script>
</head>
<body>
	<h1>Form</h1>
	<hr />
	<form method="post" name="employeeForm" id="employeeForm"
		enctype="multipart/form-data">
		<input type="file" name="file" accept=".xls,.xlsx" multiple="multiple" />
		<input type="button" value="Upload file" id="uploadFileBtn" />
		<br />
		
		<h6>
			<u>File 1:</u>
		</h6>
		<br />
		<h6>
			<u>Available Fields:</u>
			<textarea rows="1" cols="" name="file1" id="file1"></textarea>
		</h6>
		<br>
		<h6>
			<u>File 2:</u>
		</h6>
		<br />
		<h6>
			<u>Available Fields:</u>
			<textarea rows="1" cols="" name="file2" id="file2"></textarea>
		</h6>
		<br>
		</form>
		<form:form action="getFile"  method="post" modelAttribute="submitForm"  id="submitForm">
		<h6>
			<u>Set Order:</u>
		</h6>
		<textarea rows="5" cols="50" name="order" id="order"></textarea>
		<br />
		<h6>
			<u>Set File Type:</u>
		</h6>
		<select name="fileType" id="filetype">
			<option value="xlsx">xlsx</option>
			<option value="csv">csv</option>
			<option value="txt">txt</option>
		</select>
		<h6>
			<u>Delimeter:</u>
		</h6>
		<select name="delimeter" id="delimeter">
			<option value="|">|</option>
			<option value="*">*</option>
		</select>
		<br />
		<input type="button" value="Download file" id="dnldFileBtn" />
	</form:form>

</body>
</html>