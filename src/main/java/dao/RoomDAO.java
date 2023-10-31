package dao;

import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Room;
import exception.SwackException;

/**
 * RoomsテーブルとJoinRoomテーブルを操作するDAO
 * @author s20223054
 *
 */

public class RoomDAO extends BaseDAO {
	public RoomDAO() throws SwackException {
		super();
	}

	/**
	 * 現時点でのルームIDの最大値を取得
	 * ルーム作成と同時にINSERTするために使用している
	 * @return String maxRoomId
	 * @throws SwackException
	 */
	public String getMaxRoomId() throws SwackException {
		String sql = "SELECT MAX(ROOMID) AS roomId FROM ROOMS";
		String roomId = null;
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);

			ResultSet rs = pStmt.executeQuery();
			rs.next();
			roomId = rs.getString("roomId");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
		return roomId;
	}

	/**
	 * roomテーブルに新規作成したルームをINSERTする
	 * @param roomId ルームID(モデルで作成)
	 * @param roomName ルーム名
	 * @param createdUserId ルーム作成者ユーザID
	 * @param directed ダイレクトフラグ
	 * @param privated 公開フラグ
	 * @return boolean 成功(true)失敗(false)
	 * @throws SwackException
	 */
	public boolean createRoom(String roomId, String roomName, String createdUserId,
			boolean directed, boolean privated) throws SwackException {
		//Userの中にUserIDも入っている状態で受け取る
		//ルームID,ルーム名、ルーム作成ユーザID、ダイレクトフラグ、フラグの順番
		String sql = "INSERT INTO rooms (roomid,roomname,createduserid,directed,privated) VALUES (?,?,?,?,?)";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			//SQL組み立て
			//roomId,roomName,createdUserId,directed,privatedの順番でセットする
			pStmt.setString(1, roomId);
			pStmt.setString(2, roomName);
			pStmt.setString(3, createdUserId);
			pStmt.setBoolean(4, directed);
			pStmt.setBoolean(5, privated);

			//SQL実行
			int result = pStmt.executeUpdate();

			//結果
			if (result != 1) {
				return false;//失敗
			}
			return true;//成功

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * joinroomテーブルに新たに参加するユーザをINSERTする
	 * @param roomId ルームID
	 * @param userId ユーザID
	 * @return boolean 成功(true)失敗(false)
	 * @throws SwackException
	 */

	public boolean insertJoinRoom(String roomId, String userId) throws SwackException {
		//SQL
		String sql = "INSERT INTO joinroom (roomid,userid) VALUES (?,?)";
		try (Connection conn = dataSource.getConnection()) {

			PreparedStatement pStmt = conn.prepareStatement(sql);

			//SQL組み立て
			pStmt.setString(1, roomId);
			pStmt.setString(2, userId);

			//SQL実行
			int result = pStmt.executeUpdate();

			//結果
			if (result != 1) {
				return false;
			}
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	public Room getRoom(String roomId) throws SwackException {
		//SQL
		String sql = "SELECT roomid,roomname,createduserid,directed,privated FROM rooms WHERE roomid=?";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			//SQL組み立て
			pStmt.setString(1, roomId);

			//SQL実行
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			String roomid = rs.getString("roomId");
			String roomname = rs.getString("roomname");
			Room room = new Room(roomid, roomname);

			return room;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

}