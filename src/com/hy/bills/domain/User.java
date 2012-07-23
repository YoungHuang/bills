package com.hy.bills.domain;

import java.util.Date;

public class User {
	// 用户表主键ID
	private Integer id;
	// 用户名称
	private String name;
	// 添加日期
	private Date createDate = new Date();
	// 状态 0失效 1启用
	private int status = 1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
