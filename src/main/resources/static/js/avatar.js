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
image.style.backgroundImage = "url('http://localhost:8080/api/file_entities/" + avatarId + "')";
