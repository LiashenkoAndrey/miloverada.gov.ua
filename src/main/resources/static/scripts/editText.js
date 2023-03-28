
let edit_icon_wrapper = document.querySelector("#edit_icon_wrapper");
let icon = document.querySelector('#edit_icon');

if (edit_icon_wrapper != null && icon != null) {

    edit_icon_wrapper.addEventListener('mouseover', function () {
        icon.style.display = 'inline';
    });



    edit_icon_wrapper.addEventListener('mouseleave', function () {
        icon.style.display = 'none';
    });

}
