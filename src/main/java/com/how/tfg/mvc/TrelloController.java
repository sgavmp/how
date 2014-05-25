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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.how.tfg.data.domain.trello.BoardMeasure;
import com.how.tfg.data.domain.trello.CardsOfDay;
import com.how.tfg.data.domain.trello.ListHighChart;
import com.how.tfg.data.services.TrelloService;
import com.how.tfg.social.UserService;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Action;
import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;

@Controller
@RequestMapping("/measure/trello")
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
	
	@ModelAttribute("menu")
	public String getMenuOpt(){
		return "measure";
	}
	
	@ModelAttribute("app")
	public String getAppName(){
		return "trello";
	}
	
	@ModelAttribute("measuresTrello")
	public List<BoardMeasure> getMeasures(){
		return trello.getAllMeasure();
	}
	
	@RequestMapping({"","/"})
	public String boards(WebRequest request, Model model) {
		return "measure";
	}
	
	@RequestMapping("/create/board/{boardid}")
	public String createMeasure(WebRequest request, Model model,@PathVariable("boardid") String boardId) {
		if (trello.notExistBoardMeasure(boardId))
			trello.createMeasureOfBoardId(boardId);
		return "redirect:/measure/trello";
	}
	
	@RequestMapping("/delete/{measureid}")
	public String deleteMeasure(WebRequest request, Model model,@PathVariable("measureid") String measureid) {
		trello.deleteMeasureOfId(measureid);
		return "redirect:/measure/trello";
	}
	
	@RequestMapping("/data/{measureid}/json")
	public @ResponseBody List<ListHighChart> getDataOfMeasure(WebRequest request, Model model,@PathVariable("measureid") String measureid) {
		BoardMeasure measure = trello.getBoardMeasureById(measureid);
		Map<String,List<CardsOfDay>> temp = measure.getTaskForList();
		List<ListHighChart> listas = new ArrayList<ListHighChart>();
		for (String key : temp.keySet()) {
			listas.add(new ListHighChart(measure.getListName().get(key), temp.get(key)));
		}
		return listas;
	}
	
	@RequestMapping(value="/webhook/{measureId}")
	public void getWebhookAction(@PathVariable("measureId") String measureId,@RequestParam("action") Action action) {
		
	}
}
