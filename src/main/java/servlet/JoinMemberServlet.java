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

import bean.Room;
import bean.User;
import exception.SwackException;
import model.RoomModel;
import model.UserModel;

/**
 * Servlet implementation class JoinMemberServlet
 */
@WebServlet("/JoinMemberServlet")
public class JoinMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JoinMemberServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String roomId = request.getParameter("roomid");
		System.out.println(roomId);
		//セッションからuserを取得する
		HttpSession session = request.getSession();
		//		User user = (User) session.getAttribute("user");
		Room room = (Room) session.getAttribute(roomId);
		UserModel usermodel = new UserModel();
		RoomModel roommodel = new RoomModel();
		try {
			ArrayList<User> userlist = usermodel.getJoinUsers(room.getRoomId());
			Room nowroom = roommodel.getRoomId(room.getRoomId());
			request.setAttribute("userList", userlist);
			request.setAttribute("nowRoom", nowroom);
			request.getRequestDispatcher("/joinmember.jsp").forward(request, response);
		} catch (SwackException e) {
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
		//セッションからuserを取得する
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Room room = (Room) session.getAttribute("room");
		//パラメーター取得
		String RoomId = request.getParameter("roomName");
		String[] joinUseridlist = request.getParameterValues("joinUserIdList");
		System.out.println(RoomId);
		System.out.println(joinUseridlist);
		RoomModel roommodel = new RoomModel();

		try {
			boolean result = false;
			for (String joinuser : joinUseridlist) {
				result = roommodel.joinUser(RoomId, joinuser);
				if (result) {
					return;
				} else {
					break;
				}
			}
			if (result) {
				session.setAttribute("user", user);
				session.setAttribute("room", room);
				response.sendRedirect("MainServlet");
			} else {
				request.setAttribute("errorMsg", ERR_SYSTEM);
				request.getRequestDispatcher("/joinmember.jsp").forward(request, response);
				return;
			}
		} catch (SwackException e) {
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("/joinmember.jsp").forward(request, response);
			return;

		}

	}

}
