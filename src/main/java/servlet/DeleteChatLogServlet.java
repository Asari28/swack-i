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
import model.ChatModel;

/**
 * Servlet implementation class DeleteChatLogServlet
 */
@WebServlet("/DeleteChatLogServlet")
public class DeleteChatLogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteChatLogServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//パラメータ取得
		String roomId = request.getParameter("roomId");
		int chatLogId = Integer.parseInt(request.getParameter("chatLogId"));
		ChatModel chatModel = new ChatModel();
		request.setAttribute("roomId", roomId);
		//セッションからuser取得
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		try {
			//チャットを打った人のUserId
			String userId = chatModel.getChatlogUserId(chatLogId);
			//操作している人がチャットを打った人か
			if (userId.equals(user.getUserId()) || user.getUserId().equals("U0000")) {
				//成功　削除処理
				boolean result = chatModel.deleteChatLog(chatLogId);
				if (!result) {
					request.setAttribute("errorMsg", ERR_DB_PROCESS);
				}
			} else {
				//失敗
				request.setAttribute("errorMsg", "あなたは偽物です");
			}
			response.sendRedirect("MainServlet");
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			response.sendRedirect("MainServlet");
		}

	}

}
