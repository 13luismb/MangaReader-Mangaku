function $(id){
    return document.getElementById(id);
}

function register(){
    let body={
        name:$('name').value,
        email:$('email').value,
        username:$('uname').value,
        password:$('pword').value
    },
        params={
                method: "POST", 
                headers: new Headers({'Content-Type': 'application/json'}), 
                body:JSON.stringify(body) 
    };
    fetch(".././register", params)
    .then(resp => resp.json())
    .then(data => {
        console.log(data);
      if (data.status==200){
          let newDiv = document.createElement('div');
          let newCont = document.createTextNode("Te has registrado satisfactoriamente!");
          newDiv.appendChild(newCont);
          document.body.appendChild(newDiv);
          location.href = "login.html";
      }else{
          let newDiv = document.createElement('div');
          let newCont = document.createTextNode("El usuario que has tratado de registrar, o el correo que utilizaste ya existe.");
          newDiv.appendChild(newCont);
          document.body.appendChild(newDiv);
      }
    });
}

$('regbtn').addEventListener('click',register);