function c(clase) {
    return document.getElementsByClassName(clase);
}

function $(id) {
    return document.getElementById(id);
}

let generos = c('genre'); //segun mi logica, hay una lista de los generos que son: class="genre shonen", y le hacemos split.

function doGenreSearch(e) { //aqui implemento lo que te conté arriba
    let x = e.target.className.split(' ')[1];
    let url = ".././search?g=" + x;
    fetch(url, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            fillSearch(data);
        });
}

function doNameSearch() {
    let x = $('search').value;
    let url = ".././search?s=" + x;
    fetch(url, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            fillSearch(data);
        });
}



function fillSearch(data) { //hay que guardar la data en un grid. Este metodo sirve para ambos tipos de busqueda
    for (let i = 0; i < data.length; i++) {
        data[i].name;
        data[i].synopsis;
        data[i].id;
        //	data[i].photo //unimplemented
    }
}


for (let i = 0; i < generos.length; i++) {
    generos[i].addEventListener('click', doGenreSearch);
}

$('search').addEventListener('keypress', function(e) { if (e.keyCode === 13) { doNameSearch(); } });

//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//											HASTA AQUI ES SEARCH. LO SIGUIENTE ES LIKES.						//
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------

window.onload = function() {
    let x = location.search.split(/\=/)[1];
    let url = ".././likes?mid=" + x;
    fetch(url, { method: 'GET' })
        .then(resp => resp.json())
        .then(data => {
            if (data.data.isLiked) {
                $('likeBtn').className += " liked";
            }
        })
}


function like() {
    if ($('likeBtn').className.includes("liked")) {
        doDislike();
    } else {
        doLike();
    }
}


function doLike(data) {
    let x = location.search.split(/\=/)[1];
    let url = ".././likes?mid=" + x;
    console.log(url);
    fetch(url, { method: 'POST' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data.data.isLiked);
            if (data.data.isLiked) {
                $('likeBtn').className += " liked";
            }
        })
}

function doDislike(data) {
    let x = location.search.split(/\=/)[1];
    let url = ".././likes?mid=" + x;
    fetch(url, { method: 'DELETE' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data.data);
            if (!data.data.isLiked) {
                let v = $('likeBtn').className.replace("liked","").trim();
                $('likeBtn').className = v;
            }
        })
}

$('likeBtn').addEventListener('click', function() {like();});

//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//                        HASTA AQUI LOS LIKES (MANGA). LO SIGUIENTE ES LIKES (CHAPTERS).                        //
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------

window.onload = function() {
    let x = location.search.split(/\=/)[1];
    let url = ".././likes?cid=" + x;
    fetch(url, { method: 'GET' })
        .then(resp => resp.json())
        .then(data => {
            if (data.data.isLiked) {
                $('likeBtn').className += " liked";
            }
        })
}


function like() {
    if ($('likeBtn').className.includes("liked")) {
        doDislike();
    } else {
        doLike();
    }
}


function doLike(data) {
    let x = location.search.split(/\=/)[1];
    let url = ".././likes?cid=" + x;
    console.log(url);
    fetch(url, { method: 'POST' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data.data.isLiked);
            if (data.data.isLiked) {
                $('likeBtn').className += " liked";
            }
        })
}

function doDislike(data) {
    let x = location.search.split(/\=/)[1];
    let url = ".././likes?cid=" + x;
    fetch(url, { method: 'DELETE' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data.data);
            if (!data.data.isLiked) {
                let v = $('likeBtn').className.replace("liked","").trim();
                $('likeBtn').className = v;
            }
        })
}

$('likeBtn').addEventListener('click', function() {like();});

//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//                        HASTA AQUI LOS LIKES (CHAPTERS). LO SIGUIENTE ES CRUD DE CHAPTERS.                    //
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------




