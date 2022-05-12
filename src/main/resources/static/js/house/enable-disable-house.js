function disableHouse() {
    let result = confirm('確認是否要下架房屋');
    if(result === true) {
        const houseNo = $('#house-id').text();
        $.ajax({
            method: 'POST',
            url: '/houseship/account/host/cancelhouse/' + houseNo,
            async: 'true',
            dataType: "json",

            success:function(res){
                alert(res.message)
                window.location.reload();
            },
            error:function(err){
                alert(err.message)
            }
        });
    }
}

function enableHouse() {
    let result = confirm('確認是否要重新上架房屋');
    if(result === true) {
        const houseNo = $('#house-id').text();
        $.ajax({
            method: 'POST',
            url: '/houseship/account/host/enablehouse/' + houseNo,
            async: 'true',
            dataType: "json",

            success:function(res){
                alert(res.message)
                window.location.reload();
            },
            error:function(err){
                alert(err.message)
            }
        });
    }
}