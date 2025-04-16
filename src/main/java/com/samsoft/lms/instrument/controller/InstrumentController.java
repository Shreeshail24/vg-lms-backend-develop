package com.samsoft.lms.instrument.controller;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.core.dto.AgreementDueListDto;
import com.samsoft.lms.core.dto.AgreementFeeListDto;
import com.samsoft.lms.core.dto.ReceiptMisDto;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.instrument.dto.BatchCreateResDto;
import com.samsoft.lms.instrument.dto.BatchDetailsListDto;
import com.samsoft.lms.instrument.dto.BatchListDto;
import com.samsoft.lms.instrument.dto.FutureInstrumentsListDto;
import com.samsoft.lms.instrument.dto.UploadFileDto;
import com.samsoft.lms.instrument.entities.TrnInsBatchHdr;
import com.samsoft.lms.instrument.entities.VLmsHcInstrumentVsPayapplied;
import com.samsoft.lms.instrument.exceptions.InstrumentDataNotFoundException;
import com.samsoft.lms.instrument.repositories.TrnInsBatchHdrRepository;
import com.samsoft.lms.instrument.services.InstrumentServices;
import com.samsoft.lms.las.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/instrument")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in" , "https://qa-losone.4fin.in"}, allowedHeaders = "*")
@Slf4j
public class InstrumentController {
	@Autowired
	private Environment env;

	@Autowired
	private InstrumentServices instService;

	@Autowired
	private TrnInsBatchHdrRepository hdrRepo;

