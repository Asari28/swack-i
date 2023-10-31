package model;

import bean.Room;
import dao.RoomDAO;
import exception.SwackException;

public class RoomModel {

	/**
	 * 新しくルームを作成する
	 * @param roomName ルーム名
	 * @param createdUserId ルーム作成者のユーザID
	 * @param directed ダイレクトフラグ
	 * @param privated プライベートフラグ
	 * @return true=成功 false=失敗
	 * @throws SwackException
	 */
	public boolean createRoom(String roomName, String createdUserId,
			boolean directed, boolean privated) throws SwackException {
		//新しいユーザIDの取得
		GetNewIdModel getNewIdModel = new GetNewIdModel();
		String roomId = getNewIdModel.RoomId();
		//INSERT
		RoomDAO roomDAO = new RoomDAO();
		boolean result = roomDAO.createRoom(roomId, roomName, createdUserId, directed, privated);
		if (result) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 新しく作ったルームのIDを取得するメソッド
	 * CreateRoomServletで使用する
	 * @return Striang roomId 新しく作ったルームのID 
	 * @throws SwackException
	 */
	public String getMaxRoomId() throws SwackException {
		// 登録済ルームIDの最大値を取得
		RoomDAO roomDAO = new RoomDAO();
		String maxRoomId = roomDAO.getMaxRoomId();
		return maxRoomId;
	}

	/**
	 * ルームに人を追加する
	 * @param roomId ルームID
	 * @param joinUserId 参加するユーザのユーザID
	 * @return true = 成功 false = 失敗
	 * @throws SwackException
	 */
	public boolean joinUser(String roomId, String joinUserId) throws SwackException {
		RoomDAO roomDAO = new RoomDAO();
		boolean result = roomDAO.insertJoinRoom(roomId, joinUserId);
		if (result) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ルームのIDからルーム情報を取得するメソッド
	 * JoinMemberServletで使用する
	 * @return Room room ルームIDのルーム情報 
	 * @throws SwackException
	 */
	public String getRoomId(String roomId) throws SwackException {
		// ルームIDをもとにルーム情報を取得
		RoomDAO roomDAO = new RoomDAO();
		Room room = roomDAO.getRoom(roomId);
		return room;
	}
}
