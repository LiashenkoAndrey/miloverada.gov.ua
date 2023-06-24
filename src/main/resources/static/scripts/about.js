

function updateContent() {
    let updatedData = new FormData;
    updatedData.append('main_text', tinymce.activeEditor.getContent());
    postRequest(updatedData, '/about/update', '/about');
}