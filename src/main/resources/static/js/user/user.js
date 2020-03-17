//负责人信息-
function  loadUserList(id,val) {
    $.ajax({
        type:"post",
        url:"/users/loadList",
        success:function (data) {
            for(var i=0;i<data.data.length;i++){
                if(data.data[i].username!=null){
                    var  flag=data.data[i].id==val?'selected':'';
                    $("#"+id).append("<option value='"+data.data[i].id+"'  "+flag+">"+data.data[i].username+"</option>");
                }
            }
        }
    });
}
//车间列表信息
function  loadWorkshopList(id,val) {
    $.ajax({
        type:"post",
        url:"/agrWorkshops/loadList",
        async:false,
        success:function (data) {
            console.log(data);
            for(var i=0;i<data.data.length;i++){
                if(data.data[i].username!=null){
                    var  flag=data.data[i].id==val?'selected':'';
                    $("#"+id).append("<option value='"+data.data[i].id+"'  "+flag+">"+data.data[i].workshopName+"</option>");
                }
            }
        }
    });
}

//生产线列表信息
function  loadLineList(id,val) {
    $.ajax({
        type:"post",
        url:"/agrProductionLines/loadList",
        success:function (data) {
            for(var i=0;i<data.data.length;i++){
                if(data.data[i].username!=null){
                    var  flag=data.data[i].id==val?'selected':'';
                    $("#"+id).append("<option value='"+data.data[i].id+"'  "+flag+">"+data.data[i].LineName+"</option>");
                }
            }
        }
    });
}