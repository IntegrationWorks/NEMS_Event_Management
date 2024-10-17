# NEMS_Test_Harness
 A service built with Hexagonal Architecture, combining a REST API and Event-Driven Architecture to send event objects to a Solace event broker.

## Summary

* [NEMS_Test_Harness](#nems-test-harness)
* [Summary](#summary)
* [Setup and Pre-requisites](#setup-and-pre-requisites)
* [Running the Event Broker](#running-the-event-broker)
* [Running the Publisher Microservice](#running-the-publisher-microservice)
* [Deploying 3 Subscriber Microservices](#deploying-3-subscriber-microservices)
    * [Default Subscribers](#default-subscribers)
    * [Transactional Outbox Pattern Subscriber](#transactional-outbox-pattern-subscribers)
* [Verifying our Solution publishing and consuming birth events](#verifying-our-solution-publishing-and-consuming-birth-events)
    * [Using the API with Postman](#using-the-api-with-postman)
    * [Viewing our sent events in event subscriber](#viewing-our-sent-events-in-event-subscriber)
    * [Checking the Postgres Database(s) using pgAdmin](#checking-the-postgres-databases-using-pgadmin)
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

## Deploying 3 Subscriber Microservices

In this section, you will be deploying microservices to consume messages from the Solace broker.

> ### NOTE:
> Only deploy <u>**ONE**</u> kind of subscriber microservice. Deploying both is very likely to cause problems with regards to message consumption
 

### Default Subscribers

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

4. 3 containers should now be running:
    * `test-sub-birth-queue`: where a spring-boot api image, built using a Dockerfile, is containerized. This container is responsible for consuming timestamped messages as events and them logging them to the console. This subscriber listens to the `Birth` Queue.
    * `test-sub-death-queue`: another subscriber container with one key difference: This subscriber listens to the `Death` Queue.
    * `test-sub-enrollment-queue`: another subscriber container with one key difference: This subscriber listens to the `Enrollment` Queue.


5. All 3 subscribers are now ready to receive messages

### Transactional Outbox Pattern Subscribers

1. Using a Command Line Interface of your choosing, change directory to the downloaded/cloned repository, then to the `NEMS_Test_Harness` directory


2. To build the 3 subscriber application, change directory to `/NEMS_Test_Subscriber_TOP`:

    ```
    cd NEMS_Test_Subscriber_TOP
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
    docker-compose -f docker-compose_subscribers_top.yml up --build -d
    ```

4. 7 containers should now be running:
    * `test-sub-birth-queue`: where a spring-boot api image, built using a Dockerfile, is containerized. This container is responsible for consuming timestamped messages as events and them logging them to the console. This subscriber listens to the `Birth` Queue.
    * `test-sub-death-queue`: another subscriber container with one key difference: This subscriber listens to the `Death` Queue.
    * `test-sub-enrollment-queue`: another subscriber container with one key difference: This subscriber listens to the `Enrollment` Queue.
    * `db-birth-q-outbox`: outbox database for the `test-sub-birth-queue` container
    * `db-death-q-outbox`: outbox database for the `test-sub-death-queue` container
    * `db-enrollment-q-outbox`: outbox database for the `test-sub-enrollment-queue` container
    * `pgadmin`: a container that allows users to view the deployed postgres databases


5. All 3 subscriber containers are now ready to receive messages


## Verifying our Solution publishing and consuming birth events

To verify our solution, we need to send a request to the API exposed by the `publisher-microservice`. Then, we can verify that our request has been published and received as an event by checking the logs of each subscriber that's been deployed.

### Using the API with Postman

Using Postman:

1. Select `Import` on the `My Workspace` left-hand side window, then import the [NEMS_Test_Publisher.postman_collection.json](https://github.com/mpirotaiswilton-IW/NEMS_Test_Harness/blob/master/NEMS_Test_Publisher/NEMS_Test_Publisher.postman_collection.json)

2. Select the `Send Birth Event` Post request. In the `Body` tab, there is a json object with 3 fields that looks like this:
    ```json
    {
        "topic": "root/nems/birth",
        "messages": [
            {
                "content": {
                    "nhi": "ABC1234",
                    "birth_date": "3/29/2024"
                },
                "add-headers": {
                    "header-02": "Hello",
                    "header-03": "World"
                }
            },
            {
                "content": {
                    "nhi": "DEF2345",
                    "birth_date": "1/2/2024"
                },
                "add-headers": {
                    "header-02": "Arbitrary",
                    "header-03": "Sentence",
                    "header-04": "For Testing"
                }
            },
            {
                "content": {
                    "nhi": "GH43456",
                    "birth_date": "3/31/2024"
                },
                "add-headers": {
                    "header-02": "Goodbye",
                    "header-03": "Blue Sky"
                }
            },
            {
                "content": {
                    "nhi": "Jeremy",
                    "birth_date": "3/29/2024"
                }
            }
        ],
        "interval": 1
    }
    ``` 
    You can change these fields as you see fit.

    The `Headers` tab should also show 2 headers of note:
    * `sol-Test-Header`: `Hello World`
    * `sol-Test-Header-1`: `This is more test data`

    These headers are used to define message headers for an entire batch of messages, as defined in the request body. You may add more headers, just make sure to add the prefix `sol-` to the `key` so that the Publisher microservice assigns it as a message header.


3. Send the request. You should receive a 200 OK response and a response body echoing your request body's parameters: 
    ```
    new Message(s) received:
    ######
    [{
    "nhi" : "ABC1234",
    "birth_date" : "3/29/2024"
    }, {
    "nhi" : "DEF2345",
    "birth_date" : "1/2/2024"
    }, {
    "nhi" : "GH43456",
    "birth_date" : "3/31/2024"
    }, {
    "nhi" : "Jeremy",
    "birth_date" : "3/29/2024"
    }]
    ######
    to send to topic: 
    root/nems/birth
    with interval: 
    1.0
    they are being sent on an asynchronous thread and will be accessible shortly.
    ``` 

### Viewing our sent events in event subscriber

Once the publisher received the event successfully, the events will be processed sequentially based on the interval specified in the request body. After being sent to the broker, then the subscriber for the relevant queue will consume this message and log its content and timestamp. In the example above, we sent 4 messages to the topic `root/nems/birth`, which will be sent to the `Birth` Queue where message will be consumed by the microservice `test-sub-birth-queue`

1. Display the logs of the microservice with the following command:

    ```
    docker container logs test-sub-birth-queue
    ``` 

2. Observe the logs of the container, the final 8 lines should display either of the following text (Note: the timestamps will be different from the ones below):

    ```
    <!-- Using Default Subscribers -->

    A message was received @ 2024-10-17 22:14:10:029
    Content: {"nhi":"ABC1234","birth_date":"3/29/2024"}
    Metadata: {test-header=Hello World, test-header-1=This is more test data, header-02=Hello, header-03=World}
    A message was received @ 2024-10-17 22:14:11:113
    Content: {"nhi":"DEF2345","birth_date":"1/2/2024"}
    Metadata: {test-header=Hello World, test-header-1=This is more test data, header-02=Arbitrary, header-03=Sentence, header-04=For Testing}
    A message was received @ 2024-10-17 22:14:12:123
    Content: {"nhi":"GH43456","birth_date":"3/31/2024"}
    Metadata: {test-header=Hello World, test-header-1=This is more test data, header-02=Goodbye, header-03=Blue Sky}
    A message was received @ 2024-10-17 22:14:13:135
    Content: {"nhi":"Jeremy","birth_date":"3/29/2024"}
    Metadata: {test-header=Hello World, test-header-1=This is more test data}
    ```

    ```
    <!-- Using Transactional Outbox Pattern Subscribers -->

     ----- 
    A message was received @ 2024-10-17 22:26:28:756
    Content: {"nhi":"ABC1234","birth_date":"3/29/2024"}
    Headers: {test-header=Hello World, test-header-1=This is more test data, header-02=Hello, header-03=World}

    The NHI number has been validated and the message will be passed on (Mock) and deleted from outbox repository
    ----- 
    A message was received @ 2024-10-17 22:26:29:878
    Content: {"nhi":"DEF2345","birth_date":"1/2/2024"}
    Headers: {test-header=Hello World, test-header-1=This is more test data, header-02=Arbitrary, header-03=Sentence, header-04=For Testing}

    The NHI number has been validated and the message will be passed on (Mock) and deleted from outbox repository
    ----- 
    A message was received @ 2024-10-17 22:26:30:898
    Content: {"nhi":"GH43456","birth_date":"3/31/2024"}
    Headers: {test-header=Hello World, test-header-1=This is more test data, header-02=Goodbye, header-03=Blue Sky}

    The NHI number has been deemed invalid. This can be because it is either formatted incorrectly or is not present
    The message will now be deemed as failed
    ----- 
    A message was received @ 2024-10-17 22:26:31:911
    Content: {"nhi":"Jeremy","birth_date":"3/29/2024"}
    Headers: {test-header=Hello World, test-header-1=This is more test data}

    The NHI number has been deemed invalid. This can be because it is either formatted incorrectly or is not present
    The message will now be deemed as failed
    ```

3. You may repeat the instructions above using the `Send Death Event` and  the `docker container logs test-sub-death-queue` command, as well as the `Send Enrollment Event` and the `docker container logs test-sub-enrollment-queue` command to verify the death and enrollment queues and associated subscriber microservices.

## Checking the Postgres Database(s) using pgAdmin

> #### <u>This section is only relevant for the Transactional Outbox Pattern subscriber microservices. </u>

Each message received by Transactional Outbox Pattern subscribers are first written to a postgres database, then are deleted from the database or marked as `failed` depending on how whether it is successfully pushed to a consuming application (in this project the consuming application is mocked, considering if the provided NHI number conforms to the format: AAANNNN (3 alpha, 4 numeric)).

We will keep using the same 4 example messages as in [this](#using-the-api-with-postman) section. Only 2 of these messages will have correctly formatted NHI numbers: this means that 2 messages will remain in the Postgres database. To read these database entries, we can use the `pgadmin` container.

You can access the pgAdmin dashboard at [http://localhost:5050/](http://localhost:5050/)
1. Login using the pgadmin service environment variables defined in the `docker-compose.yml` file:
    * The email address field is defined under the environment variable `PGADMIN_DEFAULT_EMAIL`
    * The password field is defined under the environment variable `PGADMIN_DEFAULT_PASSWORD`

2. After you're successfully logged in, click on `Add New Server` on the Dashboard Home Page:
    * In the General tab, name your server as you see fit
    * Navigate to the Connection tab
    * For the `host name/address`, use the name of the Postgres container `db-birth-q-outbox`
    * Make sure the port field is `5432`
    * the `Username` field is defined by the `POSTGRES_USER` environment variable in the `docker-compose.yml` file for the `db-birth-q-outbox` container
    * the `Password` field is defined by the `POSTGRES_PASSWORD` environment variable in the `docker-compose.yml` file for the `db-birth-q-outbox` container
    * Click save and, in the Object explorer, under Servers you should see your newly saved server

3. If you now navigate in the Object Explorer to `Servers>{name you gave the database server}>Databases>admin`, you will find this is the Postgres database holding the `outbox_table` table. To view all entries in the table, select the Query tool in the `Tools` tab, then type and run the following SQL query: 
    ```SQL
    SELECT * FROM outbox_table
    ```

4. You should see 2 entries, they should have the NHI Numbers `GH43456` and `Jeremy` and both have a status field of `failed`


## Stopping the container

To stop the docker containers, run the following commands: 
```
<# Stop Publisher Microservice #>
docker-compose -f docker-compose_publisher.yml down

<# Stop Subscriber Microservices #>
docker-compose -f docker-compose_subscribers.yml down

<# Stop Transactional Outbox Pattern Subscriber Microservices #>
docker-compose -f docker-compose_subscribers_top.yml down

<# Stop Solace Broker. Always do so last #>
docker-compose -f docker-compose_solace.yml down
```
