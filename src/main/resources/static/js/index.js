function remove() {
    let obj = document.querySelectorAll(".check");
    let idList = new Array();
    for (let i = 0; i< obj.length; i++) {
        if (obj[i].checked == true)
        {
            idList.push(obj[i].name);
            console.log(obj[i].name);
        }
        // console.log(obj[i], obj[i].checked);
    }

    $.ajax({
        type: 'POST',
        url: '/basic/members',
        traditional: true,
        dataType: 'text',
        // contentType: 'application/json; charset=utf-8',
        data: {
            'ids': JSON.stringify(idList)
        },
    }).fail(function (error) {
        alert(JSON.stringify(error));
    })
}
