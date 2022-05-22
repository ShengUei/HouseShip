package com.grp4.houseship.forum.model;//package com.grp4.houseship.forum.model;//package com.grp4.houseship.forum.model;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.stereotype.Component;
//
//import com.grp4.houseship.member.model.Member;
//
//@Entity
//@Table(name = "message")
//@Component
//public class Message implements Serializable {
//	
//	private static final long serialVersionUID = 1L;
//	
//	@Id 
//	@Column(name = "MID")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int  mid;
//	
//	@Transient
//	private String maccount;
//	
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@Column(name = "MPOSTTIME")
//	private LocalDateTime mpostTime;
//	
//	@Transient
//	private String mimage;
//	
//	@Column(name = "CONTENT")
//	private String content;
//	
//	@Transient
//	@Column(name = "LIKE")
//	private String like;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ACCOUNT", referencedColumnName = "ACCOUNT")
////	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//	private Member member;
//	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "fid",referencedColumnName = "FiD")
//	private Forum forum;
//	
//	public Message() {
//	}
//
//	public Message(Member member, String mimage, String content) {
//		this.member = member;
//		this.mimage = mimage;
//		this.content = content;
//	}
//
//	public int getMid() {
//		return mid;
//	}
//
//	public void setMid(int mid) {
//		this.mid = mid;
//	}
//
//	public String getMaccount() {
//		return maccount;
//	}
//
//	public void setMaccount(String maccount) {
//		this.maccount = maccount;
//	}
//
//	public LocalDateTime getMpostTime() {
//		return mpostTime;
//	}
//
//	public void setMpostTime(LocalDateTime mpostTime) {
//		this.mpostTime = mpostTime;
//	}
//
//	public String getMimage() {
//		return mimage;
//	}
//
//	public void setMimage(String mimage) {
//		this.mimage = mimage;
//	}
//
//	public String getContent() {
//		return content;
//	}
//
//	public void setContent(String content) {
//		this.content = content;
//	}
//
//	public String getLike() {
//		return like;
//	}
//
//	public void setLike(String like) {
//		this.like = like;
//	}
//
//	public Member getMember() {
//		return member;
//	}
//
//	public void setMember(Member member) {
//		this.member = member;
//	}
//	
//}
