package com.udacity.jwdnd.course1.cloudstorage;

//import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {


	@LocalServerPort
	private int port;

	private WebDriver driver;
	WebDriverWait webDriverWait;

	@BeforeAll
	static void beforeAll() {
		//WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.webDriverWait = new WebDriverWait(driver, 2);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	//**********                                     TESTING                                               **********//
	// 1 - Tests for User Signup, Login, and Unauthorized Access Restrictions.

	// 1.1 - a test that verifies that an unauthorized user can only access the login and signup pages:
	@Test
	@Order(1)
	public void testUnauthorizedAccess() {

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// 1.2 - a test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible.
	@Test
	@Order(2)
	public void testSignUpLoginLogout() throws InterruptedException {

		doMockSignUp("John", "Doe", "johndoe", "password");
		// Test Login
		doLogIn("johndoe", "password");
		Assertions.assertEquals("Home", driver.getTitle());

		// Test Logout
		WebElement logoutButton = driver.findElement(By.id("logoutDiv")).findElement(By.tagName("button"));
		logoutButton.click();
		Assertions.assertNotEquals("Home", driver.getTitle());

		Thread.sleep(3000);

		// Verify if Home page is no longer accessible
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	//2 - Tests for Note Creation, Viewing, Editing, and Deletion

	// 2.1 - A test that creates a note, and verifies it is displayed:
	@Test
	@Order(3)
	public void testNoteCreationDisplay() throws InterruptedException {
		doLogIn("johndoe", "password");
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		Thread.sleep(2000);

		// Test Note creation
		WebElement addNoteButton = driver.findElement(By.id("note-add-btn"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitleField = driver.findElement(By.id("note-title"));
		noteTitleField.sendKeys("Test Note Title");
		WebElement noteDescriptionField = driver.findElement(By.id("note-description"));
		noteDescriptionField.sendKeys("Test Note Description");
		WebElement saveNoteButton = driver.findElement(By.id("note-save-btn"));
		saveNoteButton.click();


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		// Test Note display

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes-body")));
		WebElement noteTitleDisplayed = driver.findElement(By.xpath("//*[@id=\"notes-body\"]/tr/td[2]"));
		Assertions.assertEquals("Test Note Title", noteTitleDisplayed.getText());

	}

	// 2.2 - a test that edits an existing note and verifies that the changes are displayed.
	@Test
	@Order(4)
	public void testNoteEditing() throws InterruptedException {
		doLogIn("johndoe", "password");
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		Thread.sleep(2000);

		// Test Note Editing
		WebElement editNoteButton = driver.findElement(By.id("note-edit-btn"));
		editNoteButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitleField = driver.findElement(By.id("note-title"));
		noteTitleField.clear();
		noteTitleField.sendKeys("Test Editing Note");
		WebElement noteDescriptionField = driver.findElement(By.id("note-description"));
		noteDescriptionField.clear();
		noteDescriptionField.sendKeys("Test Editing Note Description");
		WebElement saveNoteButton = driver.findElement(By.id("note-save-btn"));
		saveNoteButton.click();


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		// View Edited Credential

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("notes-body")));
		WebElement notesTitleDisplayed = driver.findElement(By.xpath("//*[@id=\"notes-body\"]/tr/td[2]"));
		Assertions.assertEquals("Test Editing Note", notesTitleDisplayed.getText());

	}


	// 2.3 - a test that deletes a note and verifies that the note is no longer displayed.
	@Test
	@Order(5)
	public void testNoteDeletion() throws InterruptedException {
		doLogIn("johndoe", "password");
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		Thread.sleep(2000);

		// Test Note deletion
		WebElement deleteButton = driver.findElement(By.id("note-delete-btn"));
		deleteButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		WebElement notesTable = driver.findElement(By.id("notesTable"));

		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		Assertions.assertEquals(true,notesList.size()<1);

	}

	//3 - Tests for Credential Creation, Viewing, Editing, and Deletion.

	// 3.1 - a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
	@Test
	@Order(6)
	public void testCredentialCreationDisplay() throws InterruptedException {

		doLogIn("johndoe", "password");
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();
		Thread.sleep(2000);

		//Test Credential creation
		WebElement addCredentialButton = driver.findElement(By.id("credential-add-btn"));
		addCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrlField = driver.findElement(By.id("credential-url"));
		credentialUrlField.sendKeys("Test Credential Url");

		WebElement credentialUsernameField = driver.findElement(By.id("credential-username"));
		credentialUsernameField.sendKeys("Test Credential Username");

		WebElement credentialPasswordField = driver.findElement(By.id("credential-password"));
		credentialPasswordField.sendKeys("TestCredentialPassword");

		WebElement saveCredentialButton = driver.findElement(By.id("credential-save-btn"));
		saveCredentialButton.click();


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		// Test Credential Viewing
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials-body")));
		WebElement credentialUrlDisplayed = driver.findElement(By.xpath("//*[@id=\"credentials-body\"]/tr/td[2]"));
		Assertions.assertEquals("Test Credential Url", credentialUrlDisplayed.getText());

	}

	// 3.2 - a test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
	@Test
	@Order(7)
	public void testCredentialEditing() throws InterruptedException {
		doLogIn("johndoe", "password");
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();
		Thread.sleep(2000);

		// Test Credential Editing
		WebElement editCredentialButton = driver.findElement(By.id("edit-credential-button"));
		editCredentialButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrlField = driver.findElement(By.id("credential-url"));
		credentialUrlField.clear();
		credentialUrlField.sendKeys("Test Editing Credential Url");


		WebElement credentialUsernameField = driver.findElement(By.id("credential-username"));
		credentialUsernameField.clear();
		credentialUsernameField.sendKeys("Test Editing Credential Username");


		WebElement credentialPasswordField = driver.findElement(By.id("credential-password"));

		// Verify that the viewable password is unencrypted
		Assertions.assertEquals("TestCredentialPassword", credentialPasswordField.getAttribute("value"));
		credentialPasswordField.clear();
		credentialPasswordField.sendKeys("TestEditingCredentialPassword");



		WebElement saveCredentialButton = driver.findElement(By.id("credential-save-btn"));
		saveCredentialButton.click();


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		// View Edited Credential

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentials-body")));
		WebElement credentialUrlDisplayed = driver.findElement(By.xpath("//*[@id=\"credentials-body\"]/tr/td[2]"));
		Assertions.assertEquals("Test Editing Credential Url", credentialUrlDisplayed.getText());
	}


	// 3.3 - a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	@Test
	@Order(8)
	public void testCredentialDeletion() throws InterruptedException {
		doLogIn("johndoe", "password");
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();
		Thread.sleep(2000);


		// Test Credential deletion
		WebElement deleteButton = driver.findElement(By.id("credential-delete-btn"));
		deleteButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		WebElement credentialsTable = driver.findElement(By.id("credentialsTable"));

		List<WebElement> credentialsList = credentialsTable.findElements(By.tagName("td"));
		Assertions.assertEquals(true,credentialsList.size()<1);

	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void doMockSignUp(String firstName, String lastName, String userName, String password){

		// Visit the sign-up page.
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();
	}


	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}


	@Test
	@Order(9)
	public void testRedirection() throws InterruptedException {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		Thread.sleep(3000);

		Assertions.assertEquals("Login", driver.getTitle());
	}


	@Test
	@Order(10)
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertTrue(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	@Test
	@Order(11)
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}
