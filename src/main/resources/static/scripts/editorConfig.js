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



function customSelectCheck(value, id) {
    let elem = document.getElementById(id);
    if (value === "") elem.style.display = "block";
    else elem.style.display = "none";
}


function newNews(id) {
    let formData = new FormData();

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

    formData.append("description", description)
    formData.append('image', imageInput.files[0])
    formData.append('mainText', mainText);
    formData.append('newsId', id )

    if (document.querySelector("#dateSelect").value === "custom") {
        let data = document.querySelector('#date').value;
        if (data === '') {
            alert("Дата має бути вказана")
            return;
        }
        formData.append("date", data)
    }

    let title = document.querySelector("#typeTitle").value;
    let explanation = document.querySelector("#titleExplanation").value;
    let selectedVal  = document.querySelector("#typeSelect").value;
    if (selectedVal === "") {
        if (title === "" || explanation === "") {
            alert("Поля нового типу мають бути заповненими");
            return;
        }
        formData.append("typeTitle", title);
        formData.append("titleExplanation", explanation);
    } else {
        if (selectedVal !== 'notSelected') {
            formData.append("newsTypeId", selectedVal)
        }
    }



    postRequest(formData, '/news/new', '/');
}

function editNews(newsId) {
    let formData = new FormData();
    let description = document.querySelector("#description").value;
    let imageInput = document.querySelector("#file");

    formData.append('mainText', tinymce.activeEditor.getContent());

    if (imageInput.files.length !== 0) {
        formData.append('image', imageInput.files[0])
    }

    if (description !== '') {
        formData.append("description", description)
    }

    let title = document.querySelector("#typeTitle").value;
    let explanation = document.querySelector("#titleExplanation").value;
    let selectedVal  = document.querySelector("#typeSelect").value;
    if (selectedVal === "") {
        if (title === "" || explanation === "") {
            alert("Поля нового типу мають бути заповненими");
            return;
        }
        formData.append("typeTitle", title);
        formData.append("titleExplanation", explanation);
    } else {
        if (selectedVal !== 'notSelected') {
            formData.append("newsTypeId", selectedVal)
        }
    }


    postRequest(formData, '/news/update?newsId=' + newsId, '/news/' + newsId);
}