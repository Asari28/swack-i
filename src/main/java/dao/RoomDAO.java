package dao;

import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exception.SwackException;

/**
 * RoomsテーブルとJoinRoomテーブルを操作するDAO
 * @author s20223054 西村
 *
 */

public class RoomDAO extends BaseDAO {
	public RoomDAO() throws SwackException {
		super();
	}

	public boolean createRoom(String roomId, String roomName, String createdUserId,
			boolean directed, boolean privated) throws SwackException {
		//Userの中にUserIDも入っている状態で受け取る
		//ルームID,ルーム名、ルーム作成ユーザID、ダイレクトフラグ、フラグの順番
		String sql = "INSERT INTO rooms (roomid,roomname,createduserid,directed,privated) VALUES (?,?,?,?,?)";
		//userId,userName,mailAddress,passwordの順番でセットする
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			//SQL組み立て
			pStmt.setString(1, roomId);
			pStmt.setString(2, roomName);
			pStmt.setString(3, createdUserId);
			pStmt.setBoolean(4, directed);
			pStmt.setBoolean(5, privated);
			//SQL実行
			int result = pStmt.executeUpdate();

			if (result != 1) {
				return false;
			}
			//結果
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

}