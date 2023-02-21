
let wrapper = document.querySelector(".wrapper");

function enableForm (str) {
    let form = document.querySelector("#" + str);
    form.style.display= "block";
    wrapper.style.filter = 'blur(8px)';
}

function disableForm(str) {
    let form = document.querySelector("#" + str);
    form.style.display= "none";
    wrapper.style.filter = 'none';
}