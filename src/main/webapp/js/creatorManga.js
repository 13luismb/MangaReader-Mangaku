function $(id){
    return document.getElementById(id);
}

function createManga(){
    let body={
        name:$('name_manga').value,
        synopsis:$('synopsis_manga').value,
        genre:$('genres_manga').value
    },
        params={
                method: "POST", 
                headers: new Headers({'Content-Type': 'application/json'}), 
                body:JSON.stringify(body) 
    };
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

$('modalbtn').addEventListener('click',createManga);