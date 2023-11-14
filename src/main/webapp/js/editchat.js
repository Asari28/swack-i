"use strict";

function editChat(chatId, target, userId) {
	if (target != userId) {
		alert("あなたのチャットではありません");
		return;
	}
	console.log(String(chatId));
	const targets = document.getElementsByClassName(String(chatId));
	targets[1].value = targets[0].textContent;
	for (const target of targets) {
		target.classList.toggle('active');
	}
}

