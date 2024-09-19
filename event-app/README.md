# Event App

This is a simple React application with a header and a Create Event page.

# React Event App with PostgreSQL, pgAdmin, and Solace Integration

This project sets up a React Event app with a PostgreSQL database, pgAdmin using Docker, and integrates with the Solace broker project.

## Setup Instructions

###  Step 1. Set up the test Publisher 
Clone the Solace broker project from the provided GitHub repository:
```bash
git clone https://github.com/IntegrationWorks/Health/tree/main/NEMS_Test_Harness
```

Follow the setup instructions in the Solace broker project's README to get it up and running.

Ensure the Solace test publisher is configured to accept messages from React backend server.

The Solace Test publisher should be running for te React app to send the events. 

### Step 2: Docker Setup

1. Ensure Docker is installed on your machine.

### Step 3. Start the 4 Docker cotainers for frontend, backend, postgres db and for pgadmin.

This step to run the React app and its related containers.
Below compose file takes care of Running the front end, backend , setting up the databse to create related tables etc. 

```bash
cd event-app
docker-compose up --build
```

###  Step 4 Accessing pgAdmin

1. Open your web browser and go to `http://localhost:8083`.
2. Log in to pgAdmin with the following credentials:
   - **Email**: admin@admin.com
   - **Password**: admin
3. Add a new server in pgAdmin with the following details:
   - **Name**: eventsdb
   - **Host**: postgres-db
   - **Port**: 5432
   - **Username**: admin
   - **Password**: admin


Now navigate to http://localhost:3001/ to view the frontend.

With this setup, your React Event app project will include a PostgreSQL database, pgAdmin, all managed through Docker, a Node.js backend server to fetch data from the database and serve it to the React frontend, and integration with the Solace broker for message passing.