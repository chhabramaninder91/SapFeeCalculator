package com.sapient.util;

import java.io.File;
import java.util.List;

import com.sapient.dto.TransactionDetailsDto;

public interface TransactionParser {
	
	List<TransactionDetailsDto> parse(File file) throws Exception;

}
