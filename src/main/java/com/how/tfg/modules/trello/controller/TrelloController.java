package com.how.tfg.modules.trello.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.how.tfg.modules.core.controller.BaseController;
import com.how.tfg.modules.trello.domain.BoardMeasure;
import com.how.tfg.modules.trello.domain.ListHighChart;
import com.how.tfg.modules.trello.services.TrelloService;
import com.julienvey.trello.domain.Board;

@Controller
@RequestMapping("/measure/trello")
public class TrelloController extends BaseController {

	private TrelloService trello;
	
	@Autowired
	public TrelloController(TrelloService trello) {
		this.image="static/img/modules/trello.png";
		this.nameApp="Trello";
		this.url="https://trello.com/";
		this.code="trello";
		this.description="Trello es una aplicacion web que te permite gestionar proyectos y tareas con el metodo Kanban.";
		this.trello = trello;
	}

	@ModelAttribute("boards")
	public List<Board> getAllBoards(){
		return trello.getAllBoard();
	}
	
	@ModelAttribute("measuresTrello")
	public List<BoardMeasure> getMeasures(){
		return trello.getAllMeasure();
	}
	
	@RequestMapping({"","/"})
	public String boards(WebRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (trello.haveConnection())
			return "measure";
		redirectAttributes.addFlashAttribute("error", "web.trello.noconnection");
		return "redirect:/";
	}
	
	@RequestMapping("/create/board/{boardid}")
	public String createMeasure(WebRequest request, Model model,@PathVariable("boardid") String boardId, RedirectAttributes redirectAttributes) {
		model.asMap().clear();
		if (trello.notExistBoardMeasure(boardId)) {
			trello.createMeasureOfBoardId(boardId);
			redirectAttributes.addFlashAttribute("info", "web.measure.create");
		}
		else {
			redirectAttributes.addFlashAttribute("error", "web.measure.exist");
		}
		return "redirect:/measure/trello";
	}
	
	@RequestMapping("/refresh/measure/{measureId}")
	public String refreshMeasure(WebRequest request, Model model,@PathVariable("measureId") String measureId, RedirectAttributes redirectAttributes) {
		trello.refreshMeasure(measureId);
		model.asMap().clear();
		redirectAttributes.addFlashAttribute("info", "web.measure.refresh");
		return "redirect:/measure/trello";
	}
	
	@RequestMapping("/delete/{measureid}")
	public String deleteMeasure(WebRequest request, Model model,@PathVariable("measureid") String measureid, RedirectAttributes redirectAttributes) {
		trello.deleteMeasureOfId(measureid);
		model.asMap().clear();
		redirectAttributes.addFlashAttribute("info", "web.measure.delete");
		return "redirect:/measure/trello";
	}
	
	@RequestMapping("/data/{measureid}/json")
	public @ResponseBody List<ListHighChart> getDataOfMeasure(WebRequest request, Model model,@PathVariable("measureid") String measureid) {
		BoardMeasure measure = trello.getMeasureById(measureid);
		Map<String,Map<Long,Integer>> temp = measure.getTaskForList();
		List<ListHighChart> listas = new ArrayList<ListHighChart>();
		for (String key : temp.keySet()) {
			listas.add(new ListHighChart(measure.getListName().get(key), temp.get(key)));
		}
		return listas;
	}

}
