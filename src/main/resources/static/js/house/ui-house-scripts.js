
function searchResult () {
    const searchResultsContainer = $('#search-results');
    const countResultSpan = $('#count-result-span');

    $.ajax({
        method: 'GET',
        url: '/houseship/house/api/search-result',
        async: 'true',
        dataType: "json",
        // data: jsonData,
        // contentType: 'application/json; charset=utf-8',

        success:function(jsonData){
            countResultSpan.append('查詢結果: 共' + jsonData.length + '筆');
            render(jsonData, searchResultsContainer)
        },

        error:function(){
            searchResultsContainer.append("<div class='single-items mb-30'>查無結果</div>");
        },

    });

}

searchResult();

function render(data, target) {
    $.each(data, (index, value) => {
        let houseType = '';
        let offersList = '';
        let offers = value.houseOffers;

        if(offers.wifi === true) {
            offersList += ("<li>WiFi</li>");
        }
        if(offers.tv === true) {
            offersList += ("<li>電視</li>");
        }
        if(offers.aircon === true) {
            offersList += ("<li>冷氣</li>");
        }
        if(offers.refrigerator === true) {
            offersList += ("<li>冰箱</li>");
        }
        if(offers.microwave === true) {
            offersList += ("<li>微波爐</li>");
        }
        if(offers.kitchen === true) {
            offersList += ("<li>廚房</li>");
        }
        if(offers.washer === true) {
            offersList += ("<li>洗衣機</li>");
        }

        if (value.h_type === 1) {
            houseType = '單人房';
        } else if (value.h_type === 2) {
            houseType = '雙人房';
        } else if (value.h_type === 4) {
            houseType = '四人房';
        }

        let houseContent = "<div class='single-items mb-30'>" +
            "<div class='result-items'>" +
            "<div class='house-img'>" +
            "<a href='/houseship/house/housedetails/" + value.houseNo + "'>" +
            "<img " +
            "src='/houseship/images/house/room-details.jpg'" +
            " alt='house image'>" +
            "</a></div>" +
            "<div class='house-tittle house-tittle2' style='width: 300px'>" +
            "<a href='/houseship/house/housedetails/" + value.houseNo + "'>" +
            "<h4>" +
            value.h_title +
            "</h4></a>" +
            "<div class='description'><ul><li>" +
            houseType +
            "</li><li><i class='icon_pin_alt'></i>" +
            value.h_address +
            "</li></ul>" +
            "<ul id='house-offers' >" +
            offersList +
            "</ul>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<div class='items-link items-link2 f-right'><span>" +
            value.h_price +
            " TWD/晚</span><a href='/houseship/house/housedetails/" + value.houseNo + "'>前往查看</a></div></div>";

        target.append(houseContent);
    })
}

