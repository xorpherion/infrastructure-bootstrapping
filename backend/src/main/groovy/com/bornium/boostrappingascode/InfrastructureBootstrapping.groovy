package com.bornium.boostrappingascode

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Lazy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class InfrastructureBootstrapping {

	static void main(String[] args) {
		SpringApplication.run InfrastructureBootstrapping, args
	}
}
