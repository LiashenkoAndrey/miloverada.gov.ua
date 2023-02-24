
let wrapper = document.querySelector(".wrapper");


/**
 * Deletes subgroup by id
 * When a user clicks "Видалити підгрупу" {@link group.html, 167 line} button it enables confirm form and changes
 * in delete button {@code 'onclick'} property
 * @param str
 * @param subGroupId
 */
function enableFormAndInjectDeleteUrl (str, deleteUrl) {
    console.log(str)
    let form = document.querySelector("#" + str);
    form.style.display= "block";
    wrapper.style.filter = 'blur(8px)';
    document.querySelector("#deleteBtn").setAttribute('onclick',"getRequest('" + deleteUrl + "')");
}

function getRequest(deleteUrl) {
    let xmlhttp=new XMLHttpRequest();

    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState===4 && xmlhttp.status===200) {
            console.log("request")
            console.log(xmlhttp.responseText);
            sendNotification(xmlhttp.responseText, 'Success')
            sleepAndReload(3000);
        } else if (xmlhttp.readyState===4 &&  xmlhttp.status===500) {
            sendNotification(xmlhttp.responseText, 'Error')
        } else if (xmlhttp.readyState===4 &&  xmlhttp.status===404) {
            sendNotification("Видалення було неуспішне", 'Error')
        }
    }

    xmlhttp.open("GET", deleteUrl,true);
    xmlhttp.send();
}

async function sleepAndReload(milliseconds) {
    await new Promise(r => setTimeout(r, milliseconds)); // sleep(n) seconds
    document.location.reload();
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
