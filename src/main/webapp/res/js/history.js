function setLeftColumn(){
	$("#income").addClass("left-column-button-inactive");
	$("#income").addClass("left-column-button-inactive-font");
	$("#expenditure").addClass("left-column-button-inactive");
	$("#expenditure").addClass("left-column-button-inactive-font");
	$("#history").addClass("left-column-button-active");
	$("#history").addClass("left-column-button-active-font");
	$("#itemsManagement").addClass("left-column-button-inactive");
	$("#itemsManagement").addClass("left-column-button-inactive-font");
	$("#about").addClass("left-column-button-inactive");
	$("#about").addClass("left-column-button-inactive-font");
}

//设置年份，从2016年到今年
function setYear(){
	var nowDate = new Date().Format("yyyy-MM-dd");
	var thisYear = parseInt(nowDate.substring(0,4));
	for(var i=2016;i<=thisYear;i++){
		$("#year").append("<option value='"+i+"'>"+i+"</option>");
	}
}
//设置月份，只有选择了年份，才能选择月份。不能单选择月份
function setMonthEnable(){
	var s = document.getElementById("month");
	var options=$("#year option:selected");  //获取选中的项
	var year = options.val()
	if(year==-1){
		s.disabled = true;
	}else{
		s.disabled = false;
	}
}

function  gotoPage(forOrBackward, totalPages){
	var page = parseInt($("#curPage").text()) + parseInt(forOrBackward);
	if(page < 1){
		page = 1;
	}else if(page>totalPages){
		page = totalPages;
	}
	search(page);
}

function search(page){
	var type = $("#type option:selected").val();
	var year = $("#year option:selected").val();
	var month = $("#month option:selected").val();
	var keyword = $("#keyword").val();
	$.ajax({
		type: "POST",
		url: "searchHistory",
		data: {
			"type":type,
			"year":year,
			"month":month,
			"keyword":keyword,
			"sortBy":"date DECS",
			"curPage":page
		},
		success: function(msg){
			$("#details  tr:not(:first)").empty("");  // 将表格置为空
				if(msg.list!=null){
					var len = msg.list.length;
					for(var i=0;i<len;i++){
						var operation = 
							"<a href='#' onClick=changeDetailsItem('" +
								msg.list[i].itemType +"','"+
								msg.list[i].id +"','"+
								msg.dateList[i]  +"','"+
								msg.list[i].money  +"','"+
								msg.list[i].itemId +"','"+
								msg.list[i].remark +"','"+
								msg.list[i].type_of_money +"'"+
							")>修改</a>&nbsp;&nbsp;"+ 
							"<a href='#' onClick=delDetailsItem('"+
								msg.list[i].itemType +"','"+
								msg.list[i].id +"'"+
							")>删除</a>";
						$("#detailItems").append(
							"<tr>"+
				            "    <td>"+ msg.dateList[i] +"</td>"+
				            "    <td>"+ msg.typeList[i] +"</td>"+
				            "    <td>"+ msg.list[i].itemName +"</td>"+
				            "    <td>"+ msg.list[i].type_of_money +"</td>"+
				            "    <td>"+ msg.list[i].money +"</td>"+
				            "    <td>"+ msg.list[i].remark +"</td>"+
				            "    <td>"+ operation +"</td>"+
				            "</tr>"
						)
				}
			}
			$("#curPage").text(msg.curPage);
			$("#totalPages").text(msg.totalPages);
			$("#totalRecords").text(msg.totalRecords);
		},
		error: function (msg) {//XMLHttpRequest, textStatus, errorThrown
			alert("请求失败");
		} 
	});
}

/**
 * 根据选择的是 收入还是支出 修改其具体的项目
 */
function changeType1(){
	var content = $(".layui-layer-content");
	var changedType = content.find("#changedType").val();
	if(changedType=='in'){
		content.find("#changedItem .ex").addClass("hidden");
		content.find("#changedItem .in").removeClass("hidden");
		content.find("#changedtype_of_money .ex").addClass("hidden");
		content.find("#changedtype_of_money .in").removeClass("hidden");
	}else{
		content.find("#changedItem .in").addClass("hidden");
		content.find("#changedItem .ex").removeClass("hidden");
		content.find("#changedtype_of_money .in").addClass("hidden");
		content.find("#changedtype_of_money .ex").removeClass("hidden");
	}
}

function changeDetailsItem(itemType, detailsId, date, money, itemId, remark, type_of_money){
	layer.confirm(
		$("#changeDetailsItemLayer").html(),{
	    btn: ['修改','返回'], //按钮
		success: function(layero, index){
       		var content = $(".layui-layer-content");
 		   	content.find("#changedDate").val(date.substring(0,10));
 		   	content.find("#changedMoney").val(money);
 		   	content.find("#changedType").val(itemType);
 		   	content.find("#changedItem").val(itemId);
 		   	content.find("#changedRemark").val(remark);
 		    content.find("#changedtype_of_money").val(type_of_money);
 		    changeType1();
        }
	}, function(){
		var content = $(".layui-layer-content");
		var changedDate = content.find("#changedDate").val();
		var changedMoney = content.find("#changedMoney").val();
		var changedType = content.find("#changedType").val();
		var changedItem = content.find("#changedItem").val();
		var changedRemark = content.find("#changedRemark").val();
		var changedtype_of_money = content.find("#changedtype_of_money").val();
		$.post("../historyController/changeHistory",{
			"itemType":itemType,  // 原项的类型（收入or支出）
			"changedType":changedType,  // 现在的类型（收入or支出）
			"detailsId":detailsId,
			"changedDate":changedDate,
			"changedMoney":changedMoney,
			"changedItem":changedItem,
			"changedRemark":changedRemark,
			"changedMoneyType":changedtype_of_money
		});
		setTimeout('location.reload()', 1000);
	});
}

function delDetailsItem(itemType,historyId,itemId){
	$.post("../historyController/deleHistory",{
		"itemType":itemType,
		"historyId":historyId,
		"itemId":itemId,
	});
	setTimeout('location.reload()', 1000);
}
