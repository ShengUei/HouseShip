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
});

