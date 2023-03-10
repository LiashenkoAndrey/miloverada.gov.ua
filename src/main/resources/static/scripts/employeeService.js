

function enableFormAndInjectAction(actionUrl, submitBtnValue) {

    let employee_form_wrapper = document.querySelector("#employee-form-wrapper");
    employee_form_wrapper.style.display = 'block';
    wrapper.style.filter = 'blur(8px)';

    let submitBtn = document.querySelector("#submit-employee-form");
    submitBtn.innerHTML = submitBtnValue;

    submitBtn.addEventListener("click", async function () {
        let form = document.querySelector("#employee-form");
        let inputs = form.getElementsByTagName("input");
        let formData = parseInputsAndCreateFormData(inputs)
        await postRequest(formData, actionUrl, 'null')
    });
}

function disableForm(str) {
    let form = document.querySelector("#" + str);
    form.style.display= "none";
    wrapper.style.filter = 'none';
}

function parseInputsAndCreateFormData(inputs) {
    let formData = new FormData();
    Array.prototype.forEach.call(inputs, e => {
        let nameAttribute = e.getAttribute('name')
        if (nameAttribute === 'file') {
            formData.append(nameAttribute, e.files[0])
        } else {
            formData.append(nameAttribute, e.value)
        }
    });
    return formData;
}


