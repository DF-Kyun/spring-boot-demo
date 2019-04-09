package com.dfkyun.kettleserver.ext.vo;

import java.io.Serializable;

/**
 * 返回消息对象.
 */
public class KettleServerResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String data; // 消息数据
	private boolean status; // 消息状态
	private String desc; // 消息描述

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
