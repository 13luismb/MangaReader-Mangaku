function $(id) {
    return document.getElementById(id);
}

window.onload = function(){
    searchUser('');
    $('send').addEventListener('click',searchUser);
}

function searchUser(){
    username = $('username').value;

    let params = {
        method: "GET",
        withCredentials: true,
        credentials: 'same-origin',
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    }
    fetch('.././admins?username='+username, params)
    .then(res => res.json())
    .then(data => {
        console.log(data);
        if(data.status == 200){
            $("listUser").innerHTML = "";
            data.data.forEach(element => {
                $("listUser").innerHTML += 
                '<li class="collection-item">'+
                    '<div >'+element.username+
                        '<a id="'+element.username+'" href="#!" class="secondary-content">'+
                            '<i class="material-icons black-text">cancel'+
                            '</i>'+
                        '</a>'+
                        '<a class="secondary-content">'+
                            '<i class="material-icons black-text">assignment_ind'+
                            '</i>'+
                        '</a>'+
                    '</div>'+
                '</li>'

                $(element.username).addEventListener('click', blockedUpdate);
            });
            
        }else if(data.status == 403){
            alert("NO ADMIN NO PARTY!")
            location.href = "dashboard.html";
        }
    });
}

function blockedUpdate(e){
    console.log(e);

    params={
        method: "PUT", 
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    };
    
    fetch(".././admin?username="+username, params)
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