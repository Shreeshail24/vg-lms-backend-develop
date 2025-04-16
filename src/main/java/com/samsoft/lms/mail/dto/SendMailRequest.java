package com.samsoft.lms.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendMailRequest {
	private String sendTo;
	private String from;
	private String subject;
	private String mailBody;
	private String filePath;
	private String mailType;
	private String batchId;

	public SendMailRequest(String sendTo, String from, String subject, String mailBody, String filePath, String mailType) {
		this.sendTo = sendTo;
		this.from = from;
		this.subject = subject;
		this.mailBody = mailBody;
		this.filePath = filePath;
		this.mailType = mailType;
	}

	public SendMailRequest(String sendTo, String from, String subject, String mailBody, String filePath, String mailType, String batchId) {
		this.sendTo = sendTo;
		this.from = from;
		this.subject = subject;
		this.mailBody = mailBody;
		this.filePath = filePath;
		this.mailType = mailType;
		this.batchId = batchId;
	}
}
