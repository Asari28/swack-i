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

	boolean createRoom(String roomId, String roomName, String createdUserId,
			boolean directed, boolean privated) throws SwackException {
		//Userの中にUserIDも入っている状態で受け取る
		//ルームID,ルーム名、ルーム作成ユーザID、ダイレクトフラグ、フラグの順番
		//SQL insert into rooms (roomid,roomname,createduserid,directed,privated) values ('RTEST','テストルーム','test',true,true)
		String sql = "SELECT mailAddress FROM users WHERE mailAddress = ?";
		//userId,userName,mailAddress,passwordの順番でセットする
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			//SQL組み立て
			pStmt.setString(1, roomId);
			//SQL実行

			int result = pStmt.executeUpdate();
			//結果
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

}