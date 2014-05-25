package com.how.tfg.data.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.java.ServiceScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Service;

import com.how.tfg.data.domain.trello.BoardMeasure;
import com.how.tfg.data.domain.trello.CardsOfDay;
import com.how.tfg.data.domain.trello.BoardMeasureRepository;
import com.how.tfg.social.UserService;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;

@Service
public class TrelloService {
	
	private UserService service;
	
	private ConnectionRepository connectionRepository;
	
	private BoardMeasureRepository repository;
	
	private Trello trello;

	@Autowired
    public TrelloService(UserService service, ConnectionRepository connectionRepository,BoardMeasureRepository repository) {
        this.service = service;
        this.connectionRepository = connectionRepository;
        this.repository = repository;
    }
	
	public String getEmailOfUserLgoin() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	@SuppressWarnings("unchecked")
	public Trello getApi() {
		if (trello==null)
			trello = ((Connection<Trello>) connectionRepository.findConnections("trello").get(0)).getApi();
		return trello;
	}
	
	public List<BoardMeasure> getAllMeasure() {
		return repository.getByEmail(getEmailOfUserLgoin());
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
    
    public boolean notExistBoardMeasure(String boardId) {
    	return repository.getByEmailAndBoardId(getEmailOfUserLgoin(), boardId).size()==0;
    }
    
    public BoardMeasure createMeasureOfBoardId(String boardId) {
    	Board board = trello.getBoard(boardId, new Argument("",""));
    	List<TList> listas = trello.getBoardLists(boardId, new Argument("cards","all"));
    	BoardMeasure measure = new BoardMeasure();
    	measure.setBoardId(boardId);
    	measure.setName(board.getName());
    	measure.setEmail(getEmailOfUserLgoin());
    	measure.setDateCreation(new DateTime());
    	measure.setUpdateCreation(DateTime.now());
    	Map<String, List<CardsOfDay>> numCardsForList = new HashMap();
    	Map<String,Integer> cardsNowList = new HashMap();
    	List<Action> actionsOnList = board.fetchActions(new Argument("filter","updateCard,createCard,createBoard"),new Argument("limit","1000"));
    	Map<String, String> listName = new HashMap<String,String>();
    	for (TList list : listas){
    		listName.put(list.getId(), list.getName());
    		Integer sizeList = list.getCards().size();
    		cardsNowList.put(list.getId(), sizeList);
    		numCardsForList.put(list.getId(), new ArrayList<CardsOfDay>());
    	}
    	for (Action action : actionsOnList) {
    		if (action.getData().getListBefore()!=null) {
    			String idListFrom = action.getData().getListBefore().getId();
    			String idListTo = action.getData().getListAfter().getId();
    			numCardsForList.get(idListTo).add(new CardsOfDay(new DateTime(action.getDate()), cardsNowList.get(idListTo)));
    			numCardsForList.get(idListFrom).add(new CardsOfDay(new DateTime(action.getDate()), cardsNowList.get(idListFrom)));
    			cardsNowList.put(idListFrom, cardsNowList.get(idListFrom)+1);
    			cardsNowList.put(idListTo, cardsNowList.get(idListTo)-1);
    		} else if (action.getType().equals("createCard")) {
    			String id = action.getData().getList().getId();
    			numCardsForList.get(id).add(new CardsOfDay(new DateTime(action.getDate()), cardsNowList.get(id)));
    			cardsNowList.put(id, cardsNowList.get(id)-1);
    		} else if (action.getType().equals("createBoard")) {
    			for (TList list : listas) {
    				numCardsForList.get(list.getId()).add(new CardsOfDay(new DateTime(action.getDate()), cardsNowList.get(list.getId())));
    			}
    		}
    	}
    	measure.setListName(listName);
    	measure.setTaskForList(numCardsForList);
    	repository.save(measure);
    	return measure;
    }
    
    public void deleteMeasureOfId(String measureid) {
    	repository.delete(measureid);
    }
    
    public BoardMeasure getBoardMeasureById(String boarId){
    	return repository.findOne(boarId);
    }
    
    public boolean createWebHookBoard(String measureId) {
    	return true;
    }
}
