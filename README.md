# Super*Duper*Drive Cloud Storage
This project was part of a series on a Java Developer WebDeveloper Nanodegree from Udacity.

The applications had the following requirements:

1. **Simple File Storage:** Upload/download/remove files
2. **Note Management:** Add/update/remove text notes
3. **Password Management:** Save, edit, and delete website credentials.  

Three layers of the application to implement:

1. The back-end with Spring Boot
2. The front-end with Thymeleaf
3. Application tests with Selenium

### The Back-End
The back-end is all about security and connecting the front-end to database data and actions. 

1. Managing user access with Spring Security
3. Handling front-end calls with controllers
5. Making calls to the database with MyBatis mappers



### The Front-End

1. Login page
2. Sign Up page
3. Home page


 i. Files
  - The user should be able to upload files and see any files they previously uploaded. 
  - The user should be able to view/download or delete previously-uploaded files.

 ii. Notes
  - The user should be able to create notes and see a list of the notes they have previously created.
  - The user should be able to edit or delete previously-created notes.

 iii. Credentials
 - The user should be able to store credentials for specific websites and see a list of the credentials they've previously stored.
 - The user should be able to view/edit or delete individual credentials. When the user views the credential, they should be able to see the unencrypted password.

The home page should have a logout button that allows the user to logout of the application and keep their data private.

### Testing
For the app testing, I had to:

1. Write tests for user signup, login, and unauthorized access restrictions.

2. Write tests for note creation, viewing, editing, and deletion.

3. Write tests for credential creation, viewing, editing, and deletion.
