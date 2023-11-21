"use strict";

const form = document.getElementById("form");
const msg = document.getElementById("msg");
const msgList = ["Loading now...","ローディング中...","こんにちは！","ようこそ！"]

document.addEventListener('DOMContentLoaded',() => {

});
//ログイン成功時２秒後にメイン画面へ
setTimeout(function(){
	form.submit();
}, 2000);