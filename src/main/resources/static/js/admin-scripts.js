/*!
    * Start Bootstrap - SB Admin v7.0.4 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2021 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
// 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});


var page = {
    home: "/houseship/home",
    logout: "/houseship/SignOut",
    member: "/houseship/member",
    house: "/houseship/house",
    order: "/houseship/order/viewAll",
    coupon: "workInProgress.html",
    forum: "/houseship/forum",
};

document.querySelector("#logout").setAttribute("href", page.logout);
home = document.querySelectorAll(".home");
member = document.querySelectorAll(".member");
house = document.querySelectorAll(".house");
order = document.querySelectorAll(".order");
coupon = document.querySelectorAll(".coupon");
forum = document.querySelectorAll(".forum");
for (let e of home) { e.setAttribute("href", page.home) };
for (let e of member) { e.setAttribute("href", page.member) };
for (let e of house) { e.setAttribute("href", page.house) };
for (let e of order) { e.setAttribute("href", page.order) };
for (let e of coupon) { e.setAttribute("href", page.coupon) };
for (let e of forum) { e.setAttribute("href", page.forum) };
