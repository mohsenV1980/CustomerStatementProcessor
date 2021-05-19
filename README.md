# CustomerStatementProcessor
Customer Statement Processor

## Test instruction

`mvn test`
	
## Run instruction

`mvn spring-boot:run` 

server starts at default port (8080).
Make a post call to 'http://localhost:8080/` from postman(or any other way) with request type as JSON.
Here is an example valid JSON request body:

	{
    "Transaction reference":"1",
    "Account number": "TG6667xdWW",
    "Start Balance": "0.0",
    "Mutation" : "+90.0",
    "Description" : "testing",
    "End Balance" : "+90.0"
    }


## Logic

When RestController get a Post request,it retrieve the CustomerRecord from request body. After that it check different validation methods from Statement Processor Service and create a final HTTP response based on service response.

The Code consists of different Main parts:

*   **`StatementProcessorController`** class:  
	It is the RestController class and have just a method for processing post requests and return appropriate HTTP Response: `processStatement`
	
*   **`StatementProcessorService`** class:  
	It is a Service class that do the main logic of the service for checking different validation & creating the appropriate response

*   **`StatementProcessorRepository`** class:  
	It is a custom Repository class that work with data store(ArrayList for now) . it has methods for adding record and checking for duplications.
	
*   **`RestExceptionHandler`** class:  
	It is a Exception Handler Class. it has two methods:`handleHttpMessageNotReadable` for handling BAD_REQUEST and `exception` for all other exceptions.
	
Also There are 5 Other classes for Data Types:
*   **`CustomerRecord`** class:  for storing Customer Records.
*   **`ErrorRecord`** class:  for storing Different Response Errors.
*   **`StatementResponse`** class:  for final response of processed statement.
*   **`InvalidCustomerRecordException`** class:  Custom Exception for finding invalid Customer Records .

	
## Unit Tests & Integration Tests
* **`CustomerStatementProcessorLogicTests`** class: this class is for **Unit Testing**. it has 5 different tests.
* **`CustomerStatementProcessorIntegrationTests`** class: this class is for **Integration Testing**. it has 2 different tests.


 

