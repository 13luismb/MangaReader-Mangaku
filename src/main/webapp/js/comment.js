function fillComment(comments){
    comments.forEach(element => {
        if(!element.delete){
            if(element.isYour){
                $("list_comment").innerHTML += '<li class="collection-item"><div>'+'<b>'+element.nameCreator+'</b>'+':'+element.content+'<a href="#!" class="secondary-content comment"><i class="material-icons black-text '+element.id+'">cancel</i></a></div></li>';
            }else{
                $("list_comment").innerHTML += '<li class="collection-item"><div>'+element.nameCreator+':'+element.content+'</div></li>';
            }
        }else{
            $("list_comment").innerHTML += '<li class="collection-item"><div><b>El comentario de '+element.nameCreator+'fue eliminado</b></div></li>';
        }
        
    });
    if (localStorage.getItem("userInfo") === null || undefined){
        //
    }else{
        $("list_comment").innerHTML += '<li class="collection-item"><div class="input-field"><input id="comment" type="text" class="validate"><label for="Comment">Comment</label><a href="#!" class="secondary-content" id="send_comment"><i class="material-icons black-text">play_arrow</i></a></div></li>';
        $('send_comment').addEventListener('click',sendComment,false);
    }
    
    let button = c('comment');
    for (var i = 0; i < button.length; i++) {
        button[i].addEventListener('click', deleteComment, false);
    }
    
}

function sendComment(e){
    
    let params = location.href.split('?')[1];
    let json = {
        content:$('comment').value
    };
    console.log($('comment').value);
    let config = {
        method: "POST",
        withCredentials: true,
        credentials: 'same-origin',
        headers: new Headers({'Content-Type': 'application/json'}), 
        body:JSON.stringify(json) 
    };

    fetch('.././comment?'+params, config)
    .then(res => res.json())
    .then(data => {
        location.reload(true);
    });
}

function getParams(sParametroNombre) {
    var sPaginaURL = window.location.search.substring(1);
     var sURLVariables = sPaginaURL.split('&');
      for (var i = 0; i < sURLVariables.length; i++) {
        var sParametro = sURLVariables[i].split('=');
        if (sParametro[0] == sParametroNombre) {
          return sParametro[1];
        }
      }
     return null;
}

function deleteComment(e){
    let id = e.target.className.split(' ')[2];
    
    if(getParams("page")!=null){
        isChapter = "isChapter";    
    }else{
        isChapter = "noIsChapter";
    }
    //console.log(page);
    let config = {
        method: "DELETE",
        withCredentials: true,
        credentials: 'same-origin',
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    };
    fetch('.././comment?id='+id+'&isChapter='+isChapter, config)
    .then(res => res.json())
    .then(data => {
        console.log(data);
        location.reload(true);
    });
}

function $(id){
    return document.getElementById(id);
}

function c(clase) {
    return document.getElementsByClassName(clase);
}