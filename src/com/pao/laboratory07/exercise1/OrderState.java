package com.pao.laboratory07.exercise1;

public enum OrderState {
    PLACED,
    PROCESSED,
    SHIPPED,
    DELIVERED,
    CANCELED;

    public OrderState next() {
        return switch (this) {
            case PLACED -> PROCESSED;
            case PROCESSED -> SHIPPED;
            case SHIPPED -> DELIVERED;
            default -> null;
        };
    }

    public OrderState previous() {
        return switch (this) {
            case PROCESSED -> PLACED;
            case SHIPPED -> PROCESSED;
            case DELIVERED -> SHIPPED;
            default -> null;
        };
    }
}