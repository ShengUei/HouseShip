const searchResultsContainer = $('#search-results');
const countResultSpan = $('#count-result-span');

searchResult(1);

function searchResult (page) {
    let url = '/houseship/api/house/search-result/' + page;

    countResultSpan.html('');
    searchResultsContainer.html('');
    $("#map-iframe").html('');
    $("#room-pagination").html('');

    $.ajax({
        method: 'GET',
        url: url,
        async: 'true',
        dataType: "json",
        // data: jsonData,
        // contentType: 'application/json; charset=utf-8',

        success:function(jsonData){
            countResultSpan.append('查詢結果: 共' + jsonData.length + '筆');
            render(jsonData, searchResultsContainer);
            totalPages();
        },

        error:function(){
            countResultSpan.append('查詢結果: 共0筆');
            searchResultsContainer.append("<div class='single-items mb-30'>查無結果</div>");
        },

        complete: function(){
            $("#customer-loader").fadeOut();
            $("#customer-preloder").delay(200).fadeOut("slow");
            $("#map-iframe").append('<iframe width="100%" height="920px" style="border:0" src="/houseship/api/map"></iframe>');
        }
    });

}

function advancedSearch (page) {
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
    $("#map-iframe").html('');
    $("#room-pagination").html('');

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

    let url = '/houseship/api/house/advanced-search-result/' + page;

    $.ajax({
        method: 'POST',
        url: url,
        async: 'true',
        dataType: "json",
        data: jsonData,
        contentType: 'application/json; charset=utf-8',

        success:function(response){
            countResultSpan.append('查詢結果: 共' + response.length + '筆');
            render(response, searchResultsContainer);
            totalPagesForAdvancedSearch();
        },

        error:function(){
            countResultSpan.append('查詢結果: 共0筆');
            searchResultsContainer.append("<div class='single-items mb-30'>查無結果</div>");
        },

        complete: function(){
            $("#search-results-loader").fadeOut();
            $("#search-results-preloder").delay(200).fadeOut("slow");
            $("#map-iframe").append('<iframe width="100%" height="920px" style="border:0" src="/houseship/api/map"></iframe>');
        }

    });

}

function totalPages() {
    $.ajax({
        method: 'GET',
        url: '/houseship/api/house/totalpages',
        async: 'true',
        dataType: "json",

        success:function(jsonData){
            $('#count-result-span').html('查詢結果: 共' + jsonData.totalElements + '筆');

            if(jsonData.totalPages !== 1) {
                if(jsonData.currentPage !== 1) {
                    $("#room-pagination").append('<a href="#" id="previous-page"><i class="fa fa-long-arrow-left"></i> 上一頁</a>');
                    $("#previous-page").on('click', previousPageButton);
                }

                for(let i = 0; i < jsonData.totalPages; i++) {
                    let index = i + 1;
                    $("#room-pagination").append('<a href="#" id="page-' + index + '">'+ index + '</a>');
                    let str = '#page-' + index;
                    $(str).on('click', pageButton);
                }

                if(jsonData.currentPage !== jsonData.totalPages) {
                    $("#room-pagination").append('<a href="#" id="next-page">下一頁 <i class="fa fa-long-arrow-right"></i></a>');
                    $("#next-page").on('click', nextPageButton);
                }

                function nextPageButton(e) {
                    e.preventDefault();
                    searchResult(jsonData.currentPage + 1);
                }

                function previousPageButton(e) {
                    e.preventDefault();
                    searchResult(jsonData.currentPage - 1);
                }
            }

        },

        error:function(){
        }

    });
}

function pageButton(e) {
    e.preventDefault();
    searchResult(this.innerText);
}

function totalPagesForAdvancedSearch() {
    $.ajax({
        method: 'GET',
        url: '/houseship/api/house/totalpages',
        async: 'true',
        dataType: "json",

        success:function(jsonData){
            $('#count-result-span').html('查詢結果: 共' + jsonData.totalElements + '筆');

            if(jsonData.totalPages !== 1) {
                if(jsonData.currentPage !== 1) {
                    $("#room-pagination").append('<a href="#" id="previous-page"><i class="fa fa-long-arrow-left"></i> 上一頁</a>');
                    $("#previous-page").on('click', previousPageButton);
                }

                for(let i = 0; i < jsonData.totalPages; i++) {
                    let index = i + 1;
                    $("#room-pagination").append('<a href="#" id="page-' + index + '">'+ index + '</a>');
                    let str = '#page-' + index;
                    $(str).on('click', pageButtonForAdvancedSearch);
                }

                if(jsonData.currentPage !== jsonData.totalPages) {
                    $("#room-pagination").append('<a href="#" id="next-page">下一頁 <i class="fa fa-long-arrow-right"></i></a>');
                    $("#next-page").on('click', nextPageButton);
                }

                function nextPageButton(e) {
                    e.preventDefault();
                    searchResult(jsonData.currentPage + 1);
                }

                function previousPageButton(e) {
                    e.preventDefault();
                    searchResult(jsonData.currentPage - 1);
                }
            }
        },

        error:function(){
        }

    });
}

function pageButtonForAdvancedSearch(e) {
    e.preventDefault();
    advancedSearch(this.innerText);
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
            "<a href='/houseship/housedetails/" + value.houseNo + "'>" +
            "<img src='/houseship/api/images/" + value.housePhotos[0].photoPath + "' alt='house image'></a>" +
            "</div>" +
            "<div class='house-tittle house-tittle2' style='width: 300px'>" +
            "<a href='/houseship/housedetails/" + value.houseNo + "'>" +
            "<h4>" +
            value.h_title +
            "</h4></a>" +
            "<div class='description'><ul><li>" +
            houseType +
            "</li><li><i class='icon_pin_alt'></i>" +
            value.city + value.h_address +
            "</li></ul>" +
            "<ul id='house-offers' >" +
            offersList +
            "</ul>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<div class='items-link items-link2 f-right'><span>" +
            value.h_price.toLocaleString() +
            " TWD/晚</span><a href='/houseship/housedetails/" + value.houseNo + "'>前往查看</a></div></div>";

        target.append(houseContent);
    })
}

$('#select-price').on('change', sortByPrice);

function sortByPrice() {
    const target = $('#search-results');
    const condition = $('#select-price').val();
    target.find('.single-items').sort((a,b) => {
        const aPrice = $(a).find('.items-link > span').text().replace(',','').split(' ')[0];
        const bPrice = $(b).find('.items-link > span').text().replace(',','').split(' ')[0];
        console.log("aPrice" + aPrice);
        console.log("bPrice" + bPrice);
        if(condition === 'asc') {
            return aPrice - bPrice;
        } else if (condition === 'desc') {
            return bPrice - aPrice;
        } else {
            return 0;
        }
    }).appendTo(target);

}

