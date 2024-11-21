package com.ezsign.push.vo;

public class PushVO {
	
	private String pushId;
	private String userId;
	private String deviceId;
	private String bizId;
	private String to;
	private String toType;
	private String pushType;
	private String title;
	private String body;
	private String bookDate;
	private String sendDate;
	private String insDate;
	private String updDate;
	private String readDate;
	private String readYn;
	private String nonReadPushCnt;
	private String value1;
	private String value2;
	private String messageContents;
	
	
	public String getMessageContents() {
		return messageContents;
	}

	public void setMessageContents(String messageContents) {
		this.messageContents = messageContents;
	}	
	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	
	private boolean result;
	private int resultCode;
	
	public String getPushId() {
		return pushId;
	}
	
	public void setPushId(String pushId) {
		this.pushId = pushId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getToType() {
		return toType;
	}
	
	public void setToType(String toType) {
		this.toType = toType;
	}
	
	public String getPushType() {
		return pushType;
	}
	
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getBookDate() {
		return bookDate;
	}
	
	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}
	
	public String getSendDate() {
		return sendDate;
	}
	
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	
	public String getInsDate() {
		return insDate;
	}
	
	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}
	
	public String getUpdDate() {
		return updDate;
	}
	
	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}
	
	public String getReadYn() {
		return readYn;
	}

	public void setReadYn(String readYn) {
		this.readYn = readYn;
	}

	public String getReadDate() {
		return readDate;
	}
	
	public void setReadDate(String readDate) {
		this.readDate = readDate;
	}

	public String getNonReadPushCnt() {
		return nonReadPushCnt;
	}

	public void setNonReadPushCnt(String nonReadPushCnt) {
		this.nonReadPushCnt = nonReadPushCnt;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	@Override
	public String toString() {
		return "PushVO [pushId=" + pushId + ", userId=" + userId + ", deviceId=" + deviceId + ", bizId=" + bizId
				+ ", to=" + to + ", toType=" + toType + ", pushType=" + pushType + ", title=" + title + ", body=" + body
				+ ", bookDate=" + bookDate + ", sendDate=" + sendDate + ", insDate=" + insDate + ", updDate=" + updDate
				+ ", readDate=" + readDate + ", nonReadPushCnt=" + nonReadPushCnt + "]";
	}
	
	
	
}
