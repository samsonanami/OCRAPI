package com.fintech.oracle.service.apollo.transformer;

/**
 * Created by sasitha on 6/22/17.
 *
 */
public interface ResultTransformer<E, I>{
    E transformResults(I rawResults);
}