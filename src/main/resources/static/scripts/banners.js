
let form =
    '<div class="modalWrapper">' +
        '<div style="min-width: 500px">' +
            '<div class="text-end mb-4 ">' +
                '<button type="button" class="btn-close btn-close-white" onclick="FormService.disableForm(this.parentNode.parentNode.parentNode)" aria-label="Close"></button>' +
            '</div>' +
            '<div>' +
                '<div style="text-align: center">' +
                    '<h2 style="margin-bottom: 20px;">Оберіть тип Банеру</h2>' +
                    '<button onclick="enableLinkBannerCreationForm(this)" class="btn btn-success admin-btn mx-3">Банер посилання</button>' +
                    '<a href="/text-banner/new" class="btn btn-success admin-btn mx-3">Текстовий банер</a>' +
                '</div>' +
            '</div>' +
        '</div>' +
    '</div>';

let linkBannerForm =
    '<label class="formLabel" for="link_text">Текст посилання</label>' +
    '<input id="link_text" type="text" class="form-control" placeholder="Текст">' +
    '<label for="link_url" class="formLabel">Url</label>' +
    '<input id="link_url" class="form-control" type="text" placeholder="Url">' +
    '<button class="btn btn-success admin-btn mt-3" onclick="createLinkBanner()">Додати</button>'

function createLinkBanner() {
    let text = document.getElementById("link_text").value;
    let url = document.getElementById("link_url").value;

    if (text.length < 4 || text.length > 255 ) {
        alert("Текст посилання має бути у межах від 4 до 255 символів.");
        return;
    }

    if (!isValidHttpUrl(url)) {
        alert("Url некоректний, будь-ласка перевірте його правильність.");
        return;
    }
    fetch("/link-banner/new", {
        method: 'post',
        body: JSON.stringify( {
            text: text,
            url: url
        }),
        headers: new Headers({'content-type': 'application/json'})
    }).then(resp => {
        processResponseAndRedirect(resp, "/")
    })
}

function isValidHttpUrl(string) {
    let url;
    try {
        url = new URL(string);
    } catch (_) {
        return false;
    }
    return url.protocol === "http:" || url.protocol === "https:";
}


function enableBannersCreationForm() {
    FormService.enableForm(form);
}

function enableLinkBannerCreationForm(node) {
    let parent = node.parentNode.parentNode;
    removeAllChildNodes(parent);
    parent.insertAdjacentHTML('beforeend', linkBannerForm);

}

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}