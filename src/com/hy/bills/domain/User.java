package com.hy.bills.domain;

import java.util.Date;

public class User {
	public final static int USER_STATUS_ACTIVE = 1;
	public final static int USER_STATUS_IDLE = 0;

	// 用户表主键ID
	private Integer id;
	// 用户名称
	private String name;
	// 状态 
	private int status = USER_STATUS_ACTIVE;
	// 添加日期
	private Date createDate;

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
