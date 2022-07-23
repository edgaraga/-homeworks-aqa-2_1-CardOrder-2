package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setUp() {

        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUpHeadlessMode() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        //driver = new ChromeDriver(); //проверка на открывание страницы для себя
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void close() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendFormHappyPath() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Белов Игорь");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79510098745");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim()); // trim-удаляет лишние пробелы до и после текста
    }

    @Test
    public void shouldSendFormWithEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+74953459658");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void shouldSendFormWithEmptyPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Максим Белоусов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void shouldSendFormWithOneSymbolInName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ф");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79784561452");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void shouldSendFormWithDoubleSurname() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Тур-Тубельский");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234568536");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void shouldSendFormWithDoubleSurnameWithUnderscores() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Василий Тур_Тубельский");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79478596790");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldSendFormWithWrongValueByNameWithRuAndEn() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Максим Belousov");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+77894125790");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldSendFormWithWrongValueByNameWithSymbolYo() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Артём Фишер");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79856477485");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldSendFormWithWrongValueByNameWithEnSymbols() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Petrov Pavel");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79867482563");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldSendFormWithWrongValueByNameWithNumbers() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Афанасий 2");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78642459690");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldSendFormWithWrongValueByPhoneWith12Numbers() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Белькин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+798745614783");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    public void shouldSendFormWithWrongValueByPhoneWith10Numbers() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Белькин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7987456147");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    public void shouldSendFormWithValueByPhone11DigitsAndWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Грибанов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89019123455");
        driver.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    public void shouldSendFormWithoutCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Андрей Грибанов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79123456790");
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector(".checkbox.input_invalid")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());

    }
}



