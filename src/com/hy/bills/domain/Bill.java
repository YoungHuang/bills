package com.hy.bills.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Bill {
	public final static int STATUS_ACTIVE = 1;
	public final static int STATUS_IDLE = 0;
	
	// 支出表主键ID
	private Integer id;
	// 账本ID外键
	private Integer accountBookId;
	// 账本名称
	private String accountBookName;
	// 支出类别ID外键
	private Integer categoryId;
	// 类别名称
	private String categoryName;
	// 付款方式ID外键
	private Integer billWayId;
	// 消费地点ID外键
	private Integer addressId;
	// 消费金额
	private BigDecimal amount;
	// 消费日期
	private Date billDate;
	// 计算方式
	private String billType;
	// 消费人ID外键
	private String userIds;
	// 备注
	private String comment;
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

	public int getAccountBookId() {
		return accountBookId;
	}

	public void setAccountBookId(int accountBookId) {
		this.accountBookId = accountBookId;
	}

	public String getAccountBookName() {
		return accountBookName;
	}

	public void setAccountBookName(String accountBookName) {
		this.accountBookName = accountBookName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getBillWayId() {
		return billWayId;
	}

	public void setBillWayId(Integer billWayId) {
		this.billWayId = billWayId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
