package com.mp;

import com.mercadopago.MercadoPago;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MpSampleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MpSampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MercadoPago.SDK.setAccessToken(System.getenv("MP_ACCESS_TOKEN"));
	}
}
