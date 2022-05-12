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

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grp4.houseship.member.model.Member;

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
	private String account;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "POSTTIME")
	private LocalDateTime postTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "UPDATETIME")
	private LocalDateTime updateTime;
	
	@Column(name = "IMAGE")
	private String image;

	@Column(name = "THEME")
	private String theme;
	
	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CONTENT")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT", referencedColumnName = "ACCOUNT")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

	private Member member;

	@Transient
	@Column(name = "click")
	private int click;
	@Column(name = "GRADE")
	private String grade;

	@Column(name = "REVIEW")
	private String review;

	public Forum() {
	}

	public Forum(String theme, String title, String content) {
		this.theme = theme;
		this.title = title;
		this.content = content;
	}

	public Forum(Member member, String image, String theme, String title, String content) {
		this.member = member;
		this.image = image;
		this.theme = theme;
		this.title = title;
		this.content = content;
	}

	@PrePersist
	public void prePersist() {
		this.postTime = LocalDateTime.now();

	}
//	@PrePersist
//	public void prePersistUpdate() {
//		this.updateTime = LocalDateTime.now();
//		
//	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getAccount() {
		return member.getAccount();
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public LocalDateTime getPostTime() {
		return postTime;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

}
