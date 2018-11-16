        function $(id) {
            return document.getElementById(id);
        }

        let x = location.search.split(/\=/),
            y = x[1].split(/\&/),
            chapter = y[0],
            page = x[2],
            URL = ".././chapter?id=" + chapter + "&page=" + page;
        if (x[2] == 1) {
            $('handler').removeChild($('previous'));
        }

        function fill(data) {
            if (x[2] == data.chapterPages) {
                $('handler').removeChild($('next'));
            }
            $('image').src = ".././chapter?id=" + chapter + "&page=" + page;
        }

        function load() {
            imageLoad();
            likeLoad();
        }


        function imageLoad(){
            fetch(URL, {
                    method: 'GET'
                })
                .then(response => response.blob())
                .then(data => fill(data));
        }

        function likeLoad(){
            let x = location.search.split(/\=/)[1];
            let url = ".././likes?cid=" + x;
            fetch(url, { method: 'GET' })
                .then(resp => resp.json())
                .then(data => {
                    if (data.data.isLiked) {
                        $('likeBtn').className += " liked";
                    }
                });
        }

        function commentLoad(){

        }

        function previous() {
            prepage = parseInt(page) - 1;
            location.href = 'chapter.html?id=' + y[0] + '&page=' + prepage;
        }

        function next() {
            nextpage = parseInt(page) + 1;
            location.href = 'chapter.html?id=' + y[0] + '&page=' + nextpage;
        }

        function dashboard() {
            location.href = 'dashboard.html';
        }


        function like() {
            if ($('likeBtn').className.includes("liked")) {
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
                        $('likeBtn').className += " liked";
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
                        let v = $('likeBtn').className.replace("liked","").trim();
                        $('likeBtn').className = v;
                    }
                })
        }

       // $('likeBtn').addEventListener('click', function() {like();});
        window.addEventListener('load', load, true);
        $('next').addEventListener('click', next);
        $('dashboard').addEventListener('click', dashboard);
        $('previous').addEventListener('click', previous);