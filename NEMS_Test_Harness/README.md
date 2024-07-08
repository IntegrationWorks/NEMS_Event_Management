# NEMS_Test_Harness
 A service built with Hexagonal Architecture, combining a REST API and Event-Driven Architecture to send event objects to a Solace event broker.

## Summary

* [NEMS_Test_Harness](#nems-test-harness)
* [Summary](#summary)
* [Setup and Pre-requisites](#setup-and-pre-requisites)
* [Running the Microservice](#running-the-microservice)
* [Verifying our solution](#verifying-our-solution)
    * [Using the API with Postman](#using-the-api-with-postman)
    * [Viewing our sent event](#viewing-our-sent-event)
* [Stopping the container](stopping-the-container)

## Setup and Pre-requisites

1. If not already installed:

- Install the latest version of OpenJDK 17 on your device (The following page has a complete catalogue of OpenJDK downloads: [https://www.openlogic.com/openjdk-downloads](https://www.openlogic.com/openjdk-downloads))
- Install Docker on your device (you can use the following link for a guide: [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/))
- Install Postman on your device

2. Clone this repository or download the .zip file from GitHub (extract the downloaded zip file )

## Running the Microservices

1. Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository


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
    docker-compose up -d --build
    ```

4. 3 docker containers should now be running:
    * `publisher`: where a spring-boot api image, built using a Dockerfile, is containerized. This container is responsible for sending events contaning personal information to the event broker with an exposed API.


5. The event publisher is now ready for use


## Verifying our Solution

To verify our solution, we need to send a request to the API exposed by the `publisher-microservice`. Then, we can verify that our request has been published and received as an event by checking the database container with pgAdmin.

### Using the API with Postman

Using Postman:

1. Select `Import` on the `My Workspace` left-hand side window, then import the [Publisher-Hex-Arch.postman_collection.json](https://github.com/mpirotaiswilton-IW/NEMS_Test_Harness/blob/master/NEMS_Test_Publisher/Publisher-Hex-Arch.postman_collection.json)

2. Select the `Send Person` Post request. In the `Body` tab, there is a json object with 3 fields that looks like this:
    ```json
    {
        "topic": "test/topic",
        "payloadStrings": [
            "payload1",
            "Payload2"
        ],
        "interval": 10
    }
    ``` 

    You can change these fields as you see fit.

3. Send the request. You should receive a 200 OK response and a response body echoing your request body's parameters: 
    ```
    new Message:[payload1, Payload2] | to send to topic: test/topic | with interval: 10.0
    ``` 

### Viewing our sent event in Solace event broker

Once the publisher received the event successfully, the events will be processed sequentially based on the interval specified in the request. To view these messages in the event queue, go to <localhost:8081>: you will be greeted with a login page for the solace broker.

1. Use the credentials 
    ```
    username:admin
    password:admin
    ```

2. Select the default Message VPN
3. Select Queues, then `TestQueue`
4. Select the Messages Queued tab, you should see the same amount of messages that you sent using Postman


## Stopping the container

To stop the docker container, run the following command: 
```
docker-compose down
```
