const searchButton = $("#searchRooms");

searchButton.click(function (e){
    e.preventDefault();
    const inputCity = $("#inputCity").val();
    const checkInDate = $("#checkin_send").val();
    const checkOutDate = $("#checkout_send").val();
    const inputAdult = $("#adult").val();
    const inputChildren = $("#children").val();
    const inputRoom = $("#room").val();

    console.log(checkInDate);

    if (inputCity != "" & checkInDate == "" & checkOutDate == ""){
        location.href = "/houseship/search/" + inputCity;
    }

    if (inputCity != "" & checkInDate != "" & checkOutDate != ""){
        location.href = "/houseship/search/" + inputCity + "/" + checkInDate + "/" + checkOutDate;
    }

    if (inputCity == "" & checkInDate != "" & checkOutDate != ""){
        location.href = "/houseship/search/" + checkInDate + "/" + checkOutDate;
    }


})
