
let editableWrappers = document.getElementsByClassName("editable_wrapper");


for (let i = 0; i < editableWrappers.length; i++) {
    let elem = editableWrappers[i];
    let img = elem.getElementsByTagName('img')[0];

    if (img !== undefined) {
        elem.addEventListener('mouseover', function () {
            img.style.display = 'inline';
        });

        elem.addEventListener('mouseleave', function () {
            img.style.display = 'none';
        });

    }
}


