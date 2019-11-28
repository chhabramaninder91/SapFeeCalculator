package com.sapient.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TransactionDetailsDto {

	private String externalTransactionId;
	private String clientId;
	private String securityId;
	private String transactionType;
	private Date transactionDate;
	private BigDecimal marketValue;
	// Y or N
	private Character priority;
	
}

