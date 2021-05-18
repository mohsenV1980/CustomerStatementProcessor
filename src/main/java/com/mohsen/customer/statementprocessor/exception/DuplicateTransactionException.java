package com.mohsen.customer.statementprocessor.exception;

import com.mohsen.customer.statementprocessor.entity.CustomerRecord;

import lombok.Data;

@Data
public class DuplicateTransactionException extends Exception {
	private CustomerRecord duplicateRecord;
	public DuplicateTransactionException(CustomerRecord record) {
		this.duplicateRecord=record;
		
	}
	

}
