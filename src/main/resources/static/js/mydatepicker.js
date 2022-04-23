var today = new Date();
var tomorrow = new Date();
tomorrow.setDate(today.getDate() + 1);

var daysOfWeek = ["日", "一", "二", "三", "四", "五", "六"];

$(function () {
    // var dateRangePicker = $('input[name="daterange"]');
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
    });
});

$("#inputCheckOut").click(function () {
    $('#inputCheckIn').focus();
})

// $(window).load(function () {
$(".autoWrite").click(function(){
        $("#inputCheckIn").val(`${today.getMonth() + 1} 月 ${today.getDate()} 日 (星期${daysOfWeek[today.getDay()]})`);
        $("#checkin_send").val(`${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`)
        $("#inputCheckOut").val(`${tomorrow.getMonth() + 1} 月 ${tomorrow.getDate()} 日 (星期${daysOfWeek[tomorrow.getDay()]})`);
        $("#checkout_send").val(`${tomorrow.getFullYear()}-${tomorrow.getMonth() + 1}-${tomorrow.getDate()}`)
    // console.log($("#checkout_send").val());
})