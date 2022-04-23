package com.grp4.houseship.forum.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.grp4.houseship.forum.model.Forum;
import com.grp4.houseship.forum.model.ForumService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;

import ch.qos.logback.core.net.SyslogOutputStream;

@Controller
//@RequestMapping("/forum")
@SessionAttributes(names = { "create_forum", "forum" })
public class CRUDController {



	@Autowired
	private ForumService forumservice;

	@Autowired
	private MemberService memberService;
	
//----------------------Index_project------------------------------
	@GetMapping(path = "/index")
	public String indexActioncontroller() {
		System.out.println("index");
		return "index_project";
	}

//-----------------------CreateController--------------------------
	@GetMapping(path = "/create")
	public String createmaincontroller() {
		return "forum/ForumForm";
	}

	@PostMapping(path = "/createforumformcheck.controller")
//	@ResponseBody
		public String createForum(@RequestParam("account") String account, @RequestParam("theme") String theme,
				@RequestParam("title") String title, @RequestParam("content") String content, Model model) {
			HashMap<String, String> errors = new HashMap<String, String>();
			model.addAttribute("errors", errors);
			if (account == null || account.length() == 0) {
				errors.put("account", "格式不正確");
			}

			if (theme == null || theme.length() == 0) {
				errors.put("theme", "格式不正確");
			}
			if (title == null || title.length() == 0) {
				errors.put("title", "格式不正確");
			}
			if (content == null || content.length() == 0) {
				errors.put("content", "格式不正確");
			}
			 Member memacount = memberService.findByAccount(account);
//			Member id = new Member();
//			 id.setAccount(account);
			 
			if (memacount != null) {
				Forum forum = new Forum();
//				Member mem = new Member();

				//mem.setAccount(account);
				forum.setMember(memacount);
				forum.setTheme(theme);
				forum.setTitle(title);
				forum.setContent(content);

				System.out.println(forum);
				model.addAttribute("create_forum", forum);

				return "forum/DisplayForum";
			}
			errors.put("msg", "此表單已失效，請重新輸入");
			return "forum/ForumForm";

		}

	@PostMapping(path = "displayforum")
	public String displayForum(HttpServletRequest request) {

		Forum creatforum = (Forum) request.getSession().getAttribute("create_forum");
		forumservice.insert(creatforum);
		return "redirect:forum";
	}
//-------------------------QueryController------------------------------------------------------------------------------------------------

	@GetMapping("/querybyposttime")
	public String Querybyposttime() {
		return "forum/QueryByPosttime";
	}
//	@GetMapping("/querybyposttimeaction")
//	public List<forum>
//	
	@GetMapping(path = "/forum")
	public String querymainaction() {
		return "forum/QueryForum";
	}
	
	@PostMapping(path = "/query.controller")
	@ResponseBody
	public List<Forum> querycontroller() {
//		System.out.println(forumservice.findAll()+"this is queryall01");
		return forumservice.findAll();
//		return "forum/QueryForum";
	}
	
	@GetMapping(path = "/querybyid/{fid}")
	@ResponseBody
	public Forum queryByIdcontroller(@PathVariable("fid") Integer fid) {
		System.out.println(fid);
		return forumservice.findById(fid);
	}
//-------------------------UpdateController-----------------------------------------------------------------------------------------------

	@GetMapping(path = "/update")
	public String updatemaincontroller(@RequestParam("fid") int fid, Model model) {
		Object searchId = forumservice.findById(fid);
		model.addAttribute("forum", searchId);
		return "forum/EditForum";
	}

	@PostMapping(path = "/updateforunm.controler")
	public String updatecontroller(@RequestParam("fid") Integer fid, @RequestParam("theme") String theme,
			@RequestParam("title") String title, @RequestParam("content") String contrnt, Model model) {
		Forum forum = (Forum) model.getAttribute("forum");
		forum.setTheme(theme);
		forum.setTitle(title);
		forum.setContent(contrnt);
		forumservice.update(fid, forum);
		return "redirect:/forum";
	}
//---------------------------DeleteConreoller------------------------------------------------------------------------------------------------------
	@GetMapping(path = "/delete")
	public String deletemaincontroller(@RequestParam("fid") int fid, Model model) {
		Forum forumid = forumservice.findById(fid);
		System.out.println(fid);
		model.addAttribute("forum", forumid);
		return "forum/DeleteForum";
	}
@PostMapping(path = "/deleteforum.controller")
	public String deleteForum(@RequestParam("ForumId") int fid) {
		boolean isdelete = forumservice.delete(fid);
		System.out.println(isdelete);
		return "redirect:/forum";

	}

}
