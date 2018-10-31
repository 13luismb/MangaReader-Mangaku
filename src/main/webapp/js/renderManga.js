//Solicitud de los datos del manga
//Datos del manga, y si yo soy el poseedor.
//Datos recibidos: status solicitud,synopsis,genre,manga_status,creator
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
        $("title_manga").innerText = data.session.name;
        $("synopsis_manga").innerText = data.session.synopsis;
        $("genres_manga").innerText = data.session.genre;
        if(data.session.status){
            $("status_manga").innerText = "En emision";
        }else{
            $("status_manga").innerText = "Finalizado";
        }

        if(data.status == 201){
            $("btn_edit").hidden = false;
        }
    }
});

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

function $(id){
    return document.getElementById(id);
}