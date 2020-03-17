$.ajaxSetup({
	cache : false,
	error : function(xhr, textStatus, errorThrown) {
		var msg = xhr.responseText;
		var response = JSON.parse(msg);
		var code = response.code;
		var message = response.message;
		if (code == 400) {
			layer.msg(message);
		} else if (code == 401) {
			layer.msg('未登录');
		} else if (code == 403) {
			console.log("未授权:" + message);
			layer.msg('未授权');
		} else if (code == 500) {
			layer.msg('系统错误：' + message);
		}
	}
});

function buttonDel(data, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}
	
	var btn = $("<button class='layui-btn layui-btn-xs' title='删除' onclick='del(\"" + data +"\")'><i class='layui-icon'>&#xe640;</i></button>");
	return btn.prop("outerHTML");
}

function buttonEdit(href, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}
	
	var btn = $("<button class='layui-btn layui-btn-xs' title='编辑' onclick='window.location=\"" + href +"\"'><i class='layui-icon'>&#xe642;</i></button>");
	return btn.prop("outerHTML");
}
function buttonchild(href, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}
	
	var btn = $("<button class='layui-btn layui-btn-xs' title='添加下级' onclick='window.location=\"" + href +"\"'><i class='layui-icon'>&#xe608;</i></button>");
	return btn.prop("outerHTML");
}
function buttonDetail(href, permission, pers){
	if(permission != ""){
		if ($.inArray(permission, pers) < 0) {
			return "";
		}
	}

	var btn = $("<button class='layui-btn layui-btn-xs' title='查看' onclick='window.location=\"" + href +"\"'><i class='layui-icon'>&#xe621;</i></button>");
	return btn.prop("outerHTML");
}
//推荐
function buttonShare(data, permission, pers){
    if(permission != ""){
        if ($.inArray(permission, pers) < 0) {
            return "";
        }
    }

    var btn = $("<button class='layui-btn layui-btn-xs' title='推荐' onclick='share(\"" + data +"\")'><i class='layui-icon'>&#xe641;</i></button>");
    return btn.prop("outerHTML");
}
//报修
function buttonRepair(data, permission, pers){
    if(permission != ""){
        if ($.inArray(permission, pers) < 0) {
            return "";
        }
    }

    var btn = $("<button class='layui-btn layui-btn-xs' title='报修' onclick='repair(\"" + data +"\")'><i class='layui-icon'>&#xe631;</i></button>");
    return btn.prop("outerHTML");
}
////0为启用,1为禁用
function buttonEnable(data, permission, pers,enabled){
    if(permission != ""){
        if ($.inArray(permission, pers) < 0) {
            return "";
        }
    }
    if(enabled){
    	var btn = $("<button class='layui-btn layui-btn-xs' title='启用' onclick='enable(\"" + data +"\",0)'><i class='layui-icon layui-icon-user'></i></button>");
    	return btn.prop("outerHTML");
    }else{
    	var btn = $("<button class='layui-btn layui-btn-xs layui-bg-red' title='禁用' onclick='enable(\"" + data +"\",1)'><i class='layui-icon layui-icon-user'></i></button>");
    	return btn.prop("outerHTML");
    }
}

////0为可用,1为耗尽
function buttonDeplete(data, permission, pers,deplete){
    if(permission != ""){
        if ($.inArray(permission, pers) < 0) {
            return "";
        }
    }
    if(deplete){
        var btn = $("<button class='layui-btn layui-btn-xs layui-bg-red' title='可用' onclick='dep(\"" + data +"\",0)'><i class='layui-icon'>&#xe69c;</i></button>");
        return btn.prop("outerHTML");
    }else{
        var btn = $("<button class='layui-btn layui-btn-xs ' title='耗尽' onclick='dep(\"" + data +"\",1)'><i class='layui-icon'>&#xe6af;</i></button>");
        return btn.prop("outerHTML");
    }
}
function deleteCurrentTab(){
	var lay_id = $(parent.document).find("ul.layui-tab-title").children("li.layui-this").attr("lay-id");
	parent.active.tabDelete(lay_id);
}


$(function($) {
	var role=localStorage.getItem("role");
	if(role==4){//农场主
		$(".farmdiv").hide(); //隐藏企业
		$(".farmdiv select option[value='']").remove();
		$(".farmdiv select option").first().prop("selected",true);
		$(".farmdiv select").trigger("change");
		$(".basediv").hide();//隐藏基地
		$(".basediv select option[value='']").remove();
		$(".basediv select option").first().prop("selected",true);
		$(".basediv select").trigger("change");
	}
});