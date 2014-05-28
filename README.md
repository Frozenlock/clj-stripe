clj-stripe
================================

clj-stripe is a library that provides Clojure bindings for the Stripe API.

Usage
================================

clj-stripe wraps around the Stripe REST API. For a detailed explanation of the available operations and parameters visit https://stripe.com/api/docs .

The general way of invoking operation is:

* To execute an operation, first create it using the appropriate funcion. For example, to create an operation for retrieving a customer, just execute
(customers/get-customer "mycustomerid")
That will return a map that represents the action of retrieving that customer.
* Some operations require parameters that are provided through other functions. For example, the function customers/get-customers (that retrieves all customers) accepts two optional parameters, count and offset. To create that operation you must use auxiliary functions defined in the common namespace:
(customers/get-customers (common/limit-count 10) (common/offset 4))
or, for brevity,
(customers/get-customers (common/position 10 4))
* Once created, execute an operation by passing it as parameter to the common/execute multimethod.
* Wrap all the calls to execute in a call to common/with-token. That will provide the authentication token needed when invoking the Stripe API.

  Examples
================================

* Declare the dependency to clj-stripe in your project.clj

```clj
:dependencies [abengoa/clj-stripe "1.0.4"]
```

* Import the namespaces you may need

```clj
(:require [clj-stripe.util :as util]
	  [clj-stripe.common :as common]
	  [clj-stripe.plans :as plans]
	  [clj-stripe.coupons :as coupons]
	  [clj-stripe.charges :as charges]
	  [clj-stripe.cards :as cards]
	  [clj-stripe.subscriptions :as subscriptions]
	  [clj-stripe.customers :as customers]
	  [clj-stripe.invoices :as invoices]
	  [clj-stripe.invoiceitems :as invoiceitems])
```

* First step is to create a some subscription plans:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (plans/create-plan "plan1" (common/money-quantity 500 "usd") (plans/monthly) "Starter"))
    (common/execute (plans/create-plan "plan2" (common/money-quantity 1000 "usd") (plans/monthly) "Professional")))
```

* To show a user the list of available plans:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (plans/get-all-plans)))
```

* When a new user signs up, create a new customer:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (customers/create-customer (common/card "A card token obtained with stripe.js") (customers/email "site@stripe.com") (common/plan "plan1"))))
```

* To display the customer information:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (customers/get-customer "cu_1mXfGxS9m8")))
```

* And the billing status of the customer:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (invoices/get-upcoming-invoice (common/customer "cu_1mXfGxS9m8"))))
```

* Get all the invoices of a customer:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
	(common/execute (invoices/get-all-invoices (common/customer "cu_1mXfGxS9m8"))))
```

* Get an individual invoice:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
	(common/execute (invoices/get-invoice "INVOICE_ID")))
```

* For a one time charge to an existing customer:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (charges/create-charge (common/money-quantity 5000 "usd") (common/customer "cu_1mXfGxS9m8") (common/description "This an extra charge for some stuff"))))
```

* Get all the charges that were billed to a customer:

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (charges/get-all-charges (common/customer "cu_1mXfGxS9m8"))))
```

* Get all the charges of a customer, paginated (get 5 charges starting at index 20):

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (charges/get-all-charges (common/customer "cu_1mXfGxS9m8") (common/position 5 20))))
```

* If a charge needs to be refunded

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (charges/create-refund "charge-id")))
```

* Upgrade the plan of a customer

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (subscriptions/subscribe-customer (common/plan "plan2") (common/customer "cu_1mXfGxS9m8") (subscriptions/do-not-prorate))))
```

* Unsubscribe a customer from the current plan

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (subscriptions/unsubscribe-customer (common/customer "cu_1mXfGxS9m8") (subscriptions/immediately))))
```

* Delete a customer

```clj
(common/with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
    (common/execute (customers/delete-customer "cu_1mXfGxS9m8")))
```


License
================================

Copyright (C) 2011 Alberto Bengoa

Distributed under the Eclipse Public License, the same as Clojure.
