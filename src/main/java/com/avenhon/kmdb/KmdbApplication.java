package com.avenhon.kmdb;

import com.avenhon.kmdb.Security.CredentialsFlag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KmdbApplication {

	public static void main(String[] args) {
		CredentialsFlag credentialsFlag = new CredentialsFlag(args);

		try {
			credentialsFlag.printCredentials();
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		SpringApplication.run(KmdbApplication.class, args);
	}
}
