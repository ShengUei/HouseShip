package com.grp4.houseship.forum.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.grp4.houseship.forum.model.Forum;
import com.grp4.houseship.forum.model.ForumService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;

@Controller
@RequestMapping("/admin")
@SessionAttributes(names = { "create_forum", "forum" })
public class AdminController {

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
	public String createmaincontroller(HttpSession session, Model model) {

		Member sessionaccount = (Member) session.getAttribute("member");
		if (sessionaccount != null) {
			model.addAttribute("session_account", sessionaccount);

			System.out.println(sessionaccount);
			return "admin/forum/ForumForm";
		} else {
			return "redirect:/signinPage";
		}

	}

	@PostMapping(path = "/createforumformcheck.controller")
//	@ResponseBody
	public String createForum(@RequestParam("account") String account, @RequestParam("myfile") MultipartFile file,
			@RequestParam("theme") String theme, @RequestParam("title") String title,
			@RequestParam("content") String content,HttpServletRequest request, Model model) throws IOException {
		HashMap<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);

		String fileName = file.getOriginalFilename();
		System.out.println("originalFileName:"+fileName);
		
		if(fileName ==null) {
			fileName = "noimage_s.jpg";
		}
		System.out.println(fileName);
		String saveTempFile = "C:/DataSource/SpringProject/HouseShip-integrated-v2-1.zip_expanded/HouseShip/src/main/resources/static/images/forum/";
		
//		File saveTempDirFile = new File(saveTempFile);
//		saveTempDirFile.mkdirs();
		
		String saveFilePath = saveTempFile + fileName;
		File saveFile = new File(saveFilePath);
		if(!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}
		file.transferTo(saveFile);
		System.out.println(saveFilePath);
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

		if (memacount != null) {
			Forum forum = new Forum();
			forum.setMember(memacount);
			forum.setTheme(theme);
			forum.setTitle(title);
			forum.setContent(content);

			model.addAttribute("create_forum", forum);

			return "admin/forum/DisplayForum";
		}
		errors.put("msg", "此表單已失效，請重新輸入");
		return "admin/forum/ForumForm";

	}

	@PostMapping(path = "displayforum")
	public String displayForum(HttpServletRequest request) {

		Forum creatforum = (Forum) request.getSession().getAttribute("create_forum");
		forumservice.insert(creatforum);
		return "redirect:/admin/forum";
	}
//-------------------------QueryController------------------------------------------------------------------------------------------------

	@GetMapping("/querybyposttime")
	public String Querybyposttime() {
		return "admin/forum/QueryByPosttime";
	}

//	@GetMapping("/querybyposttimeaction")
//	public List<forum>
//	
	@GetMapping(path = "/forum")
	public String querymainaction(Model model) {
		model.addAttribute("forumList", forumservice.findAll());
		return "admin/forum/QueryForum";
	}

//	@PostMapping(path = "/query.controller")
//	@ResponseBody
//	public List<Forum> querycontroller() {
////		System.out.println(forumservice.findAll()+"this is queryall01");
//		return forumservice.findAll();
////		return "forum/QueryForum";
//	}

	@GetMapping(path = "/detail/{fid}")
	@ResponseBody
	public String queryByIdcontroller(@PathVariable("fid") int fid, Model model) {
		System.out.println(fid);
		Forum findById = forumservice.findById(fid);
		model.addAttribute("fourmById", findById);

		return "redirect:admin/forum/QueryByIdForum";
	}
//-------------------------UpdateController-----------------------------------------------------------------------------------------------

	@GetMapping(path = "/update/{fid}")
	public String updatemaincontroller(@PathVariable("fid") int fid, Model model) {
		Object searchId = forumservice.findById(fid);
		model.addAttribute("forum", searchId);
		System.out.println("forum");
		return "admin/forum/EditForum";
	}

	@PostMapping(path = "/updateforum.controller/{fid}")
	public String updatecontroller(@PathVariable("fid") int fid, @RequestParam("myfile") MultipartFile file,
			@RequestParam("theme") String theme, @RequestParam("title") String title,
			@RequestParam("content") String content) throws IOException {
		Forum forumset = new Forum();
//		forumset.setImage(file.getSize());
		forumset.setTheme(theme);
		forumset.setTitle(title);
		forumset.setContent(content);
		forumservice.update(fid, forumset);
		return "redirect:/admin/forum";
	}

//---------------------------DeleteConreoller------------------------------------------------------------------------------------------------------
	@GetMapping(path = "/delete/{fid}")
	public String deletemaincontroller(@PathVariable("fid") int fid, Model model) {
		Forum forumid = forumservice.findById(fid);
		System.out.println(fid);
		model.addAttribute("forum", forumid);
		return "admin/forum/DeleteForum";
	}

	@PostMapping(path = "/deleteforum.controller/{fid}")
	public String deleteForum(@PathVariable("fid") int fid) {
		boolean isdelete = forumservice.delete(fid);
		System.out.println(isdelete);
		return "redirect:/admin/forum";

	}

}
