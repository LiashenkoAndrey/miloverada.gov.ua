

function updateContent() {
    let updatedData = new FormData;
    updatedData.append('main_text', tinymce.activeEditor.getContent());
    doPostAndRedirect(updatedData, '/about/update', '/about');
}