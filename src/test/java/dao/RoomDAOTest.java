package dao;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bean.Room;
import context.SetUpDBConnectionPool;
import exception.SwackException;

class RoomDAOTest {

	/*
	 * 複数回テストするとき用DELETE文
	 * 
	 * DELETE FROM joinroom WHERE roomId = 'R9999' AND userid = 'U0004';
	 * DELETE FROM rooms WHERE roomId = 'R9999';
	 * 
	 * INSERT系テストする際には＠Test前のコメント//が
	 * 外れていることを確認してください
	 * 
	 */

	private static RoomDAO RoomDAO;

	//テスト動かす前に準備
	@BeforeAll
	static void setUpBeforeClass() throws SwackException {
		SetUpDBConnectionPool.setUp();
		RoomDAO = new RoomDAO();
	}

	@Test
	void testGetMaxRoomId() throws SwackException {
		String roomId = RoomDAO.getMaxRoomId();
		assertNotNull(roomId);
		System.out.println("testGetMaxRoomId():" + roomId);
	}

	//@Test
	void testCreateRoom() throws SwackException {
		boolean result = RoomDAO.createRoom("R9999", "テストルーム", "U0004", false, false);
		assertTrue(result);
		if (result) {
			System.out.println("testCreateRoom():成功");
		} else {
			System.out.println("testCreateRoom():失敗");
		}
	}

	//@Test
	void testInsertJoinRoom() throws SwackException {
		boolean result = RoomDAO.insertJoinRoom("R9999", "U0004");
		assertTrue(result);
		if (result) {
			System.out.println("testInsertJoinRoom():成功");
		} else {
			System.out.println("testInsertJoinRoom():失敗");
		}
	}

	@Test
	void testGetRoom() throws SwackException {
		Room room = RoomDAO.getRoom("R0000");
		assertNotNull(room);
		System.out.println("testGetRoom():" + room.getRoomId() + room.getRoomName());
	}
}