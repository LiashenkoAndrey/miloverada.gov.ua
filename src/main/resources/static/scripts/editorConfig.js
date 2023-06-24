// editor configuration
tinymce.init({
    selector: '.tinymce-body',

    plugins: [
        'advlist autolink lists link image charmap preview hr anchor pagebreak',
        'searchreplace wordcount visualblocks visualchars code',
        'insertdatetime media nonbreaking table directionality',
        'emoticons template paste textpattern codesample'
    ],
    toolbar: 'undo redo |  bold italic   fontsizeselect | hr alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | codesample | link | addimg image forecolor backcolor emoticons',
    // adding the new button for image upload
    setup: (editor) => {
        editor.ui.registry.addButton('addimg', {
            icon: 'browse',
            onAction: (_) => document.getElementById('uploadImage').click()
        });

        editor.on('init', () => {
            if (fillContent() != null) {
                editor.setContent(fillContent());
            }
        });
    }
});


// adding event listener for invisible form
input = document.getElementById('uploadImage');
input.addEventListener("change", () => {
    doUploadImage()
})


/**
 * Sends an image to the server and returns an url an image
 * @returns {Promise<void>}
 */
async function doUploadImage() {
    // invisible form
    let input = document.getElementById("uploadImage")

    let formData = new FormData();
    formData.append("file", input.files[0])

    await fetch("/upload/image", {
        method: "POST",
        body: formData
    }).then(response => response.text().then(result => {
        tinymce.activeEditor.setContent(tinymce.activeEditor.getContent() + '<img src="/upload/image/' + result + '" alt="image" class="img-fluid" data-action="zoom">')
    }))
}



function yesnoCheck(that) {
    if (that.value === "custom") {
        document.getElementById("ifYes").style.display = "block";
    } else {
        document.getElementById("ifYes").style.display = "none";
    }
}


function newNews(id) {
    let fromData = new FormData();

    let description = document.querySelector("#description").value;
    let imageInput = document.querySelector("#file");
    let mainText = tinymce.activeEditor.getContent();

    if (description.length < 4) {
        alert("Опис занадто короткий, має бути мінімум 4 символи.")
        return;
    }

    if (imageInput.files.length === 0) {
        alert("Завантажте зоображення")
        return;
    }

    fromData.append("description", description)
    fromData.append('image', imageInput.files[0])
    fromData.append('mainText', mainText);
    fromData.append('newsId', id )

    if (document.querySelector("#dateSelect").value === "custom") {
        let data = document.querySelector('#date').value;
        if (data === '') {
            alert("Дата має бути вказана")
            return;
        }
        fromData.append("date", data)
    }

    postRequest(fromData, '/news/new', '/');
}

function editNews(newsId) {
    let fromData = new FormData();
    let description = document.querySelector("#description").value;
    let imageInput = document.querySelector("#file");

    fromData.append('mainText', tinymce.activeEditor.getContent());

    if (imageInput.files.length !== 0) {
        fromData.append('image', imageInput.files[0])
    }

    if (description !== '') {
        fromData.append("description", description)
    }

    postRequest(fromData, '/news/update?newsId=' + newsId, '/news/' + newsId);
}