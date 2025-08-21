package testcases;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import rest.CustomResponse;

public class TestCodeValidator {
	// Method to validate if specific keywords are used in the method's source code
	public static boolean validateTestMethodFromFile(String filePath, String methodName, List<String> keywords)
			throws IOException {
		// Read the content of the test class file
		String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
		// Extract the method body for the specified method using regex
		String methodRegex = "(public\\s+CustomResponse\\s+" + methodName + "\\s*\\(.*?\\)\\s*\\{)([\\s\\S]*?)}";
		Pattern methodPattern = Pattern.compile(methodRegex);
		Matcher methodMatcher = methodPattern.matcher(fileContent);
		if (methodMatcher.find()) {
			String methodBody = fetchBody(filePath, methodName);
			// Now we validate the method body for the required keywords
			boolean allKeywordsPresent = true;
			// Loop over the provided keywords and check if each one is present in the
			// method body
			for (String keyword : keywords) {
				Pattern keywordPattern = Pattern.compile("\\b" + keyword + "\\s*\\(");
				if (!keywordPattern.matcher(methodBody).find()) {
					System.out.println("'" + keyword + "()' is missing in the method.");
					allKeywordsPresent = false;
				}
			}
			return allKeywordsPresent;
		} else {
			System.out.println("Method " + methodName + " not found in the file.");
			return false;
		}
	}

