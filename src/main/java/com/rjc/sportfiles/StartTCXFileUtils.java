
package com.rjc.sportfiles;

import static java.lang.System.exit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rjc.sportfiles.process.Controller;

@SpringBootApplication
public class StartTCXFileUtils implements CommandLineRunner {
		
	@Autowired
	private Controller controller;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StartTCXFileUtils.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
    	
    	controller.process();
        exit(0);
    }

}
