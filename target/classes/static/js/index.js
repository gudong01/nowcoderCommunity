//基于Ajax的异步请求机制		（建议重构评论功能 实现异步请求）
$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	//隐藏发布框
	$("#publishModal").modal("hide");
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送异步请求
	$.post(
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		function (data) {
			//封装成对象
			data = $.parseJSON(data);
			//在提示框中显示返回的消息
			$("#hintBody").text(data.msg);
			//显示提示框
			$("hintModal").modal("show");
			//2秒后自动隐藏提示框
			setTimeout(function (){
				$("#hintModal").modal("hide");
				//刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			},2000);
		}
	)
	$("#hintModal").modal("show");
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}