

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

    } else { sendNotification(response.text(), 'Error') }
}