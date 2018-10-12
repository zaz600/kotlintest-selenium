package io.github.zaz600

import io.kotlintest.data.forall
import io.kotlintest.extensions.TestListener
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.concurrent.TimeUnit

fun createWebDriver() : WebDriver{
    val op = ChromeOptions()
    op.addArguments("test-type")
    op.setAcceptInsecureCerts(true)
    op.setHeadless(true)
    return ChromeDriver(op)
}


class TestSignup : StringSpec(), TestListener {
    private val driver: WebDriver = autoClose(WebDriverCloseable(createWebDriver()))
    private val signupPage = SignupPage(driver)
    private val wait = WebDriverWait(driver, 10)


    init {
        driver.manage()?.timeouts()?.implicitlyWait(10, TimeUnit.SECONDS)
        driver.manage()?.window()?.maximize()

        "Страница регистрации открывается" {
            signupPage.run {
                open()
                verifyUrl()
            }
        }

        "Желаемый почтовый адрес. Невозможность задать значение меньше 4-х символов." {
            signupPage.run {
                open()
                emailInput.sendKeys("abc")
                password.click()
                wait.until { invalidLoginLengthDiv.isDisplayed }
            }
        }

        "Желаемый почтовый адрес. Невозможность задать значение больше 31 символа" {
            val str32 = "ee1439eb06e9457d86cbf1707a2937da"
            val str31 = str32.substring(0, 31)
            signupPage.run {
                with(emailInput) {
                    clear()
                    sendKeys(str32)
                }
                password.click()
                invalidLoginLengthDiv.isDisplayed.shouldBeFalse()
                emailInput.getAttribute("value").shouldBe(str31)
            }
        }

        "Желаемый почтовый адрес. Невозможность вводить кириллицу" {
            signupPage.apply {
                emailInput.apply {
                    clear()
                    sendKeys("моймейл")
                }
                password.click()
                wait.until { invalidLoginCyrillicCharsDiv.isDisplayed }
            }
        }

        "Желаемый почтовый адрес. Отображение ошибки при вводе недопустимых символов" {
            forall(
                    row("qqq@12345"),
                    row("qqq@12_345"),
                    row("qqq#12345")
            ) { email ->
                signupPage.run {
                    emailInput.apply {
                        clear()
                        sendKeys(email)
                    }
                    passwordRetry.click()
                    wait.until { invalidLoginCharsDiv.isDisplayed }
                }
            }
        }
    }
}

