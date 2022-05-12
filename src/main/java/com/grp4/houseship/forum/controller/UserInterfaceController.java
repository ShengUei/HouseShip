package com.grp4.houseship.forum.controller;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.grp4.houseship.forum.model.Forum;
import com.grp4.houseship.forum.model.ForumService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;

@Controller
//@RequestMapping("/ui")
@SessionAttributes(names = { "create_forum", "forum","session_account" })
public class UserInterfaceController {

	@Autowired
	private ForumService forumservice;

	@Autowired
	private MemberService memberService;

	@GetMapping(path = "/account/forum/My/myforum")
	public String querymyforum(HttpSession session, Model model) {
		Member sessionaccount = (Member) session.getAttribute("member");
		model.addAttribute("session_account", sessionaccount);
		return "ui/forum/MyForum";
	}


//-----------------------CreateController--------------------------
	@GetMapping(path = "/forum/create")
	public String createmaincontroller(HttpSession session,Model model) {
		Member sessionaccount = (Member) session.getAttribute("member");
		System.out.println(sessionaccount);
		
		if(sessionaccount != null) {
			model.addAttribute("session_account", sessionaccount);
			System.out.println("-----------"+ sessionaccount +"---------------");
			return "ui/forum/ForumForm";			
		}else {
			return "redirect:/signinPage";
		}

	}

	@PostMapping(path = "/createforumformcheck.controller")
//	@ResponseBody
	public String createForum(@RequestParam("myfile") MultipartFile file,
			@RequestParam("theme") String theme, @RequestParam("title") String title,
			@RequestParam("content") String content, HttpSession session, Model model) throws IOException {
		HashMap<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);
		Member sessionaccount = (Member) session.getAttribute("member");
		if (sessionaccount != null) {
            Object fileName = String.format("%s\\%s.%s", "forum", Instant.now().toEpochMilli(), file.getContentType().split("/")[1]);
			System.out.println("originalFileName:" + fileName);
            String pathname = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\images\\" + fileName;

			File saveFile = new File(pathname);
			file.transferTo(saveFile);
			System.out.println(saveFile);
			

//			Member memberaccount = memberService.findByAccount(account);
				System.out.println(sessionaccount);
				Forum forum = new Forum();
				forum.setMember(sessionaccount);
				forum.setImage(pathname);
				forum.setTheme(theme);
				forum.setTitle(title);
				forum.setContent(content);
				
				model.addAttribute("create_forum", forum);
				
				System.out.println(forum);
				
				return "ui/forum/DisplayForum";
		}

		errors.put("msg", "此表單已失效，請重新輸入");
		return "ui/forum/ForumForm";

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
		return "ui/forum/QueryByPosttime";
	}

//	@GetMapping("/querybyposttimeaction")
//	public List<forum>
//	
	@GetMapping(path = "/forum")
	public String querymainaction(Model model) {
		model.addAttribute("forumList", forumservice.findAll());

		return "ui/forum/QueryForum";
	}

	@GetMapping(path = "/forum/{fid}")
	public String querybyidaction(Model model, @PathVariable("fid") Integer fid) {
		model.addAttribute("forum", forumservice.findById(fid));
		return "ui/forum/QuerybyidForum";
	}
//	@GetMapping("downloadImage/{fid}")
//	public ResponseEntity<byte[]> downloadImage(@PathVariable("id") Integer fid){
//		Forum forumid = forumservice.findById(fid);
//		
//		String image = forumid.getImage();
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.IMAGE_JPEG);

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

	@GetMapping(path = "/update/{fid}")
	public String updatemaincontroller(@PathVariable("fid") int fid, Model model) {
		Object searchId = forumservice.findById(fid);
		model.addAttribute("forum", searchId);
		System.out.println("forum");
		return "ui/forum/EditForum";
	}

	@PostMapping(path = "/updateforunm.controller/{fid}")
	public String updatecontroller(@PathVariable("fid") int fid, @RequestParam("myfile") MultipartFile file,
			@RequestParam("theme") String theme, @RequestParam("title") String title,
			@RequestParam("content") String content) throws IOException {
		Forum forumset = new Forum();
		forumset.setTheme(theme);
		forumset.setTitle(title);
		forumset.setContent(content);
		forumservice.update(fid, forumset);
		return "redirect:forum";
	}

//---------------------------DeleteConreoller------------------------------------------------------------------------------------------------------
	@GetMapping(path = "/delete/{fid}")
	public String deletemaincontroller(@PathVariable("fid") int fid, Model model) {
		Forum forumid = forumservice.findById(fid);
		System.out.println(fid);
		model.addAttribute("forum", forumid);
		return "ui/forum/DeleteForum";
	}

	@PostMapping(path = "/deleteforum.controller/{fid}")
	public String deleteForum(@PathVariable("fid") int fid) {
		boolean isdelete = forumservice.delete(fid);
		System.out.println(isdelete);
		return "redirect:forum";

	}

}
