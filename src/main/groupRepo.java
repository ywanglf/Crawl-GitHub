package main;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class groupRepo {
    private static WebDriver driver = null;
    private static CSVWriter writer;
    static {
        try {
            writer = new CSVWriter(new FileWriter("groupRepo.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static List<String[]> list = new ArrayList<>();


    public static void groupUrl(String url) {
        String repoName = url.substring(19,url.indexOf("/",19));

        boolean flag = false;
        for (int i=0; i< list.size(); i++){
            if ((list.get(i))[0].equals(repoName)){
                (list.get(i))[2] += "\n";
                (list.get(i))[2] += url;
                flag = true;
                break;
            }
        }

        if (!flag)
        {
            driver.get(url);
            WebElement webElement = driver.findElement(new By.ById("repo-stars-counter-star"));

            String[] row = new String[3];
            row[0] = repoName;
            row[1] = webElement.getText();
            row[2] = url;

            list.add(row);
        }
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","driver/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().minimize();

        Scanner sc = null;
        try {
            sc = new Scanner(new File("Result_20220116.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] header = {"Repo Name", "Star", "URLs"};
        list.add(header);

        while (sc.hasNext()){
            String url = sc.next();
           //System.out.println(url);
            groupUrl(url);
        }

        writer.writeAll(list);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}
