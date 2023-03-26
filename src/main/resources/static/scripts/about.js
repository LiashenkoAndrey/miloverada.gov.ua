

function updateContent() {
    let updatedData = new FormData;
    console.log(tinymce.activeEditor.getContent());
    updatedData.append('main_text', tinymce.activeEditor.getContent());
    postRequest(updatedData, '/about/update', '/about');
}