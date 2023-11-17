package model;

import java.sql.Date;

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
		if (usersDAO.checkExit(user.getUserId()) != null) {
			result = true;
		}

		return result;
	}

	public boolean checkDate(String userid) throws SwackException {
		UsersDAO usersdao = new UsersDAO();
		Date date = usersdao.checkDate(userid);
		Date nowDate = (Date) new java.util.Date();
		boolean rs = true;
		if (nowDate.getTime() - date.getTime() > 21) {
			rs = false;
		}
		return rs;
	}

}
