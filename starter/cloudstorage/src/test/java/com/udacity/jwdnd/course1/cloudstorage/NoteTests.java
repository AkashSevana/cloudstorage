package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private HomePage homePage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.homePage = new HomePage(driver);
        // SignUp and Login before all tests

        driver.get("http://localhost:" + this.port + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signUp("Akash", "Sevana", "AkashSevana1", "P@$$w0rd");

        driver.get("http://localhost:" + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("AkashSevana1", "P@$$w0rd");

        driver.get("http://localhost:" + this.port + "/home");
    }

    @AfterEach
    public void afterEach() {
        // Logout after each test
        homePage.logout();
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testEditNote() {
        String noteTitle = "Note title";
        String noteDescription = "Sample description";
        homePage.createNote(noteTitle, noteDescription);
        noteTitle = "Note title (edited)";
        noteDescription = "Sample description (edited)";
        driver.get("http://localhost:" + this.port + "/home");
        homePage.editNote(noteTitle, noteDescription);
        driver.get("http://localhost:" + this.port + "/home");
        boolean isPresent = homePage.isPresentNoteTitle(noteTitle);
        homePage.deleteNote();
        Assertions.assertTrue(isPresent);
    }

    @Test
    public void testCreateNote() {
        String noteTitle = "Note title";
        String noteDescription = "Sample description";
        homePage.createNote(noteTitle, noteDescription);
        driver.get("http://localhost:" + this.port + "/home");
        boolean isPresent = homePage.isPresentNoteTitle(noteTitle);
        homePage.deleteNote();
        Assertions.assertTrue(isPresent);
    }

    @Test
    public void testDeleteNote() {
        String noteTitle = "Note title";
        String noteDescription = "Sample description";
        homePage.createNote(noteTitle, noteDescription);
        driver.get("http://localhost:" + this.port + "/home");
        homePage.deleteNote();
        driver.get("http://localhost:" + this.port + "/home");
        boolean isNotPresent = homePage.isNotPresentNoteTitle(noteTitle);
        Assertions.assertTrue(isNotPresent);
    }

}
