package servlet;

import static parameter.Messages.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.SwackException;
import model.UserModel;

/**
 * Servlet implementation class AccountUnlockServlet
 */
@WebServlet("/AccountUnlockServlet")
public class AccountUnlockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountUnlockServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//パラメーター取得
		String userid = request.getParameter("userId");
		UserModel usermodel = new UserModel();
		boolean result = false;
		String errorMsg;
		try {
			result = usermodel.unlock(userId);
			if (result) {
				errorMsg = "ユーザのアカウントロックを解除しました";
				request.setAttribute("errorMsg", errorMsg);
				request.getRequestDispatcher("/WEB-INF/jsp/.jsp").forward(request, response);
				return;
			} else {
				errorMsg = "ユーザのアカウントロックを解除できませんでした";
				request.setAttribute("errorMsg", errorMsg);
				request.getRequestDispatcher("/WEB-INF/jsp/.jsp").forward(request, response);
				return;
			}
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("/WEB-INF/jsp/.jsp").forward(request, response);
			return;
		}

	}

}