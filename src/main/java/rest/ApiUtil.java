package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtil {
	private static final Set<Integer> usedNumbers = new HashSet<>();
	private static final Random random = new Random();
	private static String BASE_URL;
	Properties prop;

	/**
	 * Retrieves the base URL from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and extracts the
	 * value associated with the key <code>base.url</code>. The value is stored in
	 * the static variable <code>BASE_URL</code> and returned.
	 *
	 * @return the base URL string if successfully read from the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the username from the configuration properties file.
	 *
	 * <p>
	 * This method reads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>username</code>.
	 *
	 * @return the username as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the password from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>password</code>.
	 *
	 * @return the password as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public static String generateUniqueName(String base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	public static int generateUniqueID(int base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	/**
	 * Sends a GET request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method uses RestAssured to construct and send a GET request to the given
	 * endpoint. It attaches a cookie named <code>orangehrm</code> with the provided
	 * value and sets the <code>Content-Type</code> header to
	 * <code>application/json</code>. If a request body is provided, it is included
	 * in the request.
	 *
	 * @param endpoint    the API endpoint to send the request to (relative to the
	 *                    base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        a map containing key-value pairs to be sent as the JSON
	 *                    request body (can be null)
	 * @return the response received from the GET request
	 */
	public CustomResponse GetHolidayData(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		List<Object> recurrings = new ArrayList<>();
		List<Object> lengths = new ArrayList<>();
		List<Object> lengthNames = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				ids.add(holiday.get("id"));
				names.add(holiday.get("name"));
				dates.add(holiday.get("date"));
				recurrings.add(holiday.get("recurring"));
				lengths.add(holiday.get("length"));
				lengthNames.add(holiday.get("lengthName"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, dates, recurrings, lengths, lengthNames);

	}

	/**
	 * Sends a GET request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, attaching a cookie
	 * named <code>orangehrm</code> with the specified value and setting the
	 * <code>Content-Type</code> header to <code>application/json</code>. If a body
	 * is provided, it is included in the request.
	 *
	 * @param endpoint    the relative URL endpoint to send the GET request to
	 * @param cookieValue the value for the <code>orangehrm</code> cookie
	 * @param body        a map of key-value pairs representing the request body
	 *                    (optional, may be null)
	 * @return the response received from the GET request
	 */
	public CustomResponse GetLeaveData(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		List<Object> recurrings = new ArrayList<>();
		List<Object> lengths = new ArrayList<>();
		List<Object> lengthNames = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> Leave : data) {
				ids.add(Leave.get("id"));
				names.add(Leave.get("name"));
				dates.add(Leave.get("date"));
				recurrings.add(Leave.get("recurring"));
				lengths.add(Leave.get("length"));
				lengthNames.add(Leave.get("lengthName"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, dates, recurrings, lengths, lengthNames);
	}

	/**
	 * Executes a GET request to the given endpoint using a specified cookie and
	 * optional request body.
	 *
	 * <p>
	 * This method prepares a GET request with RestAssured, attaching a cookie named
	 * <code>orangehrm</code> with the provided value and setting the request header
	 * <code>Content-Type</code> to <code>application/json</code>. If the request
	 * body is not null, it is included in the request payload.
	 *
	 * @param endpoint    the API endpoint to target, appended to the base URL
	 * @param cookieValue the value for the <code>orangehrm</code> cookie
	 * @param body        a map containing key-value pairs to be used as the request
	 *                    body (optional)
	 * @return the response object resulting from the GET request
	 */

	public CustomResponse GetEmpCount(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		System.out.println("Raw response:");
		response.prettyPrint(); // ✅ Debug print

		JsonPath jsonPath = response.jsonPath();

		// ✅ Get the "count" directly from the "data" object
		Map<String, Object> data = jsonPath.getMap("data");
		int count = 0;

		count = (int) data.get("count");
		System.out.println("⚠️ 'count' key is missing inside 'data'.");

		return new CustomResponse(response, statusCode, status, count); // ⬅️ return single count
	}

	/**
	 * Sends a DELETE request to the specified API endpoint with an authentication
	 * cookie and optional body.
	 *
	 * <p>
	 * This method utilizes RestAssured to perform a GET request to the provided
	 * endpoint. It includes a cookie named <code>orangehrm</code> with the
	 * specified value and sets the <code>Content-Type</code> to
	 * <code>application/json</code>. If a request body is provided, it is attached
	 * to the request.
	 *
	 * @param endpoint    the relative endpoint path to be appended to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie for session
	 *                    handling or authentication
	 * @param body        a map containing request body parameters (optional; may be
	 *                    null)
	 * @return the {@code Response} object containing the results of the DELETE request
	 */
	public CustomResponse DeletePim(String deleteEndPoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.delete(BASE_URL + deleteEndPoint).then().extract().response();
		System.out.println(response);

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		// Extract single data value (e.g., "3" from ["3"])
		List<Object> dataList = jsonPath.getList("data");
		Object dataValue = null;

		if (dataList != null && !dataList.isEmpty()) {
			dataValue = dataList.get(0); // only one element expected
		} else {
			System.out.println("❌ 'data' array is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, dataValue);
	}

	/**
	 * Sends a GET request to the specified API endpoint with an authentication
	 * cookie and optional body.
	 *
	 * <p>
	 * This method utilizes RestAssured to perform a GET request to the provided
	 * endpoint. It includes a cookie named <code>orangehrm</code> with the
	 * specified value and sets the <code>Content-Type</code> to
	 * <code>application/json</code>. If a request body is provided, it is attached
	 * to the request.
	 *
	 * @param endpoint    the relative endpoint path to be appended to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie for session
	 *                    handling or authentication
	 * @param body        a map containing request body parameters (optional; may be
	 *                    null)
	 * @return the {@code Response} object containing the results of the GET request
	 */

	public CustomResponse GetLeaveType(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}
//"id", "name", "deleted", "situational"
		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> situationals = new ArrayList<>();
		List<Object> Deletes = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> Leave : data) {
				ids.add(Leave.get("id"));
				names.add(Leave.get("name"));
				situationals.add(Leave.get("situational"));
				Deletes.add(Leave.get("deleted"));

			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, situationals, Deletes);
	}

	/**
	 * Performs a GET request to the specified endpoint with a provided cookie and
	 * optional request body.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, sets the
	 * <code>Content-Type</code> header to <code>application/json</code>, and
	 * includes a cookie named <code>orangehrm</code>. If the provided body map is
	 * not null, it is added to the request as the JSON payload.
	 *
	 * @param endpoint    the API endpoint to which the GET request will be sent,
	 *                    appended to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie used for
	 *                    session or auth
	 * @param body        a map of key-value pairs representing the request body
	 *                    (optional; may be null)
	 * @return the {@link io.restassured.response.Response} object containing the
	 *         server's response
	 */
	public CustomResponse GetUsageReport(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> names = new ArrayList<>();
		List<Object> props = new ArrayList<>();
		List<Object> sizes = new ArrayList<>();
		List<Object> pins = new ArrayList<>();
		List<Object> cellProperties = new ArrayList<>(); // Keep it List<Object> for flexibility in CustomResponse

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> headers = jsonPath.getList("data.headers");

		if (headers != null) {
			for (Map<String, Object> header : headers) {
				names.add(header.get("name"));
				props.add(header.get("prop"));
				sizes.add(header.get("size"));
				pins.add(header.get("pin"));

				Object cellProp = header.get("cellProperties");
				if (cellProp instanceof Map || cellProp == null) {
					cellProperties.add(cellProp); // add map or null as-is
				} else {
					System.out.println("⚠️ Unexpected type for cellProperties: " + cellProp.getClass().getSimpleName());
					cellProperties.add(null);
				}
			}
		} else {
			System.out.println("❌ 'data.headers' is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, props, names, sizes, pins, cellProperties);
	}

	/**
	 * Sends a PUT request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a PUT request using RestAssured, sets the
	 * <code>Content-Type</code> header to <code>application/json</code>, and
	 * includes a cookie named <code>orangehrm</code>. If a request body is
	 * provided, it is attached to the request.
	 *
	 * @param endpoint    the relative endpoint to which the PUT request is sent,
	 *                    appended to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie for session
	 *                    or authorization purposes
	 * @param requestBody an object representing the request body (can be null)
	 * @return the {@link io.restassured.response.Response} returned from the PUT
	 *         request
	 */
	public CustomResponse PutTerminationReason(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.put(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		Object id = null;
		Object name = null;

		if (data != null) {
			id = data.get("id");
			name = data.get("name");
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, id, name);
	}

	public CustomResponse PutVimEmp(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.put(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		Object id = null;
		Object name = null;
		Object lastName = null;

		if (data != null) {
			id = data.get("empNumber");
			name = data.get("firstName");
			lastName = data.get("lastName");
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, id, name, lastName);
	}

	/**
	 * Sends a POST request to the specified endpoint with a cookie and JSON string
	 * payload.
	 *
	 * <p>
	 * This method uses RestAssured to perform a POST request to the given endpoint.
	 * It sets the <code>Content-Type</code> header to
	 * <code>application/json</code>, includes a cookie named
	 * <code>orangehrm</code>, and attaches the provided JSON string as the request
	 * body.
	 *
	 * @param endpoint    the relative URL endpoint for the POST request, appended
	 *                    to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie used for
	 *                    authentication or session handling
	 * @param body        a JSON-formatted string representing the request payload
	 * @return the {@link io.restassured.response.Response} object containing the
	 *         server's response
	 */
	public CustomResponse PostPimEmp(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.post(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		Object firstName = null;
		Object employeeId = null;

		if (data != null) {
			firstName = data.get("firstName");
			employeeId = data.get("employeeId");
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, employeeId, firstName);
	}

	public CustomResponse DeletePimEmp(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.delete(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		List<Object> dataList = jsonPath.getList("data");

		Object employeeId = null;

		if (dataList != null && !dataList.isEmpty()) {
			employeeId = dataList.get(0);
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, employeeId, employeeId);
	}
}
