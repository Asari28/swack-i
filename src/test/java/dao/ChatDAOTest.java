package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bean.ChatLog;
import context.SetUpDBConnectionPool;
import exception.SwackException;

class ChatDAOTest {

	private static ChatDAO ChatDAO;

	//テスト動かす前の準備
	@BeforeAll
	static void setUpBeforeClass() throws SwackException {
		SetUpDBConnectionPool.setUp();
		ChatDAO = new ChatDAO();
	}

	//@Test
	void testGetChatlogList() throws SwackException {
		List<ChatLog> chatLogList = ChatDAO.getChatlogList("R0000");
		System.out.println("testGetChatlogList():");
		for (ChatLog log : chatLogList) { //拡張for文
			System.out.println(log);
		}
		assertNotNull(chatLogList);
	}

	//@Test
	void testDeleteChatlog() throws SwackException {
		boolean rs = ChatDAO.deleteChatlog(8);
		if (rs) {
			System.out.println("testGetChatlogList():成功");
		} else {
			System.out.println("testGetChatlogList():失敗");
		}
	}

	@Test
	void testGetChatlogUserId() throws SwackException {
		String userId = ChatDAO.getChatlogUserId(7);
		System.out.println("testGetChatlogList():'U0001'なら成功");
		System.out.println(userId);
	}

	@Test
	void testEditChatlog() throws SwackException {
		boolean rs = ChatDAO.editChatLog(15, "あああ");
		if (rs) {
			System.out.println("testEditChatlog():成功");

		} else {
			System.out.println("失敗");
		}
	}
}