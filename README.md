<center><font size="3" face="Georgia"> <h3> "Task tracker"
</h3></font>
</center>

## Overview:
-  The "Task tracker" application is a small console application based on the Representational state Transfer software architecture. 
- It implements Writing Controllers using Mono and Flux.
  Working with MongoDB in a reactive paradigm.


## Features:
- find all users,
- find user by ID,
- create a user,
- update user information,
- delete user by ID.
- find all tasks
- find a specific task by ID
- create a task;
- update task;
- add observer to task;
- delete a task by ID.
## Security
- Receiving (both by list and by ID), updating and deleting user profiles should be available only to those clients who have one of the following roles: ROLE_USER, ROLE_MANAGER.
- Getting a list of tasks, getting a task by ID, adding an observer are available to a user with one of the following roles: ROLE_USER, ROLE_MANAGER;
- Creating, updating and deleting a task is available to a user with the ROLE_MANAGER role.


## Prerequisites
- Java 17
- Maven (for building the application)
- Spring Boot 3.2.3
- Docker Desktop

## Setup and Installation
- Clone the repository:
- git clone [https://github.com/Katas77]
- Navigate to the project directory:
- cd contacts-application
- Build the application using Maven:
- mvn clean install 
- Run the application:
### For general use:
- Work with data-mongodb-reactive
- Launch and configure the database via Docker
- To run using Docker, you need to enter the following commands in the terminal:
- cd docker   
- docker-compose up



###  Management
"Task tracker" are managed through a simple command-line interface. 
Input errors are handled gracefully, with prompts for correct input.

## Technologies used:

- Java
- Spring Boot
- Docker
- Webflux
- Mongodb-reactive
-  Spring Security.


____
✉ Почта для обратной связи:
<a href="">krp77@mail.ru</a>