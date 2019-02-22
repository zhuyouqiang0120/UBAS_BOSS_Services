$("#submit").click(

		function() {
			$(".ac-Mask").css("display", "none")
			$(".ac-span").css("display", "block")
			var A_ccount = $("#firstname");
			var p_assword = $("#password");
			if (A_ccount.val().trim() == "") {
				$("#logspan")[0].innerText = "账户不能为空"
				A_ccount.css("border", "1px solid red")
				return;
			}
			if (p_assword.val().trim() == "") {
				$("#logspan")[0].innerText = "密码不能为空"
				p_assword.css("border", "1px solid red")
				return;
			}
		

		
			$("#logspan")[0].innerText = ""
			$.ajax({
				type : "post",
				dataType : "json",
				data : {},
				url : "ubas_acdemo/SLogings?Account=" + A_ccount.val()
						+ "&password=" + p_assword.val(),
				success : function(d) {
					console.log(d);
					if (d.code == 200) {

						$(".ac-span").css("display", "none")
						$(".ac-Mask").css("display", "block")
						document.getElementById("myFrame").contentWindow
								.adminIFinme(d);

					}
					if (d.code == 201) {
						$(".ac-span").css("display", "none")
						$("#logspan")[0].innerText = "账户或者密码有误"
					}
				},
				error : function() {
					$(".ac-span").css("display", "none")
					$("#logspan")[0].innerText = '系统出现错误抱歉';
				}
			})

		})

$("#firstname").mouseenter(function() {
	$(this).css("height", "45px");
	$(this).css("border", "1px solid blue");
})

$("#firstname").mouseout(function() {
	$(this).css("height", "34px");
	$(this).css("border", "");
})

$("#password").mouseenter(function() {
	$(this).css("height", "45px");
	$(this).css("border", "1px solid blue");
})

$("#password").mouseout(function() {
	$(this).css("height", "34px");
	$(this).css("border", "");
})

function sleep(numberMillis) {
	var now = new Date();
	var exitTime = now.getTime() + numberMillis;
	while (true) {
		now = new Date();
		if (now.getTime() > exitTime)
			return;
	}
}
$(function() {

})