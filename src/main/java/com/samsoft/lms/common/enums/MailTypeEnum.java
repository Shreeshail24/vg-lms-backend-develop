package com.samsoft.lms.common.enums;

public enum MailTypeEnum {
	TRAN_ERROR("Transfer error mail"),
	SAN_MAIL("Sanction mail "),
	DISB_MAIL("Disb mail"),
	UNAUTHORIZED_USER_MAIL("unauthorized user"),
	LAS_MAIL("Las Mail"),
	BATCH_CREATE("Batch Create");

	private String mailType;

	public String getMailType() {
		return mailType;
	}

	MailTypeEnum(String message) {
		this.mailType = message;
	}
}
