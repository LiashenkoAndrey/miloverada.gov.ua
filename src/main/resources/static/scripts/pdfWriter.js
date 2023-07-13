

let start = '<div style="display: flex; justify-content: center;">' +
                '<img src="/img/pdf-logo.png" width="50" height="60" alt="">' +
                '<span style="font-size: 20px; margin-left: 20px; align-self: center;">Милівська сільська територіальна громада</span>' +
            '</div>'

let date = new Date();

let end = '<div id="socialLinks">' +
            '<div><img src="/img/telegram.png" alt=""></div>'+
            '<div style="display: flex; flex-direction: column"><img src="/img/viber.png" alt=""></div>'+
        '</div>' +
        '<p style="font-size: 10px;" class="mt-3">'+ date.getFullYear() + '-' + date.getMonth() + '-' + date.getDay() + '_' + date.getHours()  + ':' + date.getMinutes() +'</p>'


function generatePDF() {
    let container = document.createElement("div");
    let info =  document.querySelector("#info");
    let note =  document.querySelector("#note");
    let table =  document.querySelector("#table");



    container.innerHTML = start + table.innerHTML + note.innerHTML + info.innerHTML + end
    let opt = {
        margin:       1,
        filename:     'Запис.pdf',
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas:  { scale: 2 },
        jsPDF:        { unit: 'in', format: 'letter', orientation: 'portrait' }
    };
    // Choose the element that our invoice is rendered in.
    html2pdf().set(opt).from(container).save();
}