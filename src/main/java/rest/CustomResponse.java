package rest;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;

public class CustomResponse {
	private Response response;
	private List<Map<String, Object>> listResults;
	private String resultMessage;
	private Map<String, Object> mapResults;
	private List<Object> genericNames;
	private Object storeId;
	private Object category;
	private Object isActive;
	private List<Object> itemIds;
	private List<Object> itemNames;
	private int statusCode;
	private String pimEmployeeId;
	private String status;
	private List<Object> counts;
	private int empCount;
	private List<Object> deletes;
	private List<Object> situationals;
	private List<Object> props;
	private List<Object> sizes;
	private List<Object> pins;
	private List<Object> cellProperties;

	@SuppressWarnings("unused")
	private List<Object> Ids;

	private Object data;

	private List<Object> ids;
	private List<Object> names;

	private List<Object> dates;
	private List<Object> recurrings;
	private List<Object> lengths;
	private List<Object> lengthNames;
	private Object name;
	private Object lastName;
	@SuppressWarnings("unused")
	private Object id;

	public CustomResponse(Response response, int statusCode, String status, List<Object> list1, List<Object> list2,
			List<Object> list3, List<Object> list4, List<Object> list5) {

		this.response = response;
		this.statusCode = statusCode;
		this.status = status;

// If props are present, treat as ReportHeader structure
		if (list1 != null && list2 != null && list3 != null && list4 != null && list5 != null && list1.size() > 0
				&& list1.get(0) instanceof String && list3.get(0) instanceof Integer) {
			this.props = list1;
			this.names = list2;
			this.sizes = list3;
			this.pins = list4;
			this.cellProperties = list5;
		}
// Otherwise treat as LeaveType structure
		else {
			this.Ids = list1;
			this.counts = list2;
			this.names = list3;
			this.situationals = list4;
			this.deletes = list5;
		}
	}

	public CustomResponse(Response response, int statusCode, String status, Object id, Object name) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.setId(id);
		this.name = name;
	}
	
	public CustomResponse(Response response, int statusCode, String status, Object id, Object name, Object lastName) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.setId(id);
		this.name = name;
		this.lastName = lastName;
	}

	public CustomResponse(Response response, int statusCode, String status, Object data) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.data = data;
	}

	public CustomResponse(Response response, int statusCode, String status, int empCount) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.empCount = empCount;
	}

	// Getters

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public CustomResponse(Response response, int statusCode, String status, List<Object> ids, List<Object> names,
			List<Object> situationals, List<Object> deletes) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;

		this.names = names;
		this.situationals = situationals;
		this.deletes = deletes;
		this.ids = ids;

	}

	public CustomResponse(Response response, int statusCode, String status, Map<String, Object> mapResults) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.mapResults = mapResults;
	}

	public CustomResponse(Response response, int statusCode, String status, String resultMessage) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.resultMessage = resultMessage;
	}

	public CustomResponse(Response response, int statusCode, String status, List<Map<String, Object>> listResults) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.listResults = listResults;
	}

	public CustomResponse(Response response, int statusCode, String status, List<Object> patientIds,
			List<Object> patientCodes) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
	}

	public CustomResponse(Response response, int statusCode, String status, List<Object> itemIds,
			List<Object> itemNames, List<Object> genericNames) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.itemIds = itemIds;
		this.itemNames = itemNames;
		this.genericNames = genericNames;
	}

	public CustomResponse(Response response, int statusCode, String status, List<Object> ids, List<Object> names,
			List<Object> dates, List<Object> recurrings, List<Object> lengths, List<Object> lengthNames) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.ids = ids;
		this.names = names;
		this.dates = dates;
		this.recurrings = recurrings;
		this.lengths = lengths;
		this.lengthNames = lengthNames;
	}

	public Object getId() {
//		Object id = null;
		return id;
	}

	public Object getName() {
		return name;
	}
	
	public Object getLastName() {
		return lastName;
	}

// Getters and Setters
	public List<Object> getCounts() {
		return counts;
	}

	public List<Object> setCounts(List<Object> counts) {
		return this.counts = counts;
	}

	public int setEmpCount(int count) {
		return this.empCount = count;
	}

	public int getEmpCount() {
		return this.empCount;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getEmployeeId() {
		return pimEmployeeId;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Object> getIds() {
		return ids;
	}

	public void setIds(List<Object> ids) {
		this.ids = ids;
	}

	public List<Object> getNames() {
		return names;
	}

	public void setNames(List<Object> names) {
		this.names = names;
	}

	public List<Object> getDates() {
		return dates;
	}

	public void setDates(List<Object> dates) {
		this.dates = dates;
	}

	public List<Object> getRecurrings() {
		return recurrings;
	}

	public void setRecurrings(List<Object> recurrings) {
		this.recurrings = recurrings;
	}

	public List<Object> getLengths() {
		return lengths;
	}

	public void setLengths(List<Object> lengths) {
		this.lengths = lengths;
	}

	public List<Object> getLengthNames() {
		return lengthNames;
	}

	public void setLengthNames(List<Object> lengthNames) {
		this.lengthNames = lengthNames;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public List<Map<String, Object>> getListResults() {
		return listResults;
	}

	public void setListResults(List<Map<String, Object>> listResults) {
		this.listResults = listResults;
	}

	public Map<String, Object> getMapResults() {
		return mapResults;
	}

	public void setMapResults(Map<String, Object> mapResults) {
		this.mapResults = mapResults;
	}

	public Object getStoreId() {
		return storeId;
	}

	public void setStoreId(Object storeId) {
		this.storeId = storeId;
	}

	public Object getCategory() {
		return category;
	}

	public void setCategory(Object category) {
		this.category = category;
	}

	public Object getIsActive() {
		return isActive;
	}

	public void setIsActive(Object isActive) {
		this.isActive = isActive;
	}

	public List<Object> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Object> itemIds) {
		this.itemIds = itemIds;
	}

	// Getter and Setter for itemNames
	public List<Object> getItemNames() {
		return itemNames;
	}

	public void setItemNames(List<Object> itemNames) {
		this.itemNames = itemNames;
	}

	// Getter and Setter for genericNames
	public List<Object> getGenericNames() {
		return genericNames;
	}

	public void setGenericNames(List<Object> genericNames) {
		this.genericNames = genericNames;
	}
	// this.names2 = names2;
	// this.situationals=situationals;
	// this.deletes= deletes;

	public void setSituationals(List<Object> situationals) {
		this.situationals = situationals;
	}

	public void setPimEmployeeId(String pimEmployeeId) {
		this.pimEmployeeId = pimEmployeeId;
	}

	public List<Object> getSituationals() {
		return situationals;
	}

	public void setDeletes(List<Object> deletes) {
		this.deletes = deletes;
	}

	public List<Object> getDeletes() {
		return deletes;
	}

	public List<Object> getProps() {
		return props;
	}

	public void setProps(List<Object> props) {
		this.props = props;
	}

	public List<Object> getSizes() {
		return sizes;
	}

	public void setSizes(List<Object> sizes) {
		this.sizes = sizes;
	}

	public List<Object> getPins() {
		return pins;
	}

	public void setPins(List<Object> pins) {
		this.pins = pins;
	}

	public List<Object> getCellProperties() {
		return cellProperties;
	}

	public void setCellProperties(List<Object> cellProperties) {
		this.cellProperties = cellProperties;
	}

	public void setId(Object id) {
		this.id = id;
	}

	// ✅ Add these getters

}
