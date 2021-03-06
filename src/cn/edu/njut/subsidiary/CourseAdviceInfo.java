package cn.edu.njut.subsidiary;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.edu.njut.common.dao.Dao;
import cn.edu.njut.entity.Advicecomment;
import cn.edu.njut.entity.Comment;
import cn.edu.njut.entity.CourseAdvice;
import cn.edu.njut.entity.CourseTask;
import cn.edu.njut.entity.Likelist;
import cn.edu.njut.entity.SchoolCourse;

public class CourseAdviceInfo {
	private CourseAdvice courseAdvice;
	private int likenum;
	private CommentList commentlist;
	private String coursename;
	private String everlike;
	private String teacherreplied;
	
	public String getTeacherreplied() {
		return teacherreplied;
	}
	public void setTeacherreplied(String teacherreplied) {
		this.teacherreplied = teacherreplied;
	}
	public CourseAdvice getCourseAdvice() {
		return courseAdvice;
	}
	public void setCourseAdvice(CourseAdvice courseAdvice) {
		this.courseAdvice = courseAdvice;
	}
	public String getEverlike() {
		return everlike;
	}
	public void setEverlike(String everlike) {
		this.everlike = everlike;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public int getLikenum() {
		return likenum;
	}
	public void setLikenum(int likenum) {
		this.likenum = likenum;
	}
	public CommentList getCommentlist() {
		return commentlist;
	}
	public void setCommentlist(CommentList commentlist) {
		this.commentlist = commentlist;
	}
	
	public CourseAdviceInfo(CourseAdvice courseAdvice) {
		this.courseAdvice = courseAdvice;
		Dao dao = new Dao();
		int likenum = (dao.getByHql("FROM Likelist l WHERE l.infoid=" + courseAdvice.getCaid())).size();
		this.likenum = likenum;
		this.commentlist = new CommentList(courseAdvice, "CourseAdvice");
		CourseTask ct = (CourseTask) dao.getOne("FROM CourseTask ct WHERE ct.ctid=" + courseAdvice.getCtid());
		SchoolCourse sc = (SchoolCourse) dao.getOne("FROM SchoolCourse sc WHERE sc.scid=" + ct.getSchoolCourse().getScid());
		coursename = sc.getCoursename();
		
		/*
		 * * ����û��Ƿ��޹�����״̬
		 */
		
		HttpServletRequest request = ServletActionContext.getRequest();
		Cookie[] cookies = request.getCookies();
		String schoolnumber = null;
		for (int i = 0; i < cookies.length; i++)
			if (cookies[i].getName().equals("email"))
				schoolnumber = cookies[i].getValue();
		Likelist ll = (Likelist) dao.getOne("FROM Likelist ll WHERE ll.infoid=" + courseAdvice.getCaid() + " AND ll.fromuser='" + schoolnumber + "' AND ll.touser='" + courseAdvice.getSchoolnumber() + "'");
		if(ll == null)
			everlike = "false";
		else 
			everlike = "true";
		
		List teacherreply = (List<Advicecomment>) dao.getByHql("FROM Advicecomment ac WHERE ac.infoid=" + courseAdvice.getCaid() + " AND ac.ts='[T]'");
		if(teacherreply == null || teacherreply.size() == 0)
			teacherreplied = "false";
		else
			teacherreplied = "true";
	}
}
