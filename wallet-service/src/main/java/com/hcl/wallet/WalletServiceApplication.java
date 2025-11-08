package com.hcl.wallet;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author srinivasa
 */

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring boot Wallet API Documentation",
				description = "Spring boot Wallet API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Srini",
						email = "Tupakula",
						url = "www.test.com"
				),
				license = @License(
						name = "HCL Hackathon",
						url = "www.hcl.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring boot Wallet API Documentation",
				url = "https://www.wallets.com"
		)
)
public class WalletServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletServiceApplication.class, args);
	}

}
