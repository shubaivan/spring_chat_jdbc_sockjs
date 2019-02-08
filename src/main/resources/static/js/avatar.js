function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            var avatarId = document.getElementById('avatar').value;
            $('#imagePreview').css('background-image', 'url(' + e.target.result + ')');
            $('#imagePreview').hide();
            $('#imagePreview').fadeIn(650);
        }
        reader.readAsDataURL(input.files[0]);
    }
}

$("#imageUpload").change(function () {
    readURL(this);
});

function rewrite() {
    var myDiv = document.getElementById('imagePreview');
    var avatarId = document.getElementById('avatar').value;
    myDiv.style.backgroundImage.concat(avatarId);
}