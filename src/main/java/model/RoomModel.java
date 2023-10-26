package model;

import bean.User;
import dao.RoomDAO;
import exception.SwackException;

public class RoomModel {

	public boolean createRoom(User user) throws SwackException {
		//				System.out.println("[getChatlogList] " + roomId);
		//		ArrayList<Room> list = new ArrayList<Room>();
		//		list.add(new Room("U0001", "ダミー太郎"));
		//		list.add(new Room("U0002", "ダミー花子"));
		//		list.add(new Room("U0003", "ダミー次郎"));
		//		list.add(new Room("U0004", "ダミー三郎"));
		//		list.add(new Room("U0005", "ダミー四郎"));

		RoomDAO roomDAO = new RoomDAO();
		boolean result = roomDAO.insert(user);

		return result;
	}
}
