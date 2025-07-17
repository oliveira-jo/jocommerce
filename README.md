
# JO-COMMERCE SYSTEM

This project was developed in parallel with the Java Spring Professional course by Nelio Alves, CEO of DEVSUPERIOR.

## Overview

I’ve developed a system that handles CRUD operations for users, products, and product categories.

Each user has the following information: name, email, phone number, birthdate, and password.
Each product includes: name, description, price, and image.

The system displays a product catalog, which can be filtered by product name. In the catalog, users can select a product to view more details and decide whether to add it to the shopping cart.

Client users can add or remove items from the shopping cart and update the quantity of each product.
When a user decides to complete the purchase, the order is saved in the system with the status "awaiting payment".

Each order includes the following data: the creation timestamp, current status, list of items, and quantity of each product.
The possible order statuses are: awaiting payment, paid, sent, received, and canceled.
When the user makes the payment, the payment timestamp is also recorded.

Users can have the role of client or administrator. By default, all registered users are clients.
Unregistered users can create an account, browse the product catalog, and add items to the shopping cart.

Clients can update their profiles, place orders, and view their order history.
Administrator users have access to an admin panel where they can manage users, products, and categories through CRUD operations.
   

## Class Diagram

![Class diagram](/src/main/resources/img/deagrama-classes.png)

* Each order item corresponds to a product in the order, along with the quantity of that product.
The price is saved in the order item to maintain a history — so if the product price changes in the future, the sale price at the time of the order is still recorded in the system.
* User can have one or more roles, which define their access profile in the system (e.g., client, admin).