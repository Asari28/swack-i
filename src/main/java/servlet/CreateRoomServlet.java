package servlet;

import static parameter.Messages.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import exception.SwackException;
import model.RoomModel;
import model.UserModel;

/**
 * Servlet implementation class CreateRoomServlet
 */
@WebServlet("/CreateRoomServlet")
public class CreateRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateRoomServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserModel usermodel = new UserModel();
		try {
			ArrayList<User> userlist = usermodel.getUsers();
			request.setAttribute("userList", userlist);
			request.getRequestDispatcher("/Mycreateroom.jsp").forward(request, response);
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
			return;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// パラメータ取得
		String roomname = request.getParameter("roomName");
		String joinuserid = request.getParameter("joinUserId");
		//		String directed = request.getParameter("privated");
		boolean privated = true;
		System.out.println(roomname);
		System.out.println(joinuserid);
		System.out.println(privated);
		//セッションからuserを取得する
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		RoomModel roommodel = new RoomModel();
		boolean result = false;
		try {
			result = roommodel.createRoom(roomname, user.getUserId(), privated, false);

			if (result) {
				String roomId;

				roomId = roommodel.getMaxRoomId();
				boolean Result = roommodel.joinUser(roomId, joinuserid);
				if (Result) {
					session.setAttribute("user", user);
					response.sendRedirect("MainServlet");
				} else {
					request.setAttribute("errorMsg", ERR_SYSTEM);
					request.getRequestDispatcher("/Mycreateroom.jsp").forward(request, response);
					return;
				}
			} else {
				request.setAttribute("errorMsg", ERR_SYSTEM);
				request.getRequestDispatcher("/Mycreateroom.jsp").forward(request, response);
				return;
			}
		} catch (SwackException e) {

			e.printStackTrace();
		}

	}

}
