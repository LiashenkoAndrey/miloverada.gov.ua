
let saveData = (function () {
    let a = document.createElement("a");
    document.body.appendChild(a);
    a.style.display = 'none'
    return function (blob, fileName) {
        let url = window.URL.createObjectURL(blob);
        a.href = url
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
    };
}());




function unzipAndPrintAsList(parent, nameOfFile) {

    parent.onclick = '';
    let d = document.createElement("p")
    parent.appendChild(d)

    fetch('http://localhost/upload/document/' + nameOfFile, {
    })
        .then(response => response.blob())
        .then((data) => {
            let ul = document.createElement("ul");
            ul.classList.add("list-group");

            let reader = new FileReader();
            reader.onload = function (event) {
                const zip = new JSZip();
                zip.loadAsync(event.target.result).then(function (zip) {
                    let filePromises = [];

                    Object.keys(zip.files).forEach(function (filename) {
                        let file = zip.files[filename];
                        if (!file.dir) {
                            filePromises.push(
                                file.async('blob').then(function (blob) {
                                    let a = document.createElement('a');
                                    a.ariaPlaceholder = "Переглянути файл"
                                    a.classList.add("filename")
                                    a.innerText = filename
                                    a.addEventListener("click", function () {
                                        saveData(blob, filename)
                                    });

                                    let docImgSrc;
                                    if (filename.includes("docx")) {
                                        docImgSrc = '/img/documents_formats/word.svg';
                                    } else if (filename.includes("xlsx")) {
                                        docImgSrc = '/img/documents_formats/exel.svg';
                                    } else if (filename.includes("pdf")) {
                                        docImgSrc = '/img/documents_formats/pdf.png';
                                    } else {
                                        docImgSrc = '/img/documents_formats/some_document.svg';
                                    }
                                    let docImage = document.createElement('img');
                                    docImage.src = docImgSrc
                                    docImage.alt = 'Документ'
                                    docImage.width = 50;
                                    docImage.height = 50;

                                    let downloadImg = document.createElement("img");
                                    downloadImg.src = "/img/download.png"
                                    downloadImg.alt = "Завантажити"
                                    downloadImg.ariaPlaceholder = "Завантажити"
                                    downloadImg.width = 40;
                                    downloadImg.height = 40;
                                    downloadImg.style.marginLeft = '20px'
                                    downloadImg.classList.add("download-icon")

                                    let linkWrapper = document.createElement("a");
                                    linkWrapper.ariaPlaceholder = "Завантажити документ"
                                    linkWrapper.style.textDecoration = 'none';
                                    linkWrapper.appendChild(downloadImg);
                                    linkWrapper.addEventListener("click", function () {
                                        saveData(blob, filename)
                                    })

                                    let listItem = document.createElement("li");
                                    listItem.classList.add("list-group-item");
                                    listItem.appendChild(docImage);
                                    listItem.appendChild(a);
                                    listItem.appendChild(linkWrapper);
                                    ul.appendChild(listItem);

                                })
                            )
                        }
                    })
                })
            }
            reader.readAsArrayBuffer(data);
            parent.appendChild(ul);
        })
}


