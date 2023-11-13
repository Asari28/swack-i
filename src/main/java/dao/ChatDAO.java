package dao;

import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bean.ChatLog;
import bean.Room;
import exception.SwackException;

/**
 * チャット機能に関するDBアクセスを行う.
 */
public class ChatDAO extends BaseDAO {
	public ChatDAO() throws SwackException {
		super();
	}

	/**
	 * 指定したルームのチャットログを取得する
	 * @param roomId ログが欲しいルームのルームID
	 * @return List<ChatLog> チャットログリスト
	 * チャットログID、ユーザID、ユーザ名、メッセージ内容、時間
	 * @throws SwackException
	 */
	public List<ChatLog> getChatlogList(String roomId) throws SwackException {
		String sql = "SELECT CHATLOGID, U.USERID AS USERID, U.USERNAME AS USERNAME, MESSAGE, CREATED_AT "
				+ "FROM CHATLOG C JOIN USERS U ON C.USERID = U.USERID WHERE ROOMID = ? " + "ORDER BY CREATED_AT ASC";

		List<ChatLog> chatLogList = new ArrayList<ChatLog>();
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, roomId);

			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int chatLogId = rs.getInt("CHATLOGID");
				String userId = rs.getString("USERID");
				String userName = rs.getString("USERNAME");
				String message = rs.getString("MESSAGE");
				Timestamp createdAt = rs.getTimestamp("CREATED_AT");

