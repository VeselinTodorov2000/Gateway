# Gateway
Simple application for obtaining currency rates and present them to different types of clients

# 📌 API Documentation

## 1️⃣ JSON API (`/json_api`)
This API handles requests in **JSON format** and provides exchange rate data relative to the **base currency**.

### 🔹 Get the latest exchange rate
**Endpoint:**  
```
POST /json_api/current
Content-Type: application/json
```
**Sample Request:**  
```json
{
  "requestId": "b89577fe-8c37-4962-8af3-7cb89a245160",
  "timestamp": 1586335186721,
  "client": "1234",
  "currency": "EUR"
}
```
**Description:**  
- **`requestId`** – Unique request identifier (used to prevent duplicate requests).
- **`timestamp`** – Request time (UTC).
- **`client`** – The client ID.
- **`currency`** – The requested currency (e.g., "EUR", "USD").

**Sample Response:**  
```json
{
  "success": true,
  "baseCurrency": "USD",
  "currency": "EUR",
  "rates": {
    "1707079200": 1.085
  }
}
```
**Explanation:**  
- The response provides the latest exchange rate for the requested currency **relative to the base currency** stored in the system.

---

### 🔹 Get historical exchange rates
**Endpoint:**  
```
POST /json_api/history
Content-Type: application/json
```
**Sample Request:**  
```json
{
  "requestId": "b89577fe-8c37-4962-8af3-7cb89a24q909",
  "timestamp": 1586335186721,
  "client": "1234",
  "currency": "EUR",
  "period": 24
}
```
**Description:**  
- **`period`** – The number of hours in the past for which to retrieve historical data.

**Sample Response:**  
```json
{
  "success": true,
  "baseCurrency": "USD",
  "currency": "EUR",
  "rates": {
    "1707075600": 1.086,
    "1707072000": 1.085
  }
}
```
**Explanation:**  
- Returns all stored exchange rates for the given currency **relative to the base currency** for the specified period.

---

## 2️⃣ XML API (`/xml_api`)
This API works with **XML format** and supports both real-time and historical exchange rate requests.

### 🔹 Get the latest exchange rate
**Endpoint:**  
```
POST /xml_api/command
Content-Type: application/xml
```
**Sample Request:**  
```xml
<command id="1234">
    <get consumer="13617162">
        <currency>EUR</currency>
    </get>
</command>
```
**Sample Response:**  
```xml
<response>
    <success>true</success>
    <baseCurrency>USD</baseCurrency>
    <currency>EUR</currency>
    <rates>
        <entry timestamp="1707079200" rate="1.085"/>
    </rates>
</response>
```
**Explanation:**  
- Returns the latest exchange rate for the requested currency **relative to the base currency**.

---

### 🔹 Get historical exchange rates
**Endpoint:**  
```
POST /xml_api/command
Content-Type: application/xml
```
**Sample Request:**  
```xml
<command id="1234-8785">
    <history consumer="13617162" currency="EUR" period="24"/>
</command>
```
**Sample Response:**  
```xml
<response>
    <success>true</success>
    <baseCurrency>USD</baseCurrency>
    <currency>EUR</currency>
    <rates>
        <rate timestamp="1707075600" value="1.086"/>
        <rate timestamp="1707072000" value="1.085"/>
    </rates>
</response>
```
**Explanation:**  
- Returns exchange rate history for the specified period, **relative to the base currency**.

---

## 💡 Notes
- **Duplicate request prevention:** Each API checks for duplicate `requestId` values.
- **Base currency:** All rates are calculated **relative to the stored base currency**.
- **Data updates:** Exchange rates are **automatically refreshed every 10 minutes**.

