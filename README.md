# Movie Database API
A robust REST API for managing a movie database using Spring Boot and JPA.
## The problem👀
The local film society in your town has been maintaining their movie database using spreadsheets for years. As their collection grows, they're finding it increasingly difficult to manage and search through their data. They've approached you to create a simple yet effective API that will allow them to digitize their records and make them more accessible to members.

The film society needs a system that can store information about movies, including their genres and the actors who starred in them. They want to be able to add new movies to the database, update existing entries, and easily retrieve information about specific films or groups of films. Additionally, they'd like to be able to manage their list of genres and actors separately.
## Project overview🚄
**Movie Database API** is a backend application designed to manage and query information about **movies**, **actors**, and **genres**. It provides a robust set of endpoints for interacting with movie data, supporting common **CRUD** (Create, Read, Update, Delete) operations as well as advanced querying. This API is built using Java (Spring Boot) and JPA (Java Persistence API) for database management, offering seamless integration with relational databases. The architecture follows standard best practices such as layered design and proper separation of concerns, with well-organized entities, DTOs, repositories, services, and controllers.

---
## Project requirements❗❗
- Java 23
- Apache Maven 3.9.9
---
## Setup and Installation💻
Before setup you need to clone **all source files** and then build **jar file** with **Maven**

`git clone https://gitea.kood.tech/yuriipankevych/kmdb.git`

Install new version of [Maven](https://maven.apache.org/download.cgi) ([Here](https://maven.apache.org/install.html) you can see how to install it) and then go to the project directory:

`cd kmdb`

Build jar file with certain command:

`mvn package`

Then the target directory will appear, which will contain the executable jar file.

`cd target`

You can setup program with command:

`java -jar kmdb-0.0.1-SNAPSHOT.jar`

For testing you need to use database file with sample data, just replace `kmdb.db` file to directory with `jar` executable file. (by standart, this is the `target` directory). 

---
## Usage guide 🔑
Project have Swagger documentation with all endpoints, responses, etc. 

During program execution you can check this and test everything by the link:

`http://localhost:8080/swagger-ui/index.html/`

But also you can use Postman for testing requests. Just import json collection to your Postman.

---
## Additional features and Bonus functionality🎃
- Flag for credentials `-c` or `--credentials`

`java -jar kmdb-0.0.1-SNAPSHOT.jar -c`

- Sorting for pagination

`GET /api/movies?page=0&size=10&sortBy=id&ascending=true`

- Swagger documentation is also an additional feature.