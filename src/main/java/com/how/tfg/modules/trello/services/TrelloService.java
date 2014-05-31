package com.how.tfg.modules.trello.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;

import com.how.tfg.modules.core.services.ServiceModuleAbstract;
import com.how.tfg.modules.trello.domain.BoardMeasure;
import com.how.tfg.modules.trello.repository.BoardMeasureRepository;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;


@Service
public class TrelloService extends ServiceModuleAbstract<Trello,BoardMeasure> {
	
	private BoardMeasureRepository repository;

	@Autowired
    public TrelloService(ConnectionRepository connectionRepository,BoardMeasureRepository repository, UsersConnectionRepository userConnectionRepository) {
        super(connectionRepository, repository, userConnectionRepository, "trello");
        this.repository = repository;
    }
	
	public String getEmailOfUserLgoin() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public List<BoardMeasure> getAllMeasure() {
		return repository.getByEmail(getEmailOfUserLgoin());
	}
    
    public Member getMeProfile() {
        return getApi().getMemberInformation("me");
    }
    
    public List<Board> getAllBoard() {
        List<String> boardsId = getMeProfile().getIdBoards();
        List<Board> boards = new ArrayList<Board>();
        for (String boardId : boardsId) {
        	Board temp = getApi().getBoard(boardId, new Argument("",""));
        	boards.add(temp);
        }
        return boards;
    }
    
    public List<TList> getAllListOfBoardId(String boardId) {
    	return getApi().getBoardLists(boardId, new Argument("cards","all"));
    }
    
    public boolean notExistBoardMeasure(String boardId) {
    	return repository.getByEmailAndBoardId(getEmailOfUserLgoin(), boardId).size()==0;
    }
    
    public BoardMeasure createMeasureOfBoardId(String boardId) {
    	Board board = getApi().getBoard(boardId, new Argument("",""));
    	List<TList> listas = getApi().getBoardLists(boardId, new Argument("cards","all"));
    	BoardMeasure measure = new BoardMeasure();
    	measure.setBoardId(boardId);
    	measure.setName(board.getName());
    	measure.setEmail(getEmailOfUserLgoin());
    	Map<String, Map<Long,Integer>> numCardsForList = new TreeMap<String,Map<Long,Integer>>();
    	Map<String,Integer> cardsNowList = new TreeMap<String,Integer>();
    	List<Action> actionsOnList = board.fetchActions(new Argument("filter","updateCard,createCard,createBoard,copyCard,moveCardToBoard,moveCardFromBoard"),new Argument("limit","1000"));
    	Collections.reverse(actionsOnList);
    	Map<String, String> listName = new TreeMap<String,String>();
    	Map<String, List<String>> cardForList = new TreeMap<String,List<String>>();
    	Map<String,String> cardClosed = new TreeMap<String,String>();
    	for (TList list : listas){
    		listName.put(list.getId(), list.getName());
    		cardsNowList.put(list.getId(), 0);
    		numCardsForList.put(list.getId(), new TreeMap<Long,Integer>());
    		cardForList.put(list.getId(), new ArrayList<String>());
    	}
    	for (Action action : actionsOnList) {
    		DateTime date = new DateTime(action.getDate());
    		Long day = new DateTime(date.getYear(),date.getMonthOfYear(),date.getDayOfMonth(),0,0).getMillis();
    		String type = action.getType();
    		if (action.getData().getListBefore()!=null) {
    			String cardId = action.getData().getCard().getId();
    			String idListFrom = action.getData().getListBefore().getId();
    			String idListTo = action.getData().getListAfter().getId();
    			if (cardForList.get(idListFrom).contains(cardId)) {
	    			if (!cardsNowList.containsKey(idListFrom)) {
	    				cardsNowList.put(idListFrom, 0);
	    				numCardsForList.put(idListFrom, new TreeMap<Long,Integer>());
	    			}
	    			if (!cardsNowList.containsKey(idListTo)) {
	    				cardsNowList.put(idListTo, 0);
	    				cardForList.put(idListTo, new ArrayList<String>());
	    				numCardsForList.put(idListTo, new TreeMap<Long,Integer>());
	    			}
					cardsNowList.put(idListTo, cardsNowList.get(idListTo)+1);
					numCardsForList.get(idListTo).put(day,cardsNowList.get(idListTo));
					cardForList.get(idListTo).add(cardId);
					cardsNowList.put(idListFrom, cardsNowList.get(idListFrom)-1);
					numCardsForList.get(idListFrom).put(day,cardsNowList.get(idListFrom));
					cardForList.get(idListFrom).remove(cardId);
    			}
    		} else if (type.equals("updateCard") & action.getData().getOld()!=null) { 
    			if (action.getData().getOld().isClosed()!=action.getData().getCard().isClosed()) {
	    			if (!action.getData().getOld().isClosed()) {
		    			String cardId = action.getData().getCard().getId();
		    			String id = null;
		    			for (String listId : cardForList.keySet()) {
		    				if (cardForList.get(listId).contains(cardId)) {
		    					id = listId;
		    					break;
		    				}
		    			}
		    			cardClosed.put(cardId, id);
		    			cardsNowList.put(id, cardsNowList.get(id)-1);
						numCardsForList.get(id).put(day,cardsNowList.get(id));
						cardForList.get(id).remove(cardId);
	    			} else if (action.getData().getOld().isClosed()) {
	        			String cardId = action.getData().getCard().getId();
	        			String id = cardClosed.get(cardId);
	        			cardClosed.remove(cardId);
	    				cardsNowList.put(id, cardsNowList.get(id)+1);
	    				numCardsForList.get(id).put(day, cardsNowList.get(id));
	    				cardForList.get(id).add(cardId);
	    			}
    			}
    		} else if (type.equals("createCard") || type.equals("copyCard") || type.equals("moveCardToBoard")) {
    			String cardId = action.getData().getCard().getId();
    			String id = action.getData().getList().getId();
    			if (!cardsNowList.containsKey(id)) {
    				cardsNowList.put(id, 0);
    				cardForList.put(id, new ArrayList<String>());
    				numCardsForList.put(id, new TreeMap<Long,Integer>());
    			}
				cardsNowList.put(id, cardsNowList.get(id)+1);
				numCardsForList.get(id).put(day, cardsNowList.get(id));
				cardForList.get(id).add(cardId);
    		} else if (type.equals("createBoard")) {
    			for (TList list : listas) {
    				numCardsForList.get(list.getId()).put(day, cardsNowList.get(list.getId()));
    			}
    		}
    	}
    	List<String> listToRemove = new ArrayList<String>(numCardsForList.keySet());
    	for (String listId : listToRemove) {
    		if (!listName.containsKey(listId))
    			numCardsForList.remove(listId);
    	}
    	measure.setListName(listName);
    	measure.setTaskForList(numCardsForList);
    	repository.save(measure);
    	return measure;
    }
    
