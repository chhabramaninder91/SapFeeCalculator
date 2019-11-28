package com.sapient.constants;

import java.text.SimpleDateFormat;

public interface FeeCalConstants {
	static final SimpleDateFormat MMddyyyy = new SimpleDateFormat("MM/dd/yyyy"); 
	static final String TRANSACTION_INTRADAY = "INTRADAY";
	static final String NORMAL_INTRADAY = "NORMAL";
	static final String TRANSACTION_TYPE_SELL = "SELL";
	static final String TRANSACTION_TYPE_BUY = "BUY";
	static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
	static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
	static final String CSV = "csv";
	

}
