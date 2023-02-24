

function deleteDocument(documentId) {
    let url= "/group/sub-group/" + documentId + "/delete";
    console.log(url);
    xmlhttp=new XMLHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            console.log("request")
            console.log(xmlhttp.responseText);
        }
    }
    xmlhttp.open(
        "GET",
        url,
        true);
    xmlhttp.send();
}
