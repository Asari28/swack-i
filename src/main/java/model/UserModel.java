package model;

import java.util.ArrayList;

import bean.User;
import dao.UsersDAO;
import exception.SwackException;

public class UserModel {
	/**
	 *　自分以外のユーザ一覧を取得するメソッド
	 * CreateRoomServletで使用する
	 * @return ArrayList<User> userList 自分以外のユーザ一覧
	 * @throws SwackException
	 */
	public ArrayList<User> getUsers(String userId) throws SwackException {
		// UserDAOからユーザ一覧を取得
		UsersDAO usersDAO = new UsersDAO();
		ArrayList<User> userList = usersDAO.selectAllUser(userId);
		return userList;

	}

	/**
	 * ルームのIDからそのルームに参加していないユーザ一覧を取得するメソッド
	 * JoinMemberServletで使用する
	 * @return ArrayList<User> userList ルームIDのルームに参加していないユーザ一覧
	 * @throws SwackException
	 */
	public ArrayList<User> getJoinUsers(String roomId) throws SwackException {
		// UserDAOからルームIDのルームに参加していないユーザ一覧を取得
		UsersDAO usersDAO = new UsersDAO();
		ArrayList<User> userList = usersDAO.selectNotJoinUser(roomId);
		return userList;

	}

	/**
	 * ユーザのIDからそのユーザとダイレクトチャットをしていないユーザ一覧を取得するメソッド
	 * CreateDeirectServletで使用する
	 * @return ArrayList<User> userList ユーザとダイレクトチャットをしていないユーザ一覧
	 * @throws SwackException
	 */
	public ArrayList<User> getUnDirectedUsers(String userId) throws SwackException {
		// UserDAOからルームIDのルームに参加していないユーザ一覧を取得
		UsersDAO usersDAO = new UsersDAO();
		ArrayList<User> userList = usersDAO.selectUnDirectedUser(userId);
		return userList;

	}

	/**
	 * ダイレクトチャットのルーム名を渡すメソっド
	 * CreateDeirectServletで使用する
	 * @return String roomName ダイレクトチャットのルーム名
	 * @throws SwackException
	 */
	public String getUnDirectRoomName(String target, String userId) throws SwackException {
		// UserDAOからルームIDのルームに参加していないユーザ一覧を取得
		int iUserId = Integer.parseInt(userId.substring(1));
		int iTarget = Integer.parseInt(target.substring(1));

		if (iTarget > iUserId) {
			String work = target;
			target = userId;
			userId = work;
		}
		String roomName = "P" + target + "," + userId;
		return roomName;

	}

}
