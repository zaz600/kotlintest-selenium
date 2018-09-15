package io.github.zaz600

import org.openqa.selenium.WebDriver
import java.io.Closeable

class WebDriverCloseable (private val delegate: WebDriver) : WebDriver by delegate, Closeable {
    override fun close() {
        delegate.quit()
    }
}