        function $(id) {
            return document.getElementById(id);
        }

        let x = location.search.split(/\=/),
            y = x[1].split(/\&/),
            chapter = y[0],
            page = x[2],
            URL = ".././chapter?id=" + chapter + "&page=" + page;

        function fill(data) {
            $('image').src = ".././chapter?id=" + chapter + "&page=" + page;
        }

        function load() {
            imageLoad();
            likeLoad();
            commentLoad();
            dataLoad();
        }


        function imageLoad(){
            fetch(URL, {
                    method: 'GET'
                })
                .then(response => response.blob())
                .then(data => fill(data));
        }
        
        function dataLoad(){
            fetch(URL, {
                    method: 'OPTIONS'
                })
                .then(response => response.json())
                .then(data => doInfoMatch(data));
        }

        function likeLoad(){
            let x = location.search.split(/\=/)[1];
            let url = ".././likes?cid=" + x;
            fetch(url, { method: 'GET' })
                .then(resp => resp.json())
                .then(data => {
                    if (data.data.isLiked) {
                        $('likeBtn2').className += " liked";
                    }
                });
        }

        function commentLoad(){
            let params = location.href.split('?')[1];
            console.log(params);            
            let config = {
                method: "GET",
                withCredentials: true,
                credentials: 'same-origin',
                headers: {
                    "Content-type": "application/x-www-form-urlencoded"
                }
            };
            
            fetch('.././comment?'+params, config)
            .then(res => res.json())
            .then(data => {
                if(data.status == 200){
                    console.log(data);
                    fillComment(data.data);
                }else{
                    console.log(data);
                }
            });
        }

        function completeChapter(){
            let params = location.href.split('?')[1];
            let config = {
                method: "POST",
                withCredentials: true,
                credentials: 'same-origin',
                headers: {
                    "Content-type": "application/x-www-form-urlencoded"
                }
            };
            
            fetch('.././tracker?'+params, config)
            .then(res => res.json())
            .then(data => {
                if(data.status == 200){
                    console.log(data);
                }
            });
        }
        
        function doInfoMatch(data){
            console.log(data);
            if(data.status==200){
                if (x[2] == data.data.chapterPages) {
                    completeChapter();
                    $('next').addEventListener('click', nextChapter);
                }
            }else if(data.status == 403){
                alert(data.message+" Error:"+data.status);
                location.href = "dashboard.html";
            }
                
        }

        function previous() {
            prepage = parseInt(page) - 1;
            location.href = 'chapter.html?id=' + y[0] + '&page=' + prepage;
        }

        function next() {
            nextpage = parseInt(page) + 1;
            location.href = 'chapter.html?id=' + y[0] + '&page=' + nextpage;
        }
        
        function nextChapter(){
            x = parseInt(y[0]);
            chapter = x+1;
            location.href = 'chapter.html?id=' + chapter + '&page=1';
        }
        
        function previousChapter(){
            x = parseInt(y[0]);
            chapter = x-1;
            location.href = 'chapter.html?id=' + chapter + '&page=1';
        }

        function dashboard() {
            location.href = 'dashboard.html';
        }


        function like() {
            if ($('likeBtn2').className.includes("liked")) {
                doDislike();
            } else {
                doLike();
            }
        }


        function doLike(data) {
            let x = location.search.split(/\=/)[1];
            let url = ".././likes?cid=" + x;
            fetch(url, { method: 'POST' })
                .then(resp => resp.json())
                .then(data => {
                    if (data.data.isLiked) {
                        $('likeBtn2').className += " liked";
                    }
                })
        }

        function doDislike(data) {
            let x = location.search.split(/\=/)[1];
            let url = ".././likes?cid=" + x;
            fetch(url, { method: 'DELETE' })
                .then(resp => resp.json())
                .then(data => {
                    if (!data.data.isLiked) {
                        let v = $('likeBtn2').className.replace("liked","").trim();
                        $('likeBtn2').className = v;
                    }
                })
        }

        $('likeBtn2').addEventListener('click', function() {like();});
        window.addEventListener('load', load, true);
        $('next').addEventListener('click', next);
        $('dashboard').addEventListener('click', dashboard);
        
        if (x[2] == 1) {
            $('previous').addEventListener('click', previousChapter);
        }else{
            $('previous').addEventListener('click', previous);
        }