	@GetMapping(value = "/getBatchList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BatchListDto>> getBatchList(@RequestParam (required = false) String instrumentType, @RequestParam String batchStatus,
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false, defaultValue = "0") Integer pageNo,
	        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	        @RequestParam(required = false, defaultValue = "batchId") String sortBy,
	        @RequestParam(required = false, defaultValue = "asc") String sortDir)
			throws Exception {
		try {
			 Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
		     Sort.Direction.DESC : Sort.Direction.ASC;
		     Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Page<BatchListDto> pageResult ;
			if (!(fromDate == null)) {
				Date frDate = null, tillDate = null;
				frDate = sdf.parse(fromDate);
				tillDate = sdf.parse(toDate);
				  pageResult = instService.getBatchList(instrumentType, batchStatus, frDate, tillDate,pageable);
				//return 
			} else {
				//return 
			    pageResult = instService.getBatchList(instrumentType, batchStatus, null, null,pageable);
						//instService.getBatchList(instrumentType, batchStatus, null, null,pageable);
			}
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	           return ResponseEntity.ok().headers(headers).body(pageResult.getContent());

		} catch (InstrumentDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@GetMapping(value = "/getBatchDetailsList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BatchDetailsListDto>> getBatchDetailsList(@RequestParam Integer batchId,
			@RequestParam(required = false, defaultValue = "0") Integer pageNo,
	        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	        @RequestParam(required = false, defaultValue = "dtInstrumentDate") String sortBy,
	        @RequestParam(required = false, defaultValue = "asc") String sortDir)
			throws Exception {
		try {
			 Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
		     Sort.Direction.DESC : Sort.Direction.ASC;
		     Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Page<BatchDetailsListDto> pageResult ;
				  pageResult = instService.getBatchDetailList(batchId,pageable);
		
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	           return ResponseEntity.ok().headers(headers).body(pageResult.getContent());

		} catch (InstrumentDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}


	@GetMapping(value = "/getFutureDues", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AgreementDueListDto> getFutureDues(@RequestParam String fromDate, @RequestParam String toDate)
			throws Exception {
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date frDate = null, tillDate = null;
			frDate = sdf.parse(fromDate);
			tillDate = sdf.parse(toDate);
			return instService.getFutureDues(frDate, tillDate);

		} catch (InstrumentDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/getReceiptMis", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ReceiptMisDto> getReceiptMis(@RequestParam String fromDate, @RequestParam String toDate)
			throws Exception {
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date frDate = null, tillDate = null;
			frDate = sdf.parse(fromDate);
			tillDate = sdf.parse(toDate);
			return instService.getReceiptMis(frDate, tillDate);

		} catch (InstrumentDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/batchCreate")
	public ResponseEntity<String> batchCreate(@RequestParam String businessDate) throws Exception {
		BatchCreateResDto batchCreateResDto = null;
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;
			bussDate = sdf.parse(businessDate);

			batchCreateResDto = instService.batchCreate(bussDate);

		} catch (Exception e) {
			throw e;
		}

		return ResponseEntity.ok(batchCreateResDto.getBatchId());
	}

	@GetMapping(value = "/batchDownloadInCsvInServerLocation")
	public ResponseEntity<String> batchDownloadInCsvInServerLocation(@RequestParam Integer batchId,
			@RequestParam String businessDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;
			bussDate = sdf.parse(businessDate);

			result = instService.batchDownloadInCsvInServerLocation(batchId, bussDate);

		} catch (Exception e) {
			throw e;
		}

		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/batchDownloadCsv")
	public void batchDownloadInCsv(@RequestParam Integer batchId, @RequestParam String businessDate,
			HttpServletResponse servletResponse) throws Exception {
		String result = "success";
		try {

			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;
			bussDate = sdf.parse(businessDate);
			String date = businessDate.replaceAll("-", "");
			TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(batchId,
					Arrays.asList(new String[] { "O" }));
			if (batchHdr == null) {
				throw new InstrumentDataNotFoundException("Batch Id not found or Batch is closed.");
			}
			String fileName = batchHdr.getInstrumentType() + "_" + Integer.toString(batchId) + "_" + date + ".csv";
			servletResponse.setContentType("text/csv");
			servletResponse.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			instService.batchDownloadInCsv(batchId, bussDate, servletResponse.getWriter(), fileName);

		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/batchDownloadInBulk")
	public ResponseEntity<byte[]> batchDownloadInBulk(@RequestParam Integer[] arrBatchId,
			@RequestParam String businessDate, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String result = "success";
		try {

			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;
			bussDate = sdf.parse(businessDate);
			String date = businessDate.replaceAll("-", "");
			String zipFileName1 = date + ".zip";
			List<String> files = instService.batchDownloadInBulk(arrBatchId, bussDate);

			// setting headers
			response.setStatus(HttpServletResponse.SC_OK);
			response.addHeader("Content-Disposition", "attachment; filename=" + zipFileName1);
			// response.setContentType("application/zip");

			File zipFile = new File(env.getProperty("lms.batch.file.download.path") + date + ".zip");
			FileInputStream fis = new FileInputStream(zipFile);
			// byte[] content = IOUtils.toByteArray(fis);
			byte[] content = Base64Utils.encode(IOUtils.toByteArray(fis));
//			ServletOutputStream outputStream = response.getOutputStream();
//			outputStream.write(content, 0, content.length);
//			outputStream.flush();
			return ResponseEntity.ok(content);

		} catch (Exception e) {

			throw e;
		}

	}

	@GetMapping(value = "/downloadDownloadedFilesInBulk", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadDownloadedFilesInBulk(@RequestParam Integer[] arrBatchId, @RequestParam String businessDate, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "success";
		try {

			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;
			bussDate = sdf.parse(businessDate);
			String date = businessDate.replaceAll("-", "");
			String zipFileName1 = date + ".zip";
			List<String> files = instService.downloadDownloadedFilesInBulk(arrBatchId, bussDate);

			// setting headers
			response.setStatus(HttpServletResponse.SC_OK);
			response.addHeader("Content-Disposition", "attachment; filename=" + zipFileName1);
			// response.setContentType("application/zip");

			File zipFile = new File(env.getProperty("lms.batch.file.download.path") + date + ".zip");
			FileInputStream fis = new FileInputStream(zipFile);
			byte[] content = IOUtils.toByteArray(fis);
			// byte[] content = Base64Utils.encode(IOUtils.toByteArray(fis));
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content, 0, content.length);
			outputStream.flush();
			// return ResponseEntity.ok(content);

		} catch (Exception e) {

			throw e;
		}

	}

	@GetMapping(value = "/batchUploadCsvInServerLocation")
	public ResponseEntity<String> batchUploadCsvInServerLocation(@RequestParam Integer batchId,
			@RequestParam String fileName, @RequestParam String businessDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;
			bussDate = sdf.parse(businessDate);

			result = instService.batchUploadCsvInServerLocation(batchId, fileName, bussDate);

		} catch (Exception e) {
			throw e;
		}

		return ResponseEntity.ok(result);
	}

	@PostMapping(value = "/batchUploadCsv")
	public ResponseEntity<String> batchUploadCsv(@RequestBody UploadFileDto fileData) throws Exception {

		String result = "Batch uploaded successfully.";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;

//			bussDate = sdf.parse(businessDate);
			String dt = fileData.getBusinessDate();
			bussDate = sdf.parse(fileData.getBusinessDate());
			log.info("FileDat enc========>" + bussDate);
			byte[] decodedBytes = Base64.getDecoder()
					.decode(fileData.getFile().substring(fileData.getFile().indexOf(",") + 1));
			String decodedString = new String(decodedBytes);
//			bussDate = sdf.parse(businessDate);
			// byte[] decodedBytes = Base64.getDecoder()
			// .decode(fileData.getFile().substring(fileData.getFile().indexOf(",") + 1));

//			String decodedString = new String(fileData.getBytes());
			result = instService.batchUploadCsv(fileData.getBatchId(), decodedString,fileData.getFileName(), bussDate);
//			result = instService.batchUploadCsv(1, decodedString, fileData.getFileName(), bussDate);

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

		return ResponseEntity.ok(result);
	}
	
	@PostMapping(value = "/batchUploadConsolidatedCsvFile")
	public ResponseEntity<String> batchUploadConsolidatedCsvFile(@RequestBody UploadFileDto fileData) throws Exception {

		String result = "Batch uploaded successfully.";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			Date bussDate = null;

//			bussDate = sdf.parse(businessDate);
			bussDate = sdf.parse(fileData.getBusinessDate());
			log.info("FileDat enc========>" + fileData.getFile());
			byte[] decodedBytes = Base64.getDecoder()
					.decode(fileData.getFile().substring(fileData.getFile().indexOf(",") + 1));
			String decodedString = new String(decodedBytes);
			log.info("FileDat========>" + decodedString);
//			bussDate = sdf.parse(businessDate);
			// byte[] decodedBytes = Base64.getDecoder()
			// .decode(fileData.getFile().substring(fileData.getFile().indexOf(",") + 1));

//			String decodedString = new String(fileData.getBytes());

//			result = instService.batchUploadCsv(batchId, decodedString, fileName, bussDate);
			result = instService.batchUploadConsolidatedCsvFile(decodedString, fileData.getFileName(), bussDate);

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/getBatchHealthDetails")
	public ResponseEntity<List<VLmsHcInstrumentVsPayapplied>> getBatchHealthDetails(@RequestParam Integer batchId)
			throws Exception {
		try {

			List<VLmsHcInstrumentVsPayapplied> result = instService.getBatchHealthDetails(batchId);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}

	}
	
	@GetMapping(value = "/getInstrumentList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FutureInstrumentsListDto>> getInstrumentList(
			@RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate,
			@RequestParam(required = false, defaultValue = "0") Integer pageNo,
	        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	        @RequestParam(required = false, defaultValue = "dtInstrumentDate") String sortBy,
	        @RequestParam(required = false, defaultValue = "asc") String sortDir)
			throws Exception {
		try {
			 Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
		     Sort.Direction.DESC : Sort.Direction.ASC;
		     Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Page<FutureInstrumentsListDto> pageResult ;
				Date frDate = null, tillDate = null;
				frDate = sdf.parse(fromDate);
				tillDate = sdf.parse(toDate);
				  pageResult = instService.getInstrumentList(frDate, tillDate,pageable);
				//return 
			 
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	           return ResponseEntity.ok().headers(headers).body(pageResult.getContent());

		} catch (InstrumentDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

}