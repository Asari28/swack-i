package model;

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
}
