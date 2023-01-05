package com.company.junit4_parameterization;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class Prototype_JUnit4ParamsWithSelenium {
    @Parameter(value=0) public String inputA;
    @Parameter(value=1) public String inputB;
    @Parameter(value=2) public String inputC;
    @Parameter(value=3) public String inputD;
    private WebDriver driver;
    private static String[] tokens;
    private static final List<String> datasetAsList = new ArrayList<>();
    private static final int FIELD_1_INDEX = 0;
    private static final int FIELD_2_INDEX = 1;

    @BeforeClass
    public static void beforeClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void beforeTest() {
        driver = RemoteWebDriver.builder().oneOf(new ChromeOptions()).build();
        new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void afterTest() {
        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
        driver.quit();
    }

    @Parameters(name = "{index}: firstTestScenario()")
    public static List<Object> sampleData() {
        BufferedReader fileReader = null;   // Ignores the first line header record
        String line = "";
        try {
            fileReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\Sample_Data.csv"));
            fileReader.readLine();

            while ((line = fileReader.readLine()) != null) {
                tokens = line.split(",");
                if(tokens.length > 0) {
                    // Cleansing happens here
                    if(tokens[FIELD_1_INDEX].contains("a\'")) {
                        tokens[FIELD_1_INDEX].replaceAll("a\'","");
                    }
                    if(tokens[FIELD_2_INDEX].contains("n\'")) {
                        tokens[FIELD_2_INDEX].replaceAll("n\'","");
                    }
                }
                datasetAsList.addAll(Arrays.asList(tokens));
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int rowSize = 3 - 1;
        int colSize = 4;
        int k = 0;
        String arr[][] = new String[rowSize][colSize];

        for (int i=0; i<rowSize; i++) {
            for (int j=0; j<colSize; j++)
                arr[i][j] = datasetAsList.get(k++);
        }
        return Arrays.asList(arr);
    }

    @Test
    public void firstTestScenario() {
        seleniumMethod1();
        seleniumMethod2();
        seleniumMethod3();
    }

    private void seleniumMethod1() {
        driver.get("http://www.google.com");
        driver.findElement(By.id("L2AGLb")).click();
    }

    private void seleniumMethod2() {
        driver.findElement(By.tagName("input")).sendKeys(inputA + " " +  inputB + " " + inputC+ " " + inputD);
    }

    private void seleniumMethod3() {
        driver.findElement(By.xpath("//div[@class='FPdoLc lJ9FBc']//input[@name='btnK']")).click();
    }
}