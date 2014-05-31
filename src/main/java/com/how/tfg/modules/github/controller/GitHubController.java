package com.how.tfg.modules.github.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.github.api.GitHubRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.how.tfg.modules.core.controller.BaseController;
import com.how.tfg.modules.github.domain.CommitHighChart;
import com.how.tfg.modules.github.domain.GithubMeasure;
import com.how.tfg.modules.github.services.GitHubService;

@Controller
@RequestMapping("/measure/github")
public class GitHubController extends BaseController {

	private GitHubService github;
	
	@Autowired
	public GitHubController(GitHubService github) {
		this.image="static/img/modules/github.jpg";
		this.nameApp="GitHub";
		this.url="https://github.com/";
		this.code="github";
		this.description="GitHub es una forja para alojar proyectos utilizando el sistema de control de versiones Git.";
		this.github = github;
	}
	
	@ModelAttribute("repos")
	public List<GitHubRepo> getAllRepo(){
		return github.getAllRepo();
	}
	
	@ModelAttribute("measuresGithub")
	public List<GithubMeasure> getMeasure(){
		return github.getAllMeasure();
	}
	
	@RequestMapping( value = "/create/repo/{repoName}", method = RequestMethod.GET)
	public String createMeasure(WebRequest request, Model model, @PathVariable("repoName") String repoName, RedirectAttributes redirectAttributes) {
		model.asMap().clear();
		if (github.notExistRepoMeasure(repoName)) {
			github.createMeasureOfRepodId(repoName);
			redirectAttributes.addFlashAttribute("info", "web.measure.create");
		}
		else {
			redirectAttributes.addFlashAttribute("error", "web.measure.exist");
		}
		return "redirect:/measure/github";
	}
	
	@RequestMapping(value = "/refresh/measure/{measureid}", method = RequestMethod.GET)
	public String refreshMeasure(WebRequest request, Model model,@PathVariable("measureid") String measureid, RedirectAttributes redirectAttributes) {
		github.refreshMeasure(measureid);
		model.asMap().clear();
		redirectAttributes.addFlashAttribute("info", "web.measure.refresh");
		return "redirect:/measure/github";
	}
	
	@RequestMapping(value = "/delete/{measureid}", method = RequestMethod.GET)
	public String deleteMeasure(WebRequest request, Model model,@PathVariable("measureid") String measureid, RedirectAttributes redirectAttributes) {
		github.deleteMeasureOfId(measureid);
		model.asMap().clear();
		redirectAttributes.addFlashAttribute("info", "web.measure.delete");
		return "redirect:/measure/github";
	}
	
	@RequestMapping(value = "/data/{measureid}/json", method = RequestMethod.GET)
	public @ResponseBody List<CommitHighChart> getDataOfMeasure(WebRequest request, Model model,@PathVariable("measureid") String measureid) {
		GithubMeasure measure = github.getMeasureById(measureid);
		CommitHighChart commit = new CommitHighChart(measure.getCommitsDay());
		return Arrays.asList(commit);
	}
	
	@RequestMapping(value={"","/"}, method = RequestMethod.GET)
	public String boards(WebRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (github.haveConnection())
			return "measure";
		redirectAttributes.addFlashAttribute("error", "web.github.noconnection");
		return "redirect:/";
	}

}
