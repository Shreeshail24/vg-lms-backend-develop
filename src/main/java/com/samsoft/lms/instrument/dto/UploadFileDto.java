package com.samsoft.lms.instrument.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileDto {

	private String file;
	private Integer batchId;
	private String fileName;
	private String businessDate;
}
