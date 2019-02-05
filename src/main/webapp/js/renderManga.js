window.onload = function() {
    let x = location.search.split(/\=/)[1];
    let url = ".././likes?mid=" + x;
    fetch(url, { method: 'GET' })
        .then(resp => resp.json())
        .then(data => {
            //console.log("hola");
            if(data.status!=500){
                if (data.data.isLiked) {
                    $('likeBtn').className += " liked";
                }
            }else{
                $('likeBtn').hidden = true;
            }
        })
    }

let params = location.href.split('?')[1];
getIdManga();

let config = {
    method: "GET",
    withCredentials: true,
    credentials: 'same-origin',
    headers: {
        "Content-type": "application/x-www-form-urlencoded"
    }
};

fetch('.././manga?'+params, config)
.then(res => res.json())
.then(data => {
    console.log(data)
    if(data.status == 200 || data.status == 201){
        $("title").innerText = data.data.name;
        $("synopsis").innerText = data.data.synopsis;
        $("genres").innerHTML = getGenresText(data.data.genres);
        $("status").innerHTML = getStatus(data.data.status);
        $("likeIcon").innerText = getLike();
        fillChapter(data.data.chapters);
        fillComment(data.data.comment);
        if(data.status == 201){
            $("btn_edit").hidden = false;
            $("name_edit").value = data.data.name;
            $("synopsis_edit").innerText = data.data.synopsis;
            $("status_edit").value = getStatus(data.data.status); 
            //setGenresActive(data.data.genres);

        }
    } else if(data.status == 404){
        alert(data.message+" Error:"+data.status);
        location.href = "dashboard.html";
    }
});

function getLike(){
    if(!(localStorage.getItem("userInfo") === null || undefined)){
        if ($('likeBtn').className.includes("liked")) {
            return "favorite";
        } else {
            return "favorite_border";
        }    
    }else{
        return "";
    }
}

/*function setGenresActive(genres){ 
    console.log($("genres_edit").childNodes.length);
    for(var i=0;i<$("genres_edit").childNodes.length;i++){
        if($("genres_edit").childNodes[i].innerText!=null||undefined){
            genres.forEach(function(genre){
                if ($("genres_edit").childNodes[i].innerText == genre){
                    $("genres_edit").childNodes[i].classList.add("selected");
                    console.log($("genres_edit").childNodes[i].innerHTML);
                }
            });
        }
    }
}*/

function fillChapter(chapters){
    chapters.forEach(element => {
        if(element.tracker){
            $("list_chapter").innerHTML += '<li class="collection-item"><div>'+element.chapterName+'<a href="chapter.html?id='+element.chapterId+'&page=1" class="secondary-content"><i class="material-icons black-text">play_arrow</i></a><a href="#!" onclick="disTrack('+element.chapterId+')" class="secondary-content"><i id="icon-'+element.chapterId+'" class="material-icons black-text">visibility</i></a></div></li>';
        }else{
            $("list_chapter").innerHTML += '<li class="collection-item"><div>'+element.chapterName+'<a href="chapter.html?id='+element.chapterId+'&page=1" class="secondary-content"><i class="material-icons black-text">play_arrow</i></a><a href="#!" onclick="track('+element.chapterId+')" class="secondary-content"><i id="icon-'+element.chapterId+'" class="material-icons black-text">visibility_off</i></a></div></li>';
        }
    });
}

function disTrack(id){
    let x = location.search.split(/\=/)[1];
    let url = ".././tracker?mid=" + x +"&cid=" + id;
    fetch(url, { method: 'DELETE' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data);
            $("icon-"+id).innerText = "visibility_off";
            $("icon-"+id).onclick = track();
    });
}

function track(id){
    let x = location.search.split(/\=/)[1];
    let url = ".././tracker?id=" + id;
    fetch(url, { method: 'POST' })
        .then(resp => resp.json())
        .then(data => {
            console.log(data);
            $("icon-"+id).innerText = "visibility";
            $("icon-"+id).onclick = disTrack();
    });
}

function getStatus(status){
    if(status){
        return '<div class="chip grey darken-3 white-text">En emision</div>';
    }else{
        return '<div class="chip grey darken-3 white-text">Finalizado</div>';
    }
}

function getGenresText(genres){
    var genresText = "";
    for(var i = 0; i < genres.length; i++)
        genresText += '<div class="chip grey darken-3 white-text">'+genres[i]+'</div>';
    return genresText
}


function getIdManga(){
    var url_string = window.location.href;
    var url = new URL(url_string);
    var id = url.searchParams.get("id");
    if(id === null || undefined){
        location.href = "dashboard.html";
    }else{
        return id;
    }
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
                $('likeIcon').innerText = "favorite";
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
                let v = $('likeBtn').className.replace("liked","").trim(); //esto hay que cambiarlo, no se como hacer que se mantenga la clase sin el "liked"
                $('likeBtn').className = v;
                $('likeIcon').innerText = "favorite_border";
            }
        })
}

$('likeBtn').addEventListener('click', function() {like();});

function $(id){
    return document.getElementById(id);
}

function c(clase) {
    return document.getElementsByClassName(clase);
}

