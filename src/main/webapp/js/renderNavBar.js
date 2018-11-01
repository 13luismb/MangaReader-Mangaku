const navLogin = document.getElementById('login-register');
const navName = document.getElementById('name-logout');
var dataUser = localStorage.getItem("userInfo");
console.log(JSON.parse(dataUser));
if (localStorage.getItem("userInfo") === null || undefined) {
    navLogin.hidden = false;
    navName.hidden = !navLogin.hidden;
} else {
    navName.hidden = false;
    navLogin.hidden = !navName.hidden;
    document.getElementById("user-name").innerHTML = JSON.parse(dataUser).username;
}

readGenres();

function logout() {
    let params = {
        method: "GET",
        withCredentials: true,
        credentials: 'same-origin',
        headers: {
            "Content-type": "application/x-www-form-urlencoded"
        }
    }
    fetch('.././session', params)
    .then(res => res.json())
    .then(data => {
        localStorage.clear();
        location.href = "dashboard.html";
    })
}

function readGenres(){
    $("genres_manga").innerHTML += '<option value="JABUENO!">JABUENO!</option>';
}