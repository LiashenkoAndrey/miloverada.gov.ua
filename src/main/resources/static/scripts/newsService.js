
function customSelectCheck(value, id) {
    let elem = document.getElementById(id);
    if (value === "custom") elem.style.display = "block";
    else elem.style.display = "none";
}


let description = document.querySelector("#description");
let title = document.querySelector("#typeTitle");
let explanation = document.querySelector("#titleExplanation");
let selectedNewsType  = document.querySelector("#typeSelect");
let image = document.querySelector("#file");
let created = document.querySelector("#date");

async function newNews() {
    let formData = new FormData();
    if (description.value.length < 4) {
        alert("Опис занадто короткий, має бути мінімум 4 символи.")
        return;
    }
    if (image.files.length === 0) {
        alert("Завантажте зоображення")
        return;
    }
    if (document.querySelector("#dateSelect").value === "custom") {
        let data = document.querySelector('#date').value;
        if (data === '') {
            alert("Дата має бути вказана")
            return;
        }
        formData.append("date", data)
    }
    if (selectedNewsType.value === "custom") {
        if (title.value === "" || explanation.value === "") {
            alert("Поля нового типу мають бути заповненими");
            return;
        } else formData.append("news_type_id", '');
    } else formData.append("news_type_id", selectedNewsType.value);

    formData.append("typeTitle", title.value);
    formData.append("titleExplanation", explanation.value);
    formData.append("description", description.value)
    formData.append('image', image.files[0])
    formData.append('main_text', tinymce.activeEditor.getContent());
    formData.append("created", created.value);

    doPostAndRedirect(formData, '/news/new', '/');
}

async function editNews(newsId) {
    let formData = new FormData();
    if (image.files.length !== 0) formData.append("image", image.files[0]);

    if (selectedNewsType.value === "custom") {
        if (title.value === "" || explanation.value === "") {
            alert("Поля нового типу мають бути заповненими");
            return;
        } else formData.append("news_type_id", '');
    } else formData.append("news_type_id", selectedNewsType.value);

    formData.append("typeTitle", title.value);
    formData.append("titleExplanation", explanation.value);
    formData.append("id", newsId);
    formData.append("description", description.value);
    formData.append("main_text", tinymce.activeEditor.getContent());
    formData.append("created", created.value);

    await fetch("/news/edit", {
        method: "POST",
        body: formData
    });
    doPostAndRedirect(formData, '/news/update', '/news/' + new URLSearchParams(location.search).get('newsId'));
}

function newTextBanner() {
    if (description.value.length < 4) {
        alert("Опис занадто короткий, має бути мінімум 4 символи.")
        return;
    }

    fetch("/text-banner/new", {
        method: 'post',
        body: JSON.stringify( {
            description: description.value,
            mainText: tinymce.activeEditor.getContent()
        }),
        headers: new Headers({'content-type': 'application/json'})
    }).then(resp => {
        processResponseAndRedirect(resp, "/")
    })
}

function updateTextBanner() {
    if (description.value.length < 4) {
        alert("Опис занадто короткий, має бути мінімум 4 символи.")
        return;
    }

    fetch("/text-banner/update", {
        method: 'put',
        body: JSON.stringify( {
            description: description.value,
            mainText: tinymce.activeEditor.getContent(),
            id: new URLSearchParams(window.location.search).get("id")
        }),
        headers: new Headers({'content-type': 'application/json'})
    }).then(resp => {
        processResponseAndRedirect(resp, "/");
    })
}
