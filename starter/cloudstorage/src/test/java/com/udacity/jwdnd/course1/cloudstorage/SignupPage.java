package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    private WebElement firstName;

    @FindBy(id = "inputLastName")
    private WebElement lastName;

    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "signUpButton")
    private WebElement signupButton;

    public SignupPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void signUp(String firstName, String lastName, String username, String password) {
        this.firstName.sendKeys(firstName);
        this.lastName.sendKeys(lastName);
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        signupButton.click();
    }
}
