

let newsArr = document.getElementsByClassName("news");

for (let i = 0; i < newsArr.length; i++) {
    let img = newsArr[i].getElementsByTagName("img")[0];
    makeAppropriateStyle(img);
}

function makeAppropriateStyle(img) {
    img.onload = function () {
        if (img.naturalHeight > img.naturalWidth) {
            img.style.objectFit = 'cover'
            img.style.objectPosition = 'top'
        } else if (img.naturalHeight < img.naturalWidth) {
            img.style.objectFit = 'scale-down'
        } else {
            console.log("h: " + img.naturalHeight + ", w: " + img.naturalWidth + ", src: " + img.src)
        }
    }
}

