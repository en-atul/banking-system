package com.proj.customer_service.proxy;

import org.springframework.cloud.openfeign.FeignClient;

//@FeignClient(name = "auth-service", url = "localhost:8080")
@FeignClient(name = "auth-service")
public interface AuthServiceProxy {
}
