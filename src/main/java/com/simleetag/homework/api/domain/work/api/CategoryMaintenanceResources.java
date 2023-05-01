package com.simleetag.homework.api.domain.work.api;

public class CategoryMaintenanceResources {
    public static class Request {
        public record Category(
                Long homeId,
                String name,
                com.simleetag.homework.api.domain.work.Category.CategoryType type
        ) {

        }
    }
}
