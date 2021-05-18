package com.cognizant.mohsen.customerstatementprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;



import com.mohsen.customer.statementprocessor.entity.CustomerRecord;
import com.mohsen.customer.statementprocessor.repository.StatementProcessorRepository;
import com.mohsen.customer.statementprocessor.service.StatementProcessorService;





class CustomerStatementProcessorServiceTests {


	
	private StatementProcessorService statementService=new StatementProcessorService();
	private StatementProcessorRepository statementRepository=new StatementProcessorRepository();
	
	
	private CustomerRecord generateRecord(Double startBalance,Double mutation, Double endBalance) {
		CustomerRecord rec= new CustomerRecord();
		rec.setStartBalance(startBalance);
		rec.setMutation(mutation);
		rec.setEndBalance(endBalance);
		rec.setAccountNo("TEST");
		rec.setTransactionRef(new BigInteger("111"));
		
				
		return rec;
		
	}
	

	private CustomerRecord generateRecord(BigInteger transactionRef) {
		CustomerRecord rec= new CustomerRecord();
		rec.setStartBalance(0.0);
		rec.setMutation(50.0);
		rec.setEndBalance(50.0);
		rec.setAccountNo("TEST");
		rec.setTransactionRef(transactionRef);
		
				
		return rec;
		
	}

	@Test
	public void testCheckEndingBalance() {
		assertTrue(statementService.checkEndingBalance(generateRecord(0.0, 50.0, 50.0) ) );
		assertTrue(statementService.checkEndingBalance(generateRecord(-10.0, 50.0, 40.0) ) );
		assertTrue(statementService.checkEndingBalance(generateRecord(0.0, 0.0, 0.0) ) );
		assertTrue(statementService.checkEndingBalance(generateRecord(-10.0, -100.0, -110.0) ) );
		assertTrue(statementService.checkEndingBalance(generateRecord(-56.0, 00.0, -56.0) ) );
		assertTrue(statementService.checkEndingBalance(generateRecord(0.0, -50.0, -50.0) ) );
		
		for(int i=0;i<100;i++) {
			double s=(Math.round(Math.random()*3-1))*Math.random()*100000;
			double m=(Math.round(Math.random()*3-1))*Math.random()*100000;
			double e=s+m;
			assertTrue(statementService.checkEndingBalance(generateRecord(s, m, e) ) );
		}
	}
	
	@Test
	public void testUniqueTransactions() {
		CustomerRecord rec=null;
		rec=generateRecord(BigInteger.valueOf(111111));
		assertEquals(null,statementRepository.findDuplicateRef(rec ) );
		statementRepository.addRecord(rec);
		assertNotEquals(null,statementRepository.findDuplicateRef(rec ) );
		
		
		List<BigInteger> list=new ArrayList<BigInteger>();
		for(int i=0;i<100;i++) {
			BigInteger t=BigInteger.valueOf(Math.round(Math.random()*Long.MAX_VALUE));
			rec=generateRecord(t);
			if(list.contains(t)) {
				assertNotEquals(null,statementRepository.findDuplicateRef(rec ) );	
			}
			else {
				assertEquals(null,statementRepository.findDuplicateRef(rec ) );	
				statementRepository.addRecord(rec);
				list.add(t);
			}		

		}	
	}
	

}
