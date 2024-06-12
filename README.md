# Car Rental System

#### *Codename: `Hozo Cabby`*

---
### **Description**

Hozo Cabby is currently a command line application that mimics a typical car rental system. 
It covers basics use cases for customer, owner, driver and admin. The Pseudo - Business Model
focuses on renting out cars to customers from the pool cars made available by owners, while the driver 
are considered as add-on, opt-in for each rental booking.

---
### Tech Stack

**Language:** `Java 17.0.2` 

**Database:** `SQLite 3`

**JDBC Driver:** `XSerial SQLite 3.46.0.0`

---
### **Architecture**

![Diagram Showcasing Architecture](/uml/Architecture/HozoCabby-architecture.svg "Architecture Diagram")

**Architecture:** Model - View - Controller (Controller Behaves More Like a Service)

- Here the views are different console menu that is created specifically for each type of user.
- Service Layer just handles business logic and interacts with Data Layer to retrieve data.
- Data Layer abstracts the underlying data store (eg: Bunch of hashMaps, file or 
in this case sqlite database) from the layers below it.
- The View Layer is modelled using [State Design Pattern](https://refactoring.guru/design-patterns/state) meaning it behaves like an state machine.
- The Service Layer and View Layer are loosely coupled as each methods in service classes mainly deals with one specific use case.
- The DAO [[Data Access Objects]](https://en.wikipedia.org/wiki/Data_access_object) interacts with underlying data store (here sqlite) as such 
they contain sql queries and exposes data manipulation through methods.
---

### **Use Cases**
![Diagram Showcasing Usecase](uml/UseCase/HozoCabby-UseCase.drawio.svg)

>From This diagram, only some of first layer use case are covered in this application.  
---
