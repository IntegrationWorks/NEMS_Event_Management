# NEMS_Test_Harness
 A service built with Hexagonal Architecture, combining a REST API and Event-Driven Architecture to send event objects to a Solace event broker.

## Summary

* [NEMS_Test_Harness](#nems-test-harness)
* [Summary](#summary)
* [Setup and Pre-requisites](#setup-and-pre-requisites)
* [Running the Event Broker](#running-the-event-broker)
* [Running the Publisher Microservice](#running-the-publisher-microservice)
* [Running 3 Subscriber Microservices](#running-3-subscriber-microservices)
* [Verifying our Solution publishing and consuming birth events](#verifying-our-solution-publishing-and-consuming-birth-events)
    * [Using the API with Postman](#using-the-api-with-postman)
    * [Viewing our sent events in event subscriber](#viewing-our-sent-events-in-event-subscriber)
* [Stopping the container](stopping-the-container)

## Setup and Pre-requisites

1. If not already installed:

- Install the latest version of OpenJDK 17 on your device (The following page has a complete catalogue of OpenJDK downloads: [https://www.openlogic.com/openjdk-downloads](https://www.openlogic.com/openjdk-downloads))
- Install Docker on your device (you can use the following link for a guide: [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/))
- Install Postman on your device

2. Clone this repository or download the .zip file from GitHub (extract the downloaded zip file )

## Running the Event Broker

1. Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository, then to the `NEMS_Test_Harness` directory

2. Run the following command: 

    ```
    docker-compose -f docker-compose_solace.yml up --build -d
    ```

3. 2 containers should now be running: 
    * `solace`: where a Solace event broker is containerized.
    * `solace-init`: where a python script runs to set up our `solace` container with all the queues and subscribed topics needed for our microservices to communicate.

4. After a minute or so, the solace broker will be ready for use


## Running the Publisher Microservice

1. Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository, then to the `NEMS_Test_Harness` directory


2. To build the publisher application, change directory to `/NEMS_Test_Publisher`:

    ```
    cd NEMS_Test_Publisher
    ```

    then, run the following command:  

    ```
    <# Linux/MacOs #>
    ./mvnw clean package -DskipTests

    <# Windows #>
    .\mvnw clean package -DskipTests
    ```


3. Once the build is successful, change back to the cloned repository's directory, then run this command to deploy it as a docker container:

    ```
    docker-compose -f docker-compose_publisher.yml up --build -d
    ```

4. a single container should now be running:
    * `test-publisher`: where a spring-boot api image, built using a Dockerfile, is containerized. This container is responsible for sending events contaning personal information to the event broker with an exposed API.


5. The event publisher is now ready for use


## Running 3 Subscriber Microservices

1. Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository, then to the `NEMS_Test_Harness` directory


2. To build the 3 subscriber application, change directory to `/NEMS_Test_Subscriber`:

    ```
    cd NEMS_Test_Subscriber
    ```

    then, run the following command:  

    ```
    <# Linux/MacOs #>
    ./mvnw clean package -DskipTests

    <# Windows #>
    .\mvnw clean package -DskipTests
    ```


3. Once the build is successful, change back to the cloned repository's directory, then run this command to deploy it as a docker container:

    ```
    docker-compose -f docker-compose_subscribers.yml up --build -d
    ```

4. 3 container should now be running:
    * `test-sub-birth-queue`: where a spring-boot api image, built using a Dockerfile, is containerized. This container is responsible for consuming timestamped messages as events and them logging them to the console. This subscriber listens to the `Birth` Queue.
    * `test-sub-death-queue`: another subscriber container with one key difference: This subscriber listens to the `Death` Queue.
    * `test-sub-enrollment-queue`: another subscriber container with one key difference: This subscriber listens to the `Enrollment` Queue.


5. All 3 subscribers are now ready to receive messages



## Verifying our Solution publishing and consuming birth events

To verify our solution, we need to send a request to the API exposed by the `publisher-microservice`. Then, we can verify that our request has been published and received as an event by checking the logs of each subscriber that's been deployed.

### Using the API with Postman

Using Postman:

1. Select `Import` on the `My Workspace` left-hand side window, then import the [NEMS_Test_Publisher.postman_collection.json](https://github.com/mpirotaiswilton-IW/NEMS_Test_Harness/blob/master/NEMS_Test_Publisher/NEMS_Test_Publisher.postman_collection.json)

2. Select the `Send Birth Event` Post request. In the `Body` tab, there is a json object with 3 fields that looks like this:
    ```json
    {
    "topic": "root/nems/birth",
    "payload": [{
		  "nhi": "",
		  "birth_date": "3/29/2024"
		}, {
		  "nhi": "",
		  "birth_date": "1/2/2024"
		}, {
		  "nhi": "",
		  "birth_date": "3/31/2024"
		}, {
		  "nhi": "",
		  "birth_date": "7/17/2023"
		}],
    "interval": 3
}
    ``` 

    You can change these fields as you see fit.

3. Send the request. You should receive a 200 OK response and a response body echoing your request body's parameters: 
    ```
    new Message(s) received:[{"nhi":"","birth_date":"3/29/2024"}, {"nhi":"","birth_date":"1/2/2024"}, {"nhi":"","birth_date":"3/31/2024"}, {"nhi":"","birth_date":"7/17/2023"}]
    to send to topic: 
    root/nems/birth
    with interval: 
    3.0
    they are being sent on an asynchronous thread and will be accessible shortly.
    ``` 

### Viewing our sent events in event subscriber

Once the publisher received the event successfully, the events will be processed sequentially based on the interval specified in the request body. After being sent to the broker, then the subscriber for the relevant queue will consume this message and log its content and timestamp. In the example above, we sent 4 messages to the topic `root/nems/birth`, which will be sent to the `Birth` Queue where message will be consumed by the microservice `test-sub-birth-queue`

1. Display the logs of the microservice with the following command:

    ```
    docker container logs test-sub-birth-queue
    ```

2. Observe the logs of the container, the final 8 lines should display the following (Note: the timestamps will be different from the ones below):
    ```
    A message was received @ 2024-07-08 21:35:59:411
    Content: {"nhi":"","birth_date":"3/29/2024"}
    A message was received @ 2024-07-08 21:36:02:426
    Content: {"nhi":"","birth_date":"1/2/2024"}
    A message was received @ 2024-07-08 21:36:05:429
    Content: {"nhi":"","birth_date":"3/31/2024"}
    A message was received @ 2024-07-08 21:36:08:433
    Content: {"nhi":"","birth_date":"7/17/2023"}
    ```

3. You may repeat the instructions above using the `Send Death Event` and using the `docker container logs test-sub-death-queue` command, as well as the `Send Enrollment Event` and using the `docker container logs test-sub-enrollment-queue` command to verify the death and enrollment queues and associated subscriber microservices.


## Stopping the container

To stop the docker containers, run the following commands: 
```
<# Stop Solace Broker #>
docker-compose -f docker-compose_solace.yml down

<# Stop Publisher Microservice #>
docker-compose -f docker-compose_publisher.yml down

<# Stop Subscribers Microservice #>
docker-compose -f docker-compose_subscribers.yml down
```