    public void refreshMeasure(String measureId) {
    	BoardMeasure measure = repository.findOne(measureId);
    	this.refreshMeasure(measure);
    }    
    
    public void refreshMeasure(BoardMeasure measure) {
    	Board board = getApi().getBoard(measure.getBoardId(), new Argument("",""));
    	List<TList> listas = getApi().getBoardLists(measure.getBoardId(), new Argument("cards","all"));
    	measure.setName(board.getName());
    	Map<String, Map<Long,Integer>> numCardsForList = new TreeMap<String,Map<Long,Integer>>();
    	Map<String,Integer> cardsNowList = new TreeMap<String,Integer>();
    	List<Action> actionsOnList = board.fetchActions(new Argument("filter","updateCard,createCard,createBoard,copyCard,moveCardToBoard,moveCardFromBoard"),new Argument("limit","1000"));
    	Collections.reverse(actionsOnList);
    	Map<String, String> listName = new TreeMap<String,String>();
    	Map<String, List<String>> cardForList = new TreeMap<String,List<String>>();
    	Map<String,String> cardClosed = new TreeMap<String,String>();
    	for (TList list : listas){
    		listName.put(list.getId(), list.getName());
    		cardsNowList.put(list.getId(), 0);
    		numCardsForList.put(list.getId(), new TreeMap<Long,Integer>());
    		cardForList.put(list.getId(), new ArrayList<String>());
    	}
    	for (Action action : actionsOnList) {
    		DateTime date = new DateTime(action.getDate());
    		Long day = new DateTime(date.getYear(),date.getMonthOfYear(),date.getDayOfMonth(),0,0).getMillis();
    		String type = action.getType();
    		if (action.getData().getListBefore()!=null) {
    			String cardId = action.getData().getCard().getId();
    			String idListFrom = action.getData().getListBefore().getId();
    			String idListTo = action.getData().getListAfter().getId();
    			if (cardForList.get(idListFrom).contains(cardId)) {
	    			if (!cardsNowList.containsKey(idListFrom)) {
	    				cardsNowList.put(idListFrom, 0);
	    				numCardsForList.put(idListFrom, new TreeMap<Long,Integer>());
	    			}
	    			if (!cardsNowList.containsKey(idListTo)) {
	    				cardsNowList.put(idListTo, 0);
	    				cardForList.put(idListTo, new ArrayList<String>());
	    				numCardsForList.put(idListTo, new TreeMap<Long,Integer>());
	    			}
					cardsNowList.put(idListTo, cardsNowList.get(idListTo)+1);
					numCardsForList.get(idListTo).put(day,cardsNowList.get(idListTo));
					cardForList.get(idListTo).add(cardId);
					cardsNowList.put(idListFrom, cardsNowList.get(idListFrom)-1);
					numCardsForList.get(idListFrom).put(day,cardsNowList.get(idListFrom));
					cardForList.get(idListFrom).remove(cardId);
    			}
    		} else if (type.equals("updateCard") & action.getData().getOld()!=null) { 
    			if (action.getData().getOld().isClosed()!=action.getData().getCard().isClosed()) {
	    			if (!action.getData().getOld().isClosed()) {
		    			String cardId = action.getData().getCard().getId();
		    			String id = null;
		    			for (String listId : cardForList.keySet()) {
		    				if (cardForList.get(listId).contains(cardId)) {
		    					id = listId;
		    					break;
		    				}
		    			}
		    			cardClosed.put(cardId, id);
		    			cardsNowList.put(id, cardsNowList.get(id)-1);
						numCardsForList.get(id).put(day,cardsNowList.get(id));
						cardForList.get(id).remove(cardId);
	    			} else if (action.getData().getOld().isClosed()) {
	        			String cardId = action.getData().getCard().getId();
	        			String id = cardClosed.get(cardId);
	        			cardClosed.remove(cardId);
	    				cardsNowList.put(id, cardsNowList.get(id)+1);
	    				numCardsForList.get(id).put(day, cardsNowList.get(id));
	    				cardForList.get(id).add(cardId);
	    			}
    			}
    		} else if (type.equals("createCard") || type.equals("copyCard") || type.equals("moveCardToBoard")) {
    			String cardId = action.getData().getCard().getId();
    			String id = action.getData().getList().getId();
    			if (!cardsNowList.containsKey(id)) {
    				cardsNowList.put(id, 0);
    				cardForList.put(id, new ArrayList<String>());
    				numCardsForList.put(id, new TreeMap<Long,Integer>());
    			}
				cardsNowList.put(id, cardsNowList.get(id)+1);
				numCardsForList.get(id).put(day, cardsNowList.get(id));
				cardForList.get(id).add(cardId);
    		} else if (type.equals("createBoard")) {
    			for (TList list : listas) {
    				numCardsForList.get(list.getId()).put(day, cardsNowList.get(list.getId()));
    			}
    		}
    	}
    	List<String> listToRemove = new ArrayList<String>(numCardsForList.keySet());
    	for (String listId : listToRemove) {
    		if (!listName.containsKey(listId))
    			numCardsForList.remove(listId);
    	}
    	measure.setListName(listName);
    	measure.setTaskForList(numCardsForList);
    	repository.save(measure);
    }    
    
