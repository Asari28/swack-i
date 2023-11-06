"use strict";

document.addEventListener("DOMContentLoaded", () => {
  // elements
  const elSendButton = document.getElementById("send");
  const elRooms = document.getElementById("rooms");

  if (countSelectelRooms(elRooms) == 0) {
    elSendButton.style.color = "rgba(44, 45, 48, 0.75)";
    elSendButton.style.backgroundColor = "#e8e8e8";
    elSendButton.disabled = true;
  }
  elRooms.addEventListener("change", () => {
    if (countSelectelRooms(elRooms) < 1) {
      elSendButton.style.color = "rgba(44, 45, 48, 0.75)";
      elSendButton.style.backgroundColor = "#e8e8e8";
      elSendButton.disabled = true;
    } else {
      elSendButton.style.color = "#ffffff";
      elSendButton.style.backgroundColor = "#008952";
      elSendButton.disabled = false;
    }
  });
});
