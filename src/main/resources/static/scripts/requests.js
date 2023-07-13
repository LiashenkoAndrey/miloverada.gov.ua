let wrapper = document.querySelector(".wrapper");


async function postRequest(formData ,url, redirectUrl) {
    await fetch(url, {
        method: "POST",
        body: formData
    }).then(response => {
        processResponse(response, redirectUrl);
    })
}

async function postRequest(formData, url) {
    return await fetch(url, {
        method: "POST",
        body: formData
    });
}

async function getRequest(url, redirectUrl) {
    await fetch(url, {
        method: "GET"
    }).then(response => {
        processResponse(response, redirectUrl);
    })
}

function processResponse(response, redirectUrl) {
    console.log(response.status)
    if (response.status === 200) {
        response.text().then(function (resp) {
            sendNotification(resp, true)
        })

        sleepAndRedirect(1100, redirectUrl);
    } else if (response.status >= 500) {
        console.log(response.text().then(function (resp) {
            sendNotification(resp, false);
        }))
    } else {
        sendNotification("Нажаль сталася помилка", false)
    }
}


async function sleepAndRedirect(milliseconds, redirectUrl) {
    await new Promise(r => setTimeout(r, milliseconds)); // sleep(n) seconds
    if (redirectUrl === undefined || redirectUrl === 'undefined') {
        document.location.reload();
    } else {
        window.location.replace(redirectUrl)
    }
}

class GetRequestBuilder {
    constructor() {
    }

    params = [];

    submitBtn(val) {
        this.submitBtnVal = val;
        return this;
    }

    url(url) {
        this.urlVal = url;
        return this;
    }

    submitText(text) {
        this.submitText = text;
        return this;
    }

    redirectUrl(url) {
        this.redirectUrlPath = url;
        return this;
    }

    param(name, value) {
        this.params.push({name: name, value: value})
        return this;
    }

    init() {
        let params = this.params;

        for (let i = 0; i < params.length; i++) {
            if (i !== 0) {
                this.urlVal += "&" + params[i].name + "=" + params[i].value;
            } else {
                this.urlVal += "?" + params[i].name + "=" + params[i].value;
            }
        }
        return this;
    }


    build() {
        let form =
            '<div style="height: 100%; width: 100%; top: 0; position:absolute; " >' +
                '<div class="h1" style="right: 30%; left: 30%; top:30%; position: absolute">' +
                    '<div class="text-end">' +
                    '   <button type="button" class="btn-close" onclick="FormService.disableForm(this.parentNode.parentNode.parentNode)" aria-label="Close"></button>' +
                    '</div>' +
                    '<h1 style="color: black">' + this.submitText + '</h1>'+
                    '<button class="btn btn-danger mt-2 h2 admin-btn" onclick="getRequest(\'' + this.urlVal + '\',\'' + this.redirectUrlPath + '\')">'+ this.submitBtnVal +'</button>' +
                '</div>' +
            '</div>';

        wrapper.style.filter = 'blur(8px)';
        document.body.insertAdjacentHTML('beforeend', form)
    }
}



class PostRequestFormBuilder {

    fields = []

    param(vals) {
        this.fields.push(vals)
        return this;
    }


    pathParams = [];

    pathParam(name, value) {
        this.pathParams.push({name: name, value: value});
        return this;
    }

    submitBtn(val) {
        this.submitBtnVal = val;
        return this;
    }

    setTitle(title) {
        this.title = title;
        return this;
    }

    setUrl(url) {
        this.urlPath = url;
        return this;
    }

    setHeader(header) {
        this.header = header;
        return this;
    }

    setRedirectUrl(url) {
        this.redirectUrl = url;
        return this;
    }

    createField(field) {
        if (field.type === undefined || field.type === 'email') {
            if (field.isRequired === undefined) field.isRequired = true;
            return '<label for="'+ field.label +'" class="form-label">'+ field.label +'</label>\n' +
            '<input type="' + (field.type === undefined  ? 'text' : 'email')  + '"' +
                'class="form-control"' +
                'value="' + (field.oldValue === undefined ? '' : field.oldValue) + '" ' +
                'style="font-size: 25px" ' +
                'id="'+ field.label +'" ' +
                'placeholder="'+ (field.placeholder !== undefined ? field.placeholder : '') +'" ' +
                'name="'+ field.name +'"' +
                (field.isRequired ? 'required' : '') + '>';

        } else if (field.type === 'file') {
            return '<label for="'+ field.label +'" class="form-label">'+ field.label +'</label>\n' +
                '<input type="file" class="form-control" style="font-size: 25px" id="'+ field.label +'" name="'+ field.name +'">';
        }
    }

