package com.growthhub.global.client;

import com.growthhub.global.dto.response.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "USER-SERVICE")
public interface RatingClient {

    @GetMapping("/rating/average")
    List<RatingResponse> getRatingAverage(@RequestParam("mentorIds") List<Long> mentorIds);
}
