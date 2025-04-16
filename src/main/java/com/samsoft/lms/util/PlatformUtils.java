package com.samsoft.lms.util;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.samsoft.lms.common.enums.MailTypeEnum;
import com.samsoft.lms.config.MailConfiguration;
import com.samsoft.lms.instrument.dto.BatchCreateResDto;
import com.samsoft.lms.mail.dto.SendMailRequest;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;

@Service
@Slf4j
public class PlatformUtils {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${lms-token}")
	private String lmsToken;

	@Autowired
	private MailConfiguration configuration;

	@Value("${lms.batch.create.mailcc}")
	private String batchCreateMailCC;
	
	public Boolean sendUnauthorizedUserMail(String email,String ipAddress) {
		try {
			if (email.equals("")) {
				log.info("Mail Stopped...."+email);
				return Boolean.FALSE;
			} else {
				log.info("inside Mail Stopped...."+email);
				StringBuffer htmlString = new StringBuffer();

				htmlString.append("<b>Dear Sir,</b>");
				htmlString.append("<br></br>");

				htmlString.append("<p style='color:red'>The Below user trying to login in credit portal.<br><b>"
						+ email + "</b> </p>");
				htmlString.append("<br><br><p style='color:red'>Ip Address<br><b>"
						+ ipAddress + "</b> </p>");
				htmlString.append("<br></br>");

				htmlString.append("<b>Regards,</b>");
				htmlString.append("<br></br>");
				htmlString.append("<b>www.4fin.in</b>");
				log.info("Sending email for unauthorized user!!!"+email+"\n ipAddress=====>"+ipAddress);
//				this.sendCommunicationMail(new SendMailRequest("rahul.suryawanshi@4fin.in", "contact@4fin.in",
//						"Unauthorized User" , htmlString.toString(), null,
//						MailTypeEnum.UNAUTHORIZED_USER_MAIL.getMailType()));
				this.sendCommunicationMail(new SendMailRequest("abhilash@4fin.in", "contact@4fin.in",
						"Unauthorized User" , htmlString.toString(), null,
						MailTypeEnum.UNAUTHORIZED_USER_MAIL.getMailType()));
				log.info("Mail sent");
//				communicationLogService.saveCommunicationLogs(new CommunicationLogsRequest(Integer.parseInt(leadNo),
//						null, 'E', htmlString.toString(), unAuthorizedUserMailToSend));
				return Boolean.TRUE;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name : sendUnauthorizedUserMail");
			log.error("Request email : " + email);
			log.error("Exception : " + e);
		}

		return Boolean.FALSE;
	}

	public Boolean sendLasMail(String email, String message, String subject, String mastAgrId) {
		try {
			if (email.equals("")) {
				log.info("Mail Stopped...."+email);
				return Boolean.FALSE;
			} else {
				log.info("inside Mail Stopped...."+email);
				StringBuffer htmlString = new StringBuffer();

				htmlString.append("<b>Dear Account holder,</b>");
				htmlString.append("<br></br>");

				htmlString.append("<p>"+message+"</p>");
				htmlString.append("<br></br>");
				htmlString.append("<div style='text-align: center;'><a href='https://qa-lms.4fin.in/lasprocess/"+lmsToken+"/"+mastAgrId+"' style='color: white; background-color: blue; padding: 12px 28px; border-radius: 10px;'>Proceed</a></div>");
				htmlString.append("<br></br>");

				htmlString.append("<b>Regards,</b>");
				htmlString.append("<br></br>");
				htmlString.append("<b>www.4fin.in</b>");
				this.sendCommunicationMail(new SendMailRequest(email, "contact@4fin.in",
						subject, htmlString.toString(), null,
						MailTypeEnum.LAS_MAIL.getMailType()));
				log.info("Mail sent");

				return Boolean.TRUE;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name : sendLasMail");
			log.error("Request email : " + email);
			log.error("Exception : " + e);
		}

		return Boolean.FALSE;
	}

	public Boolean sendBatchCreateMail(String email, BatchCreateResDto batchCreateResDto, String filePath) {
		try {
			if (email.equals("")) {
				log.info("Mail Stopped...."+email);
				return Boolean.FALSE;
			} else {
				log.info("inside Mail Stopped...."+email);
				StringBuffer htmlString = new StringBuffer();

				htmlString.append("<b>Dear Team,</b>");
				htmlString.append("<br></br>");
				htmlString.append("<p>Please find Batch No. "+ batchCreateResDto.getBatchId() +" with a list of cases whose NACH presentation is due on "+ batchCreateResDto.getInstrumentDate() +".</p>");
				htmlString.append("<p>Requesting you to kindly inform the customer accordingly and make the presentation of the same.</p>");
				htmlString.append("<br></br>");
				htmlString.append("<b>Regards,</b>");
				htmlString.append("<br></br>");
				htmlString.append("<b>Team 4Fin Tech</b>");

				this.sendCommunicationMail(new SendMailRequest(email, "contact@4fin.tech",
						"Batch No. "+ batchCreateResDto.getBatchId() +" for NACH Presentation on "+ batchCreateResDto.getInstrumentDate() +" - Reminder", htmlString.toString(), filePath,
						MailTypeEnum.BATCH_CREATE.getMailType(), batchCreateResDto.getBatchId()));
				log.info("Mail sent");

				return Boolean.TRUE;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name : sendBatchCreateMail");
			log.error("Request email : " + email);
			log.error("Exception : " + e);
		}

		return Boolean.FALSE;
	}

	public Boolean sendCommunicationMail(SendMailRequest mailRequest) {
		try {
			log.info("\n\n\n Mail request" + mailRequest);
			if (mailRequest.getMailType().equalsIgnoreCase(MailTypeEnum.UNAUTHORIZED_USER_MAIL.getMailType())) {
//				javaMailSender = configuration.getErrorMailSender();
				MimeMessage mMimemsg = javaMailSender.createMimeMessage();
				MimeMessageHelper mMimemsgHelper = new MimeMessageHelper(mMimemsg, true);
				mMimemsgHelper.setFrom(mailRequest.getFrom());
				mMimemsgHelper.setSubject(mailRequest.getSubject());
				mMimemsgHelper.setText(mailRequest.getMailBody(), true);
				mMimemsgHelper.setTo(InternetAddress.parse(mailRequest.getSendTo()));
				javaMailSender.send(mMimemsg);

				return Boolean.TRUE;
			} else if (mailRequest.getMailType().equalsIgnoreCase(MailTypeEnum.LAS_MAIL.getMailType())) {
				MimeMessage mMimemsg = javaMailSender.createMimeMessage();
				MimeMessageHelper mMimemsgHelper = new MimeMessageHelper(mMimemsg, true);
				mMimemsgHelper.setFrom(mailRequest.getFrom());
				mMimemsgHelper.setSubject(mailRequest.getSubject());
				mMimemsgHelper.setText(mailRequest.getMailBody(), true);
				mMimemsgHelper.setTo(InternetAddress.parse(mailRequest.getSendTo()));

				javaMailSender.send(mMimemsg);
        
				return Boolean.TRUE;
			} else if (mailRequest.getMailType().equalsIgnoreCase(MailTypeEnum.BATCH_CREATE.getMailType())) {
				javaMailSender = configuration.getFourFinTechMailSender();
				MimeMessage mMimemsg = javaMailSender.createMimeMessage();
				MimeMessageHelper mMimemsgHelper = new MimeMessageHelper(mMimemsg, true);
				mMimemsgHelper.setFrom(mailRequest.getFrom());
				mMimemsgHelper.setSubject(mailRequest.getSubject());
				mMimemsgHelper.setText(mailRequest.getMailBody(), true);

				if (mailRequest.getFilePath() != null) {
					mMimemsgHelper.addAttachment("ENACH_" + mailRequest.getBatchId() + ".csv", new File(mailRequest.getFilePath()));
				}
				mMimemsgHelper.setTo(InternetAddress.parse(mailRequest.getSendTo()));

				mMimemsgHelper.setBcc(batchCreateMailCC);
				javaMailSender.send(mMimemsg);

				return Boolean.TRUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name : sendCommunicationMail");
			log.error("Request object : " + mailRequest);
			log.error("Exception : " + e);
		}
		return Boolean.FALSE;
	}
	

}
