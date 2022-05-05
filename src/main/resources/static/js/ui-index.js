$(function () {
  //autocomplete
  var DestinationTags = [
    "台北",
    "台中",
    "台南",
    "高雄",
    "新北",
    "桃園",
    "新竹",
    "苗栗",
    "彰化",
    "南投",
    "雲林",
    "嘉義",
    "屏東",
    "宜蘭",
    "花蓮",
    "台東",
    "澎湖",
    "金門",
    "馬祖",
    "基隆"
  ];
  $("#inputCity").autocomplete({
    source: DestinationTags
  });

  var sidebar = new StickySidebar('#sidebar', { // 要固定的側邊欄
    containerSelector: '#sidebarMainContent', // 側邊欄外面的區塊
    innerWrapperSelector: '.sidebar__inner',
    topSpacing: 20, // 距離頂部 20px，可理解成 padding-top:20px
    bottomSpacing: 20 // 距離底部 20px，可理解成 padding-bottom:20px
  });

});

