package com.example.client;

import com.example.client.access.CurrentUserProvider;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/client")
@ResponseBody
@AllArgsConstructor

@SpringBootApplication
public class ClientApplication
{
	private final CurrentUserProvider currentUserProvider;
	public static void main(String[] args) {SpringApplication.run(ClientApplication.class, args);}

	@GetMapping(path = "access")
	@NonNull
	public ResponseEntity<?> getAccess()
	{
		return ResponseEntity.ok(currentUserProvider.get().isEnabled()?"Access Granted":"Access Denied!");
	}

}
