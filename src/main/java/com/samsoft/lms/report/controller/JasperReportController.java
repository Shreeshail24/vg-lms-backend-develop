package com.samsoft.lms.report.controller;


import java.io.IOException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;

@RestController
@RequestMapping("/jasper_report")
public class JasperReportController {

    	private final Logger log = LoggerFactory.getLogger(JasperReportController.class);
    	
    	@Value("${custom.jasper.url}")
        private String reportServerUrl;
    	
        @Value("${custom.jasper.username}")
        private String reportServerUseranme;

        @Value("${custom.jasper.password}")
        private String reportServerPassword;

        private final RestTemplate restTemplate;

        public JasperReportController(RestTemplateBuilder builder) {
            this.restTemplate = builder.build();
            ((RestTemplate) this.restTemplate).setErrorHandler(
                    new DefaultResponseErrorHandler() {
                        @Override
                        public void handleError(ClientHttpResponse response) throws IOException {
                            if (response.getStatusCode() != HttpStatus.BAD_REQUEST) {
                                super.handleError(response);
                            }
                        }
                    }
                );
        }
        
        //Special_Mension_Account_Two_Report
        @GetMapping("/sma2")
        public ResponseEntity<byte[]> getSmaReport(
            @RequestParam(name = "format", required = true) String format,
            @RequestParam(name = "name", required = true) String name
                   
        ) {
        	System.out.println("reportServerUrl: " + reportServerUrl);
        	System.out.println("name : " + name );
        	System.out.println("format : " + format );
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.reportServerUrl + name + "." + format);
          
            String auth = this.reportServerUseranme + ":" + this.reportServerPassword;
            String base64Creds = Base64.getEncoder().encodeToString(auth.getBytes());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(base64Creds);

            HttpEntity request = new HttpEntity(httpHeaders);

            return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, byte[].class);
        }     
        
       
        //Customerwise_Outstanding_Portfolio
        @GetMapping("/customerwise-outstanding")
        public ResponseEntity<byte[]> getCustomerWiseOutstandingReport(
            @RequestParam(name = "format", required = true) String format,
            @RequestParam(name = "name", required = true) String name
                   
        ) {
        	System.out.println("reportServerUrl: " + reportServerUrl);
        	System.out.println("name : " + name );
        	System.out.println("format : " + format );
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.reportServerUrl + name + "." + format);
          
            String auth = this.reportServerUseranme + ":" + this.reportServerPassword;
            String base64Creds = Base64.getEncoder().encodeToString(auth.getBytes());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth(base64Creds);

            HttpEntity request = new HttpEntity(httpHeaders);

            return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, byte[].class);
        }     
}


