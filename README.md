# StockSip Platform API #

Stocksip Android Native Application is made with Kotlin, Jetpack Compose, and Room local database. It also illustrates Clean Architecture with MVVM pattern.

## Summary ##

This application contains the following key features:

- Inventory management tailored for liquor stores.
- Real-time tracking of stock levels.
- Automated alerts for low stock items.
- Sales order management for liquor products.
- Supplier management and order tracking.
- Efficient management of liquor stock across multiple warehouses.
- Integration with suppliers for seamless order management.
- Support for multiple warehouses and multiple products.

## Technologies Used ##

For the development of the Stocksip Android Native Application, the following technologies and tools were used:

- **Kotlin**: The primary programming language for Android development, known for its modern features and interoperability with Java.
- **Jetpack Compose**: A modern toolkit for building native Android UI, allowing for declarative UI development.
- **Room**: A persistence library that provides an abstraction layer over SQLite, making database access easier and more efficient.
- **MVVM (Model-View-ViewModel)**: An architectural pattern that separates the UI from the business logic, enhancing testability and maintainability.

The application also incorporates several advanced features to enhance its functionality and user experience:

- **Dependency Injection**: To manage dependencies and improve code modularity using libraries like Hilt or Dagger.
- **Coroutines**: For managing background tasks and asynchronous programming in a more efficient way.
- **LiveData**: To observe data changes and update the UI reactively.
- **Navigation Component**: To handle in-app navigation and ensure a smooth user experience.
- **Data Binding**: To bind UI components in layouts to data sources, improving code readability and maintainability.
- **Material Design**: To create a visually appealing and user-friendly interface following Google's design guidelines.
- **RESTful API Integration**: To connect with backend services and fetch/store data.
- **User Authentication**: Secure login and registration features to protect user data.
- **Offline Support**: To allow users to access certain features without an internet connection.
- **UI/UX Design Principles**: To create an intuitive and engaging user experience.
- **Modular Architecture**: To separate different features into modules for better maintainability and scalability.

## Documentation

Stocksip Platform API includes its own documentation and it's available in the `docs` folder. It includes the following documentation:

- User Stories: related user stories to mobile app development. Is available in [docs/user-stories](docs/user-stories.md).
- C4 Model Software Architecture Diagram: illustrating the architecture of the application. Is available in [docs/software-architecture.dsl](docs/software-architecture.dsl).

## Bounded Context Divided System ##

This version of StockSip Android Native App is divided into seven bounded contexts in the feature module: Authentication, Profile Management, Payments and Subscriptions, Inventory Management, Alerts and Notifications, Procurement Ordering and Order Monitoring.

### Authentication Context

This context is responsible for user authentication and authorization, ensuring secure access to the application.
This context includes the following features:

- User registration and login (sign-in and sign-up).
- Role-based access control.
- Password management (reset, change).
- Token-based authentication with JWT.

### Profile Management Context

The Profile Management Context is responsible for showing the information of the registered user and the ability of modify the information (name, email, location).
This context includes the following features:

- User profile creation and updating.
- Profile picture management.
- View and edit personal information.

### Payments and Subscriptions Context

This context handles payment processing and subscription management, allowing users to manage their billing and subscription plans along with their accounts. It includes the following features:

- Creation of a new account with a subscription plan.
- Management of existing subscription plans (upgrade, downgrade, cancel).
- Payment processing for subscriptions with PayPal and MercadoPago.

### Inventory Management Context

This context focuses on managing product stock across multiple warehouses, allowing users to track stock levels, add new products, and manage inventory efficiently. It includes the following features:

- Warehouse management (create new warehouse, update information and delete empty warehouses).
- Product management (register new products, update information and delete zero-stock products).
- Stock tracking (view current stock levels and update stock).
- Product exits and entries management.
- Product expiration date management.
- Product care guides management.
- Product transferal between warehouses.
- Catalog management for liquor providers (view available products and search products).

### Alerts and Notification Context

This context manages real-time stock updates and notifications, ensuring users are always informed about their inventory status. It includes the following features:

- Stock level alerts.
- Expiration date notifications.
- The creation of alerts for low stock levels.

### Procurement Ordering Context

This context provides the capability of generating new orders to suppliers when stock levels are low, ensuring that liquor stores can maintain optimal inventory levels. It includes the following features:

- Cart management (add products to cart, view cart, update quantities, remove products).
- Order creation (place orders to suppliers).
- Order history (view past orders and their statuses).
- Order status tracking (view current status of orders).

### Order Monitoring Context

This context focuses on managing orders and operations, allowing liquor suppliers to track orders and ensure smooth operations. It includes the following features:

For suppliers:

- Supplier management (view orders, update order status).
- Order tracking and history.
- Inventory management (view and manage available products).