package servlet;

import static parameter.Messages.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import exception.SwackException;
import model.LoginModel;
import model.UserModel;

/**
 * ログイン処理を実行するServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// パラメータ取得
		String mailAddress = request.getParameter("mailAddress");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// パラメータチェック
		StringBuilder errorMsg = new StringBuilder();
		if (mailAddress == null || mailAddress.length() == 0) {
			errorMsg.append("メールアドレスが入っていません<br>");
		}
		if (password == null || password.length() == 0) {
			errorMsg.append("パスワードが入っていません<br>");
		}
		if (errorMsg.length() > 0) {
			// エラー
			request.setAttribute("errorMsg", errorMsg.toString());
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
			return;
		}

		// 処理
		try {
			// ログインチェック
			LoginModel loginModel = new LoginModel();
			user = loginModel.checkLogin(mailAddress, password);
			if (user == null) {
				// 認証失敗
				request.setAttribute("errorMsg", ERR_LOGIN_PARAM_MISTAKE);

				request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
				return;
			}
			boolean result = loginModel.checkExit(user);
			int cnt = loginModel.getCount(user);
			boolean rs = loginModel.checkDate(user.getUserId());
			if (rs || cnt >= 5) {
				// 認証失敗
				cnt += 1;
				if (cnt >= 5) {
					UserModel usermodel = new UserModel();
					if (!(usermodel.lockUser(user.getUserId()))) {
						request.setAttribute("errorMsg", ERR_DB_PROCESS);
						cnt -= 1;
					} else {
						request.setAttribute("errorMsg", ACCOUNT_LOCK);
					}
					loginModel.setCount(user, cnt);

				}
				request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
				return;
			} else if (result) {
				// 認証失敗
				request.setAttribute("errorMsg", ACCOUNT_EXIT);
				request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
				return;
			} else {
				// 認証成功(ログイン情報をセッションに保持)
				loginModel.setCount(user, 0);
				session.setAttribute("user", user);
				request.getRequestDispatcher("/WEB-INF/jsp/loading.jsp").forward(request, response);
				return;
			}

		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
			return;
		}

	}

}