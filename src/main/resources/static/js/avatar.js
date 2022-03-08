function uploadFile(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            var image = document.getElementById("avatar");
            image.style.backgroundImage = "url('" + e.target.result + "')";
        };
        reader.readAsDataURL(input.files[0]);
    }
};

var image = document.getElementById("avatar");
var avatarId = image.getAttribute("field");
if (avatarId > 0) {
    var full = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
    image.style.backgroundImage = "url('" + full + '/api/file_entities/' + avatarId + "')";
}
