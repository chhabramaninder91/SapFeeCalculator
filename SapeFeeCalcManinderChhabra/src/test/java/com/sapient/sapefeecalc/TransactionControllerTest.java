package com.sapient.sapefeecalc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import com.sapient.controller.TransactionController;
import com.sapient.dto.TransactionReportDto;
import com.sapient.service.TransactionReportService;

@RunWith(PowerMockRunner.class)
public class TransactionControllerTest {

	@Mock
	private TransactionReportService transactionService;

	@InjectMocks
	private TransactionController controller;
	
	private MockMvc mockMvc;
	
	@Mock
	private Errors err;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
		this.mockMvc = standaloneSetup(controller).build();
	}
	
	@Test
	public void getTransactionReportWithSuccess() throws Exception {
		List<TransactionReportDto> reportList = new ArrayList<>();
		TransactionReportDto dto = new TransactionReportDto();
		dto.setClientId("ABC");
		dto.setProcessingFee(new BigDecimal(100));
		dto.setTransactionDate(new Date());
		dto.setTransactionType("SELL");
		reportList.add(dto);
		when(transactionService.getTransactionDetails("csv")).thenReturn(reportList);
		this.mockMvc.perform(get("/api/transaction/report/csv").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		verify(transactionService, times(1)).getTransactionDetails(anyString());
	}
	
	@Test
	public void getTransactionReportWithBadRequest() throws Exception {
		List<TransactionReportDto> reportList = new ArrayList<>();
		
		when(transactionService.getTransactionDetails("csv")).thenReturn(reportList);
		this.mockMvc.perform(get("/api/transaction/report/csv").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
		verify(transactionService, times(1)).getTransactionDetails(anyString());
	}
	
	@Test
	public void getTransactionReportWithError() throws Exception {
		List<TransactionReportDto> reportList = new ArrayList<>();
		TransactionReportDto dto = new TransactionReportDto();
		dto.setClientId("ABC");
		dto.setProcessingFee(new BigDecimal(100));
		dto.setTransactionDate(new Date());
		dto.setTransactionType("SELL");
		reportList.add(dto);
		when(transactionService.getTransactionDetails("csv")).thenThrow(new Exception());
		this.mockMvc.perform(get("/api/transaction/report/csv").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isInternalServerError());
		verify(transactionService, times(1)).getTransactionDetails(anyString());
	}

}
