package com.sapient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sapient.constants.FeeCalConstants;
import com.sapient.dto.TransactionDetailsDto;

public class TransactionCSVParser implements TransactionParser {

	@Override
	public List<TransactionDetailsDto> parse(File file) throws Exception {
		List<TransactionDetailsDto> transactionDetails = new ArrayList<>();
		final CSVParser parser = new CSVParserBuilder().withSeparator(',')
				.build();

		try (final CSVReader reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(file)))
				.withCSVParser(parser).build()) {
			Iterator<String[]> iterator = reader.iterator();
			while (iterator.hasNext()) {
				String[] inputStringArr = reader.readNext();
				if (inputStringArr != null) {
					TransactionDetailsDto detailsDto = new TransactionDetailsDto();
					detailsDto.setExternalTransactionId(inputStringArr[0]);
					detailsDto.setClientId(inputStringArr[1]);
					detailsDto.setSecurityId(inputStringArr[2]);
					detailsDto.setTransactionType(inputStringArr[3]);
					detailsDto.setTransactionDate(FeeCalConstants.MMddyyyy.parse(inputStringArr[4]));
					detailsDto.setMarketValue(new BigDecimal(inputStringArr[5]));
					detailsDto.setPriority(inputStringArr[6].charAt(0));
					transactionDetails.add(detailsDto);
				} else
					break;
			}
			return transactionDetails;
		}
	}

}
