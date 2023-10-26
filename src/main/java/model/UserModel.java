package model;

import java.util.ArrayList;

import bean.User;
import dao.UsersDAO;
import exception.SwackException;

public class UserModel {
	public ArrayList<User> getUsers() throws SwackException {
		// UserDAOからユーザ一覧を取得
		UsersDAO usersDAO = new UsersDAO();
		ArrayList<User> userList = usersDAO.selectAllUser();
		return userList;

	}

}
