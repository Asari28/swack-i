package servlet;

import static parameter.Messages.*;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.SwackException;
import model.ChatModel;

/**
 * Servlet implementation class EditChatLogServlet
 */
@WebServlet("/EditChatLogServlet")
public class EditChatLogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditChatLogServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//パラメーター取得
		String roomId = request.getParameter("roomId");
		int chatLogId = Integer.parseInt(request.getParameter("chatLogId"));
		String message=request.getParameter("message");
		ChatModel chatModel = new ChatModel();
		String errorMsg = null;
		try {
			//編集処理
			boolean result=chatModel.editChatLog(chatLogId , message);
			if(result) {
				errorMsg="編集処理成功";
				response.sendRedirect("MainServlet?errorMsg=" + URLEncoder.encode(errorMsg, "UTF-8") + "&roomId="
						+ URLEncoder.encode(roomId, "UTF-8"));
			}else {
				errorMsg="編集処理失敗";
				response.sendRedirect("MainServlet?errorMsg=" + URLEncoder.encode(errorMsg, "UTF-8") + "&roomId="
						+ URLEncoder.encode(roomId, "UTF-8"));
			}
		
		}catch(SwackException e){
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			response.sendRedirect("MainServlet");
		}

}
