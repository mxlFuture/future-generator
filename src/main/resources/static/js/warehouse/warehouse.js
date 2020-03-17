function  warehouseShow(id,val){
    $.ajax({
        type:'get',
        url:'/agrWarehouses/getWarehouseByParam',
        success:function (data) {
            if(data.result=='100'){
                var array=data.data;
                for(var i=0;i<array.length;i++){
                    var  flag=array[i].id==val?'selected':'';
                    $("#"+id).append("<option value='"+array[i].id+"' "+flag+">"+array[i].name+" </option>");
                }
            }
        }
    });
}
