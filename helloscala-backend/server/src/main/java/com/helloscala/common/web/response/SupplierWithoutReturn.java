package com.helloscala.common.web.response;

/**
 * @author steve
 */
@FunctionalInterface
public interface SupplierWithoutReturn {
    void call() throws Exception;
}
