# Requirements

## Server Layer Stories
As the server...
```
I want to request all peeps from the "/" root,
so that all peeps can be displayed on the homepage.

I want to pass name, username, peep content, date and time to front end,
so that the correct infomation can be displayed.

I want to be able to recieve post requests on the "/" root with newPeep details,
so that a new peep can be posted to the Shouty Box.

I want to be able to recieve post requests on the "/signup" root with user details,
so that new user can sign up.

I want to be able to recieve post requests on the "/login" route with username and password,
so that users are able to sign in.
```

## Testing Plan
#### Get "/" allPeeps
- [/] Returns all peeps with 200 status code
- [/] Returns no peeps message with 204 status code if there are no peeps in database

#### Post "/" newPeep
(Because of the way the front end is designed, the newPeep component is not displayed on the homepage unless the user is logged in, therefore do not need to test if user data is supplied, since jsx element will not be displayed if that is the case)
- [/] Returns 201 status code with new Peep when posted
- [/] Returns error message when there is no Peep content (204)

#### Post "/login" loginUser
- [/] Returns 200 status code and success message of successful login
- [/] Returns error message when there is an invalid password given (400)
- [/] Returns error message when there is an invalid username given (400)

#### Post "/signup" signUpUser
- [/] Returns 201 status code and success message of successful signup
- [/] Returns error message when the email is already taken (400)
- [/] Returns error message when the username is already taken (400)


## Routes
| Method | URL |  Action | Res/Req | Error Res |
|--|--|--|--|--|
| GET | '/' | get all peeps | **res**: {[peeps]} | { error } |
| POST | '/' | post new peep | **req**: { _id, username, name, content, dateCreated } **res**: {PeepModel: _id, username, name, content, dateCreated} | { message : "cannot post a new peep" } |
| POST | '/login' | authenticate user | **req**: { username, password } **res**: { message, user} | { message: "Login failed" } |
| POST | '/signup' | create new user | req: { newUser: {user}} res: {message: "sign up successful}| { error } |

## Further Scope for the Project
- Implement Spring Security and JWT
- Add functionality to edit and delete peeps

## Extended Requirements
After a functioning application - the further scope for the project can be implemented. This will include adding Spring Security and JWT for authentication, and incorporating different roles so that there is an admin user who can act as a moderator and edit or delete peeps.
This will be developed with BDD, as the existing unit tests should be able to test that the secured controller is working as MockMvc is automatically configured with the necessary filter chain.
Additionally testing will be added for the addition of roles and update and delete functionalities as they are implemented.

## Extended User Stories
As the server ...
```
I want to have structured login and signup requests
So that the validation and authentication can take place on these requests.

I want to have my application utilise JWT on Reqs and Res
So that secure authentication and authorization can take place.

I want to allow for users to be stored as either user or admin
So that I can allow for different types of users.

I want to allow admin users to edit peeps,
So that admins are able to moderate peep content.

I want to allow admin users to delete peeps,
So that admins are able to moderate peep content.

I want to not allow users to edit or delete peeps,
So that this functionality is reserved for admin users.

```

#### Payloads
- Requests:
LoginRequest: { username, password }
SignUpRequest: { username, name, email, password }

- Responses:
JwtResponse: {token, type, id, username, name, email, roles }
MessageResponseL { message }



