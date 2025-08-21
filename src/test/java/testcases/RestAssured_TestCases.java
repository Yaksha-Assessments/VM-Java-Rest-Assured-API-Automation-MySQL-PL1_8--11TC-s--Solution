package testcases;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import coreUtilities.utils.FileOperations;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.ApiUtil;
import rest.CustomResponse;

//import rest.ApiUtil;
public class RestAssured_TestCases {
	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	private int userIdToDelete;
	private int userIdToDelete1;
	private int userId;

	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\TestData.xlsx";

	/**
	 * @BeforeClass method to perform login via Selenium and retrieve session cookie
	 *              for authenticated API calls.
	 * 
	 *              Steps: 1. Setup ChromeDriver using WebDriverManager. 2. Launch
	 *              browser and open the OrangeHRM login page. 3. Perform login with
	 *              provided username and password. 4. Wait for login to complete
	 *              and extract the 'orangehrm' session cookie. 5. Store the cookie
	 *              value to be used in API requests. 6. Quit the browser session.
	 * 
	 *              Throws: - InterruptedException if thread sleep is interrupted. -
	 *              RuntimeException if the required session cookie is not found.
	 */

	@Test(priority = 0, groups = { "PL1" }, description = "Login to OrangeHRM and retrieve session cookie")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(6000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		for (org.openqa.selenium.Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}
		RestAssured.useRelaxedHTTPSValidation();
	}

	@Test(priority = 1, groups = {
			"PL1" }, description = "1. Define the endpoint to fetch holiday data for the year 2025\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/holidays' with a valid cookie\n"
					+ "3. Validate if the implementation uses correct RestAssured steps (given, cookie, get, response)\n"
					+ "4. Print the response status code and body for verification\n"
					+ "5. Assert the status code is 200 and implementation is correct")
	public void GetHolidayData() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/holidays?fromDate=2025-01-01&toDate=2025-12-31";

		CustomResponse customResponse = apiUtil.GetHolidayData(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetHolidayData",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		// Step 2: Validate structure of response
		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetHolidayData", customResponse),
				"Response must contain all required fields (id, name, date)");

		// Step 3: Validate status code
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		// Step 5: Validate id, name, and date fields
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> itemNames = customResponse.getNames(); // name
		List<Object> itemDates = customResponse.getDates(); // date

		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(itemNames.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(itemDates.isEmpty(), "Date list should not be empty.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemNames.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemDates.get(i), "Date at index " + i + " should not be null.");
		}

		// Step 6: Print for debug
		System.out.println("Holiday API Response:");
		customResponse.getResponse().prettyPrint();
	}

	// Test Case 02

	@Test(priority = 2, groups = {
			"PL1" }, description = "1. Define the endpoint to retrieve holiday details for the year 2025\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/holidays' using a valid cookie\n"
					+ "3. Validate whether the method contains RestAssured steps like given, cookie, get, and response\n"
					+ "4. Print and verify the response status code and response body\n"
					+ "5. Assert that the response status code is 200 and implementation is as expected")

	public void GetLeaveData() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/holidays?fromDate=2025-01-01&toDate=2025-12-31";

		CustomResponse customResponse = apiUtil.GetLeaveData(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetLeaveData",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect, "GetLeaveData must be implemented using RestAssured methods only!");

		// Step 2: Validate structure of response
		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetLeaveData", customResponse),
				"Response must contain all required fields (id, name, date)");

		// Step 3: Validate status code
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		// Step 5: Validate id, name, and date fields
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> itemNames = customResponse.getNames(); // name
		List<Object> itemDates = customResponse.getDates(); // date

		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(itemNames.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(itemDates.isEmpty(), "Date list should not be empty.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemNames.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemDates.get(i), "Date at index " + i + " should not be null.");
		}

		// Step 6: Print for debug
		System.out.println("GetLeaveData API Response:");
		customResponse.getResponse().prettyPrint();
	}

	// Test Case 03
	@Test(priority = 3, groups = { "PL1" }, description = "1. Define the endpoint to get employee count\n"
			+ "2. Send a GET request to '/web/index.php/api/v2/pim/employees/count' with a valid cookie\n"
			+ "3. Validate presence of RestAssured steps: given, cookie, get, and response\n"
			+ "4. Print and verify the status code and response body\n"
			+ "5. Assert the response status code is 200 and implementation is correct")

