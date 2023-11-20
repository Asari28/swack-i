package model;

import bean.User;
import dao.UsersDAO;
import exception.SwackException;

/**
 * ログイン認証を実行するクラス
 */
public class LoginModel {

	/**
	 * 存在するユーザか確認
	 * @param mailAddress メールアドレス
	 * @param password パスワード
	 * @return ユーザ情報(ログインできなかった場合はnull)
	 */
	public User checkLogin(String mailAddress, String password) throws SwackException {
		UsersDAO usersDAO = new UsersDAO();
		User user = usersDAO.select(mailAddress, password);
		return user;
	}

	/**
	 * 退会、ロックされているかの確認
	 * @param user ユーザの情報
	 * @return ture(退会、ロック済み)　false(異常なし)
	 */
	public boolean checkExit(User user) throws SwackException {
		UsersDAO usersDAO = new UsersDAO();
		boolean result = false;
		try {
			String state = usersDAO.checkExit(user.getUserId());
			if (state == "Exit") {
				result = true;
			}
		} catch (NullPointerException e) {
			return result;
		}

		return result;
	}

	public boolean checkDate(String userid) throws SwackException {
		boolean rs = false;
		UsersDAO usersdao = new UsersDAO();
		int date = usersdao.checkDate(userid);
		System.out.println(date);
		if (date > 21) {
			rs = true;
		}
		return rs;
	}

	/**
	 * 連続失敗回数の確認
	 * @param user ユーザの情報
	 * @return int 回数
	 */
	public int getCount(User user) throws SwackException {
		return new UsersDAO().getMissCount(user.getUserId());
	}

	/**
	 * 連続失敗回数のセット
	 * @param user ユーザの情報、回数
	 * @return true(異常なし)　false(処理失敗)
	 */
	public boolean setCount(User user, int cnt) throws SwackException {
		UsersDAO usersDAO = new UsersDAO();
		boolean result = usersDAO.setMissCount(user.getUserId(), cnt);
		if (!result) {
			result = false;
		} else {
			result = true;
		}

		return result;
	}
}
