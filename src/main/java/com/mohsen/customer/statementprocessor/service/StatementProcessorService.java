package com.mohsen.customer.statementprocessor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohsen.customer.statementprocessor.entity.CustomerRecord;
import com.mohsen.customer.statementprocessor.entity.ErrorRecord;
import com.mohsen.customer.statementprocessor.entity.StatementResponse;
import com.mohsen.customer.statementprocessor.exception.DuplicateTransactionException;
import com.mohsen.customer.statementprocessor.repository.StatementProcessorRepository;

@Service
public class StatementProcessorService {
	
	@Autowired
	private StatementProcessorRepository statementRepository;

	

	
	public boolean addRecord(CustomerRecord customerRec)  {

		return statementRepository.addRecord(customerRec);
	}
	
	
	
	public boolean checkEndingBalance(CustomerRecord rect) {
		return (rect.getEndBalance()==rect.getStartBalance()+rect.getMutation());
	}
	
	public CustomerRecord findDuplicateRef(CustomerRecord rec) {
		return statementRepository.findDuplicateRef(rec);
	}
	
	
	public StatementResponse createResponse(CustomerRecord currentRecord, 
												CustomerRecord dupRecord,
												boolean validBalance) {
		
		StatementResponse response=new StatementResponse();
		

		if(validBalance && dupRecord==null) {
			response.setResult("SUCCESSFUL");
		
		}
		else if(validBalance && dupRecord!=null) {
			response.setResult("DUPLICATE_REFERENCE");
			ErrorRecord error= new ErrorRecord(dupRecord.getTransactionRef(),dupRecord.getAccountNo());
			response.getErrorRecords().add(error);
		}
		else if(!validBalance && dupRecord==null) {
			response.setResult("INCORRECT_END_BALANCE");
			ErrorRecord error= new ErrorRecord(currentRecord.getTransactionRef(),currentRecord.getAccountNo());
			response.getErrorRecords().add(error);
		}
		else if(!validBalance && dupRecord!=null) {
			response.setResult("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE");
			ErrorRecord error= new ErrorRecord(dupRecord.getTransactionRef(),dupRecord.getAccountNo());
			response.getErrorRecords().add(error);
			error= new ErrorRecord(currentRecord.getTransactionRef(),currentRecord.getAccountNo());
			response.getErrorRecords().add(error);			
		}
		
		return response;
	}


	
	
	
	
}
