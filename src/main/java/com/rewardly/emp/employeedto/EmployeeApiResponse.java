package com.rewardly.emp.employeedto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Success = True/False
 * Data
 * Message 
 * Path
 * StatusCode
 * TimeStamp 
 */

/*
 * A standard backend API response (for RESTful APIs) usually follows a consistent JSON structure that helps clients handle success, errors, and metadata easily. Here’s a clean, industry-standard format:

✅ Successful Response Example
{
  "status": "success",
  "code": 200,
  "message": "Employee fetched successfully",
  "data": {
    "id": 101,
    "name": "Amol Gadkari",
    "designation": "Manager",
    "salary": 85000
  },
  "timestamp": "2025-11-04T10:20:35Z"
}

⚠️ Error Response Example
{
  "status": "error",
  "code": 400,
  "message": "Invalid employee ID",
  "errors": [
    {
      "field": "id",
      "issue": "Must be a positive number"
    }
  ],
  "timestamp": "2025-11-04T10:20:35Z"
}

🔧 Standard Fields
Field	Type	Description
status	String	"success" or "error"
code	Number	HTTP status code (200, 400, 404, etc.)
message	String	Summary of result
data	Object / Array	Actual payload (if success)
errors	Array	Validation or server errors (if failure)
timestamp	String	ISO 8601 timestamp of the response
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeApiResponse <T> {
	
	private boolean success;
	private T data;
	private String message;
	private String path;
	private int statusCode;
	
	@Builder.Default
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")	
	private LocalDateTime timeStamp = LocalDateTime.now();	

}