	public static String fetchBody(String filePath, String methodName) {
		StringBuilder methodBody = new StringBuilder();
		boolean methodFound = false;
		boolean inMethodBody = false;
		int openBracesCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Check if the method is found by matching method signature
				if (line.contains("public CustomResponse " + methodName + "(")
						|| line.contains("public String " + methodName + "(")) {
					methodFound = true;
				}
				// Once the method is found, start capturing lines
				if (methodFound) {
					if (line.contains("{")) {
						inMethodBody = true;
						openBracesCount++;
					}
					// Capture the method body
					if (inMethodBody) {
						methodBody.append(line).append("\n");
					}
					// Check for closing braces to identify the end of the method
					if (line.contains("}")) {
						openBracesCount--;
						if (openBracesCount == 0) {
							break; // End of method body
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return methodBody.toString();
	}

	public static boolean validateResponseFields(String methodName, CustomResponse customResponse) {
		boolean isValid = true;

		switch (methodName) {
		case "GetHolidayData":
			List<Map<String, Object>> holidayList = customResponse.getResponse().jsonPath().getList("data");

			if (holidayList == null || holidayList.isEmpty()) {
				System.out.println("'data' section is missing or empty in the response.");
				return false;
			}

			List<String> requiredFields = List.of("id", "name", "date", "recurring", "length", "lengthName");
			Set<Object> uniqueDates = new HashSet<>();

			for (int i = 0; i < holidayList.size(); i++) {
				Map<String, Object> holiday = holidayList.get(i);
				for (String field : requiredFields) {
					if (!holiday.containsKey(field)) {
						System.out.println("❌ Missing field '" + field + "' in holiday at index " + i);
						isValid = false;
					} else {
						Object value = holiday.get(field);
						if (value == null || value.toString().trim().isEmpty()) {
							System.out.println("⚠️ Field '" + field + "' is null or empty in holiday at index " + i);
							isValid = false;
						}
					}
				}
				Object date = holiday.get("date");
				if (date != null && !uniqueDates.add(date)) {
					System.out.println("⚠️ Duplicate date found: " + date + " at index " + i);
					isValid = false;
				}
			}
			break;

		case "GetLeaveData":
			List<Map<String, Object>> LeaveList = customResponse.getResponse().jsonPath().getList("data");

			if (LeaveList == null || LeaveList.isEmpty()) {
				System.out.println("'data' section is missing or empty in the response.");
				return false;
			}

			List<String> LeaverequiredFields = List.of("id", "name", "date", "recurring", "length", "lengthName");
			Set<Object> LeaveuniqueDates = new HashSet<>();

			for (int i = 0; i < LeaveList.size(); i++) {
				Map<String, Object> holiday = LeaveList.get(i);
				for (String field : LeaverequiredFields) {
					if (!holiday.containsKey(field)) {
						System.out.println("❌ Missing field '" + field + "' in holiday at index " + i);
						isValid = false;
					} else {
						Object value = holiday.get(field);
						if (value == null || value.toString().trim().isEmpty()) {
							System.out.println("⚠️ Field '" + field + "' is null or empty in holiday at index " + i);
							isValid = false;
						}
					}
				}
				Object date = holiday.get("date");
				if (date != null && !LeaveuniqueDates.add(date)) {
					System.out.println("⚠️ Duplicate date found: " + date + " at index " + i);
					isValid = false;
				}
			}
			break;

		case "GetEmpCount":
			Object rawData = customResponse.getResponse().jsonPath().get("data");

			if (rawData == null) {
				System.out.println("'data' section is missing in the response.");
				return false;
			}

			List<String> CountFields = List.of("count");

			if (rawData instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> countData = (Map<String, Object>) rawData;

				for (String field : CountFields) {
					if (!countData.containsKey(field)) {
						System.out.println("❌ Missing field '" + field + "' in 'data' object.");
						isValid = false;
					} else {
						Object value = countData.get(field);
						if (value == null || value.toString().trim().isEmpty()) {
							System.out.println("⚠️ Field '" + field + "' is null or empty.");
							isValid = false;
						}
					}
				}
			} else {
				System.out.println("⚠️ 'data' is not a valid object (Map): " + rawData.getClass().getName());
				return false;
			}
			break;

		case "GetLeaveType":
			List<Map<String, Object>> leaveTypeList = customResponse.getResponse().jsonPath().getList("data");

			if (leaveTypeList == null || leaveTypeList.isEmpty()) {
				System.out.println("'data' section is missing or empty in the response.");
				return false;
			}
			System.out.println(leaveTypeList);
			List<String> leaveFields = List.of("id", "name", "deleted", "situational");

			for (int i = 0; i < leaveTypeList.size(); i++) {
				Map<String, Object> leaveType = leaveTypeList.get(i);
				for (String field : leaveFields) {
					if (!leaveType.containsKey(field)) {
						System.out.println("❌ Missing field '" + field + "' in leaveType at index " + i);
						isValid = false;
					} else {
						Object value = leaveType.get(field);
						if (value == null) {
							System.out.println("⚠️ Field '" + field + "' is null in leaveType at index " + i);
							isValid = false;
						} else if (value instanceof String && ((String) value).trim().isEmpty()) {
							System.out.println("⚠️ Field '" + field + "' is empty string in leaveType at index " + i);
							isValid = false;
						}
					}
				}
			}
			break;

		case "GetUsageReport":
			List<Map<String, Object>> headerList = customResponse.getResponse().jsonPath().getList("data.headers");

			if (headerList == null || headerList.isEmpty()) {
				System.out.println("❌ 'data.headers' is missing or empty in the response.");
				return false;
			}

			System.out.println("✅ Headers Found: " + headerList.size());

			List<String> usageFields = List.of("name", "prop", "size", "pin");
			// there is another key in this , sometimes it null so i skip it
			// ."cellProperties";

			for (int i = 0; i < headerList.size(); i++) {
				Map<String, Object> header = headerList.get(i);

				for (String field : usageFields) {
					if (!header.containsKey(field)) {
						System.out.printf("❌ Missing field '%s' in header at index %d%n", field, i);
						isValid = false;
					} else {
						Object value = header.get(field);

						if (value == null) {
							if (!field.equals("pin")) { // ✅ skip null check for 'pin'
								System.out.printf("⚠️ Field '%s' is null in header at index %d%n", field, i);
								isValid = false;
							}
						} else if (value instanceof String && ((String) value).trim().isEmpty()) {
							System.out.printf("⚠️ Field '%s' is empty string in header at index %d%n", field, i);
							isValid = false;
						}
					}
				}
			}
			break;

		case "PutTerminationReason":
			Map<String, Object> data = customResponse.getResponse().jsonPath().getMap("data");

			if (data == null || data.isEmpty()) {
				System.out.println("❌ 'data' is missing or empty in the response.");
				return false;
			}

			System.out.println("✅ 'data' object is present.");

			List<String> PutrequiredFields = List.of("id", "name");

			for (String field : PutrequiredFields) {
				if (!data.containsKey(field)) {
					System.out.printf("❌ Missing field '%s' in 'data'%n", field);
					isValid = false;
				} else {
					Object value = data.get(field);
					if (value == null) {
						System.out.printf("⚠️ Field '%s' is null in 'data'%n", field);
						isValid = false;
					} else if (value instanceof String && ((String) value).trim().isEmpty()) {
						System.out.printf("⚠️ Field '%s' is empty string in 'data'%n", field);
						isValid = false;
					}
				}
			}
			break;

		case "DeletePim":
			Object dataValue = customResponse.getData(); // single value extracted from ["3"]

			if (dataValue == null) {
				System.out.println("❌ 'data' value is missing in the response.");
				isValid = false;
			} else {
				System.out.println("✅ 'data' value is present: " + dataValue);

				if (dataValue instanceof String && ((String) dataValue).trim().isEmpty()) {
					System.out.println("⚠️ 'data' value is an empty string.");
					isValid = false;
				}
			}

			break;

		case "PostPimEmp":
			Map<String, Object> postPimEmpdata = customResponse.getResponse().jsonPath().getMap("data");

			if (postPimEmpdata == null || postPimEmpdata.isEmpty()) {
				System.out.println("❌ 'data' is missing or empty in the response.");
				return false;
			}

			System.out.println("✅ 'data' object is present.");

			List<String> postPimEmpRequiredData = List.of("empNumber", "lastName", "firstName", "middleName",
					"employeeId");

			for (String field : postPimEmpRequiredData) {
				if (!postPimEmpdata.containsKey(field)) {
					System.out.printf("❌ Missing field '%s' in 'data'%n", field);
					isValid = false;
				} else {
					Object value = postPimEmpdata.get(field);
					if (value == null) {
						System.out.printf("⚠️ Field '%s' is null in 'data'%n", field);
						isValid = false;
					} else if (value instanceof String && ((String) value).trim().isEmpty()) {
						System.out.printf("⚠️ Field '%s' is empty string in 'data'%n", field);
						isValid = false;
					}
				}
			}
			break;

		case "PutVimEmp":
			Map<String, Object> putPimEmpdata = customResponse.getResponse().jsonPath().getMap("data");

			if (putPimEmpdata == null || putPimEmpdata.isEmpty()) {
				System.out.println("❌ 'data' is missing or empty in the response.");
				return false;
			}

			System.out.println("✅ 'data' object is present.");

			List<String> putPimEmpRequiredData = List.of("empNumber", "lastName", "firstName");

			for (String field : putPimEmpRequiredData) {
				if (!putPimEmpdata.containsKey(field)) {
					System.out.printf("❌ Missing field '%s' in 'data'%n", field);
					isValid = false;
				} else {
					Object value = putPimEmpdata.get(field);
					if (value == null) {
						System.out.printf("⚠️ Field '%s' is null in 'data'%n", field);
						isValid = false;
					} else if (value instanceof String && ((String) value).trim().isEmpty()) {
						System.out.printf("⚠️ Field '%s' is empty string in 'data'%n", field);
						isValid = false;
					}
				}
			}
			break;

		default:
			System.out.println("Method " + methodName + " is not recognized for validation.");
			isValid = false;
			break;
		}
		return isValid;
	}
}
