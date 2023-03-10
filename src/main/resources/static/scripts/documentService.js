
let wrapper = document.querySelector(".wrapper");


/**
 * todo: update doc
 * When a user clicks "delete" button it enables confirm form and changes
 * in delete button {@code 'onclick'} property
 * @param message
 * @param confirm_btn_text
 * @param getUrl
 * @param redirectUrl
 */
function enableFormAndDoGetRequest(message, confirm_btn_text, getUrl, redirectUrl) {
    document.querySelector("#confirm-form").style.display= "block";
    document.querySelector("#confirm-form-message").innerHTML = message;
    wrapper.style.filter = 'blur(8px)';

    let confirm_btn = document.querySelector("#confirm-operation-btn");
    confirm_btn.innerHTML = confirm_btn_text;
    confirm_btn.addEventListener('click', function () {
        getRequest(getUrl, redirectUrl);
    });
}



function enableFormAndSaveDocument(title, btnValue,url, redirectUrl) {
    document.querySelector("#document").style.display= "block";
    wrapper.style.filter = 'blur(8px)';
    let document_title = document.querySelector("#nameOfDocument")
    let document_file = document.querySelector("#input-file")
    let confirmBtn = document.querySelector("#document-submit");

    confirmBtn.value = btnValue;
    document.querySelector("#documentTitle").innerHTML = title

    confirmBtn.addEventListener('click', async function () {
        console.log(!document_file.files[0])

        let formData = new FormData();
        if (document_file.files[0]) {
            if (checkFileSize(document_file.files[0].size)) {
                formData.append("file", document_file.files[0])
            }
        }
        formData.append("title", document_title.value)
        await fetch(url, {
            method: "POST",
            body: formData
        }).then(response => {
            processResponse(response, redirectUrl)
        })

    })
}

function checkFileSize(size) {
    if (size < 10485760) {
        return true;
    } else {
        sendNotification("Перевищено ліміт файлу 10MB!", 'Error')
        return false
    }
}


async function sleepAndRedirect(milliseconds, redirectUrl) {
    await new Promise(r => setTimeout(r, milliseconds)); // sleep(n) seconds

    if (redirectUrl === 'null') {
        document.location.reload();
    } else {
        window.location.replace(redirectUrl)
    }
}

/**
 * Sends notification to user
 * @param message
 * @param status identifies theme of notification e.g(Success/Error)
 * @returns {Promise<void>}
 */
async function sendNotification (message, status) {
    let notificationOk = document.querySelector("#notification" + status);
    document.querySelector('#notificationValue' + status).innerHTML = message; // inject message
    notificationOk.style.display = 'block';

    await new Promise(r => setTimeout(r, 4000)); // sleep(4sec)
    notificationOk.style.display = 'none';
}


function enableForm (str) {
    let form = document.querySelector("#" + str);
    form.style.display= "block";
    wrapper.style.filter = 'blur(8px)';
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
