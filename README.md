# drivers_springAPI
Java spring basics

Backend API Rest done with the Spring Boot framework.

The services connect to a mongoDB database where we load the JSON collection with the documents.

------------------------------------------------------------------------------------------------

JSON collection: "data_dos.json" can be found on the app folder.

- MongoDB: load the "data_dos.json" on "admin" database. If you want to load the file in a different mongoDB database you can change that in the file
"drivers_springAPI/src/main/java/com/everis/appf1/Repository/MongoConfig.java".

- Endpoints: 
  
  * "api/ranking" -> calls the controller findAll() that calls the service getDrivers(). Example of request using a service like postman for example or the browser            itself; "localhost:8080/api/ranking".

  * "/api/ranking-gp?id=0" -> calls the controller findDriversByRace(int id) that calls the service getDriversByRace(id), id represents a number from 0-9, one for each        one of the races on database. Example of request; "localhost:8080/api/ranking-gp?id=0", will call drivers info related to race mapped to position 0 in array of          races.
  
  * "/api/driver?id=Name Surname" -> calls the controller findDriverInfo(String id) that calls the service getDriverInfo(id), id represents the fullname of the driver.        Example of request; "localhost:8080/api/driver?id=Lewis Hamilton".

Calls can be made through postman or directly from the browser once the springboot app is launched.
