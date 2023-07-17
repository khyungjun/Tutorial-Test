var main = {
    init : function () {
        var _this = this;
        
        $('#btn-save').on('click', function () {
            _this.save();
        });
        
        $('#btn-update').on('click', function () { // btn-update란 id를 가진 HTML 엘리먼트에 click 이벤트가 발생할 때 update function을 실행하도록 이벤트를 등록한다.
            _this.update();
        });
        
        $('#btn-delete').on('click', function () {
            _this.delete();
        });
    },
    save : function () {
        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert('글이 등록되었습니다.');
            window.location.href = '/'; // 글 등록이 성공하면 메인페이지(/)로 이동한다.
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function () { // 신규로 추가될 update function
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();
        
        $.ajax({
            type: 'PUT', // 여러 HTTP Method 중 PUT 메소드를 선택한다. PostsApiController에 있는 API에서 이미 @PutMapping으로 선언했기 떄문에 PUT을 사용해야 한다. 참고로 이는 REST 규약에 맞게 설정된 것이다. REST에서 CRUD는 다음과 같이 HTTP Method에 매핑된다. 생성(Create)-POST, 읽기(Read)-GET, 수정(Update)-PUT, 삭제(Delete)-DELETE
            url: '/api/v1/posts/'+id, // 어느 게시글을 수정할지 URL Path로 구분하기 위해 Path에 id를 추가한다.
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert('글이 수정되었습니다.');
            window.location.href = '/'; // 글 등록이 성공하면 메인페이지(/)로 이동한다.
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var id = $('#id').val();
        
        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (){
            alert('글이 삭제되었습니다.');
            window.location.href = '/'; // 글 등록이 성공하면 메인페이지(/)로 이동한다.
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();