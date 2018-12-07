function $(id) {
    return document.getElementById(id);
}

window.onload = function(){
    searchUser('');
    $('send').addEventListener('click',searchUser);
}

var list = [];

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
            list = [];
            let i = 0;
            data.data.forEach(element => {
                list.push(element.username);
                $("listUser").innerHTML += 
                '<li class="collection-item">'+
                    '<div>'+element.username+
                        '<a href="#!" onclick="blockedUpdate('+i+')" class="secondary-content">'+
                            '<i id="'+element.username+'-block" class="material-icons black-text">cancel'+
                            '</i>'+
                        '</a>'+
                        '<a href="#!" onclick="setTypeUser('+i+')" class="secondary-content">'+
                            '<i id="'+element.username+'-admin" class="material-icons black-text">assignment_ind'+
                            '</i>'+
                        '</a>'+
                    '</div>'+
                '</li>';
                i++;
            });
        }else if(data.status == 403){
            alert("NO ADMIN NO PARTY!")
            location.href = "dashboard.html";
        }
    });
}

function blockedUpdate(position){
    console.log(list[position]);
    params={
        method: "DELETE", 
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    };
    
    fetch(".././admins?username="+list[position], params)
    .then(resp => resp.json())
    .then(data => {
        console.log(data);
      if (data.status==200){
      }else{
          alert("Error al crear el manga, status:"+data.status);
      }
    });
}



function setTypeUser(position){
    console.log(list[position]);
    params={
        method: "PUT", 
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    };
    fetch(".././admins?username="+list[position], params)
    .then(resp => resp.json())
    .then(data => {
        console.log(data);
      if (data.status==200){
      }else{
          alert("Error al crear el manga, status:"+data.status);
      }
    });
}

function getUsername(){
    var url_string = window.location.href;
    var url = new URL(url_string);
    var id = url.searchParams.get("username");
    return id;
}