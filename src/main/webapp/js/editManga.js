function $(id){
    return document.getElementById(id);
}

function editManga(){
    let body={
        name:$('name_edit').value,
        synopsis:$('synopsis_edit').value,
        //genre:$('genres_edit').value,
        status:getStatusText($('status_edit').value),
        id:getIdManga()
    },
        params={
                method: "PUT", 
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

function getStatusText(status){
    if(status == "En emision"){
        return true;
    }else{
        return false;
    }
}

function deleteManga(){
    
    let params = location.href.split('?')[1];

    let config = {
        method: "DELETE",
        withCredentials: true,
        credentials: 'same-origin',
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    };
    
    fetch('.././manga?'+params, config)
    .then(res => res.json())
    .then(data => {
        if(data.status == 200){
            alert("Manga deleteado gg");
            location.href = "dashboard.html"; 
        }else{
            alert(data.message+" Error:"+data.status);
        }
    });
}
$('editbtn').addEventListener('click',editManga);
$('deletebtn').addEventListener('click',deleteManga);