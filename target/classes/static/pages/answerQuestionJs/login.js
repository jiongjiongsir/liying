var UserName = document.getElementById("UserName");
var Password = document.getElementById("Password");
var LoginButton = document.getElementById("LoginButton");
var UserNameText = '';

// isLoginFun();
//登录
// $(function () {
//     var da={
//         "grant_type":"client_credentials",
//         "client_id":"KGGscAYPRT2fLaLbSgYmOHMb",
//         "client_secret":"zPn3SGkGEpGMrffHCOfHsXiR6qk72Ltc"
//     };
//     // $.get("https://aip.baidubce.com/oauth/2.0/token",da,function (data) {
//     //     console.log(data)
//     // })
//     $.ajax({
//
//         "async": true,
//         "url": "https://aip.baidubce.com/oauth/2.0/token",
//         "type": "GET",
//         "data":da,
//         "dataType": 'jsonp',
//         "contentType": "application/x-www-form-urlencoded; charset=utf-8",
//         "cache": false,
//         "timeout": 5000,
//         // jsonp 字段含义为服务器通过什么字段获取回调函数的名称
//         "jsonp": 'callback',
//         // 声明本地回调函数的名称，jquery 默认随机生成一个函数名称
//         "jsonpCallback": 'jsonpCallback',
//         success:  function (data) {
//             console.log(data)
//         },
//         error:  function (jqXHR, textStatus, errorThrown) {
//             console.log(jqXHR);
//             // alert(jqXHR);
//             //  //console.log(jqXHR);
//         },
//     });
// })

/*
在用getUserMediaToPhoto之前要写两个回调函数，一个success 一个 error
格式：
 function success(stream){
 }
//失败回调函数
function error(error) {
}
*/
//成功回调函数
var video = document.getElementById("video");
var canvas = document.getElementById("canvas");
var context = canvas.getContext("2d");
var mediaStreamTrack=null;
function success(stream){
    //兼容webkit核心浏览器
    // var CompatibleURL = window.URL || window.webkitURL;
    //将视频流转化为video的源
    mediaStreamTrack=stream;
    try {
        // video.src = CompatibleURL.createObjectURL(stream);
        video.srcObject=stream;
    }catch (e) {
        console.log("访问用户媒体设备失败：",error.name,error.message);
    }

    video.play();//播放视频

    //将视频绘制到canvas上
}
//错误回调函数
function error(error) {
    console.log('访问用户媒体失败：',error.name,error.message);
}
function getUserMediaToPhoto(constraints,success,error) {
    if(navigator.mediaDevices.getUserMedia){
        //最新标准API
        navigator.mediaDevices.getUserMedia(constraints).then(success).catch(error);
    }else if (navigator.webkitGetUserMedia) {
        //webkit核心浏览器
        navigator.webkitGetUserMedia(constraints,success,error);
    }else if(navigator.mozGetUserMedia){
        //firefox浏览器
        navigator.mozGetUserMedia(constraints,success,error);
    }else if(navigator.getUserMedia){
        //旧版API
        navigator.getUserMedia(constraints,success,error);
    }
}

function getFace() {
    context.drawImage(video,0,0,300,150);
    this.img=canvas.toDataURL('image/jpg')
    //获取完整的base64编码
    this.img=img.split(',')[1];
    return this.img;
}
function openUserMedia() {
    if(navigator.mediaDevices.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia){
        getUserMediaToPhoto({video:{width:480,height:320,facingMode: "user"}},success,error);
    }else{
        alert('你的浏览器不支持访问用户媒体设备');
    }
    setTimeout(function () {
        img=getFace();
        da={
            "imgpath":img,
            "imgType":"BASE64"
        }
        $.ajax({
            type:"POST",
            url:"http://localhost:8085/admin/FaceLogin",
            contentType: "application/json",
            "dataType": "json",
            data:JSON.stringify(da),
            success:loginSuccess,
            error:function () {
                alert("连接服务器失败")
            },
            async:true
        })
    },3000)
}
function  offUserMedia() {
    if(mediaStreamTrack!=null)
        mediaStreamTrack.getTracks()[0].stop();
}



function login() {
    if (!UserName.value) {
        alert("请先输入用户名");
        UserName.focus();
        return;
    }
    if(!Password.value) {
        alert("请输入密码");
        Password.focus();
        return;
    }

    UserNameText = $("#UserName").val();
    var PasswordTest = $("#Password").val();

    var da = {
        "username":UserNameText,
        "password":PasswordTest
    };
    commonAjaxPost(true, "/admin/userLogin", da, loginSuccess)
}

//登录成功回调
function loginSuccess(result){
    console.log(result);
    if (result.code == '666') {
        layer.msg(result.message, {icon:1});
        setCookie('isLogin','1');
        setCookie('userId',result.data.id);
        setCookie('userName',result.data.username);
        setCookie('power',result.data.role);
        setCookie('modelId',result.data.modelId);
        window.location.href = "myQuestionnaires.html"
    }else{
        layer.msg("此用户不存在",{icon:2});
    }
}

//回车事件
$(document).keydown(function (event) {
    if (event.keyCode == 13) {
        login();
    }
});