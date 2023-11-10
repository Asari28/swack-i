package servlet;

import static parameter.Messages.*;

import java.io.IOException;
import java.net.URLEncoder;

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
			String errorMsg = null;
			//操作している人がチャットを打った人か
			if (userId.equals(user.getUserId()) || user.getUserId().equals("U0000")) {
				//成功　削除処理
				boolean result = chatModel.deleteChatLog(chatLogId);
				if (!result) {
					errorMsg = ERR_DB_PROCESS;
				} else {
					//エラーメッセージの有無で判断しようとすると、ログイン時などの
					//関係ないときにも動いてしまうため変更しました。
					//より良いコードがあれば置き換えていただいて構いません。

					//成功
					errorMsg = "成功";
				}
			} else {
				//失敗
				errorMsg = "失敗";
			}
			response.sendRedirect("MainServlet?errorMsg=" + URLEncoder.encode(errorMsg, "UTF-8") + "&roomId="
					+ URLEncoder.encode(roomId, "UTF-8"));

		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			response.sendRedirect("MainServlet");
		}

	}

}
