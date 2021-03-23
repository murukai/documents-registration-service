package com.afrikatek.documentsregistrationservice.cucumber;

import com.afrikatek.documentsregistrationservice.DocumentsregistrationserviceApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = DocumentsregistrationserviceApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
