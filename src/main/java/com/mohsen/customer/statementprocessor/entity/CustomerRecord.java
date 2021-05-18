package com.mohsen.customer.statementprocessor.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRecord {
	private BigDecimal transactionRef;
	private String accountNo;
	private Double startBalance;
	private Double mutation;
	private Double endBalance;
	private String description;
	
}
