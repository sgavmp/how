package com.how.tfg.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.how.tfg.data.services.TrelloService;
import com.how.tfg.social.UserService;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;

@Controller
@RequestMapping("/apps/trello")
public class TrelloController {

	private TrelloService trello;
	
	@Autowired
	public TrelloController(TrelloService trello) {
		this.trello = trello;
	}

	@ModelAttribute("boards")
	public List<Board> getAllBoards(){
		return trello.getAllBoard();
	}
	
	@RequestMapping({"","/"})
	public String boards(WebRequest request, Model model) {
		return "apps";
	}
	
	@RequestMapping("/board/{boardid}")
	public String listOfBoard(WebRequest request, Model model,@RequestParam("boardid") String boardid) {
		List<TList> listas = trello.getAllListOfBoardId(boardid);
		model.addAttribute("listas", listas);
		return "apps";
	}

}
