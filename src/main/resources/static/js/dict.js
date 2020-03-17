function showDictSelect(id, type, all) {
	var data = getDict(type);
	var select = $("#" + id);
	select.empty();

	if (all != undefined || all) {
		select.append("<option value=''>请选择</option>");
	}

	$.each(data, function(k, v) {
		select.append("<option value ='" + k + "'>" + v + "</option>");
	});

	return data;
}

function getDict(type) {
	//var v = sessionStorage[type];
	//if (v == null || v == "") {
	var v = {};
		$.ajax({
			type : 'get',
			url : '/dicts?type=' + type,
			async : false,
			success : function(data) {
				
				$.each(data, function(i, d) {
					v[d.k] = d.val;
				});

				//sessionStorage[type] = JSON.stringify(v);
			}
		});
	//}
	return v;
	//return JSON.parse(sessionStorage[type]);
}
function loadSelect(id, url, all) {
	var data = getCodes(url);
	var select = $("#" + id);
	select.empty();
	if (all != undefined || all) {
		select.append("<option value=''>请选择</option>");
	}
	$.each(data, function(k, v) {
		select.append("<option value ='" + k + "'>" + v + "</option>");
	});
	return data;
}

function getCodes(url) {
	//var v = sessionStorage[url];
	//if (v == null || v == "") {
	var v = {};
		$.ajax({
			type : 'get',
			url : url,
			async : false,
			success : function(data) {
				$.each(data.data, function(i, d) {
					v[d.id] = d.name;
				});
				//sessionStorage[url] = JSON.stringify(v);
			}
		});
	//}
		return v;
	//return JSON.parse(sessionStorage[url]);
}
function loadUser(id,companyId,def) {
	$.ajax({
	    url:"/users/getUserByCompany",
	    type:"get",
	    contentType: "application/json; charset=utf-8",
	    data:{"companyId":companyId},
	    success:function (data) {
	        var company="<option value=''>请选择</option>";
	        if(data.result==100){
	            for(var i=0;i<data.data.length;i++){
	               company+="<option value='"+data.data[i].id+"'>"+data.data[i].nickname+"</option>";
	            }
	        }
	        $("#"+id).html(company);
	        if(def){
	        	$("#"+id).val(def);	
	        }
	    }
	});
}


function loadArea(id, url, all) {
	var data = getAreaCodes(url);
	var select = $("#" + id);
	select.empty();
	if (all != undefined || all) {
		select.append("<option value=''>请选择</option>");
	}
	$.each(data, function(k, v) {
		select.append("<option value ='" + k + "'>" + v + "</option>");
	});
	return data;
}
function getAreaCodes(url) {
	//var v = sessionStorage[url];
	//if (v == null || v == "") {
	var v = {};
		$.ajax({
			type : 'post',
			url : url,
			async : false,
			success : function(data) {
				$.each(data, function(i, d) {
					v[d.code] = d.name;
				});
				//sessionStorage[url] = JSON.stringify(v);
			}
		});
	//}
		return v;
	//return JSON.parse(sessionStorage[url]);
}