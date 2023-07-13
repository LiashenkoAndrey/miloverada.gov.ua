

const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

let selectedPlace = undefined;
let inputsWrapper = document.querySelector("#inputsWrapper");

function selectPlace(record) {
    if (selectedPlace !== undefined) {
        selectedPlace.style.border = 'none'
    }

    record.style.border = '7px solid black'
    selectedPlace = record;
    inputsWrapper.style.display = 'block'
}


let records = document.querySelector("#records");
let date = document.querySelector("#dateVal");

window.onload = () => {
    date.valueAsDate  = new Date();
    refreshRecords()
};

let icon = document.querySelector("#loadingIcon");
function startLoading() {
    records.classList.remove("selectable")
    records.classList.add("noselect")
    records.style.filter = 'blur(3px)'
    records.style.pointerEvents = 'none'
    icon.style.display = 'block';
}

function stopLoading() {
    records.classList.add("selectable")
    records.classList.remove("noselect")
    records.style.filter = 'none'
    records.style.pointerEvents = 'all'
    icon.style.display = 'none';
}

async function refreshRecords() {
    selectedPlace = undefined;

    startLoading()
    await fetch("/queue/record/find?" + new URLSearchParams({date: date.value, serviceId:  urlParams.get("serviceId")}), {
        method: "GET"
    }).then(response => {
        return response.json();
    }).then(dataArr => {
        let recordsList = '';
        if (dataArr.length === 0) {
            for (let i = 0; i < 12; i++) {
                recordsList += makeEmptyPlace(i);
            }
        } else {
            for (let i = 0; i < 12; i++) {
                let time = hours[i] + ':00' // added seconds to the time to make it in format hh:mm:ss
                if (dataArr.includes(time)) {
                    recordsList += makeBusyPlace(i);
                } else {
                    recordsList += makeEmptyPlace(i);
                }
            }
        }

        records.innerHTML = '';
        records.insertAdjacentHTML('beforeend', recordsList);
        records.style.display = 'grid'
        stopLoading()
    })
}

let hours = ['8:30', '9:00', '9:30', '10:00', '10:30', '11:00', '11:30', '13:00', '13:30', '14:00', '14:30', '15:00'];
let imagesUrls = ["/img/people/1.svg", "/img/people/2.svg", "/img/people/3.svg", "/img/people/4.svg", "/img/people/5.svg", "/img/people/6.svg"]

function makeBusyPlace(index) {
    return '<div class="record">' +
                '<h4>'+ hours[index] +'</h4>' +
                '<img src="'+ imagesUrls[getRandomInt(0, 5)] +'" onclick="showMessage(this)" alt="">'+
            '</div>';
}

function makeEmptyPlace(index) {
    return '<div class="record">' +
                '<h4>'+ hours[index] +'</h4>' +
                '<div title="Записатися на 12:00" class="empty-place" onclick="selectPlace(this)"></div>'+
            '</div>';
}


function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function stepUpDate(elem, isUp) {
    let input = elem.parentNode.getElementsByTagName('input')[0];
    if (isUp) input.stepUp(1);
    else input.stepDown(1);

    refreshRecords()
}

let elem = document.querySelector("#errorMessage");
function showErrorMessage(msg) {
    elem.innerHTML = msg;
}

function clearError() {
    elem.innerHTML = '';
}

function inputsAreValid() {
    let firstName = document.querySelector("#lastName").value;
    let lastName = document.querySelector("#lastName").value;
    let surname = document.querySelector("#surname").value;
    let phoneNumber = document.querySelector("#phoneNumber").value;

    if (firstName.length < 3 || firstName.length > 100) {
        showErrorMessage("Поле 'Ім'я' має бути заповненим");
        if (firstName.length > 100) showErrorMessage("Поле 'Ім'я' занадто довге")
        return false;
    }

    if (lastName.length < 3 || lastName.length > 100) {
        showErrorMessage("Поле 'Прізвище' має бути заповненим");
        if (lastName.length > 100) showErrorMessage("Поле 'Прізвище' занадто довге")
        return false;
    }

    if (surname.length < 3 || surname.length > 100) {
        showErrorMessage("Поле 'Ім'я по батькові' має бути заповненим");
        if (surname.length > 100) showErrorMessage("Поле 'Ім'я по батькові' занадто довге")
        return false;
    }

    if (firstName.length < 3 || firstName.length > 100) {
        showErrorMessage("Поле 'Ім'я' має бути заповненим");
        if (firstName.length > 100) showErrorMessage("Поле 'Ім'я' занадто довге")
        return false;
    }

    if (phoneNumber.length > 15 || phoneNumber.length < 10) {
        console.log(phoneNumber.length)
        showErrorMessage("Поле 'Телефон' має неправильний формат");
        return false;
    }

    return true;
}

function createRecord() {
    if (inputsAreValid()) {
        let firstName = document.querySelector("#firstName").value;
        let lastName = document.querySelector("#lastName").value;
        let surname = document.querySelector("#surname").value;
        let phoneNumber = document.querySelector("#phoneNumber").value;
        let time = selectedPlace.parentNode.getElementsByTagName("h4")[0].innerHTML;
        let note = document.querySelector("#note").value;
        let dateOfRecord = date.value;

        let data = new FormData();

        data.append("firstName", firstName);
        data.append("lastName", lastName);
        data.append("surname", surname);
        data.append("phoneNumber", phoneNumber);
        data.append("dateOfVisit", dateOfRecord);
        data.append("timeOfVisit", time);
        data.append("note", note)


        data.append("serviceId", urlParams.get("serviceId"))


        postRequest(data, "/queue/record/new").then((r) => {
            if (r.status === 200) {
                console.log('OK!!!')
                r.json().then((data) => {
                    window.location.replace("/queue/record/details?recordId=" + data)
                })
            }
        })
    }
}



