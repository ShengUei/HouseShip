const searchResultsContainer = $('#search-results');
const countResultSpan = $('#count-result-span');

searchResult();

function searchResult () {
    $.ajax({
        method: 'GET',
        url: '/houseship/house/api/search-result',
        async: 'true',
        dataType: "json",
        // data: jsonData,
        // contentType: 'application/json; charset=utf-8',

        success:function(jsonData){
            countResultSpan.append('查詢結果: 共' + jsonData.length + '筆');
            render(jsonData, searchResultsContainer);
        },

        error:function(){
            countResultSpan.append('查詢結果: 共0筆');
            searchResultsContainer.append("<div class='single-items mb-30'>查無結果</div>");
        },

        complete: function(){
            $("#customer-loader").fadeOut();
            $("#customer-preloder").delay(200).fadeOut("slow");
        }
    });

}

function advancedSearch () {
    $($('.nice-select')[1]).find('span').text('默認');
    $($('.nice-select')[1]).find('li').each((index, value) => {
        if(index === 0) {
            value.className = 'option selected';
        } else {
            value.className = 'option';
        }
    })

    countResultSpan.html('');
    searchResultsContainer.html('');

    $('#search-results-preloder').show();
    $('#search-results-loader').show();

    const minPrice = $('#amountFrom').val();
    const maxPrice = $('#amountTo').val();

    const offers = new Map();
    const rules = new Map();
    const checked = $('#select-Categories').find('input:checked');

    $(checked).each((index, value) => {
        if(value.id === 'smoking' || value.id === 'pet') {
            rules[value.id] = value.value;
        } else {
            offers[value.id] = value.value;
        }
    });

    let inputData = {
        houseType: $('#select-type').val(),
        priceZone: [minPrice, maxPrice],
        greaterPrice: $('#price3000').is(':checked'),
        houseOffers: offers,
        houseRules: rules,
    }

    const jsonData = JSON.stringify(inputData);

    $.ajax({
        method: 'POST',
        url: '/houseship/house/api/advanced-search-result',
        async: 'true',
        dataType: "json",
        data: jsonData,
        contentType: 'application/json; charset=utf-8',

        success:function(response){
            countResultSpan.append('查詢結果: 共' + response.length + '筆');
            render(response, searchResultsContainer)
        },

        error:function(){
            countResultSpan.append('查詢結果: 共0筆');
            searchResultsContainer.append("<div class='single-items mb-30'>查無結果</div>");
        },

        complete: function(){
            $("#search-results-loader").fadeOut();
            $("#search-results-preloder").delay(200).fadeOut("slow");
        }

    });

}

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
        if(value.houseRules.smoking === false) {
            offersList += ("<li>禁菸客房</li>");
        }
        if(value.houseRules.pet === true) {
            offersList += ("<li>寵物友善</li>");
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
            "<div class='house-img' style='width: 350px'>" +
            "<a href='/houseship/house/housedetails/" + value.houseNo + "'>" +
            "<img src='/houseship/images/house/" + value.housePhotos[0].photoPath + "' alt='house image'></a>" +
            "</div>" +
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

$('#select-price').on('change', sortByPrice);

function sortByPrice() {
    const target = $('#search-results');
    const condition = $('#select-price').val();
    target.find('.single-items').sort((a,b) => {
        const aPrice = $(a).find('.items-link > span').text().split(' ')[0];
        const bPrice = $(b).find('.items-link > span').text().split(' ')[0];
        if(condition === 'asc') {
            return aPrice - bPrice;
        } else if (condition === 'desc') {
            return bPrice - aPrice;
        } else {
            return 0;
        }
    }).appendTo(target);

}

