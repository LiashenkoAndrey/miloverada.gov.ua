
function getCurrenTopMarginOfLine(hours, currentTime) {
    return Math.abs(currentTime - hours * 60);
}


function updateTimeLine() {
    updateInfo();

    let timeCells = document.querySelector("#timeCells").getElementsByTagName('tr');
    let lastTimeArr = timeCells[timeCells.length-1].getElementsByClassName('time')[0].innerHTML.split(":");
    let date = new Date();
    let timeLine = document.querySelector("#time-line");

    if (lastTimeArr[0] >= date.getHours()) {
        let startTimeArr = timeCells[0].getElementsByClassName('time')[0].innerHTML.split(':'); // get time from first record;
        let currentTime = date.getMinutes()  + (date.getHours() * 60);
        let startTime = parseInt(startTimeArr[1], 10) / 60 + parseInt(startTimeArr[0], 10);

        timeLine.style.top = (getCurrenTopMarginOfLine(startTime, currentTime) + 72) + 'px'
    } else {
        timeLine.style.display = 'none';
    }
}


function emptyCell() { return '<td></td>'}


function reservedCell(record, index) {
    let current = new Date();
    console.log('index = ' + index + " , current = " + current.getDay() + ", " + (index === current.getDay()-1));

    if (index === current.getDay()-1) {
        console.log("reserved")
        return "<td><div class='reserved' data-person='"+ JSON.stringify(record.person) +"' onclick='"+  'printPersonData(this)' +"' ></div></td>"
    } else {
        console.log("disabled")
        return "<td><div class='reserved disabledCell' data-person='"+ JSON.stringify(record.person) +"' onclick='"+  'printPersonData(this)' +"' ></div></td>"
    }
}

let personName = document.querySelector("#personName");
let personPhoneNumber = document.querySelector("#personPhoneNumber");
let personComment = document.querySelector("#personComment");

function printPersonData(elem) {
    let person = JSON.parse(elem.getAttribute("data-person"));
    personName.innerHTML = person.firstName + " " + person.lastName + " " + person.surname;
    personPhoneNumber.innerHTML = person.phoneNumber;
    if (person.note != null) {
        personComment.innerHTML = person.note;

    } else {
        personComment.innerHTML = '';
    }
}

let hours = ['08:00:00', '08:30:00', '09:00:00', '09:30:00', '10:00:00', '10:30:00', '11:00:00', '11:30:00', "12:00:00", "12:30:00", '13:00:00', '13:30:00', '14:00:00', '14:30:00', '15:00:00', '15:30:00','16:00:00', '16:30:00'];


async function getRequest(url, params) {
    return await fetch(url + "?" + new URLSearchParams(params), {
        method: "GET"
    })
}

function renderContent(serviceId) {
    startLoading()


    getRequest('http://localhost/queue/record/find-week',
        {
            date: new Date().toISOString().split('T')[0],
            serviceId: serviceId
        }
    ).then(response => {
        return response.json();

    }).then(dataArr => {
        let arrOfCells = document.querySelector("#timeCells").getElementsByTagName('tr');

        for (let i = 0; i < arrOfCells.length; i++) {
            let arr = arrOfCells[i].getElementsByTagName('td');
            let time = hours[i];

            for (let j = 0; j < 7; j++) {

                const found = dataArr[j].find(element => {
                    return element.timeOfVisit === time;
                });

                if (found !== undefined) {
                    arr[j+1].innerHTML = reservedCell(found, j);
                } else {
                    arr[j+1].innerHTML = emptyCell();
                }
            }
        }

        stopLoading();
        updateInfo();

        setCookie('serviceId', serviceId, 5);
    })

}

function updateContent(serviceId) {
    renderContent(serviceId);
}

let iframe = document.querySelector("#table");
let loading = document.querySelector("#loadingIcon");

function startLoading() {
    iframe.classList.remove("selectable")
    iframe.classList.add("noselect")
    iframe.style.filter = 'blur(3px)'
    iframe.style.pointerEvents = 'none'
    loading.style.display = 'block';
}

function stopLoading() {
    iframe.classList.add("selectable")
    iframe.classList.remove("noselect")
    iframe.style.filter = 'none'
    iframe.style.pointerEvents = 'all'
    loading.style.display = 'none';
}


