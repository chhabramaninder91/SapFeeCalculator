package com.sapient.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sapient.constants.FeeCalConstants;
import com.sapient.dto.TransactionDetailsDto;
import com.sapient.dto.TransactionReportDto;
import com.sapient.service.TransactionReportService;
import com.sapient.util.TransactionParser;
import com.sapient.util.TransactionParserFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionReportServiceImpl implements TransactionReportService {

	@Value("${input.file.path:C:\\Input data fil.csv}")
	private String filePath;
	
	@Value("${intraday.processing.charge:10}")
	private Integer intraDayProcessingCharge;
	@Value("${normal.high.priority.processing.charge:500}")
	private Integer normalHighPriorityProcessingCharge;
	@Value("${normal.normal.priority.sell.withdraw.processing.charge:100}")
	private Integer normalNormalPrioritySellWithdrawProcessingCharge;
	@Value("${normal.normal.priority.buy.deposit.processing.charge:50}")
	private Integer normalNormalPriorityBuyDepositProcessingCharge;
	
	@Override
	public List<TransactionReportDto> getTransactionDetails(String format) throws Exception {
		log.info("Inside TransactionReportServiceImpl :: getTransactionDetails");
		File inputFile = new File(filePath);
		List<TransactionDetailsDto> transactionDetails = null;
		if (FeeCalConstants.CSV.equalsIgnoreCase(format)) {
			TransactionParser parser = TransactionParserFactory.getParser(format);
			transactionDetails = parser.parse(inputFile);
			// Grouping based on ClientId, SecurityId, TransactionDate
			Function<TransactionDetailsDto, List<Object>> compositeKey = dto -> Arrays.<Object>asList(dto.getClientId(),
					dto.getSecurityId(), dto.getTransactionDate());

			Map<List<Object>, List<TransactionDetailsDto>> transactionMap = transactionDetails.stream()
					.collect(Collectors.groupingBy(compositeKey));
			List<TransactionDetailsDto> intraDayTransList = new ArrayList<>();
			List<TransactionDetailsDto> normalTransList = new ArrayList<>();
			transactionMap.forEach((key, value) -> {
				// For intraday transactions ClientId, SecurityId, TransactionDate are same
				// but transactionType will not be same hence the size should be 2 for buy and
				// sell
				if (value.size() > 1 && value.stream().map(TransactionDetailsDto::getTransactionType)
						.filter(tt -> FeeCalConstants.TRANSACTION_TYPE_SELL.equalsIgnoreCase(tt)
								|| FeeCalConstants.TRANSACTION_TYPE_BUY.equalsIgnoreCase(tt))
						.count() > 1)
					intraDayTransList.addAll(value);
				else if (value.size() == 1)
					normalTransList.addAll(value);
			});
			
			log.info("Total intraday transactions is/are {}", intraDayTransList.size());
			log.info("Total normal transactions is/are {}", normalTransList.size());

			List<TransactionReportDto> finalTransactionReportList = new ArrayList<>();
			finalTransactionReportList.addAll(generateIntradayTransactionreport(intraDayTransList));
			finalTransactionReportList.addAll(generateNormalTransactionreport(normalTransList));

			log.info("Transaction Report generated Successfully!!");
			return finalTransactionReportList;
		}
		
		log.debug("No valid format provided !!");
		
		return new ArrayList<>();
		
	}
	
	/**
	 * @param intraDayTransList
	 * @return
	 */
	private List<TransactionReportDto> generateIntradayTransactionreport(List<TransactionDetailsDto> intraDayTransList) {
		BigDecimal tenValue = new BigDecimal(intraDayProcessingCharge);
		List<TransactionReportDto> intraDayReportList = new ArrayList<>();
		generateTransactionReport(intraDayTransList, intraDayReportList, tenValue);
		return intraDayReportList;
	}
	
	/**
	 * @param normalTransList
	 * @return
	 */
	private List<TransactionReportDto> generateNormalTransactionreport(List<TransactionDetailsDto> normalTransList) {
		List<TransactionReportDto> finalNormalTransReportList = new ArrayList<>();
		List<TransactionDetailsDto> highPriorityNormalTransList = normalTransList.stream()
				.filter(e -> e.getPriority() == 'Y' || e.getPriority() == 'y').collect(Collectors.toList());
		generateTransactionReport(highPriorityNormalTransList, finalNormalTransReportList,
				new BigDecimal(normalHighPriorityProcessingCharge));
		if (CollectionUtils.isNotEmpty(highPriorityNormalTransList))
			normalTransList.removeAll(highPriorityNormalTransList);
		List<TransactionDetailsDto> sellWithdrawNormalTransList = normalTransList.stream()
				.filter(e -> FeeCalConstants.TRANSACTION_TYPE_SELL.equalsIgnoreCase(e.getTransactionType())
						|| FeeCalConstants.TRANSACTION_TYPE_WITHDRAW.equalsIgnoreCase(e.getTransactionType()))
				.collect(Collectors.toList());
		generateTransactionReport(sellWithdrawNormalTransList, finalNormalTransReportList,
				new BigDecimal(normalNormalPrioritySellWithdrawProcessingCharge));
		
		List<TransactionDetailsDto> buyDepositNormalTransList = normalTransList.stream()
				.filter(e -> (FeeCalConstants.TRANSACTION_TYPE_BUY.equalsIgnoreCase(e.getTransactionType())
						|| FeeCalConstants.TRANSACTION_TYPE_DEPOSIT.equalsIgnoreCase(e.getTransactionType())))
				.collect(Collectors.toList());
		generateTransactionReport(buyDepositNormalTransList, finalNormalTransReportList,
				new BigDecimal(normalNormalPriorityBuyDepositProcessingCharge));
		
		return finalNormalTransReportList;
		
	}
	
	/**
	 * @param inputList
	 * @param outPutList
	 * @param fee
	 */
	private void generateTransactionReport(List<TransactionDetailsDto> inputList, List<TransactionReportDto> outPutList, BigDecimal fee) {
		inputList.forEach(dto -> {
			TransactionReportDto reportDto = new TransactionReportDto();
			reportDto.setClientId(dto.getClientId());
			reportDto.setProcessingFee(fee);
			reportDto.setTransactionDate(dto.getTransactionDate());
			reportDto.setTransactionType(dto.getTransactionType());
			outPutList.add(reportDto);
		});
	}

}
