function $(id){
    return document.getElementById(id);
}

function login(){
    let body={
        username:$('uname').value,
        password:$('pword').value
    },
        params={
                method: "POST", 
                headers: new Headers({'Content-Type': 'application/json'}), 
                body:JSON.stringify(body) 
    };
    fetch(".././login", params)
    .then(resp => resp.json())
    .then(data => {

        console.log(data);
        
      if (data.status==200){
          let newDiv = document.createElement('div');
          let newCont = document.createTextNode("Haz iniciado satisfactoriamente sesion!");
          newDiv.appendChild(newCont);
          document.body.appendChild(newDiv);
      }else{
          let newDiv = document.createElement('div');
          let newCont = document.createTextNode("Usuario o contrase;a erronea");
          newDiv.appendChild(newCont);
          document.body.appendChild(newDiv);
      }
    });
}


$('logbtn').addEventListener('click',login);