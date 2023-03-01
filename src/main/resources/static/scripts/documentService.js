
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
    confirm_btn.setAttribute('onclick',"getRequest('" + getUrl + "','" + redirectUrl + "')");
}


function getRequest(deleteUrl, redirectUrl) {
    let xmlhttp=new XMLHttpRequest();

    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState===4 && xmlhttp.status===200) {
            console.log("request")
            console.log(xmlhttp.responseText);
            sendNotification(xmlhttp.responseText, 'Success')
            sleepAndRedirect(3000, redirectUrl);
        } else if (xmlhttp.readyState===4 &&  xmlhttp.status===500) {
            sendNotification("Видалення було неуспішне", 'Error')
        } else if (xmlhttp.readyState===4 &&  xmlhttp.status===404) {
            sendNotification("Видалення було неуспішне", 'Error')
        }
    }

    xmlhttp.open("GET", deleteUrl,true);
    xmlhttp.send();
}

function enableFormAndUpdateDocument(url, redirectUrl) {
    document.querySelector("#update-document").style.display= "block";
    wrapper.style.filter = 'blur(8px)';
    let document_title = document.querySelector("#new-document-title")
    let document_file = document.querySelector("#new-document-file")
    let confirmBtn = document.querySelector("#new-document-confirm-operation-btn");
    confirmBtn.addEventListener('click', async function () {
        let formData = new FormData();
        formData.append("file", document_file.files[0])
        formData.append("title", document_title.value)
        await fetch(url, {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.status === 200) {
                sendNotification("Оновлення успішне", 'Success')
                sleepAndRedirect(3000, redirectUrl);
            } else {
                sendNotification("Оновлення було неуспішне", 'Error')
            }

        })
    })

}

function enableFormAndSaveDocument() {

}



async function postRequest(url) {
    let input = document.getElementById("uploadImage")
    let formData = new FormData();
    formData.append("file", input.files[0])
    await fetch("/image", {
        method: "POST",
        body: formData
    }).then(response => response.text().then(result => console.log(result)))
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
