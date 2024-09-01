package com.devops.accomodation_service.service.feignClients;

import com.devops.accomodation_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${user-service.ribbon.listOfServers}")
public interface UserClient {

    @GetMapping("/api/internal/users/{id}")
    UserDto findById(@PathVariable UUID id);
}
