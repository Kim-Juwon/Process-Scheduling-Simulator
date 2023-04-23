package com.pssimulator.controller;

import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;
import com.pssimulator.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SchedulingController {
    private final SchedulingService schedulingService;

    @PostMapping("/schedule")
    public Response schedule(@RequestBody @Valid Request request) {
        return schedulingService.schedule(request);
    }
}
