$(document).ready(function () {
    var mainContainer = $('#main_container');
    $('.chats_info').on('click', function () {
        var current = $(this);

        $.ajax({
            type: "GET",
            url: '/api/chats/' + current.data('elId'),
            success: function (data) {
                console.log(data);
                var startContent = '<div id="main_container">';
                $.each( data, function( key, value ) {
                    startContent+= '<p>' + key + ': ' + value + '</p>';
                });
                startContent += '</div>';
                mainContainer.replaceWith(startContent);
            },
            error: function (result) {
                console.log(result)
            }
        })
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