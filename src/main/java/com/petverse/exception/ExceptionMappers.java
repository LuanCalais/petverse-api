package com.petverse.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ExceptionMappers {

    @Provider
    public static class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
        @Override
        public Response toResponse(ResourceNotFoundException exception) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Resource Not Found");
            error.put("message", exception.getMessage());
            error.put("timestamp", LocalDateTime.now().toString());
            error.put("status", 404);

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error).build();
        }
    }

    @Provider
    public static class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
        @Override
        public Response toResponse(BusinessException exception) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Business Exception");
            error.put("message", exception.getMessage());
            error.put("timestamp", LocalDateTime.now().toString());
            error.put("status", 400);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error).build();
        }
    }
}
