$(function () {
  //autocomplete
  let DestinationTags = [
    "台北市",
    "台中市",
    "台南市",
    "高雄市",
    "新北市",
    "桃園市",
    "新竹市",
    "新竹縣",
    "苗栗縣",
    "彰化縣",
    "南投縣",
    "雲林縣",
    "嘉義縣",
    "嘉義市",
    "屏東縣",
    "宜蘭縣",
    "花蓮縣",
    "台東縣",
    "澎湖縣",
    "金門縣",
    "馬祖(連江縣)",
    "基隆市"
  ];
  $("#inputCity").autocomplete({
    source: DestinationTags
  });

  $(".city").autocomplete({
    source: DestinationTags
  });

  let sidebar = new StickySidebar('#sidebar', { // 要固定的側邊欄
    containerSelector: '#sidebarMainContent', // 側邊欄外面的區塊
    innerWrapperSelector: '.sidebar__inner',
    topSpacing: 20, // 距離頂部 20px，可理解成 padding-top:20px
    bottomSpacing: 20 // 距離底部 20px，可理解成 padding-bottom:20px
  });

});

