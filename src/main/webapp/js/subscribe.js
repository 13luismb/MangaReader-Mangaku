function $(id){
    return document.getElementById(id);
}

window.onload = function(){
    if(localStorage.getItem("userInfo") == null||undefined){
        $('subscribeGuest').hidden = false;
        $('subscribeUser').hidden = true;
    }else{
        $('subscribeGuest').hidden = true;
        $('subscribeUser').hidden = false;
    }
}

function subscribe() {
    if ($('subBtn').className.includes("subbed")) {
        doUnsubscribe();
    } else {
        doSubscription();
    }
}



function doSubscription() {
    let x = location.search.split(/\=/)[1];
    let url = ".././subscribe?id=" + x;
    console.log(url);
    fetch(url, { method: 'POST' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data);
            if (data.data.isSubscribed) {
                $('subBtn').className += " subbed";
                $('subBtn').innerText = "Desuscribirse";
            }
        })
}

function doUnsubscribe() {
    let x = location.search.split(/\=/)[1];
    let url = ".././subscribe?id=" + x;
    fetch(url, { method: 'DELETE' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data);
            if (!data.data.isSubscribed) {
                let v = $('subBtn').className.replace("subbed","").trim(); //esto hay que cambiarlo, no se como hacer que se mantenga la clase sin el "liked"
                $('subBtn').className = v;
                $('subBtn').innerText = "Subscribete!";
            }
        })
}
        function getSubscribeStatus(){
            let x = location.search.split(/\=/)[1];
            let url = ".././subscribe?id=" + x;
            console.log(url);
            fetch(url, { method: 'GET' })
                .then(resp => resp.json())
                .then(data => {
                    console.log(data);
                    if (data.data.isSubscribed) {
                        $('subBtn').className += " subbed";
                        $('subBtn').innerText = "Desuscribirse";
                    }else{
                        $('subBtn').innerText = "Suscribirse";
                    }
                });
        }

        function visitorSubscribe(){
            let x = location.search.split(/\=/)[1];
            let y = $('email_inline').value;
            let url = ".././subscribe?id=" + x + "&email=" + y;
            console.log(url);
            fetch(url, { method: 'POST' })
                .then(resp => resp.json())
                .then(data => {
                    if(data.status === 202){
                        alert('You subscribed!');
                        $('email_inline').value= "";
                    }
                });
        }

$('subBtn').addEventListener('click', function() {subscribe();});
$('visitorBtn').addEventListener('click',visitorSubscribe);
getSubscribeStatus();