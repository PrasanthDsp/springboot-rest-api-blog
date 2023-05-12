package com.springboot.blog;

import com.springboot.blog.entity.Role;
import com.springboot.blog.repository.RoleRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Blog Rest Api",
				description = "Blog Application using Springboot RestApi",
				version = "v1.0",
				contact = @Contact(
						name = "Prasanth",
						email = "prasanthsakthiveld@gmail.com",
						url = "https://myaccount.google.com/?hl=en&utm_source=OGB&utm_medium=act"
				),
				license = @License(
						name = "Apache2.0",
						url = "https://www.youtube.com/results?search_query=java+web+development+roadmap"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "It is External Blog Api Documentation",
				url="https://github.com/PrasanthDsp"
		)
)
public class SpringbootRestApiBlogApplication implements CommandLineRunner
{

	@Bean
	public ModelMapper modelMapper(){

		return new ModelMapper();
	}


	public static void main(String[] args) {

		SpringApplication.run(SpringbootRestApiBlogApplication.class, args);
	}


	@Autowired
	private RoleRepository roleRepository;

	/**
	 * @param args incoming main method arguments
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {

		Role adminRole = new Role();
		adminRole.setName("ROLE_ADMIN");
		roleRepository.save(adminRole);

		Role userRole = new Role();
		userRole.setName("ROLE_USER");
		roleRepository.save(userRole);
	}
}