				ChatLog chatLog = new ChatLog(chatLogId, roomId, userId, userName, message, createdAt);
				chatLogList.add(chatLog);
			}
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
		return chatLogList;
	}

	/**
	 * ルーム情報を取得する？
	 * @param roomId ほしいルームのID
	 * @param userId 取得しようとしているユーザのID
	 * @return Room ルーム情報
	 * 複数人公開ルームの場合：ルーム名、人数
	 * ダイレクトルームの場合：ルーム名、人数(２で固定)
	 * @throws SwackException
	 */
	public Room getRoom(String roomId, String userId) throws SwackException {
		String sqlGetRoom = "SELECT R.ROOMID, R.ROOMNAME, COUNT(*) AS MEMBER_COUNT, R.DIRECTED"
				+ " FROM ROOMS R JOIN JOINROOM J ON R.ROOMID = J.ROOMID" + " WHERE R.ROOMID = ?"
				+ " GROUP BY R.ROOMID, R.ROOMNAME, R.DIRECTED";
		String sqlGetDirectRoom = "SELECT U.USERNAME AS ROOMNAME FROM JOINROOM R"
				+ " JOIN USERS U ON R.USERID = U.USERID" + " WHERE R.USERID <> ? AND ROOMID = ?";

		boolean directed = false;
		String roomName = "";
		int memberCount = 0;

		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sqlGetRoom);
			pStmt.setString(1, roomId);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				directed = rs.getBoolean("DIRECTED");
				roomName = rs.getString("ROOMNAME");
				memberCount = rs.getInt("MEMBER_COUNT");
			}

			// for Direct
			if (directed) {
				PreparedStatement pStmt2 = conn.prepareStatement(sqlGetDirectRoom);
				pStmt2.setString(1, userId);
				pStmt2.setString(2, roomId);

				ResultSet rs2 = pStmt2.executeQuery();
				if (rs2.next()) {
					roomName = rs2.getString("ROOMNAME");
					memberCount = 2;
				}
			}
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
		Room room = new Room(roomId, roomName, memberCount, directed);
		return room;
	}

	/**
	 * ルームリストを取得する
	 * @param userId 取得しようとしているユーザのID
	 * @return ArrayList<Room> 参加しているルームのリスト
	 * ルームIDとルーム名が入っている
	 * @throws SwackException
	 */
	public ArrayList<Room> getRoomList(String userId) throws SwackException {
		String sql = "SELECT R.ROOMID, R.ROOMNAME FROM JOINROOM J JOIN ROOMS R ON J.ROOMID = R.ROOMID "
				+ "WHERE J.USERID = ? AND R.DIRECTED = FALSE ORDER BY R.ROOMNAME ASC";

		ArrayList<Room> roomlist = new ArrayList<Room>();

		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userId);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String roomId = rs.getString("ROOMID");
				String roomName = rs.getString("ROOMNAME");
				roomlist.add(new Room(roomId, roomName));
			}

		} catch (Exception e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}

		return roomlist;

	}

	/**
	 * ダイレクトチャットの一覧を取得する？
	 * @param userId ユーザID
	 * @return ArrayList<Room> ダイレクトチャットリスト。
	 * ログ取得のためのルームID、左メニューに表示するためのルーム名(ユーザ名)が入っている
	 * @throws SwackException
	 */
	public ArrayList<Room> getDirectList(String userId) throws SwackException {
		String sql = "SELECT R.ROOMID, U.USERNAME AS ROOMNAME FROM JOINROOM R " + "JOIN USERS U ON R.USERID = U.USERID "
				+ "WHERE R.USERID <> ? AND ROOMID IN "
				+ "(SELECT R.ROOMID FROM JOINROOM J JOIN ROOMS R ON J.ROOMID = R.ROOMID "
				+ "WHERE J.USERID = ? AND R.DIRECTED = TRUE) " + "ORDER BY R.USERID";

		ArrayList<Room> roomlist = new ArrayList<Room>();

		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userId);
			pst.setString(2, userId);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String roomId = rs.getString("ROOMID");
				String roomName = rs.getString("ROOMNAME");
				roomlist.add(new Room(roomId, roomName));
			}

		} catch (Exception e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}

		return roomlist;

	}

	/**
	 * チャットログを保存(INSERT)する
	 * @param roomId ルームID
	 * @param userId ユーザID
	 * @param message メッセージ内容
	 * @return voidのためなし
	 * @throws SwackException
	 */
	public void saveChatlog(String roomId, String userId, String message) throws SwackException {
		String sql = "INSERT INTO CHATLOG (CHATLOGID, ROOMID, USERID, MESSAGE, CREATED_AT)"
				+ " VALUES (nextval('CHATLOGID_SEQ'), ?, ?, ?, CURRENT_TIMESTAMP)";

		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, roomId);
			pStmt.setString(2, userId);
			pStmt.setString(3, message);

			pStmt.executeUpdate();
		} catch (SQLException e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * chatLogIdのチャットログを削除する
	 * @param chatLogId チャットログのID
	 * @return boolean 成功(true)失敗(false)
	 * @throws SwackException
	 */
	public boolean deleteChatlog(int chatLogId) throws SwackException {
		String sql = "DELETE FROM CHATLOG WHERE CHATLOGID = ?";

		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, chatLogId);

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
	 * chatLogIdのチャットをしたユーザのIDを取得
	 * @param chatLogId チャットログのID
	 * @return String userId ユーザのID
	 * @throws SwackException
	 */
	public String getChatlogUserId(int chatLogId) throws SwackException {
		String sql = "SELECT USERID FROM CHATLOG WHERE CHATLOGID = ?";

		String userId = null;

		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, chatLogId);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				userId = rs.getString("USERID");
			}

		} catch (Exception e) {
			throw new SwackException(ERR_DB_PROCESS, e);
		}

		return userId;

	}

	/**
	 * チャットログのメッセージ内容を編集するDAO
	 * @param chatLogId 編集するチャットログのID
	 * @param message 置き換えるメッセージの内容
	 * @return boolean（true:成功 false:失敗）
	 * @throws SwackException
	 */
	public boolean editChatLog(int chatLogId, String message) throws SwackException {
		//SQL文
		String sql = "UPDATE chatlog SET message=? where chatlogid = ?";

		try (Connection conn = dataSource.getConnection()) {

			//SQL文作成
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, message);
			pStmt.setInt(2, chatLogId);

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

}
