package com.how.tfg.data.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Service;

import com.how.tfg.social.UserService;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;

@Service
public class TrelloService {
	
	private UserService service;
	
	private ConnectionRepository connectionRepository;
	
	private Trello trello;

	@Autowired
    public TrelloService(UserService service, ConnectionRepository connectionRepository) {
        this.service = service;
        this.connectionRepository = connectionRepository;
    }
	
	@SuppressWarnings("unchecked")
	public Trello getApi() {
		if (trello==null)
			trello = ((Connection<Trello>) connectionRepository.findConnections("trello").get(0)).getApi();
		return trello;
	}
    
    public Member getProfileMe() {
        return getApi().getMemberInformation("me");
    }
    
    public List<Board> getAllBoard() {
        List<String> boardsId = getProfileMe().getIdBoards();
        List<Board> boards = new ArrayList<Board>();
        for (String boardId : boardsId) {
        	Board temp = getApi().getBoard(boardId, new Argument("",""));
        	boards.add(temp);
        }
        return boards;
    }
    
    public List<TList> getAllListOfBoardId(String boardId) {
    	return trello.getBoardLists(boardId, new Argument("cards","all"));
    }

}
