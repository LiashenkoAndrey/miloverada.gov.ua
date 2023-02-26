// editor configuration
tinymce.init({
    selector: '.tinymce-body',
    menubar: false,
    inline: true,
    plugins: [
        'advlist autolink lists link image charmap preview hr anchor pagebreak',
        'searchreplace wordcount visualblocks visualchars code',
        'insertdatetime media nonbreaking table directionality',
        'emoticons template paste textpattern codesample'
    ],
    toolbar: 'undo redo |  bold italic   fontsizeselect |  addimg image | alignleft aligncenter alignright | bullist numlist outdent indent | link | forecolor backcolor emoticons',
    // adding the new button for image upload
    setup: (editor) => {
        editor.ui.registry.addButton('addimg', {
            icon: 'browse',
            onAction: (_) => document.getElementById('uploadImage').click()
        });
    },
    valid_elements: 'p[style],strong,em,span[style],a[href],ul,ol,li',
    valid_styles: {
        '*': 'font-size,font-family,color,text-decoration,text-align'
    },
    powerpaste_word_import: 'clean',
    powerpaste_html_import: 'clean',
});

function showContent() {
    console.log(tinymce.activeEditor.getContent())
}

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

function injectTextAndSendForm() {
    let content = tinymce.activeEditor.getContent();
    document.querySelector("#main_text").innerHTML = content;
    document.querySelector("#submit-form").click();
}