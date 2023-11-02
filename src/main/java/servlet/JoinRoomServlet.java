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

/**
 * Servlet implementation class JoinRoomServlet
 */
@WebServlet("/JoinRoomServlet")
public class JoinRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JoinRoomServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//参加するルームIDのパラメータをmain.jspから取得
		String userId = request.getParameter("userId");
		String roomId = request.getParameter("roomId");
		//準備
		RoomModel roommodel = new RoomModel();
		try {
			//自分が参加していないルーム一覧を取得
			ArrayList<Room> allRoom = roommodel.getRoomId(roomId);
			//allRoom.jspに値を入れる
			request.setAttribute("nowRoom", allRoom);
			request.setAttribute("userId", userId);
			//allRoom.jspにフォワード
			request.getRequestDispatcher("/allRoom.jsp").forward(request, response);
		} catch (SwackException e) {
			//何らかのエラーになった場合はエラーメッセージをmain.jspに表示
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
		//セッションからuserとroomを取得する
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Room room = (Room) session.getAttribute("room");
		//パラメーター取得
		String RoomId = request.getParameter("roomId");
		System.out.println(RoomId);
		//準備
		RoomModel roommodel = new RoomModel();
		
		try {
			//受け取った参加するroomIdでデータベースにuserIdを格納させる
			boolean result = false;
			result=
			if (result) {
				//正常に終了した場合はuser,roomをセットしてMainServletにリダイレクト
				session.setAttribute("user", user);
				session.setAttribute("room", room);
				response.sendRedirect("MainServlet");
			} else {
				//falseだった場合はエラーメッセージをallRoom.jspに表示
				request.setAttribute("errorMsg", ERR_SYSTEM);
				request.getRequestDispatcher("/allRoom.jsp").forward(request, response);
				return;
			}
		} catch (SwackException e) {
			//何らかのExceptionになった場合はエラーメッセージをjoinmember.jspに表示
			request.setAttribute("errorMsg", ERR_SYSTEM);
			request.getRequestDispatcher("/allRoom.jsp").forward(request, response);
			return;

		}

	}

}
