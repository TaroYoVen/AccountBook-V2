<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ABV2 项目管理</title>
<%@ include file="common/common.jsp" %> 
<link href="../res/css/manageItems.css" rel="stylesheet">
<script src="../res/js/manageItems.js"></script>

</head>
<body>
<%@ include file="common/top.jsp"%>
<script language="JavaScript" type="text/JavaScript"> 
	document.onload = setLeftColumn();
</script>
<div class="col-xs-10">

	<div class="total"> 
		<label class="total-label1"><span class="tableTitle">收入项</span></label>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="add-button" onclick="addItem('in')">添加收入项</button>
	</div>
	<div>
		<table class="table table-bordered"> 
		    <thead> 
		        <tr> 
					<th class="col1">项目</th>
					<th class="col2">备注</th>
					<th class="col3">操作</th>
					<th class="col4">排序</th>
		        </tr>
		    </thead>
		    <tbody> 
		        <c:forEach items="${incomeItems}" var="incomeItem">
                <tr>
                    <td>${incomeItem.name }</td>
                    <td>${incomeItem.remark }</td>
                    <td><a href="#" onClick="changeItem('${incomeItem.id}','${incomeItem.name }','${incomeItem.remark }','in')">修改&nbsp;&nbsp;</a> 
                    	<a href="#" onClick="delItem('${incomeItem.id}')">删除</a></td>
                    <td>上移&nbsp;&nbsp;下移</td>
                </tr>
          		</c:forEach>
		    </tbody> 
		</table>
	</div>
	
	<div class="total"> 
		<label class="total-label1"><span class="tableTitle">支出项</span></label>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<button class="add-button" onclick="addItem('ex')">添加支出项</button>
	</div>
	<div>
		<table class="table table-bordered"> 
		    <thead> 
		        <tr> 
					<th class="col1">项目</th>
					<th class="col2">备注</th>
					<th class="col3">操作</th>
					<th class="col4">排序</th>
		        </tr>
		    </thead>
		    <tbody> 
		        <c:forEach items="${expenditureItems}" var="expenditureItem">
                <tr>
                    <td>${expenditureItem.name }</td>
                    <td>${expenditureItem.remark }</td>
                    <td><a href="#" onClick="changeItem('${expenditureItem.id}','${expenditureItem.name }','${expenditureItem.remark }','ex')">修改&nbsp;&nbsp;</a> 
                    	<a href="#" onClick="delItem('${expenditureItem.id}')">删除</a></td>
                    <td>上移&nbsp;&nbsp;下移</td>
                </tr>
          		</c:forEach>
		    </tbody> 
		</table>
	</div>
</div>
<%@ include file="common/bottom.jsp"%>



<div id="addItemLayer" class="hidden">
    <div class="addItem">
        <label class="addItem-label">项目名称</label>
        <input type="text" id="addedItemName" placeholder="项目名称" >
    </div>
	<div>
        <label class="addItem-label">备注</label>
        <input type="text" id="addedItemRemark" placeholder="备注" >
	</div>
</div>


