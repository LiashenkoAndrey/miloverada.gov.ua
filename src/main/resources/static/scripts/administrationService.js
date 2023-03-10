

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
