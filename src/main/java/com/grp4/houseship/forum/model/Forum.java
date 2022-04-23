package com.grp4.houseship.forum.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.grp4.houseship.member.model.Member;
//import com.sun.istack.NotNull;

@Entity
@Table(name = "forum")
@Component
public class Forum implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fid;

	@Transient
//	@Column(name = "ACCOUNT")
	private String account;

//	@Temporal(TemporalType.TIMESTAMP)
//	@NotNull
	@Column(name = "POSTTIME")
	private LocalDateTime postTime;
//	@NotNull
	@Column(name = "THEME")
	private String theme;
//	@NotNull
	@Column(name = "TITLE")
	private String title;
//	@NotNull
	@Column(name = "CONTENT")
	private String content;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCOUNT")
	private Member member;

	public Forum() {
	}

	public Forum(String theme, String title, String content) {
		this.theme = theme;
		this.title = title;
		this.content = content;
	}

	public Forum(Member member, String theme, String title, String content) {
		this.member = member;
		this.theme = theme;
		this.title = title;
		this.content = content;
	}

	@PrePersist
	public void prePersist() {
		this.postTime = LocalDateTime.now();

	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public LocalDateTime getPostTime() {
		return postTime;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getAccount() {
		return member.getAccount();
	}

	public void setAccount(String account) {
		this.account = account;
	}



}
