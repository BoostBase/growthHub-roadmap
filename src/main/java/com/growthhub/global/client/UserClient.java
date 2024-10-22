package com.growthhub.global.client;

import com.growthhub.global.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "AUTH-SERVICE")
public interface UserClient {
    @GetMapping("/users")
    List<UserResponse> getUser(@RequestParam("userIds") List<Long> userIds);
}
