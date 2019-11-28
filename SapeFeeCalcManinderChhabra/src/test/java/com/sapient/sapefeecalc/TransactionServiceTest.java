package com.sapient.sapefeecalc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.sapient.dto.TransactionReportDto;
import com.sapient.service.TransactionReportService;
import com.sapient.service.impl.TransactionReportServiceImpl;

@RunWith(PowerMockRunner.class)
public class TransactionServiceTest {
	
	@InjectMocks
	private TransactionReportService transactionService = new TransactionReportServiceImpl();
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		ReflectionTestUtils.setField(transactionService, "intraDayProcessingCharge", 10);
		ReflectionTestUtils.setField(transactionService, "normalHighPriorityProcessingCharge", 500);
		ReflectionTestUtils.setField(transactionService, "normalNormalPrioritySellWithdrawProcessingCharge", 100);
		ReflectionTestUtils.setField(transactionService, "normalNormalPriorityBuyDepositProcessingCharge", 50);
		ReflectionTestUtils.setField(transactionService, "filePath", "C:\\Input data fil.csv");
	}
	
	private String type = "csv";
	
	
	@Test
	public void testGetTransactionDetailsNullResponse() throws Exception {
		type = "excel";
		List<TransactionReportDto> transactionDetails = transactionService.getTransactionDetails(type);
		
		assertNull(transactionDetails);
		
	}
	
	@Test
	public void testGetTransactionDetailsProperResponse() throws Exception {
		List<TransactionReportDto> transactionDetails = transactionService.getTransactionDetails(type);
		
		assertNotNull(transactionDetails);
		
	}
}