    public void refreshMeasureOffline(BoardMeasure measure) {
    	Trello api = super.getApiOfMeasureUser(measure);
    	Board board = api.getBoard(measure.getBoardId(), new Argument("",""));
    	List<TList> listas = api.getBoardLists(measure.getBoardId(), new Argument("cards","all"));
    	measure.setName(board.getName());
    	Map<String, Map<Long,Integer>> numCardsForList = new TreeMap<String,Map<Long,Integer>>();
    	Map<String,Integer> cardsNowList = new TreeMap<String,Integer>();
    	List<Action> actionsOnList = board.fetchActions(new Argument("filter","updateCard,createCard,createBoard,copyCard,moveCardToBoard,moveCardFromBoard"),new Argument("limit","1000"));
    	Collections.reverse(actionsOnList);
    	Map<String, String> listName = new TreeMap<String,String>();
    	Map<String, List<String>> cardForList = new TreeMap<String,List<String>>();
    	Map<String,String> cardClosed = new TreeMap<String,String>();
    	for (TList list : listas){
    		listName.put(list.getId(), list.getName());
    		cardsNowList.put(list.getId(), 0);
    		numCardsForList.put(list.getId(), new TreeMap<Long,Integer>());
    		cardForList.put(list.getId(), new ArrayList<String>());
    	}
    	for (Action action : actionsOnList) {
    		DateTime date = new DateTime(action.getDate());
    		Long day = new DateTime(date.getYear(),date.getMonthOfYear(),date.getDayOfMonth(),0,0).getMillis();
    		String type = action.getType();
    		if (action.getData().getListBefore()!=null) {
    			String cardId = action.getData().getCard().getId();
    			String idListFrom = action.getData().getListBefore().getId();
    			String idListTo = action.getData().getListAfter().getId();
    			if (cardForList.get(idListFrom).contains(cardId)) {
	    			if (!cardsNowList.containsKey(idListFrom)) {
	    				cardsNowList.put(idListFrom, 0);
	    				numCardsForList.put(idListFrom, new TreeMap<Long,Integer>());
	    			}
	    			if (!cardsNowList.containsKey(idListTo)) {
	    				cardsNowList.put(idListTo, 0);
	    				cardForList.put(idListTo, new ArrayList<String>());
	    				numCardsForList.put(idListTo, new TreeMap<Long,Integer>());
	    			}
					cardsNowList.put(idListTo, cardsNowList.get(idListTo)+1);
					numCardsForList.get(idListTo).put(day,cardsNowList.get(idListTo));
					cardForList.get(idListTo).add(cardId);
					cardsNowList.put(idListFrom, cardsNowList.get(idListFrom)-1);
					numCardsForList.get(idListFrom).put(day,cardsNowList.get(idListFrom));
					cardForList.get(idListFrom).remove(cardId);
    			}
    		} else if (type.equals("updateCard") & action.getData().getOld()!=null) { 
    			if (action.getData().getOld().isClosed()!=action.getData().getCard().isClosed()) {
	    			if (!action.getData().getOld().isClosed()) {
		    			String cardId = action.getData().getCard().getId();
		    			String id = null;
		    			for (String listId : cardForList.keySet()) {
		    				if (cardForList.get(listId).contains(cardId)) {
		    					id = listId;
		    					break;
		    				}
		    			}
		    			cardClosed.put(cardId, id);
		    			cardsNowList.put(id, cardsNowList.get(id)-1);
						numCardsForList.get(id).put(day,cardsNowList.get(id));
						cardForList.get(id).remove(cardId);
	    			} else if (action.getData().getOld().isClosed()) {
	        			String cardId = action.getData().getCard().getId();
	        			String id = cardClosed.get(cardId);
	        			cardClosed.remove(cardId);
	    				cardsNowList.put(id, cardsNowList.get(id)+1);
	    				numCardsForList.get(id).put(day, cardsNowList.get(id));
	    				cardForList.get(id).add(cardId);
	    			}
    			}
    		} else if (type.equals("createCard") || type.equals("copyCard") || type.equals("moveCardToBoard")) {
    			String cardId = action.getData().getCard().getId();
    			String id = action.getData().getList().getId();
    			if (!cardsNowList.containsKey(id)) {
    				cardsNowList.put(id, 0);
    				cardForList.put(id, new ArrayList<String>());
    				numCardsForList.put(id, new TreeMap<Long,Integer>());
    			}
				cardsNowList.put(id, cardsNowList.get(id)+1);
				numCardsForList.get(id).put(day, cardsNowList.get(id));
				cardForList.get(id).add(cardId);
    		} else if (type.equals("createBoard")) {
    			for (TList list : listas) {
    				numCardsForList.get(list.getId()).put(day, cardsNowList.get(list.getId()));
    			}
    		}
    	}
    	List<String> listToRemove = new ArrayList<String>(numCardsForList.keySet());
    	for (String listId : listToRemove) {
    		if (!listName.containsKey(listId))
    			numCardsForList.remove(listId);
    	}
    	measure.setListName(listName);
    	measure.setTaskForList(numCardsForList);
    	repository.save(measure);
    }
    
}