	public void GetEmpCount() throws IOException {

		String endpoint = "/web/index.php/api/v2/pim/employees/count";
		CustomResponse customResponse = apiUtil.GetEmpCount(endpoint, cookieValue, null);
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetEmpCount",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect, "GetEmpCount must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetEmpCount", customResponse),
				"Response must contain all required fields (count)");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		int Empcount = customResponse.getEmpCount();
		System.out.println(Empcount);

		Assert.assertNotEquals(endpoint, 0, "The employee count is 0!");

//	    for (int i = 0; i < Empcount.size(); i++) {
		Assert.assertNotNull(Empcount, "Empoyee Count should not be null.");
//	    }

		System.out.println("GetEmpCount API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 4, groups = {
			"PL1" }, description = "1. Define the endpoint to retrieve all leave types with no limit\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/leave-types?limit=0' using a valid session cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct")

	public void GetLeaveType() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/leave-types?limit=0";

		CustomResponse customResponse = apiUtil.GetLeaveType(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetLeaveType",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect, "GetLeaveType must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetLeaveType", customResponse),
				"Response must contain all required fields (id, name, date)");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		List<Object> itemIds = customResponse.getIds();
		List<Object> itemNames = customResponse.getNames();

		List<Object> itemDelete = customResponse.getDeletes();
		List<Object> itemSituationals = customResponse.getSituationals();

		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(itemNames.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(itemDelete.isEmpty(), "Date list should not be empty.");
		Assert.assertFalse(itemSituationals.isEmpty(), "Date list should not be empty.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemNames.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemDelete.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemSituationals.get(i), "Name at index " + i + " should not be null.");
		}

		System.out.println("GetLeaveType API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 5, groups = {
			"PL1" }, description = "1. Define the endpoint to retrieve all leave types with no limit\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/leave-types?limit=0' using a valid session cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct")

	public void GetUsageReport() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/reports?name=my_leave_entitlements_and_usage";

		CustomResponse customResponse = apiUtil.GetUsageReport(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetUsageReport",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect,
				"GetUsageReport must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetUsageReport", customResponse),
				"Response must contain all required fields (name, prop, size, pin, cellProperties)");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		List<Object> names = customResponse.getNames();
		List<Object> props = customResponse.getProps();
		List<Object> sizes = customResponse.getSizes();
		List<Object> pins = customResponse.getPins();
		List<Object> cellProperties = customResponse.getCellProperties();

		// Assert essential lists are not empty
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(props.isEmpty(), "Prop list should not be empty.");
		Assert.assertFalse(sizes.isEmpty(), "Size list should not be empty.");
		Assert.assertFalse(cellProperties.isEmpty(), "CellProperties list should not be empty.");

		for (int i = 0; i < names.size(); i++) {
			Assert.assertNotNull(names.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(props.get(i), "Prop at index " + i + " should not be null.");
			Assert.assertNotNull(sizes.get(i), "Size at index " + i + " should not be null.");
			// Optional: Assert.assertNotNull(cellProperties.get(i), "CellProperties at
			// index " + i + " should not be null.");
		}

		// ✅ Print all pin values
		System.out.println("📌 Pin Values:");
		for (int i = 0; i < pins.size(); i++) {
			System.out.println("Pin at index " + i + ": " + pins.get(i));
		}

		System.out.println("✅ GetUsageReport API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 6, groups = { "PL1" }, description = "1. Define the endpoint to update termination reason by ID\n"
			+ "2. Prepare request body with updated name value\n"
			+ "3. Send a PUT request to '/web/index.php/api/v2/pim/termination-reasons/1' with valid cookie and JSON body\n"
			+ "4. Print the request body, status code, and response body\n"
			+ "5. Assert the response status code is 200 and validate implementation correctness")

	public void PutTerminationReason() throws Exception {

		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PutTerminationReason");

		String requestBody = "{" + "\"name\": \"" + TestData.get("name") + "\"" + "}";

		String endpoint = "/web/index.php/api/v2/pim/termination-reasons/1";

		// Step 1: Call API
		CustomResponse customResponse = apiUtil.PutTerminationReason(endpoint, cookieValue, requestBody);

		// Step 2: Validate method implementation
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"PutTerminationReason", List.of("given", "cookie", "put", "response"));
		Assert.assertTrue(isImplementationCorrect,
				"PutTerminationReason must be implemented using RestAssured methods only!");

		// Step 3: Validate response code and status
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		// Step 4: Extract and validate fields
		JsonPath jsonPath = customResponse.getResponse().jsonPath(); // ✅ correct object
		Object name = jsonPath.get("data.name");

		Assert.assertNotNull(name, "Field 'name' should not be null.");
		Assert.assertEquals(name, "abcd", "Field 'name' should match the request.");

		// Step 5: Print response
		System.out.println("PutTerminationReason API Response:");
		customResponse.getResponse().prettyPrint(); // ✅ fixed object name
	}

	@Test(priority = 7, groups = {
			"PL1" }, description = "1. Send GET request to '/web/index.php/api/v2/pim/termination-reasons' to retrieve list of termination reasons\n"
					+ "2. Extract the ID of the second termination reason from the response\n"
					+ "3. Construct DELETE request body using extracted ID\n"
					+ "4. Send DELETE request to '/web/index.php/api/v2/pim/termination-reasons' with the ID\n"
					+ "5. Print and verify request body, status code, and response body\n"
					+ "6. Assert the response status code is 200 and validate implementation correctness")

	public void DeletePim() throws IOException {
		String endpoint = "/web/index.php/api/v2/pim/employees";
		Response Iresponse = getEmpId(endpoint, cookieValue, null);

		JsonPath getResponsePath = Iresponse.jsonPath(); // ✅ Renamed
		userIdToDelete = getResponsePath.getInt("data[1].empNumber");
		System.out.println(userIdToDelete);

		String requestBody = "{\"ids\": [" + userIdToDelete + "]}";
		String deleteEndPoint = "/web/index.php/api/v2/pim/employees";

		// Step 1: Call API
		CustomResponse customResponse = apiUtil.DeletePim(deleteEndPoint, cookieValue, requestBody);

		// Step 2: Validate method implementation
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "DeletePim",
				List.of("given", "cookie", "delete", "response"));
		Assert.assertTrue(isImplementationCorrect, "DeletePim must be implemented using RestAssured methods only!");

		// Step 3: Validate response code and status
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for success.");
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");
		// Step 5: Print response for visibility
		System.out.println("DeletePim API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 8, groups = { "PL1" }, description = "1. Generate a unique employee ID dynamically\n"
			+ "2. Construct the request body with first name, middle name, last name, null picture, and unique employee ID\n"
			+ "3. Send POST request to '/web/index.php/api/v2/pim/employees' with valid session cookie\n"
			+ "4. Print and verify request body, status code, and response body\n"
			+ "5. Assert that the response status code is 200 and validate implementation correctness")

	public void PostPimEmp() throws Exception {

		String endpoint = "/web/index.php/api/v2/pim/employees";
		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PostPimEmp");

		// Fetch the employeeId from Excel and generate a unique one
		String rawEmpID = TestData.get("employeeId");
		String empID = ApiUtil.generateUniqueName(rawEmpID);

		// Handle empPicture (allowing null)
		String empPicture = TestData.get("empPicture");
		String empPictureValue = (empPicture == null || empPicture.equalsIgnoreCase("null")) ? "null"
				: "\"" + empPicture + "\"";

		// Build the request body dynamically from Excel
		String requestBody = "{" + "\"firstName\": \"" + TestData.get("firstName") + "\"," + "\"middleName\": \""
				+ TestData.get("middleName") + "\"," + "\"lastName\": \"" + TestData.get("lastName") + "\","
				+ "\"empPicture\": " + empPictureValue + "," + "\"employeeId\": \"" + empID + "\"" + "}";

		System.out.println("Request Body: " + requestBody);

		CustomResponse customResponse = apiUtil.PostPimEmp(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PostPimEmp",
				List.of("given", "cookie", "post", "response"));

		Assert.assertTrue(TestCodeValidator.validateResponseFields("PostPimEmp", customResponse),
				"Response must contain all required fields (empNumber, lastName, firstName, middleName, employeeId, terminationId)");

		System.out.println("Status Code: " + customResponse.getStatusCode());
		Assert.assertTrue(isImplementationCorrect,
				"PostPimEmp must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
		assertEquals(customResponse.getId(), empID, "The request employee ID " + empID
				+ " does not match the response employee ID " + customResponse.getId() + "!");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 9, groups = {
			"PL1" }, description = "1. Send GET request to '/web/index.php/api/v2/pim/employees' to retrieve employee data\n"
					+ "2. Extract empNumber of the second employee from the response\n"
					+ "3. Construct the PUT endpoint using the extracted empNumber\n"
					+ "4. Create request body with updated employee details (firstName, lastName, etc.)\n"
					+ "5. Send PUT request with valid session cookie and request body\n"
					+ "6. Print and verify request and response details\n"
					+ "7. Assert that the response status code is 200 and validate implementation correctness")

	public void PutVimEmp() throws Exception {
		String Getendpoint = "/web/index.php/api/v2/pim/employees";
		Response Iresponse = GetId(Getendpoint, cookieValue, null);

		JsonPath jsonPath = Iresponse.jsonPath();

		userId = jsonPath.getInt("data[1].empNumber");

		String endPoint = "/web/index.php/api/v2/pim/employees/" + userId + "/personal-details";

		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PutVimEmp");

		String firstName = TestData.get("firstName");
		String lastName = TestData.get("lastName");

		String requestBody = "{" + "\"lastName\": \"" + lastName + "\"," + "\"firstName\": \"" + firstName + "\"" + "}";

		System.out.println("this is testcase 9 endpoint " + endPoint);
		CustomResponse customResponse = apiUtil.PutVimEmp(endPoint, cookieValue, requestBody);

		System.out.println("PUT Status Code: " + customResponse.getStatusCode());

		boolean isImplementationCorrectt = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PutVimEmp",
				List.of("given", "cookie", "put", "response"));

		Assert.assertTrue(TestCodeValidator.validateResponseFields("PutVimEmp", customResponse),
				"Response must contain all required fields (empNumber, lastName, firstName)");

		// Assert that the status code is 200 or 201 (depends on API behavior)
		assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for success.");
		assertEquals(customResponse.getId(), userId, "The request employee number " + userId
				+ " does not match the response employee number " + customResponse.getId() + "!");
		assertEquals(customResponse.getName(), "achrrr",
				"The request first name \"achrrr\" does not match the response employee number "
						+ customResponse.getName() + "!");
		assertEquals(customResponse.getLastName(), "haji",
				"The request first name \"haji\" does not match the response employee number "
						+ customResponse.getLastName() + "!");
		Assert.assertTrue(isImplementationCorrectt,
				"PutVimEmp must be implementated using the Rest assured  methods only!");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 10, groups = {
			"PL1" }, description = "1. Send GET request to '/web/index.php/api/v2/pim/employees' to fetch employee data\n"
					+ "2. Extract the 'empNumber' of the second employee from the response\n"
					+ "3. Construct DELETE request body with extracted employee ID\n"
					+ "4. Send DELETE request to '/web/index.php/api/v2/pim/employees' with valid session cookie\n"
					+ "5. Print and verify request body, status code, and response body\n"
					+ "6. Assert that the response status code is 200 and validate implementation correctness")

	public void DeletePimEmp() throws IOException {
		String endpoint = "/web/index.php/api/v2/pim/employees";
		Response Iresponse = GetId(endpoint, cookieValue, null);

		JsonPath jsonPath = Iresponse.jsonPath();

		userIdToDelete1 = jsonPath.getInt("data[1].empNumber");

		System.out.println(userIdToDelete1);

		// this is used to delete the id by extract above get method.
		String deleteEndPoint = "/web/index.php/api/v2/pim/employees";
		String requestBody = "{\"ids\": [" + userIdToDelete1 + "]}";

		CustomResponse customResponse = apiUtil.DeletePim(deleteEndPoint, cookieValue, requestBody);

		System.out.println("DELETE Status Code: " + customResponse.getStatusCode());
		boolean isImplementationCorrectt = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "DeletePim",
				List.of("given", "cookie", "delete", "response"));

		Assert.assertTrue(TestCodeValidator.validateResponseFields("DeletePim", customResponse),
				"Response must contain the employee number of the deleted employee");

		// Assert that the status code is 200 or 201 (depends on API behavior)
		assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for success.");
		assertEquals(customResponse.getData(), userIdToDelete1, "The request employee number "
				+ userIdToDelete1 + " does not match the response employee number " + customResponse.getData() + "!");
		Assert.assertTrue(isImplementationCorrectt,
				"DeletePimEmp must be implementated using the Rest assured  methods only!");

	}

	// -------------------------------heperfunction------------------------

	public Response getEmpId(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		return request.get(baseUrl + endpoint).then().extract().response();
	}

	public Response GetId(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		return request.get(baseUrl + endpoint).then().extract().response();
	}

}
