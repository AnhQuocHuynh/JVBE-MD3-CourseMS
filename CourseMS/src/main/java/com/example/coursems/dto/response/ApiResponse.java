package com.example.coursems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<ValidationError> errors;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, List<ValidationError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(errors == null || errors.isEmpty() ? null : errors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<PaginatedData<T>> successPage(
            String message,
            List<T> items,
            int currentPage,
            int pageSize,
            long totalItems
    ) {
        int safePageSize = Math.max(pageSize, 1);
        int totalPages = (int) Math.ceil((double) totalItems / safePageSize);
        PaginationMeta pagination = PaginationMeta.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();
        PaginatedData<T> paginatedData = PaginatedData.<T>builder()
                .items(items)
                .pagination(pagination)
                .build();
        return success(message, paginatedData);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}
