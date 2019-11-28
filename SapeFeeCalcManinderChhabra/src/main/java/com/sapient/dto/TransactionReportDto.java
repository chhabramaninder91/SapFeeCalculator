package com.sapient.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TransactionReportDto {

	String clientId;
	String transactionType;
	Date transactionDate;
	BigDecimal processingFee;
}
