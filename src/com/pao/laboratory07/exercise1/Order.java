package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

public class Order {
    private OrderState currentState;
    private OrderState previousState;
    private final OrderState initialState;

    public Order(OrderState initialState) {
        this.currentState = initialState;
        this.initialState = initialState;
        this.previousState = null;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (currentState == OrderState.DELIVERED || currentState == OrderState.CANCELED) {
            throw new OrderIsAlreadyFinalException();
        }

        OrderState next = currentState.next();
        if (next != null) {
            this.previousState = currentState;
            currentState = next;
            System.out.println("Order state updated to: " + currentState);
        }
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (currentState == OrderState.DELIVERED || currentState == OrderState.CANCELED) {
            throw new CannotCancelFinalOrderException();
        }

        this.previousState = currentState;
        currentState = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (currentState == initialState) {
            throw new CannotRevertInitialOrderStateException();
        }

        if (previousState != null) {
            currentState = previousState;
            System.out.println("Order state reverted to: " + currentState);
            previousState = currentState.previous();
        }
    }
}