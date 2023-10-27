package model;

import dao.RoomDAO;
import exception.SwackException;

public class RoomModel {

	public boolean createRoom(String roomId, String roomName, String createdUserId,
			boolean directed, boolean privated) throws SwackException {
		//				System.out.println("[getChatlogList] " + roomId);
		//		ArrayList<Room> list = new ArrayList<Room>();
		//		list.add(new Room("U0001", "ダミー太郎"));
		//		list.add(new Room("U0002", "ダミー花子"));
		//		list.add(new Room("U0003", "ダミー次郎"));
		//		list.add(new Room("U0004", "ダミー三郎"));
		//		list.add(new Room("U0005", "ダミー四郎"));

		RoomDAO roomDAO = new RoomDAO();
		boolean result = roomDAO.createRoom(roomId, roomName, createdUserId, directed, privated);

		return result;
	}

	public boolean joinUser(String roomId, String joinUserId) throws SwackException {
		RoomDAO roomDAO = new RoomDAO();
		boolean result = roomDAO.insertJoinRoom(roomId, joinUserId);
		if (result) {
			return true;
		} else {
			return false;
		}
	}
}
