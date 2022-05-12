package com.grp4.houseship.forum.model;//package com.grp4.houseship.forum.model;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//import javax.persistence.Column;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Transient;
//
//import org.springframework.format.annotation.DateTimeFormat;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.grp4.houseship.member.model.Member;
//
//public class message implements Serializable {
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
//	@Column(name = "MIMAGE")
//	private String mimage;
//	
//	@Column(name = "CONTENT")
//	private String content;
//	
//	@Column(name = "LIKE")
//	private String like;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ACCOUNT", referencedColumnName = "ACCOUNT")
//	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//
//	private Member member;
//	
//	public message() {
//	
//	}
//
//	public message(Member member, String mimage, String content) {
//		this.member = member;
//		this.mimage = mimage;
//		this.content = content;
//	}
//	
//}
