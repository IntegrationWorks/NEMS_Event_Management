# NEMS_Event_Management

### Overview
The NEMS Event Management project consists of two primary components:
the NEMS Test Harness and the React Event App. 

The NEMS Test Harness is built with Hexagonal Architecture, combining a REST API and Event-Driven Architecture to send event objects to a Solace event broker. 

The React Event App is a simple React application with a header and a Create Event page, integrated with a PostgreSQL database, pgAdmin, and the Solace broker.

### Setup and Pre-requisites
If not already installed:

Install the latest version of OpenJDK 17 on your device (OpenJDK downloads)

Install Docker on your device (Docker installation guide)

Install Postman on your device

Clone this repository or download the .zip file from GitHub and extract the downloaded zip file.


 Running the Publisher Microservice
Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository, then to the NEMS_Test_Harness directory.

### To build the publisher application, change directory to NEMS_Test_Harness/NEMS_Test_Publisher:

```
cd NEMS_Test_Publisher
./mvnw clean package -DskipTests  # Linux/MacOs
.\mvnw clean package -DskipTests  # Windows
 ```
 
### To build the 3 Subscriber Microservices
Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository, then to the NEMS_Test_Harness directory.

To build the 3 subscriber applications, change directory to NEMS_Test_Harness/NEMS_Test_Subscriber:

```
cd NEMS_Test_Subscriber
./mvnw clean package -DskipTests  # Linux/MacOs
.\mvnw clean package -DskipTests  # Windows
```

### Running the React Event App and Solace containers
Using a Command Line Interface of your choosing, change directory to the event-app directory.

Run the following command to build and start the React app and the Solace related containers
```
cd event-app
docker-compose --build -d
```

This setup will get both the NEMS Test Harness and the React Event App running, with integrations with the Solace broker, PostgreSQL database, and pgAdmin and solace containers.

