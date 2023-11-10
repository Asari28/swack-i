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
import model.RoomModel;

/**
 * Servlet implementation class LastJoinRoomServlet
 */
@WebServlet("/LastJoinRoomServlet")
public class LastJoinRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LastJoinRoomServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String roomId = request.getParameter("roomId");

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		RoomModel roomModel = new RoomModel();
		try {
			boolean result = roomModel.setLastJoinRoom(user.getUserId(), roomId);
			if (!result) {
				request.setAttribute("errorMsg", ERR_DB_PROCESS);
			}
			response.sendRedirect("MainServlet");
			return;
		} catch (SwackException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("MainServlet").forward(request, response);
			return;
		}

	}

}