    static parseDataAndDoRequest(node, url, redirect) {

        let formData = new FormData();
        let fieldsWrapper = node.getElementsByClassName("fields")[0];
        let inputsArr = fieldsWrapper.getElementsByTagName('input')
        let labels = fieldsWrapper.getElementsByTagName("label")
        for (let i = 0; i < inputsArr.length; i++) {
            let input = inputsArr[i];
            if (input.value === '' && input.required) {
                alert("Поле '"+ labels[i].innerText +"' не може бути пустим")
                return
            }
            if (input.type === 'file') {
                formData.append(input.name, input.files[0]);
            } else if(input.type === 'email' && input.value !== '') {
                if (!input.value.includes('@')) {
                    console.log(input.value)
                    alert("Пошта вказана невірно")
                    return;
                }
                formData.append(input.name, input.value);
            } else {
                formData.append(input.name, input.value);
            }
        }
        postRequest(formData, url, redirect)
    }

    init() {
        let params = this.pathParams;

        for (let i = 0; i < params.length; i++) {
            if (i === 0)  this.urlPath += '?' + params[i].name + '=' + params[i].value;
            else this.urlPath +=  "&" + params[i].name + '=' + params[i].value
        }

        let inputs = '';
        this.fields.forEach((fieldParams) => {
            inputs += this.createField(fieldParams);
        })
        this.inputs = inputs;
        return this;
    }

    build() {
        console.log(this.urlPath)
        let form =
                '<div class="modalWrapper" style="height: 100%; width: 100%; top: 0; position:absolute; " >' +
                    '<div class="h1" >' +
                        '<div class="text-end">' +
                            '<button type="button" class="btn-close" onclick="FormService.disableForm(this.parentNode.parentNode.parentNode)" aria-label="Close"></button>' +
                        '</div>' +
                        '<h1>' + (this.title !== undefined ? this.title : '') + '</h1>'+
                        '<div class="fields" style="background-color: #00000080;padding: 10px; color: white">'+ this.inputs + '</div>' +
                        '<button class="btn btn-success mt-2 h2 admin-btn" onclick="PostRequestFormBuilder.parseDataAndDoRequest(this.parentNode,\'' + this.urlPath + '\',' + this.redirectUrl + ')">'+ this.submitBtnVal +'</button>' +
                    '</div>' +
                '</div>';

        wrapper.style.filter = 'blur(8px)';
        document.body.insertAdjacentHTML('beforeend', form)
    }
}

class FormService {
    static disableForm(node) {
        node.style.display= "none";
        wrapper.style.filter = 'none';
    }
}



let okForm = '<div class="col-sm-12 notification animateOpen" id="notificationSuccess" style="background-color: rgba(250, 235, 215, 0);">' +
    '<div class="alert  fade alert-simple alert-success alert-dismissible text-left font__family-montserrat font__size-16 font__weight-light brk-library-rendered rendered show">' +
        '<i class="bi bi-bookmark-check-fill h3"></i>' +
        '<span style="font-size: 30px" id="notificationValue"></span>' +
    '</div>' +
'</div>';

let errorFrom = '<div class="col-sm-12 notification animateOpen" id="notificationError" style="background-color: rgba(250, 235, 215, 0);">' +
    '<div class="alert fade alert-simple alert-danger alert-dismissible text-left font__family-montserrat font__size-16 font__weight-light brk-library-rendered rendered show" role="alert" data-brk-library="component__alert">' +
        '<i class="bi bi-x-circle h3"></i>' +
        '<span style="font-size: 30px" id="notificationValue"></span>' +
    '</div>' +
'</div>';



/**
 * Sends notification to user
 * @param message
 * @param isOk identifies theme of notification Success/Error
 * @returns {Promise<void>}
 */
 async function sendNotification (message, isOk) {
     if (isOk) {
         document.body.insertAdjacentHTML('beforeend', okForm)
         document.getElementById("notificationValue").innerText = message;

         await new Promise(r => setTimeout(r, 4000)); // sleep(4sec)
         document.getElementById("notificationSuccess").remove();
     } else {
         document.body.insertAdjacentHTML('beforeend', errorFrom)
         document.getElementById("notificationValue").innerText = message;

         await new Promise(r => setTimeout(r, 4000)); // sleep(4sec)
         document.getElementById("notificationError").remove();
     }
}


