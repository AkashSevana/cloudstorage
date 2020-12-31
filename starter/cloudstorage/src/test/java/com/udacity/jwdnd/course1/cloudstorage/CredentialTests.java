package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {

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
    public void testCreateCredentials() {
        String url = "https://www.franko.com";
        String username = "razor657";
        String password = "raze712";

        homePage.createCredential(url, username, password);
        driver.get("http://localhost:" + this.port + "/home");

        boolean passwordMatch = homePage.checkPassword(password);
        homePage.deleteCredential(url, username, password);
        Assertions.assertFalse(passwordMatch, "verify password is encrypted");
    }

    @Test
    public void testEditCredentials() {
        String url = "https://www.franko.com";
        String username = "razor657";
        String password = "raze712";

        homePage.createCredential(url, username, password);
        driver.get("http://localhost:" + this.port + "/home");

        String decryptedPassword = homePage.viewCredential();
        Assertions.assertEquals(password, decryptedPassword, "verify password is decrypted");
        driver.get("http://localhost:" + this.port + "/home");

        username = "akser657";
        password = "aks1234ui";
        homePage.editCredential(url, username, password);
        driver.get("http://localhost:" + this.port + "/home");

        boolean isPresent = homePage.checkIsCredentialPresent(url, username);
        homePage.deleteCredential(url, username, password);
        Assertions.assertTrue(isPresent, "verify record edited");
    }

    @Test
    public void testDeleteCredentials() {
        String url = "https://www.franko.com";
        String username = "razor657";
        String password = "raze712";

        homePage.createCredential(url, username, password);
        driver.get("http://localhost:" + this.port + "/home");

        homePage.deleteCredential(url, username, password);
        driver.get("http://localhost:" + this.port + "/home");

        boolean isNotPresent = homePage.checkIsCredentialNotPresent(url, username);
        Assertions.assertTrue(isNotPresent, "verify record doesn't exist");
    }
}
