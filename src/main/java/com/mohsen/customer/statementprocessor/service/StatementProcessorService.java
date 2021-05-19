package com.mohsen.customer.statementprocessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohsen.customer.statementprocessor.entity.CustomerRecord;
import com.mohsen.customer.statementprocessor.entity.ErrorRecord;
import com.mohsen.customer.statementprocessor.entity.StatementResponse;
import com.mohsen.customer.statementprocessor.exception.InvalidCustomerRecordException;
import com.mohsen.customer.statementprocessor.repository.StatementProcessorRepository;

@Service
public class StatementProcessorService {

	@Autowired
	private StatementProcessorRepository statementRepository;

	public boolean addRecord(CustomerRecord customerRec) throws InvalidCustomerRecordException {
		if (!checkValidRecord(customerRec))
			throw new InvalidCustomerRecordException();
		return statementRepository.addRecord(customerRec);
	}

	public boolean checkValidRecord(CustomerRecord rec) {
		if ((rec.getAccountNo() == null) || (rec.getTransactionRef() == null) || (rec.getStartBalance() == null)
				|| (rec.getMutation() == null) || (rec.getEndBalance() == null)) {
			return false;
		}

		return true;
	}

	public boolean checkEndingBalance(CustomerRecord rec) {
		return (rec.getEndBalance() == rec.getStartBalance() + rec.getMutation());
	}

	public CustomerRecord findDuplicateRef(CustomerRecord rec) {
		return statementRepository.findDuplicateRef(rec);
	}

	public StatementResponse createResponse(boolean isValid, CustomerRecord currentRecord, CustomerRecord dupRecord,
			boolean validBalance) {

		StatementResponse response = new StatementResponse();
		if (!isValid) {
			response.setResult("BAD_REQUEST");
		} else if (validBalance && dupRecord == null) {
			response.setResult("SUCCESSFUL");

		} else if (validBalance && dupRecord != null) {
			response.setResult("DUPLICATE_REFERENCE");
			ErrorRecord error = new ErrorRecord(dupRecord.getTransactionRef(), dupRecord.getAccountNo());
			response.getErrorRecords().add(error);
		} else if (!validBalance && dupRecord == null) {
			response.setResult("INCORRECT_END_BALANCE");
			ErrorRecord error = new ErrorRecord(currentRecord.getTransactionRef(), currentRecord.getAccountNo());
			response.getErrorRecords().add(error);
		} else if (!validBalance && dupRecord != null) {
			response.setResult("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE");
			ErrorRecord error = new ErrorRecord(dupRecord.getTransactionRef(), dupRecord.getAccountNo());
			response.getErrorRecords().add(error);
			error = new ErrorRecord(currentRecord.getTransactionRef(), currentRecord.getAccountNo());
			response.getErrorRecords().add(error);
		}

		return response;
	}

}
