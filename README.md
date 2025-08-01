
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
![Class diagram](/src/main/resources/img/class-diagram.png)
* Each order item corresponds to a product in the order, along with the quantity of that product.
The price is saved in the order item to maintain a history — so if the product price changes in the future, the sale price at the time of the order is still recorded in the system.
* User can have one or more roles, which define their access profile in the system (e.g., client, admin).

## Security
### Model User Role
![Class diagram](/src/main/resources/img/user-role-model.png)
* User
* Role

### Model Spring Security
![Class diagram](/src/main/resources/img/spring-security-checklist.png)
*	UserDetails: Informações do usuário (nome e perfiles) E algumas informações adicionais;
*	GrantedAuthority: coleção de Roles que o usuário tem;
*	UserDetailsService: Componente que o spring precisa, com o método loadUserByUsername;
*	UsernameNotFoundE3xception: Quando o usunário não é encontrado.


### Model OAuth2
![Class diagram](/src/main/resources/img/oauth2.png)
* Implementação customizada do password grant
* Authorization server 
  * Habilitar Authorization server
  * Configurar token (codificação, formato, assinatura)
  * Configurar autenticação / password encoder
  * Registrar aplicação cliente
* Resource server 
  * Configurar controle de acesso aos recursos
  * Configurar CSRF, CORS
  * Configurar token
  * Liberar H2 Console no modo teste


## Endpoints
### Oauth2 Token
#### * POST Login
```http
 POST /oauth2/token
```
| Parâmetro   | Tipo       |Descrição|
| :---------- | :--------- | :------------------------------------------ |
| `username`  | `String` | **Required**. Client Username |
| `password`  | `String` | **Required**. Client Password |

### Category
#### * GET All Categories (PUBLIC)
```http
 GET /categories
```

### Order
#### * GET Order by id (PRIVATE)
```http
 GET /orders/{id}
```
| Parâmetro   | Tipo       |Descrição|
| :---------- | :--------- | :------------------------------------------ |
| `id`  | `Long` | **Required**. Order id |

#### * POST Order (PRIVATE)
```http
 POST /orders
```
| Parâmetro   | Tipo       |Descrição|
| :---------- | :--------- | :------------------------------------------ |
| `items`  | `List` | **Required**. List of products with productId and quantity |

### Product
### * GET Products (PUBLIC)
```http
 GET /produts
```

### * GET Products by id (PUBLIC)
```http
  GET /produts/{id}
```
| Parâmetro   | Tipo       |Descrição|
| :---------- | :--------- | :------------------------------------------ |
| `id`  | `Long` | **Required**. Product id |

### * POST Product (PRIVATE)
```http
  POST /produts
```
| Parâmetro   | Tipo       |Descrição|
| :---------- | :--------- | :------------------------------------------ |
| `name`      | `String` | **Required**. Product name |
| `description` | `String` | **Required**. Product description |
| `imageUrl`    | `String` | **Required**. Product image |
| `price` | `Double` | **Required**. Product price |
| `categories` | `List` | **Required**. Product categories |

### * PUT Product (PRIVATE)
```http
  PUT /produts/{id}
```
| Parâmetro   | Tipo       |Descrição|
| :---------- | :--------- | :------------------------------------------ |
| `id`  | `Long` | **Required**. Product id |
| `name`      | `String` | **Required**. Product name |
| `description` | `String` | **Required**. Product description |
| `imageUrl`    | `String` | **Required**. Product image |
| `price` | `Double` | **Required**. Product price |
| `categories` | `List` | **Required**. Product categories |

### * DELETE Products by id (PRIVATE)
```http
  DELETE /produts/{id}
```
| Parâmetro   | Tipo       |Descrição|
| :---------- | :--------- | :------------------------------------------ |
| `id`  | `Long` | **Required**. Product id |


