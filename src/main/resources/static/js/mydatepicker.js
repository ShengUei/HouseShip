var today = new Date();
var tomorrow = new Date();
tomorrow.setDate(today.getDate() + 1);

var daysOfWeek = ["日", "一", "二", "三", "四", "五", "六"];

$(function () {
    var dateRangePicker = $('#inputCheckIn');
    dateRangePicker.daterangepicker({
        autoApply: false,
        autoUpdateInput: false,
        // drops: 'up',
        parentEl: $('#inputCheckIn').closest($(".modal")),
        minDate: today,
        startDate: today,
        endDate: tomorrow,
        applyButtonClasses: "btn-primary",
        cancelButtonClasses: "btn-default",
        locale: {
            "format": "M 月 DD 日",
            "separator": " - ",
            "applyLabel": "選擇",
            "cancelLabel": "重設",
            "fromLabel": "From",
            "toLabel": "To",
            "customRangeLabel": "Custom",
            "weekLabel": "W",
            "daysOfWeek": [
                "日",
                "一",
                "二",
                "三",
                "四",
                "五",
                "六"
            ],
            "monthNames": [
                "1月",
                "2月",
                "3月",
                "4月",
                "5月",
                "6月",
                "7月",
                "8月",
                "9月",
                "10月",
                "11月",
                "12月"
            ],
            "firstDay": 1
        },
    });
    dateRangePicker.on('apply.daterangepicker', function (event, picker) {
        // $(this).val(picker.startDate.format('M 月 DD 日') + ' - ' + picker.endDate.format('M 月 DD 日'));
        $("#inputCheckIn").val(picker.startDate.format('M 月 DD 日 (星期') + daysOfWeek[picker.startDate._d.getDay()] + ')');
        $("#checkin_send").val(picker.startDate.format('YYYY-MM-DD'));
        $("#inputCheckOut").val(picker.endDate.format('M 月 DD 日 (星期') + daysOfWeek[picker.endDate._d.getDay()] + ')');
        $("#checkout_send").val(picker.endDate.format('YYYY-MM-DD'));
        // console.log($("#checkout_send").val());
    });

    dateRangePicker.on('cancel.daterangepicker', function (event, picker) {
        $(this).val('');
        $("#inputCheckOut").val('');
    });
});

$("#inputCheckOut").click(function () {
    $('#inputCheckIn').focus();
})

//Booking Option Picker
$(function () {
    var bookingDateRangePicker = $('#inputCheckIn_booking');
    bookingDateRangePicker.daterangepicker({
        autoApply: true,
        autoUpdateInput: false,
        parentEl: $('#inputCheckIn').closest($(".modal")),
        minDate: today,
        // startDate: today,
        // endDate: tomorrow,
        applyButtonClasses: "btn-primary",
        cancelButtonClasses: "btn-default",
        locale: {
            "format": "YYYY 年 M 月 DD 日",
            "separator": " - ",
            "applyLabel": "選擇",
            "cancelLabel": "重設",
            "fromLabel": "From",
            "toLabel": "To",
            "customRangeLabel": "Custom",
            "weekLabel": "W",
            "daysOfWeek": [
                "日",
                "一",
                "二",
                "三",
                "四",
                "五",
                "六"
            ],
            "monthNames": [
                "1月",
                "2月",
                "3月",
                "4月",
                "5月",
                "6月",
                "7月",
                "8月",
                "9月",
                "10月",
                "11月",
                "12月"
            ],
            "firstDay": 1
        },
    });
    bookingDateRangePicker.on('apply.daterangepicker', function (event, picker) {
        // $(this).val(picker.startDate.format('M 月 DD 日') + ' - ' + picker.endDate.format('M 月 DD 日'));
        $("#inputCheckIn_booking").val(picker.startDate.format('YYYY 年 M 月 DD 日'));
        $("#checkin_send_booking").val(picker.startDate.format('YYYY-MM-DD'));
        $("#inputCheckOut_booking").val(picker.endDate.format('YYYY 年 M 月 DD 日'));
        $("#checkout_send_booking").val(picker.endDate.format('YYYY-MM-DD'));
        let bookPrice = parseInt($("#h_bookPrice_num").val());
        let bookNight = getBookingNight(picker.startDate._d, picker.endDate._d);
        let totalPay = bookPrice * bookNight;
        $("#bookingNight").text(bookNight);
        $("#payTotal").text(totalPay.toLocaleString('zh-TW', {
            style: "currency",
            currency: "TWD",
            maximumFractionDigits: 0
        }));
        $(".showAfterPick").show();
    });

    bookingDateRangePicker.on('cancel.daterangepicker', function (event, picker) {
        $("#inputCheckIn_booking").val('');
        $("#inputCheckOut_booking").val('');
    });
});

$("#inputCheckOut_booking").click(function () {
    $('#inputCheckIn_booking').focus();
})


// $(window).load(function () {
$(".autoWrite").click(function () {
    $("#inputCheckIn").val(`${today.getMonth() + 1} 月 ${today.getDate()} 日 (星期${daysOfWeek[today.getDay()]})`);
    $("#checkin_send").val(`${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`)
    $("#inputCheckOut").val(`${tomorrow.getMonth() + 1} 月 ${tomorrow.getDate()} 日 (星期${daysOfWeek[tomorrow.getDay()]})`);
    $("#checkout_send").val(`${tomorrow.getFullYear()}-${tomorrow.getMonth() + 1}-${tomorrow.getDate()}`)
    // console.log($("#checkout_send").val());
})

function getBookingNight(startDate, endDate) {
    var t1 = startDate.getTime();
    var t2 = endDate.getTime();
    return parseInt((t2 - t1) / (24 * 3600 * 1000));
}

function showPayment() {
    let bookPrice = parseInt($("#h_bookPrice").text());
    let checkInDate = new Date( $("#checkin_send_booking").val());
    let checkOutDate = new Date( $("#checkout_send_booking").val());
    let bookNight = getBookingNight(checkInDate, checkOutDate);
    let totalPay = bookPrice * bookNight;
    $("#payTotalSend").val(totalPay);
    $("#bookingNight").text(bookNight);
    $("#payTotal").text(totalPay.toLocaleString('zh-TW', {
        style: "currency",
        currency: "TWD",
        maximumFractionDigits: 0
    }));
    $(".showAfterPick").show();
}

if ($("#checkin_send_booking").val() != "") {
    // console.log($("#checkin_send_booking").val());
    showPayment();
}

