package com.sapient.service;

import java.util.List;

import com.sapient.dto.TransactionReportDto;

public interface TransactionReportService {

	List<TransactionReportDto> getTransactionDetails(String transactiontype) throws Exception;
}
