package dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import context.SetUpDBConnectionPool;
import exception.SwackException;

class ChatDAOTest {

	private static ChatDAO ChatDAO;

	//テスト動かす前に準備
	@BeforeAll
	static void setUpBeforeClass() throws SwackException {
		SetUpDBConnectionPool.setUp();
		ChatDAO = new ChatDAO();
	}

	//	@Test
	//	void testGetChatlogList() throws SwackException {
	//		List<ChatLog> chatLogList = ChatDAO.getChatlogList("R0000");
	//		System.out.println("testGetChatlogList():");
	//		for (ChatLog log : chatLogList) { //拡張for文
	//			System.out.println(log);
	//		}
	//		assertNotNull(chatLogList);
	//	}

	@Test
	void testDeleteChatlog() throws SwackException {
		boolean rs = ChatDAO.deleteChatlog(8);
		if (rs) {
			System.out.println("testGetChatlogList():成功");
		} else { //拡張for文
			System.out.println("testGetChatlogList():失敗");
		}
	}
}