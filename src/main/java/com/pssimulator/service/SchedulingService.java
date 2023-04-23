package com.pssimulator.service;

import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;
import com.pssimulator.scheduler.Scheduler;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {
    public Response schedule(Request request) {
        Scheduler scheduler = Scheduler.from(request);
        return scheduler.schedule(request);
    }
}
