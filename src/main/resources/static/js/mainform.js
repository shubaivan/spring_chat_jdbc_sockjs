jQuery(document).ready(function () {
    jQuery('.chats_info').on('click', function () {
        var current = jQuery(this);
        alert(current.attr('el_id'));
    });
});

function openSideMenu() {
    document.getElementById('side-menu').style.width = '225px';
    document.getElementById('main').style.marginLeft = '225px';
}

function closeSideMenu() {
    document.getElementById('side-menu').style.width = '0px';
    document.getElementById('main').style.marginLeft = '0px';
}