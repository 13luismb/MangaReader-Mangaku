function $(id){
    return document.getElementById(id);
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
                    }
                });
        }

$('subBtn').addEventListener('click', function() {subscribe();});
getSubscribeStatus();