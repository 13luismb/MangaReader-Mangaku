function $(id){
    return document.getElementById(id);
}


let params = {
        method: "GET",
        withCredentials: true,
        credentials: 'same-origin',
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    }
    fetch('.././dashboard', params)
    .then(res => res.json())
    .then(data => {
        console.log(data);
        if(!(data.data.newManga == null || undefined)){
            data.data.newManga.forEach(element => {
                $('newManga').innerHTML += getCard(element);
            });
        }else{
            $('newManga').innerHTML = "";
        }

        if(!(data.data.myManga == null || undefined)){
            data.data.myManga.forEach(element => {
                $('myManga').innerHTML += getCard(element);
            });
        }else{
            $('myManga').innerHTML = "";
        }

        if(!(data.data.subbedManga == null || undefined)){
            data.data.subbedManga.forEach(element => {
                $('subbedManga').innerHTML += getCard(element);
            });
        }else{
            $('subbedManga').innerHTML = "";
        }
    });

    function getCard(data){
        return '<div class="card grey darken-1 col s3"></br>'+
                    '<div class="card-content white-text">'+
                        '<span class="card-title center">'+data.name+'</span>'+
                    '</div>'+
                    '<div class="card-action center">'+
                       '<br>'+
                       '<a href="manga.html?id=' + data.id + '" class="waves-effect waves-light btn lime">Acceder</a>'+
                   '</div>'+
                '</div>';
    }