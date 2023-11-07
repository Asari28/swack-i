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
 * Servlet implementation class CreateDirectServlet
 */
@WebServlet("/CreateDirectServlet")
public class CreateDirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateDirectServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String roomId = request.getParameter("roomId");
		if (roomId == null) {
			roomId = (String) request.getAttribute("roomId");
		}

		String errorMsg = (String) request.getAttribute("errorMsg");
		System.out.println(errorMsg);
		UserModel usermodel = new UserModel();
		try {
			ArrayList<User> userlist = usermodel.getUnDirectedUsers(user.getUserId());
			request.setAttribute("userList", userlist);
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("/WEB-INF/jsp/startdirectchat.jsp").forward(request, response);
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.setAttribute("roomId", ERR_SYSTEM);
			request.getRequestDispatcher("MainServlet").forward(request, response);
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// パラメータ取得
		String joinUserId = request.getParameter("joinUserId");
		boolean privated = true;
		boolean directed = true;

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String roomId = request.getParameter("roomId");

		RoomModel roomModel = new RoomModel();
		try {
			String roomName = new UserModel().getDirectRoomName(user.getUserId(), joinUserId);
			boolean result = roomModel.createRoom(roomName, "U0000", directed, privated);
			if (result) {
				boolean Result = false;
				String newRoomId = roomModel.getMaxRoomId();
				boolean userresult = roomModel.joinUser(newRoomId, user.getUserId());
				if (userresult) {
					Result = roomModel.joinUser(newRoomId, joinUserId);
				}

				if (Result) {
					session.setAttribute("user", user);
					request.setAttribute("roomId", roomId);
					response.sendRedirect("MainServlet");
				} else {
					throw new SwackException();
				}
			} else {
				throw new SwackException();
			}
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.setAttribute("roomId", roomId);
			doGet(request, response);
			return;
		}
	}

}
