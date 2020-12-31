package com.udacity.jwdnd.course1.cloudstorage;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.regex.Pattern;

public class HomePage {

    @FindBy(id = "logoutButton", how = How.ID)
    private WebElement logoutButton;

    @FindBy(id = "createNote", how = How.ID)
    private WebElement createNote;

    @FindBy(id = "createCredential", how = How.ID)
    private WebElement createCredential;

    @FindBy(id = "editNote", how = How.ID)
    private WebElement editNote;

    @FindBy(id = "editCredential", how = How.ID)
    private WebElement editCredential;

    @FindBy(id = "deleteNote", how = How.ID)
    private WebElement deleteNote;

    @FindBy(id = "deleteCredential", how = How.ID)
    private WebElement deleteCredential;

    @FindBy(id = "nav-notes-tab", how = How.ID)
    private WebElement notesTab;

    @FindBy(id = "nav-credentials-tab", how = How.ID)
    private WebElement credentialsTab;

    @FindBy(id = "note-title", how = How.ID)
    private WebElement noteTitle;

    @FindBy(id = "note-description", how = How.ID)
    private WebElement noteDescription;

    @FindBy(id = "noteSaveChanges", how = How.ID)
    private WebElement noteSubmit;

    @FindBy(name = "tableNoteTitle", how = How.NAME)
    private WebElement tableNoteTitle;

    @FindBy(id = "credential-url", how = How.ID)
    private WebElement url;

    @FindBy(id = "credential-username", how = How.ID)
    private WebElement username;

    @FindBy(id = "credential-password", how = How.ID)
    private WebElement password;

    @FindBy(id = "credentialSave", how = How.ID)
    private WebElement credentialSave;

    private WebDriverWait wait;

    @FindBy(id = "tablePassword", how = How.ID)
    private WebElement tablePassword;

    @FindBy(id = "tableUsername", how = How.ID)
    private WebElement tableUsername;

    @FindBy(id = "tableUrl", how = How.ID)
    private WebElement tableUrl;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        wait = new WebDriverWait(webDriver, 10);
    }

    public void logout() {
        this.logoutButton.click();
    }

    public void createNote(String noteTitle, String noteDescription) {
        notesTab.click();
        this.createNote = wait.until(ExpectedConditions.elementToBeClickable(By.id("createNote")));
        this.createNote.click();
        submitNoteForm(noteTitle, noteDescription);
    }

    public void createCredential(String url, String username, String password) {
        credentialsTab.click();
        this.createCredential = wait.until(ExpectedConditions.elementToBeClickable(By.id("createCredential")));
        this.createCredential.click();
        submitCredentialForm(url, username, password);
    }

    public void editCredential(String url, String username, String password) {
        credentialsTab.click();
        this.editCredential = wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential")));
        this.editCredential.click();
        submitCredentialForm(url, username, password);
    }

    public String viewCredential() {
        credentialsTab.click();
        this.editCredential = wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential")));
        this.editCredential.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("credential-password")));
        return password.getAttribute("value");
    }

    public void deleteCredential(String url, String username, String password) {
        credentialsTab.click();
        this.deleteCredential = wait.until(ExpectedConditions.elementToBeClickable(By.id("deleteCredential")));
        this.deleteCredential.click();
    }

    public boolean checkPassword(String password) {
        credentialsTab.click();
        this.password = wait.until(ExpectedConditions.visibilityOf(this.tablePassword));
        return password.equalsIgnoreCase(this.password.getText());
    }

    public void editNote(String noteTitle, String noteDescription) {
        notesTab.click();
        editNote = wait.until(ExpectedConditions.visibilityOf(editNote));
        editNote.click();
        submitNoteForm(noteTitle, noteDescription);
    }

    public void deleteNote() {
        notesTab.click();
        deleteNote = wait.until(ExpectedConditions.visibilityOf(deleteNote));
        deleteNote.click();
    }

    public boolean isPresentNoteTitle(String noteTitle) {
        notesTab.click();
        return wait.until(ExpectedConditions.textToBe(By.name("tableNoteTitle"), noteTitle));
    }

    private void submitNoteForm(String noteTitle, String noteDescription) {
        this.noteTitle = wait.until(ExpectedConditions.visibilityOf(this.noteTitle));
        this.noteTitle.clear();
        this.noteTitle.sendKeys(noteTitle);
        this.noteDescription = wait.until(ExpectedConditions.visibilityOf(this.noteDescription));
        this.noteDescription.clear();
        this.noteDescription.sendKeys(noteDescription);
        this.noteSubmit = wait.until(ExpectedConditions.elementToBeClickable(this.noteSubmit));
        this.noteSubmit.click();
    }

    private void submitCredentialForm(String url, String username, String password) {
        this.url = wait.until(ExpectedConditions.visibilityOf(this.url));
        this.url.clear();
        this.url.sendKeys(url);
        this.username = wait.until(ExpectedConditions.visibilityOf(this.username));
        this.username.clear();
        this.username.sendKeys(username);
        this.password = wait.until(ExpectedConditions.visibilityOf(this.password));
        this.password.clear();
        this.password.sendKeys(password);
        this.credentialSave = wait.until(ExpectedConditions.visibilityOf(this.credentialSave));
        this.credentialSave.click();
    }

    public boolean isNotPresentNoteTitle(String noteTitle) {
        return !this.noteTitle.getText().equals(noteTitle);
    }

    public boolean checkIsCredentialPresent(String url, String username) {
        credentialsTab.click();
        this.tableUrl = wait.until(ExpectedConditions.visibilityOf(this.tableUrl));
        this.tableUsername = wait.until(ExpectedConditions.visibilityOf(this.tableUsername));
        return tableUrl.getText().equals(url)
                && tableUsername.getText().equals(username);
    }

    public boolean checkIsCredentialNotPresent(String url, String username) {
        credentialsTab.click();
        try {
            return !this.tableUrl.getText().equals(url);
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}
