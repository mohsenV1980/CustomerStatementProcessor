package com.mohsen.customer.statementprocessor.entity;


import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRecord {
	
	@JsonProperty("Transaction reference")
	private BigInteger transactionRef;
	
	@JsonProperty("Account number")
	private String accountNo;
	
	@JsonProperty("Start Balance")
	private Double startBalance;
	
	@JsonProperty("Mutation")
	private Double mutation;
	
	@JsonProperty("End Balance")
	private Double endBalance;
	
	@JsonProperty("Description")
	private String description;
	
}
