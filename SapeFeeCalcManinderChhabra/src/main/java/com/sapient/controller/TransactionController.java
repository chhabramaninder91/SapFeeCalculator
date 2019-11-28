package com.sapient.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.dto.TransactionReportDto;
import com.sapient.service.TransactionReportService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/transaction")
@Slf4j
public class TransactionController {

	@Autowired
	private TransactionReportService transactionReportService;

	@GetMapping(value = "/report/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> getTransactionReport(@PathVariable String type) {
		log.info("Inside TransactionController:: getTransactionReport");
		List<TransactionReportDto> transactionDetails;
		try {
			transactionDetails = transactionReportService.getTransactionDetails(type);
			if (CollectionUtils.isEmpty(transactionDetails))
				return new ResponseEntity<>(transactionDetails, HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(transactionDetails, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error Occured !!", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
