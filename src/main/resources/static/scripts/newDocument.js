
let wrapper = document.querySelector(".wrapper");

function enableForm (str) {
    let form = document.querySelector("#" + str);
    form.style.display= "block";
    wrapper.style.filter = 'blur(8px)';

}

/**
 * Deletes subgroup by id
 * When a user clicks "Видалити підгрупу" {@link group.html, 167 line} button it enables confirm form and changes
 * in delete button {@code 'onclick'} property
 * @param str
 * @param subGroupId
 */
function enableDeleteSubGroupForm (str, subGroupId) {
    console.log(str)
    let form = document.querySelector("#" + str);
    form.style.display= "block";
    wrapper.style.filter = 'blur(8px)';
    document.querySelector("#deleteBtn").setAttribute('onclick',"deleteDocument('" + subGroupId + "')");

}


function enableDocumentForm (str) {
    let form = document.querySelector("#" + str);
    form.style.display= "block";
    let add  = document.querySelector("#btn-" + str);
    add.style.display = 'none';
}

function disableForm(str) {
    let form = document.querySelector("#" + str);
    form.style.display= "none";
    wrapper.style.filter = 'none';
}

function disableBtn(str) {
    let form = document.querySelector("#" + str);
    form.style.display= "none";
    wrapper.style.filter = 'none';
    let add  = document.querySelector("#btn-" + str);
    add.style.display = 'inline-block';
}