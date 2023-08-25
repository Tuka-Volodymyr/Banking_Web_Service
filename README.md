# Banking_Web_Service

Banking_Web_Service  is a Java-based web service that offers a diverse range of functionalities for managing banking operations and overseeing financial resources through an online interface. This service empowers users to conduct banking transactions, such as fund transfers, view account balances and transaction history, ensuring convenience, security.

## Main Features:

* User Registration and Authentication: Users can register for an account and log in securely.
* View Account Balance: Users can check their current account balance.
* Fund Transfers: Users can initiate transfers between their own accounts and to external accounts.
* Transaction History: Users can view transaction history with details like date, amount, and status.
* Account Settings: Users can modify their personal account settings.

## Documentation

### Class AccountController

Description
* AccountController is a class responsible for handling HTTP requests related to bank accounts.

Dependencies
* This controller utilizes the AccountService to perform operations on accounts.

#### Methods

✔️POST /bank/addAccount
* This method allows adding a new bank account to the system.

Parameters:
* The request body contains data for creating an account (Account).

Returns:
* Status code 201 Created on successful account creation.
* Status code 400 Bad Request if the input data is invalid.

Example request:
* POST /bank/addAccount

{
     "fullName": "Tuka Volodymyr",
     "email": "fff@gmail.com",
     "password": "1111"
}


✔️POST /bank/change/password
* This method allows changing the password of an account.

Parameters:
* code (optional) - code for confirming the password change.
* The request body contains data for changing the password (ChangePassword).

Returns:
* Status code 200 OK on successful password change.
* Status code 400 Bad Request if the input data is invalid.
* Status code 401 Unauthorized if the user is not authenticated.
* Status code 403 Unauthorization if the user is not unauthorization.

Example request:
* http://localhost:3306/bank/change/password?code=4596

{
    "email": "tukavovy@gmail.com",
    "newPassword": "1111"
}


✔️POST /bank/lock/account
* This method allows locking an account with the specified email.

Parameters:
* email - email of the account to be locked.

Returns:
* Status code 423 Locked on successful account locking.
* Status code 401 Unauthorized if the user is not authenticated.
*  Status code 403 Unauthorization if the user is not unauthorization.

Example request:
* POST /bank/lock/account?email=john@example.com


✔️POST /bank/unlock/account
* This method allows unlocking a previously locked account with the specified email.

Parameters:
* email - email of the account to be unlocked.

Returns:
* Status code 200 OK on successful account unlocking.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 401 Unauthorized if the user is not authenticated.
  
Example request:
* POST /bank/unlock/account?email=john@example.com

### Class CardController

Description
* CardController is a class responsible for handling HTTP requests related to bank cards and their operations.

Dependencies
* This controller uses the CardService to perform operations on cards.

#### Methods


✔️GET /bank/add/card
* This method allows users to add a new credit card to the bank system.

Parameters:
*Authentication context is used to retrieve the currently authenticated user details (UserDetails).

Returns:
* Status code 201 Created on successful card creation.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 401 Unauthorized if the user is not authenticated.
  
Example request:
* GET /bank/add/card


✔️GET /bank/card/info
* This method allows users to retrieve information about their card.

Parameters:
* Authentication context is used to retrieve the currently authenticated user details (UserDetails).

Returns:
* Status code 200 OK on successful retrieval of card information.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 401 Unauthorized if the user is not authenticated.

Example request:
* GET /bank/card/info


✔️POST /bank/card/balance/add/money
* This method allows users to add money to their card's balance.

Parameters:
* The request body contains data for adding money to the card (MoneyToCard).

Returns:
* Status code 200 OK on successful money addition.
* Status code 400 Bad Request if the input data is invalid.

Example request:
* POST /bank/card/balance/add/money

{
    "card":"4000001571154806",
    "amount":200000
}


✔️POST /bank/card/balance/transfer/money
* This method allows users to transfer money from their card to another card.

Parameters:
* The request body contains data for transferring money (MoneyToCard).
* Authentication context is used to retrieve the currently authenticated user details (UserDetails).

Returns:
* Status code 200 OK on successful money transfer.
* Status code 401 Unauthorized if the user is not authenticated.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 400 Bad Request if the input data is invalid.

Example request:
* POST /bank/card/balance/transfer/money

{
    "card":"4000000187139474",
    "amount":2000
}


✔️GET /bank/card/lock
* This method allows users to lock their card.

Parameters:
* Authentication context is used to retrieve the currently authenticated user details (UserDetails).
  
Returns:
* Status code 423 Locked on successful card locking.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 401 Unauthorized if the user is not authenticated.

Example request:
* GET /bank/card/lock


✔️GET /bank/card/unlock
* This method allows users to unlock their card.

Parameters:
* Authentication context is used to retrieve the currently authenticated user details (UserDetails).

Returns:
* Status code 200 OK on successful card unlocking.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 401 Unauthorized if the user is not authenticated.

Example request:
* GET /bank/card/unlock


✔️POST /bank/card/change/limitOfTurnover
* This method allows users to change the maximum turnover limit for their card.

Parameters:
* The request body contains data for changing the turnover limit (ChangeTurnoverLimit).
* Authentication context is used to retrieve the currently authenticated user details (UserDetails).

Returns:
* Status code 200 OK on successful turnover limit change.
* Status code 401 Unauthorized if the user is not authenticated.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 400 Bad Request if the input data is invalid.

Example request:
* POST /bank/card/change/limitOfTurnover

{
    "limitOfTurnover":1000000,
    "email":"tukavolody@gmail.com"
}

### Class EventController

Description
* EventController is a class responsible for handling HTTP requests related to events, both global events and user transaction history.

Dependencies
* This controller uses the EventService to retrieve event data.

#### Methods


✔️GET /bank/events
* This method allows users to retrieve global events.

Parameters:
* No parameters required.

Returns:
* Status code 200 OK on successful retrieval of global events.
* Status code 401 Unauthorized if the user is not authenticated.
* Status code 403 Unauthorization if the user is not unauthorization.

Example request:
* GET /bank/events

✔️GET /bank/history
* This method allows users to retrieve their transaction history.

Parameters:
* Authentication context is used to retrieve the currently authenticated user details (UserDetails).

Returns:
* Status code 200 OK on successful retrieval of transaction history.
* Status code 403 Unauthorization if the user is not unauthorization.
* Status code 401 Unauthorized if the user is not authenticated.

Example request:
* GET /bank/events

