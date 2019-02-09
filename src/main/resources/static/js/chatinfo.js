$(document).ready(function () {
    var mainContainer = $('#main_container');
    $('.chats_info_full').on('click', function () {
        var current = $(this);

        $.ajax({
            type: "GET",
            url: '/api/chats/' + current.data('elId'),
            success: function (data) {
                console.log(data);
                var startContent = '<div id="main_container">';
                $.each(data, function (key, value) {
                    startContent += '<p>' + key + ': ' + value + '</p>';
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
