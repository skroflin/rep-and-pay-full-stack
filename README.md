# Rep & Pay

This project was created during my summer job at FINA - financial agency. The app consists of the the backend, in the form of a REST API which communicates with a MariaDB database (consisting of 4 tables - `user`, `training_session`, `membership` and `booking`) and a frontend which serves as a view and sends requests and receives responses which are rendered and displayed.

For membership payment and data insertion into `membership` table, Stripe's own Spring Boot library was utilised for creating webhooks, creating checkout sessions and enabling payment.

More about utilizing Stripe read [here](https://www.baeldung.com/java-stripe-api).

## Technology stack

The project was made utilizing Spring Boot and Java for developing the REST API and entire backend, Hibernate for Object Relational Mapping and communication with the MariaDB database, React.js, Typescript and Vite for developing the client side i.e. frontend. For creating the UI, Ant Design was used as a component library.

Database used was MariaDB, which runs via XAMPP and it's own MariaDB server.

Technology list:
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Hibernate](https://www.baeldung.com/spring-boot-hibernate)
- [XAMPP](https://www.apachefriends.org/)
- [React with Vite](https://vite.dev/guide/)
- [Ant Desgin](https://ant.design/)

---

### Features

3 separate roles (`SUPERUSER`, `COACH` and `USER`), each role has it's own features and possibilites.

#### SUPERUSER

The superuser can view all of the regular users and coaches, view their information (i.e. full name, email, username, view memberships of each basic user).
Superuser can view all active, inactive memberships of each regular user.

#### COACH

The coach can create training sessions, view existing sessions of other coaches, view and review booking reservations for training sessions made by regular users.
Coach has an insight into all of the reservations made by regular users, how many beginner, intermediate and advanced training sessions he has, how many requests are pending, how many are accepted and how many are rejected.

#### USER

The user can view existing training sessions and make reservation requests for said training sessions. If a new user hasn't paid for his membership (e.g. for October), he cannot make any reservation request for the given month. He can view all of his requests that were sent and training sessions that he has made reservations for and that are accepted.
The user can view all of his past/prior memberships that he has paid for.

### How to start - Backend

Firstly you must build the app (Build with dependencies), afterwards start the MariaDB/MySQL server via XAMPP control panel.
Secondly, you must install Stripe's own CLI (available via the following [link](https://docs.stripe.com/stripe-cli)).
After succesfully installing the CLI, visit the Stripe Dashboard where you will be able to generate the Stripe secret key. The given key is then copied and pasted in the `application.properties` under the variable `stripe.secret-key`.

Using Stripe CLI, run the command:
```
stripe login
```
afterwards you will be redirected to the Stripe Dashboard to confirm the oauth.

Afterwards you run the command:
```
stripe listen --forward-to your_backend_rest_api/your_webhook_controller
```

Upon using the given command you will receive the webhook secret which you will paste in the `application.properties` under the variable `stripe.webhook-secret`.

You will also need a `jwt.secret`, this can be generated via a local Key Generator or online (e.g. [example](https://jwtsecrets.com/)).

The Jwt secret is necessary for user authorization and logic rendering of each functionality that each user has and represents.

After importing each necessary dependency, you may run the backend by running the `Start.java` file.

---

### How to start - Frontend

Frontend is run using [npm](https://www.npmjs.com/), running the command:
```
npm run dev
```

You will afterwards receive the port and local server on which the frontend is running currently.