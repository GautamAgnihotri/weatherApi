
# Weather Information Service

This project is a Spring Boot REST API that provides weather information for a specific pincode and date using the OpenWeather API for weather data and geocoding. The pincode’s latitude, longitude, and weather data are stored in a PostgreSQL database to optimize future requests.





## Features

- Fetch weather information by pincode and date
- Automatic caching of location coordinates (latitude/longitude)
- Database persistence of weather information


## Technologies Used

- Spring Boot 3.3.4
- Spring Data JPA
- PostgreSQL
- Lombok


## Installation



1. Clone the repository:

```bash
    git clone https://github.com/GautamAgnihotri/weatherApi.git
    cd weatherApi
```
2. Database Setup
```sql
    CREATE DATABASE weatherdb;
```
3. Application Properties:
    
 Create application.properties in src/main/resources. Here are the key configurations needed:
```bash
   
   # Database Configuration
    spring.datasource.url=jdbc:postgresql://localhost:5432/weatherdb
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.datasource.driver-class-name=org.postgresql.Driver

    # Hibernate settings
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    # API Keys Configuration
    openWeather.api.key = your openWeather api key

    openweather.api.url = https://api.openweathermap.org/data/2.5/

    openweather.geocoding.url = http://api.openweathermap.org/geo/1.0/
 ```
3. Build and run the application:
```bash
    # Build the project
    mvn clean install

    # Run the application
    mvn spring-boot:run
```
4. The application will run on ``http://localhost:8080``
## API Reference

### 1. Generate PDF

**Endpoint**: `/api/weather/{pincode}?forDate="yyyy-mm-dd"`  
**Method**: `Get`  
**Description**: Retrieves weather information for a specific pincode and date..

**Query Parameters:**

| Parameter  | Type | Required | Description|
| ----------| ------ | --------| ----------- |
| pincode   | string | Yes  | Indian postal code (6 digits)  |
| forDate   | string |  Yes |    Date in ISO format (YYYY-MM-DD) |

**Response**:

Success Response (HTTP Status 200):
 
       Content-Type: application/json
       Body: 
       {
            "id": 3,
            "location": {
                "id": 1,
                "pincode": "457001",
                "latitude": 23.1084,
                "longitude": 75.3841
            },
            "date": "2024-10-24",
            "temperature": 304.3,
            "humidity": 26.0,
            "description": "clear sky",
            "lastUpdated": "2024-10-24T15:20:22.0303238"
        }




