

function enableFormAndInjectAction(actionUrl, submitBtnValue) {

    let employee_form_wrapper = document.querySelector("#employee-form-wrapper");
    employee_form_wrapper.style.display = 'block';
    wrapper.style.filter = 'blur(8px)';

    let submitBtn = document.querySelector("#submit-employee-form");
    submitBtn.innerHTML = submitBtnValue;

    submitBtn.addEventListener("click", async function () {
        let formData = new FormData();
        let form = document.querySelector("#employee-form");

        let inputs = form.getElementsByTagName("input");
        Array.prototype.forEach.call(inputs, e => {
            let nameAttribute = e.getAttribute('name')
            if (nameAttribute === 'file') {
                formData.append(nameAttribute, e.files[0])
            } else {
                formData.append(nameAttribute, e.value)
            }
        });

        await fetch(actionUrl, {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.status === 200) {
                response.text().then(function (resp) {
                    sendNotification(resp, 'Success')
                })
                sleepAndRedirect(1100, 'null');

            } else { sendNotification(response.text(), 'Error') }
        })
    });
}

function disableForm(str) {
    let form = document.querySelector("#" + str);
    form.style.display= "none";
    wrapper.style.filter = 'none';
}


