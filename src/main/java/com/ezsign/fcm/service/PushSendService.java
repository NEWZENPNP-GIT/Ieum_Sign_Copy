package com.ezsign.fcm.service;

public interface PushSendService {
	//구글 파이어 베이스 서버 키
	public static final String SERVER_AUTH = "key=AAAAFAjRu-k:APA91bFcyqoD2BPr07raVXz8UgHpgJX2zsOlnhaJJOXRp21kOQFv3tH2oXOIjESzCxiXxsAhot-xCB-Rft7VQn2tpGD2Ksm8wv3KsvYMJKn0inaL8yx31JL5pvqZJcSqhs538HMz0Je9";
	public static final String SERVER_URL = "https://fcm.googleapis.com/fcm/send";
	public static final String CLIENT_CONT_TYPE = "application/json";
	public static final String PUSH_TITLE = "Candy";
	
	public Boolean candySendPushMessage(String bizId, String userId, String message) throws Exception;
}
