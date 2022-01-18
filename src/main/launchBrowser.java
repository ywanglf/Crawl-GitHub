package main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class launchBrowser {
    private static int count = 0;
    public static int MAX_PAGE = 100;
    public static WebDriver driver = null;
    public static FileWriter writer;

    static {
        try {
            writer = new FileWriter("Result_20220116.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static StringBuilder stringBuilder = new StringBuilder();

    public static void openGithub(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://github.com/login");
        WebElement userName = driver.findElement(By.id("login_field"));
        WebElement password = driver.findElement(By.id("password"));
        userName.sendKeys("");
        password.sendKeys("");
        password.submit();
    }

    public static void directToURL(int page) throws IOException {
        ++count;
        System.out.println("page: "+page);
        if (count == 20) {
            count = 0;
            driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(2));
        }
        if (page != 1)
            driver.get("https://github.com/search?o=desc&p="+page+"&q=import+spoon.processing&s=indexed&type=Code");
        else
            driver.get("https://github.com/search?o=desc&q=import+spoon.processing&s=indexed&type=Code");

        //driver.navigate().refresh();
       // driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        //get and save href

        List<WebElement> webElementList = driver.findElements(By.cssSelector(".f4.text-normal"));

        while (webElementList.size() <= 2) {
            System.out.println(">> Empty ");
            driver.navigate().refresh();
            webElementList = driver.findElements(By.cssSelector(".f4.text-normal"));
        }
        for (var i:webElementList){
            String url = i.findElement(By.tagName("a")).getAttribute("href");
            System.out.println(url);
            stringBuilder.append(url);
            stringBuilder.append("\n");
        }
        writer.write(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        writer.flush();
    }


    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver","driver/chromedriver");

        openGithub();

        Duration d = Duration.ofSeconds(1);
        driver.manage().timeouts().implicitlyWait(d);

        //open the web app
        for (int i=1; i<=MAX_PAGE; i++){
            directToURL(i);
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        }
       // writer.write(stringBuilder.toString());
        writer.close();

        driver.quit();
    }
}

