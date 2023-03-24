

async function postRequest(formData ,url, redirectUrl) {
    await fetch(url, {
        method: "POST",
        body: formData
    }).then(response => {
        processResponse(response, redirectUrl);
    })
}

async function getRequest(url, redirectUrl) {
    await fetch(url, {
        method: "GET"
    }).then(response => {
        processResponse(response, redirectUrl);
    })
}



function processResponse(response, redirectUrl) {
    console.log(response.status)
    if (response.status === 200) {
        response.text().then(function (resp) {
            sendNotification(resp, 'Success')
        })
        sleepAndRedirect(1100, redirectUrl);

    } else if (response.status >= 500) {
        console.log(response.text().then(function (resp) {
            sendNotification(resp, 'Error');
        }))
    } else {
        sendNotification("Нажаль сталася помилка", 'Error')
    }
}

function enableFormAndDoPostRequest(submitBtnValue, url, title,) {
    let form = document.querySelector("#formWithOneInput");
    form.style.display = 'block';
    wrapper.style.filter = 'blur(8px)';
    document.querySelector("#inputTitle").innerHTML = title;
    let btn = document.querySelector("#formWithOneInputSubmitBtn")
    btn.value = submitBtnValue;

    btn.addEventListener("click", function () {
        let formData = new FormData();
        formData.append("title", document.querySelector("#title").value)
        postRequest(formData, url, 'null')

    })
}