function fillTableInfo() {
    let current = new Date();
    let arr = document.getElementsByClassName("table-day-number");
    let start = current.getDate() - current.getDay() + 1;

    for (let i = 0; i < 7; i++) {
        arr[i].innerHTML = start;
        start++;
    }
}



function clearTable() {
    let arrOfCells = document.querySelector("#timeCells").getElementsByTagName('tr');

    for (let i = 0; i < arrOfCells.length; i++) {
        let arr = arrOfCells[i].getElementsByTagName('td');

        for (let i = 1; i < arr.length; i++) {
            arr[i].innerHTML = '';
        }
    }
}

function changeService() {
    let serviceSelect = document.querySelector("#serviceSelect");
    let selected = serviceSelect.options[serviceSelect.selectedIndex];
    let serviceId = selected.getAttribute("data-serviceId");

    clearTable();
    updateContent(serviceId);
}

let currentPersonFullName = document.querySelector("#currentPersonFullName");
let currentPersonPhoneNumber = document.querySelector("#currentPersonPhoneNumber");

function updateInfo() {
    let date = new Date();
    let currentHours = date.getHours();

    if (currentHours !== 12) {
        let currentMins  = date.getMinutes();
        let mins;
        let hour;
        if (currentMins < 30) mins = '00';
        else mins = '30'

        if (currentHours.length < 2) hour = '0' + currentHours;
        else hour = currentHours;

        let arrOfCells = document.querySelector("#timeCells").getElementsByTagName('tr');

        let time = hour + ":" + mins + ":00"
        let cellLine = arrOfCells[hours.indexOf(time)];

        if (cellLine !== undefined) {
            let cells = cellLine.getElementsByTagName("td");

            let divArr = cells[date.getDay()].getElementsByTagName('div');

            if (divArr.length !== 0) {
                enableInfo();
                let personJSON = divArr[0].getAttribute("data-person");
                document.querySelector("#infoIcon").setAttribute('data-person', personJSON)
                let person = JSON.parse(personJSON);
                currentPersonFullName.innerHTML = person.firstName + " " + person.lastName + " " + person.surname;
                currentPersonPhoneNumber.innerHTML = person.phoneNumber;
            } else {
                disableInfo();
            }
        } else {
            disableInfo();
        }

    } else {
        disableInfo()
    }

}

function disableInfo() {
    document.querySelector("#infoWrapper").style.display = 'none';
    document.querySelector("#infoAndUpdateInfoWrapper").style.borderBottom = 'none'
}

function enableInfo() {
    document.querySelector("#infoWrapper").style.display = 'flex';
    document.querySelector("#infoAndUpdateInfoWrapper").style.borderBottom = '1px solid #ced4da'
}

let months = ['Січень', 'Лютий', 'Березень', 'Квітень', 'Травень', 'Червень', 'Липень', 'Серпень', 'Вересень', 'Жовтень', 'Листопад', 'Грудень'];

function init() {
    let currentDate = new Date();
    let date = document.querySelector("#date");
    date.innerHTML = months[currentDate.getMonth()] + " " + currentDate.getFullYear();

    let cellsLines = document.querySelector("#timeCells")

    for (let i = 0; i < 18; i++) {
        let row = document.createElement('tr');
        let timeCell = document.createElement("td");
        let time= hours[i];
        timeCell.appendChild(document.createTextNode(time.substring(0, time.length-3)))
        timeCell.classList.add('time');

        row.appendChild(timeCell);

        for (let j = 0; j < 7; j++) {
            row.insertCell();
        }

        cellsLines.appendChild(row)
    }
}


window.addEventListener("load", () => {
    init();
    fillTableInfo();

    let serviceId = getCookie("serviceId");

    let serviceSelect = document.querySelector("#serviceSelect");
    if (serviceId != null) {
        renderContent(serviceId);

        const vals = [...serviceSelect.options].map(el => el = {id: el.getAttribute('data-serviceId'), item: el});
        let found = vals.find(elem => elem.id === serviceId);

        serviceSelect.selectedIndex = vals.indexOf(found);
    } else {
        if (serviceSelect.options.length !== 0) {
            let id = serviceSelect.options[0].getAttribute("data-serviceId");
            renderContent(id);
        } else {
            console.log("serviceSelect, options is empty!")
        }
    }
    updateTimeLine();
    setInterval(updateTimeLine, 60000);
});