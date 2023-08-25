# Banking_Web_Service

Banking_Web_Service  is a Java-based web service that offers a diverse range of functionalities for managing banking operations and overseeing financial resources through an online interface. This service empowers users to conduct banking transactions, such as fund transfers, view account balances and transaction history, ensuring convenience, security.

## Main Features:

User Registration and Authentication: Users can register for an account and log in securely.

View Account Balance: Users can check their current account balance.

Fund Transfers: Users can initiate transfers between their own accounts and to external accounts.

Transaction History: Users can view transaction history with details like date, amount, and status.

Account Settings: Users can modify their personal account settings.

## Documentation

### Class AccountController

Description: accountController is a class responsible for handling HTTP requests related to bank accounts.

Dependencies: this controller utilizes the AccountService to perform operations on accounts.

#### Methods

POST /bank/addAccount

This method allows adding a new bank account to the system.

Parameters:

The request body contains data for creating an account (Account).

Returns:

Status code 201 Created on successful account creation.

Status code 400 Bad Request if the input data is invalid.

Example request:

POST /bank/addAccount

{
    "fullName":"Tuka Volodymyr",
    "email": "fff@gmail.com",
    "password": "1111"
}

POST /bank/change/password

This method allows changing the password of an account.

Parameters:

code (optional) - code for confirming the password change.

The request body contains data for changing the password (ChangePassword).

Returns:

Status code 200 OK on successful password change.

Status code 400 Bad Request if the input data is invalid.

Example request:

http://localhost:3306/bank/change/password?code=4596

{
    "email": "tukavovy@gmail.com",
    "newPassword": "1111"
}

POST /bank/lock/account

This method allows locking an account with the specified email.

Parameters:

email - email of the account to be locked.

Returns:

Status code 423 Locked on successful account locking.

Example request:

POST /bank/lock/account?email=john@example.com

POST /bank/unlock/account

This method allows unlocking a previously locked account with the specified email.

Parameters:

email - email of the account to be unlocked.

Returns:

Status code 200 OK on successful account unlocking.

Example request:

POST /bank/unlock/account?email=john@example.com




