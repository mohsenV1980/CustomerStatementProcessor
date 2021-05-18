package com.mohsen.customer.statementprocessor.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mohsen.customer.statementprocessor.entity.CustomerRecord;
import com.mohsen.customer.statementprocessor.entity.ErrorRecord;
import com.mohsen.customer.statementprocessor.entity.StatementResponse;
import com.mohsen.customer.statementprocessor.exception.DuplicateTransactionException;
import com.mohsen.customer.statementprocessor.repository.StatementProcessorRepository;
import com.mohsen.customer.statementprocessor.service.StatementProcessorService;

@RestController
public class StatementProcessorController {

	@Autowired
	private StatementProcessorService statementService;
	
	
	@PostMapping("/")
	public ResponseEntity<StatementResponse> processStatement(@RequestBody CustomerRecord customerRec) {
		
		boolean validBalance=statementService.checkEndingBalance(customerRec);	
		CustomerRecord dupRec= statementService.findDuplicateRef(customerRec);
	
		if(validBalance && dupRec==null)
				statementService.addRecord(customerRec);
		
		StatementResponse response=statementService.createResponse(customerRec, dupRec, validBalance);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

	


	  
	
	
}
