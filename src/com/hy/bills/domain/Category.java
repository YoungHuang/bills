package com.hy.bills.domain;

import java.util.Date;

public class Category {
	public final static int STATUS_ACTIVE = 1;
	public final static int STATUS_IDLE = 0;

	public final static int TYPE_PAY = 1;
	public final static int TYPE_INCOME = 0;

	// 类别表主键ID
	private Integer id;
	// 类别名称
	private String name;
	// 类型标记名称
	private int type = TYPE_PAY;
	// 父类型ID
	private int parentId = 0;
	// 添加日期
	private Date createDate;
	// 状态 0失效 1启用
	private int status = STATUS_ACTIVE;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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
