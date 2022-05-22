//選取
$("#checkAll").on("click", function () {
    var checkboxAll = document.querySelectorAll(".checkMe");
    for (let checkbox of checkboxAll) {
        if (this.checked == true) {
            checkbox.checked = true;
        } else {
            checkbox.checked = false;
        }
    }
});

//取消事件
function cancelOrder(e) {
    console.log('click');
    e.preventDefault();
    let msg = "是否取消該筆預訂?";
    if (confirm(msg) == true) {
        window.location = e.target.getAttribute("href");
    }
}

//帳號確認
$("#inputAccount").keyup(checkAccount);

function checkAccount() {
    var account = $("#inputAccount").val();
    if (account == "") {
        $("#inputAccount").attr('class', 'form-control is-invalid')
        $("#account_status").attr('class', 'invalid-feedback')
        $("#account_status").html("請輸入會員帳號");
        $("#next").attr('disabled', true);
        return
    }

    $.ajax({
        type: 'post',
        url: '/houseship/admin/order/checkAccount/' + account,
        dataType: 'text',
        success: function (msg) {
            $("#account_status").empty("");
            if (msg == "") {
                $("#inputAccount").attr('class', 'form-control is-invalid')
                $("#account_status").attr('class', 'invalid-feedback')
                $("#account_status").html("帳號不存在");
                $("#next").attr('disabled', true);
            } else {
                $("#inputAccount").attr('class', 'form-control is-valid')
                $("#account_status").attr('class', 'valid-feedback')
                $("#account_status").html("ok");
                $("#memberid").val(account);
                $("#next").attr('disabled', false);
            }
        }
    });
}

//房源確認
$("#inputHouse").keyup(checkHouse);

function checkHouse() {
    var houseNo = $("#inputHouse").val();
    if (houseNo == "") {
        $("#inputHouse").attr('class', 'form-control is-invalid')
        $("#house_status").attr('class', 'invalid-feedback')
        $("#house_status").html("請輸入房屋編號");
        $("#next").attr('disabled', true);
        return
    }

    $.ajax({
        type: 'post',
        url: '/houseship/admin/order/checkHouse/' + houseNo,
        dataType: 'json',
        success: function (data) {
            $("#house_status").empty("");
            if (data == "") {
                $("#inputHouse").attr('class', 'form-control is-invalid')
                $("#house_status").attr('class', 'invalid-feedback')
                $("#house_status").html("查無此房源");
                $("#next").attr('disabled', true);
            } else {
                $("#inputHouse").attr('class', 'form-control is-valid')
                $("#house_status").attr('class', 'valid-feedback')
                $("#house_status").html("ok");
                $("#houseid").val(houseNo);
                $("#h_bookPrice_num").val(data.h_price);
                $("#next").attr('disabled', false);
            }
        }
    });
}

//優惠確認
$("#couponCode").keyup(checkCoupon);
$("#couponCode").change(checkCoupon);

function checkCoupon() {
    $("#couponDetail").text("");
    let code = $("#couponCode").val();
    $.ajax({
        type: 'get',
        url: '/houseship/coupon/check/' + code,
        contentType: 'application/json',
        success: function (data) {
            if (data == "") {
                $("#couponDetail").text("查無可用的優惠券");
                $("#discount_m").val(1);
                $("#coupon_send").val("");
                showPayment();
            } else {
                $("#couponDetail").text(data.title);
                $("#discount_m").val(data.discount);
                $("#coupon_send").val(data.couponNo);
                showPayment();
                $("#next2").attr('disabled', false);
            }
        }
    });
}

//自動寫入
$("#writeAccount").click(function () {
    $("#inputAccount").val("test123");
    $("#inputHouse").val("1");
    checkAccount();
    checkHouse();
});

$("#writeOrder").click(function () {
    $("#guestNum").val(2);
    $("#couponCode").val('999');
    checkCoupon();
});

$("#writeOrder2").click(function () {
    $.ajax({
        type: 'get',
        url: '/houseship/order/getMemberData',
        contentType: 'application/json',
        success: function (data) {
            $("#firstname").val(data.firstname);
            $("#lastname").val(data.lastname);
            $("#email").val(data.email);
            $("#phone").val(data.phone);
        }
    });
    $("#checkInTime").val("18:00");
});

//刪除
$("#deleteAll").click(function (e) {
    e.preventDefault();
    let msg = "請確定是否丟棄所選資料?";
    if (confirm(msg) == true) {

        var selectCheck = $(".checkMe:checked");
        var listOrderid = [];
        console.log("select: " + selectCheck.length);
        for (var order of selectCheck) {
            console.log("orderid: " + $(order).val());
            let orderid = $(order).val();
            listOrderid.push(orderid)
        }
        console.log(listOrderid);

        $.ajax({
            type: 'post',
            url: '/houseship/admin/order/adminInvalid',
            contentType: 'application/json',
            data: JSON.stringify(listOrderid),
            success: function (data) {
                console.log(data);
                window.location.href = "/houseship/admin/order";
            }
        });
    }
})
