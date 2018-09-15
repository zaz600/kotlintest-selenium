package io.github.zaz600

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait

class SignupPage(private val driver: WebDriver) {

    private val pageUrl = "https://account.mail.ru/signup"

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(css = "input[data-blockid='email_name']")
    lateinit var emailInput: WebElement

    @FindBy(name = "password")
    lateinit var password: WebElement

    @FindBy(css = "div[class~='js-invalid_login_invalid_length']")
    lateinit var invalidLoginLengthDiv: WebElement

    @FindBy(css = "div[class~='js-invalid_login']")
    lateinit var invalidLoginCharsDiv: WebElement

    @FindBy(css = "div[class~='js-invalid_cyrillic']")
    lateinit var invalidLoginCyrillicCharsDiv: WebElement

    fun open() = driver.get(pageUrl)

    fun verifyUrl() {
        WebDriverWait(driver, 10).until { it.currentUrl == pageUrl }
    }

}
