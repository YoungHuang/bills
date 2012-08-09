package com.hy.bills.domain;

import java.util.Date;

public class AccountBook {
	public final static int STATUS_ACTIVE = 1;
	public final static int STATUS_IDLE = 0;
	
	public final static int YES_DEFAULT = 1;
	public final static int NOT_DEFAULT = 0;

	// 账本表主键ID
	private int id;
	// 账本名称
	private String name;
	// 添加日期
	private Date createDate;
	// 状态
	private int status = STATUS_ACTIVE;
	// 是否默账本
	private int isDefault = NOT_DEFAULT;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int isDefault() {
		return isDefault;
	}

	public void setDefault(int isDefault) {
		this.isDefault = isDefault;
	}
}
