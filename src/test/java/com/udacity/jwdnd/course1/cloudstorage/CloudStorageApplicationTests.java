package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CloudStorageApplicationTests {

	private final static String firstName = "Sebastian";
	private final static String lastName = "Zankel";
	private final static String userName = "szankel";
	private final static String password = "password123";
	private final static String noteTitle = "Test title";
	private final static String noteDescription = "Test description";
	private final static String noteTitle2 = "Test title 2";
	private final static String noteDescription2 = "Test description 2";
	private final static String url = "http://www.udacity.com";
	private final static String userName2 = "szankel2";
	private final static String password2 = "password321";
	private final static String url2 = "http://www.google.com";

	@Autowired
	public CredentialMapper credentialMapper;

	@Autowired
	public FileMapper fileMapper;

	@Autowired
	public NoteMapper noteMapper;

	@Autowired
	public CredentialService credentialService;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			this.driver.quit();
		}
		this.credentialMapper.truncate();
		this.fileMapper.truncate();
		this.noteMapper.truncate();
	}

	// Test 1: An unauthorized user can only access the login and signup pages
	@Test
	public void test1() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Test 2: Signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that
	// the home page is no longer accessible
	@Test
	public void test2() throws InterruptedException {
		this.signUp();
		this.login();
		Assertions.assertEquals("Home", driver.getTitle());
		this.logout();
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Creates a note, and verifies it is displayed.
	@Test
	public void test3() {
		this.signUp();
		this.login();
		this.openSlide("note");
		this.createNote();
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("note");
		WebElement notesTable = driver.findElement(By.cssSelector("#nav-notes table"));
		List<WebElement> notesList = notesTable.findElements(By.cssSelector("tbody tr"));
		Assertions.assertEquals(1, notesList.size());
	}

	// Edits an existing note and verifies that the changes are displayed.
	@Test
	public void test4() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		this.signUp();
		this.login();
		this.openSlide("note");
		this.createNote();
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("note");
		WebElement editButton = driver.findElement(By.cssSelector("#nav-notes .table tbody tr button.btn-success"));
		wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
		this.setNoteContent(noteTitle2, noteDescription2);
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("note");
		editButton = driver.findElement(By.cssSelector("#nav-notes .table tbody tr button.btn-success"));
		wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
		WebElement notesTable = driver.findElement(By.cssSelector("#nav-notes table"));
		List<WebElement> notesList = notesTable.findElements(By.cssSelector("tbody tr"));
		String title = notesList.get(0).findElement(By.cssSelector("th")).getText();
		Assertions.assertEquals(noteTitle2, title);
		String description = notesList.get(0).findElement(By.cssSelector("td:last-child")).getText();
		Assertions.assertEquals(noteDescription2, description);
	}

	// Deletes a note and verifies that the note is no longer displayed
	@Test
	public void test5() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		this.signUp();
		this.login();
		this.openSlide("note");
		this.createNote();
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("note");
		WebElement deleteButton = driver.findElement(By.cssSelector("#nav-notes .table tbody tr a.btn-danger"));
		wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("note");
		WebElement newNote = driver.findElement(By.cssSelector("#nav-notes button"));
		wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
		WebElement notesTable = driver.findElement(By.cssSelector("#nav-notes table"));
		List<WebElement> notesList = notesTable.findElements(By.cssSelector("tbody tr"));
		Assertions.assertEquals(0, notesList.size());
	}

	// Creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted
	@Test
	public void test6() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		this.signUp();
		this.login();
		this.openSlide("credential");
		this.createCredential(url, userName, password);
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("credential");
		WebElement credentialsTable = driver.findElement(By.cssSelector("#nav-credentials table"));
		List<WebElement> credentialList = credentialsTable.findElements(By.cssSelector("tbody tr"));
		Assertions.assertEquals(1, credentialList.size());
		WebElement _password = credentialsTable.findElement(By.cssSelector("tbody td:last-child"));
		Assertions.assertNotEquals(_password.getText(), password);
	}

	// Views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
	@Test
	public void test7() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		this.signUp();
		this.login();
		this.openSlide("credential");
		this.createCredential(url, userName, password);
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("credential");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("td"))));
		WebElement credentialsTable = driver.findElement(By.cssSelector("#nav-credentials table"));
		List<WebElement> credentialsList = credentialsTable.findElements(By.cssSelector("tbody tr"));
		String _url = credentialsList.get(0).findElements(By.cssSelector("td")).get(1).getText();
		Assertions.assertEquals(url, _url);
		String _username = credentialsList.get(0).findElements(By.cssSelector("td")).get(2).getText();
		Assertions.assertEquals(userName, _username);
		String _password = credentialsList.get(0).findElements(By.cssSelector("td")).get(3).getText();
		Assertions.assertNotEquals(password, _password);
		WebElement editCredential = driver.findElement(By.cssSelector("#nav-credentials button.btn-success"));
		wait.until(ExpectedConditions.elementToBeClickable(editCredential)).click();
		WebElement _credentialPassword = driver.findElement(By.id("credential-password"));
		wait.until(ExpectedConditions.elementToBeClickable(_credentialPassword));
		_credentialPassword = driver.findElement(By.id("credential-password"));
		Assertions.assertEquals(password, _credentialPassword.getAttribute("value"));
		this.setCredentialContent(url2, userName2, password2);
		this.openSlide("credential");
		Thread.sleep(1000);
		credentialsTable = driver.findElement(By.cssSelector("#nav-credentials table"));
		credentialsList = credentialsTable.findElements(By.cssSelector("tbody tr"));
		_url = credentialsList.get(0).findElements(By.cssSelector("td")).get(1).getText();
		Assertions.assertEquals(url2, _url);
		_username = credentialsList.get(0).findElements(By.cssSelector("td")).get(2).getText();
		Assertions.assertEquals(userName2, _username);
		_password = credentialsList.get(0).findElements(By.cssSelector("td")).get(3).getText();
		String decryptedPassword = this.credentialService.decryptPassword(_password);
		Assertions.assertEquals(decryptedPassword, password2);
	}

	// Deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	@Test
	void test8 () {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		this.signUp();
		this.login();
		this.openSlide("credential");
		this.createCredential(url, userName, password);
		Assertions.assertEquals("Result", driver.getTitle());
		this.openSlide("credential");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("td"))));
		WebElement deleteCredential = driver.findElement(By.cssSelector("#nav-credentials .btn-danger"));
		deleteCredential.click();
		this.openSlide("credential");
		WebElement credentialTable = driver.findElement(By.cssSelector("#nav-notes table"));
		List<WebElement> credentialList = credentialTable.findElements(By.cssSelector("tbody tr"));
		Assertions.assertEquals(0, credentialList.size());
	}


	private void signUp() {
		driver.get("http://localhost:" + this.port + "/signup");
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.sendKeys(firstName);
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.sendKeys(lastName);
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement signUpButton = driver.findElement(By.cssSelector("button"));
		signUpButton.click();
	}

	private void login() {
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.cssSelector("button"));
		loginButton.click();
	}

	private void logout() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement logoutButton = driver.findElement(By.cssSelector("#logoutDiv button"));
		logoutButton.click();
	}

	private void openSlide(String slideName) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.get("http://localhost:" + this.port + "/home?slide=" + slideName);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("tab-content"))));
	}

	private void createNote() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		WebElement newNote = driver.findElement(By.cssSelector("#nav-notes button"));
		wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
		this.setNoteContent(noteTitle, noteDescription);
	}

	private void createCredential(String url, String username, String password) {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		WebElement newCredential = driver.findElement(By.cssSelector("#nav-credentials button"));
		wait.until(ExpectedConditions.elementToBeClickable(newCredential)).click();
		this.setCredentialContent(url, userName, password);
	}

	private void setNoteContent(String title, String description) {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		WebElement _noteTitle = driver.findElement(By.id("note-title"));
		_noteTitle.clear();
		_noteTitle.sendKeys(title);
		WebElement _noteDescription = driver.findElement(By.id("note-description"));
		_noteDescription.clear();
		_noteDescription.sendKeys(description);
		WebElement saveChanges = driver.findElement(By.cssSelector("#noteModal .btn-primary"));
		saveChanges.click();
	}

	private void setCredentialContent(String url, String userName, String password) {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
		WebElement _credentialUrl = driver.findElement(By.id("credential-url"));
		_credentialUrl.clear();
		_credentialUrl.sendKeys(url);
		WebElement _credentialUsername = driver.findElement(By.id("credential-username"));
		_credentialUsername.clear();
		_credentialUsername.sendKeys(userName);
		WebElement _credentialPassword = driver.findElement(By.id("credential-password"));
		_credentialPassword.clear();
		_credentialPassword.sendKeys(password);
		WebElement saveChanges = driver.findElement(By.cssSelector("#nav-credentials .btn-primary"));
		saveChanges.click();
	}

}
