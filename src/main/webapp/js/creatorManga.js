function $(id){
    return document.getElementById(id);
}

function createManga(){
    let body={
        name:$('name_manga').value,
        synopsis:$('synopsis_manga').value,
        genres:getGenresList()
    },
        params={
                method: "POST", 
                headers: new Headers({'Content-Type': 'application/json'}), 
                body:JSON.stringify(body) 
    };
    alert(JSON.stringify(body));
    fetch(".././manga", params)
    .then(resp => resp.json())
    .then(data => {
        console.log(data);
      if (data.status==200){
          location.href = "manga.html?id="+data.data.id;
      }else{
          alert("Error al crear el manga, status:"+data.status);
      }
    });
}

function getGenresList(){
    var elems = document.getElementsByClassName("selected");
    var genres = [];
    for(var i = 0; i < elems.length; i++)
        genres.push(elems[i].innerText);
    return genres
}

$('modalbtn').addEventListener('click',createManga);