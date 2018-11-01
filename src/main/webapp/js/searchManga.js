function $(id){
    return document.getElementById(id);
}

$("search").onkeyup('enter',function(){
    alert($("search").value);
});