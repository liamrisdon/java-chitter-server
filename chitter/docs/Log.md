Day 1 – 22/11/23

I decided on rough project aims, which are:

- To use the front end of my Chitter challenge (Twitter clone) and
  rebuild the back end using Spring Boot, retaining MongoDB as the data
  persistence layer.
- Implement posting/getting peeps (tweets) and
  login/sign up functionality.
- Do this using TDD.
- Add JWT authentication if time allowed for enhanced security.

Since I had not used Spring Boot before, I began doing some general research. I decided to work through two BezKoder tutorials that walked through building a Spring Boot REST API and testing. The repository I was working on can be found [here](https://github.com/liamrisdon/spring-boot-tutorial-walkthrough).

These allowed me to see an example of a CRUD operations being made to a MongoDB database and analyse what each line of code was doing. I identified further tutorials that looked at adding JWT authentication with Spring Security which I could work through to add authentication to my login/sign up functionality, if time allowed.


Day 2 - 23/11/23

On this day I detailed the requirements for the project and created a testing plan so that I could effectively implement TDD. I initialised and set up the project, but ran into an issue when trying to pass my first test. I had initialised my project with spring security, which I was unfamiliar with, and hoped to implement after basic functionality way added. However, this lead to a 401 status code (Unauthorized) to be return as I had not set up Spring Security correctly. This taught me to not be so eager, and only initialise and begin a project with what is actually needed, rather than making it more complicated than necessary - as there is always scope to build on it later.

This cost me some time as I had to then set up a new project without Spring Security. But by the end of the day I had my first passing test, which tested a GET request to return all peeps.

Day 3 - 24/11/23

I was able to get the main functionality of the API written, developing using TDD and having all of my main unit test passed by the end of the day. However, what still needed to be done was structure the response entity so that the user could be returned for the frontend to then login/signup the user. Therefore, I would need to create classes to structure the res and reqs so that the API could talk to my Chitter frontend.

Day 4 - 25/11/23

On this day I implement a responseHandler class to be able to return the user during login and sign up. This meant I had a working application, with both the frontend and backend working together and passing tests - fulfilling the minimal requirements I had set out.

This meant I could move on to extending the scope of my project. I decided I would look to implement Spring Security and JWT to increase the security of my application, and potentially seek to add edit and deleting functionality at a later point. I would begin this the following day and create a new branch to work on the development of this.