package com.sapient.util;

public class TransactionParserFactory {

	/**
	 * @param type
	 * @return
	 */
	public static TransactionParser getParser(String type) {
		// Default Parser is CSV for now
		return new TransactionCSVParser();
	}

}
