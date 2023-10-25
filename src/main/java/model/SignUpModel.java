package model;

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
			return false;
		} else {
			return true;
		}
	}

	private String getNewUserId() {
		String maxUserId = null;
		return maxUserId;

	}

	public boolean insert(String username, String mailAddress, String password) {

		boolean result = false;
		return result;
	}

}
