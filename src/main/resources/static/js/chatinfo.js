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


// <h2 class="text"> All public chats</h2>
// <div layout:fragment="content">
//     <div th:each="chat: ${allPublic}">
//     <a href="#" class="chats_info_full" th:attr="data-el-id=${chat.id}">
//     <h th:text="${chat.name}">Name</h>
//     </a>
//     <br>
//     </div>
//     </div>
//
