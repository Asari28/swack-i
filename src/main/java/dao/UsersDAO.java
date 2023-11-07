package dao;

import static parameter.Messages.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.User;
import exception.SwackException;

public class UsersDAO extends BaseDAO {
	public UsersDAO() throws SwackException {
		super();
	}

	/**
	 * メールアドレスとパスワードがあっているか確認する
	 * （備え付けのまま変更なし）
	 * @param mailAddress メールアドレス
	 * @param password パスワード
	 * @return User(該当ユーザデータを返却)
	 * @throws SwackException
	 */
	public User select(String mailAddress, String password) throws SwackException {
		String sql = "SELECT USERID, USERNAME FROM USERS WHERE MAILADDRESS = ? AND PASSWORD = ?";
		User user = null;
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, mailAddress);
			pStmt.setString(2, password);

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				String userId = rs.getString("USERID");
				String userName = rs.getString("USERNAME");
				// mask password
				user = new User(userId, userName, mailAddress, "********");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
		return user;
	}

	/**
	 * メールアドレスに重複がないか確認する
	 * @param mailAddress
	 * @return true = 使っている人がいないので成功
	 * 			false = 使っている人がいるので失敗
	 * @throws SwackException
	 */
	public boolean checkMailAddress(String mailAddress) throws SwackException {
		//Userの中にUserIDも入っている状態で受け取る
		//SQL
		String sql = "SELECT mailAddress FROM users WHERE mailAddress = ?";
		//userId,userName,mailAddress,passwordの順番でセットする
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			//SQL組み立て
			pStmt.setString(1, mailAddress);

			//SQL実行
			ResultSet rs = pStmt.executeQuery();

			//結果
			String uMailAddress = null;
			if (rs.next()) {
				uMailAddress = rs.getString("mailAddress");
			}
			System.out.println(uMailAddress);
			if (uMailAddress == null) {
				return true;//nullなら成功
			} else {
				return false;//人いたから失敗
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 新規ユーザのために現時点でのユーザIDの最大値を取得
	 * @return String maxUserId
	 * @throws SwackException
	 */
	public String maxUserId() throws SwackException {
		String sql = "SELECT MAX(USERID) AS userId FROM USERS";
		String userId = null;
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);

			ResultSet rs = pStmt.executeQuery();
			rs.next();
			userId = rs.getString("userId");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
		return userId;
	}

	/**
	 * Userに入っている情報をもとにusersに新規登録をする
	 * @param user
	 * @return boolean(登録に成功したらtrue)
	 * @throws SwackException
	 */

	public boolean insert(User user) throws SwackException {
		//Userの中にUserIDも入っている状態で受け取る
		//SQL
		String sql = "INSERT INTO users (userid,username,mailaddress,password) VALUES(?,?,?,?);";
		//userId,userName,mailAddress,passwordの順番でセットする
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			//SQL組み立て
			pStmt.setString(1, user.getUserId());
			pStmt.setString(2, user.getUserName());
			pStmt.setString(3, user.getMailAddress());
			pStmt.setString(4, user.getPassword());

			//SQL実行
			int result = pStmt.executeUpdate();

			//結果
			if (result != 1) {
				return false;//失敗
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}

		//結果の返却
		return true;
	}

	/**
	 * ユーザIDとユーザ名を一覧で取得する
	 * @param userId リストから除外するユーザID
	 * @return ArrayList<User> 
	 * @throws SwackException
	 */

	public ArrayList<User> selectAllUser(String userId) throws SwackException {
		//SQL
		//Adminと自分を除去したリストを取得するSQL
		String sql = "SELECT * FROM USERS WHERE userid <> 'U0000' AND userid<>?";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, userId);

			//SQL実行
			ResultSet rs = pStmt.executeQuery();

			//結果をリストに詰める
			ArrayList<User> userList = new ArrayList<User>();
			while (rs.next()) {
				String listUserId = rs.getString("userId");
				String userName = rs.getString("userName");
				String mailAddress = rs.getString("mailAddress");
				User user = new User(listUserId, userName, mailAddress);
				userList.add(user);
			}

			return userList;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	/**
	 * 招待するルームに参加していないユーザのユーザIDとユーザ名を取得する
	 * @param roomId 招待するルームID
	 * @return ArrayList<User> ルームにまだ参加していないユーザのユーザIDとユーザ名
	 * @throws SwackException
	 */

	public ArrayList<User> selectNotJoinUser(String roomId) throws SwackException {
		//SQL
		//Adminとすでに参加しているユーザを除去したリストを取得するSQL
		String sql = "SELECT userid,username FROM users WHERE userid not in (SELECT userid FROM joinroom WHERE roomid=?) AND userid<>'U0000'";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, roomId);

			//SQL実行
			ResultSet rs = pStmt.executeQuery();

			//結果をリストに詰める
			ArrayList<User> userList = new ArrayList<User>();
			while (rs.next()) {
				String listUserId = rs.getString("userId");
				String userName = rs.getString("userName");
				User user = new User(listUserId, userName);
				userList.add(user);
			}

			return userList;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}

	public ArrayList<User> selectUnDirectedUser(String userId) throws SwackException {
		//SQL
		//Adminとすでに参加しているユーザを除去したリストを取得するSQL
		String sql = "SELECT userId,userName FROM users WHERE userId <> 'U0000' AND userId NOT IN(SELECT userId FROM joinroom WHERE roomId IN (SELECT roomId FROM joinroom WHERE userId = ? AND roomId IN (SELECT roomId FROM ROOMS WHERE createdUserId = 'U0000' AND directed = true)))";
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, userId);

			//SQL実行
			ResultSet rs = pStmt.executeQuery();

			//結果をリストに詰める
			ArrayList<User> userList = new ArrayList<User>();
			while (rs.next()) {
				String listUserId = rs.getString("userId");
				String userName = rs.getString("userName");
				User user = new User(listUserId, userName);
				userList.add(user);
			}

			return userList;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SwackException(ERR_DB_PROCESS, e);
		}
	}
}
