package model;

import bean.User;
import dao.UsersDAO;
import exception.SwackException;

public class SignUpModel {
	/**
	 * メールアドレスに不備がないか確認する
	 * @param userName
	 * @param mailaddress
	 * @param password
	 * @return true = 不備あり false = 不備なし
	 * @throws SwackException 
	 */
	public boolean checkSignup(String mailaddress) throws SwackException {
		UsersDAO usersDAO = new UsersDAO();
		boolean result = usersDAO.checkMailAddress(mailaddress);
		if (result) {
			//不備なし
			return true;
		} else {
			//不備あり
			return false;
		}
	}

	/**
	 * 名前、メールアドレス、パスワードを受け取ってInsertする
	 * ユーザIDはgetNewUserId()で取得する
	 * @param username
	 * @param mailAddress
	 * @param password
	 * @return
	 * @throws SwackException
	 */
	public boolean insert(String username, String mailAddress, String password) throws SwackException {
		//DAOインポート
		UsersDAO usersDAO = new UsersDAO();
		//ユーザIDの取得
		String userId = getNewUserId();
		//User生成
		User user = new User(userId, username, mailAddress, password);
		boolean result = usersDAO.insert(user);
		return result;
	}

	/**
	 * Insertから呼び出されて新しいユーザIDを作る
	 * Usersから最大値を取得して新しいユーザIDを返す
	 * @return String userId
	 * @throws SwackException 
	 */
	private String getNewUserId() throws SwackException {
		// 登録済ユーザIDの最大値を取得
		UsersDAO usersDAO = new UsersDAO();
		String maxUserId = usersDAO.maxUserId();
		if (maxUserId != null) {
			// 登録済ユーザIDの最大値から"U"を取り除き、+1して次の番号(数値)にする
			int nextNo = Integer.parseInt(maxUserId.substring(1)) + 1;
			// 次の番号(数値)を4桁の文字列にして、先頭に"U"をつける
			System.out.println(nextNo);
			return "U" + String.format("%04d", nextNo);
		} else {
			// データが無かった(ユーザが登録されていない)場合は初期番号を設定
			System.out.println("初期番号設定");
			return "U" + "0001";
		}

	}

}
