package dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bean.ChatLog;
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

	@Test
	void testGetChatlogList() throws SwackException {
		List<ChatLog> chatLogList = ChatDAO.getChatlogList("R0000");
		System.out.println("testGetChatlogList():");
		for (ChatLog log : chatLogList) { //拡張for文
			System.out.println(log);
		}
		assertNotNull(chatLogList);
	}
}