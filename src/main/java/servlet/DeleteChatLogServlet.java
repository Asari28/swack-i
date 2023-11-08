package servlet;

import static parameter.Messages.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		try {
			boolean result = chatModel.deleteChatlog(chatLogId);
			if (!result) {
				request.setAttribute("errorMsg", ERR_DB_PROCESS);
			}
			request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
		}

	}

}
