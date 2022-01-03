# Route Finder

## Prerequisites

Please make sure you have installed at least java 11 and Maven 3.8.3

## Building the application

In order to build the application open a terminal and run the following command in the project folder:

```
mvn clean install
```

## Running the application

### From command line
For starting the application run the following command:

```
mvn spring-boot:run
```
### From your IDE

If you want to run the application inside your IDE(for eg. IntelliJ), right click the RouteFinderApplication and select 
run RouteFinderApplication.main()  

Now open a browser window and type the following:
```
http://localhost:8080/routing/CZE/ITA
```

in order to get the route between the Czech Republic and Italy for example. You should get a response like:
```
{"route":["CZE","AUT","ITA"]}
```
In case a route is not possible between the two countries, or at least one of the country names is not valid you will get an HTTP 400 response.