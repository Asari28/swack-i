"use strict";

function editChat(chatId, target, userId) {
//	編集ボタンを押下したユーザとチャットしたユーザが
	if (target != userId) {
//		一致していなかったらエラーのダイアログを出す
		alert("あなたのチャットではありません");
		return;
	}
//	一致していたら
	console.log(String(chatId));
	const targets = document.getElementsByClassName(String(chatId));
	// チャットの表示内容を編集フォームに設定
	targets[1].value = targets[0].textContent;
	for (const target of targets) {
		target.classList.toggle('active');
	}
}

