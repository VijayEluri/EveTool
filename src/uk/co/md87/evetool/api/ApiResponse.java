/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api;

import uk.co.md87.evetool.api.parser.ApiResult;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiResponse<T> {

    private final T result;
    private final String error;
    private final ApiResult apiResult;

    public ApiResponse(final T result, final ApiResult apiResult) {
        this.result = result;
        this.error = null;
        this.apiResult = apiResult;
    }

    public ApiResponse(final String error, final ApiResult apiResult) {
        this.result = null;
        this.error = error;
        this.apiResult = apiResult;
    }

    public boolean wasSuccessful() {
        return result != null && error == null;
    }

    public T getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public ApiResult getApiResult() {
        return apiResult;
    }

    @Override
    public String toString() {
        return "[ApiResponse - " + (wasSuccessful() ? "success" : "failure")
                + " - " + (wasSuccessful() ? result.toString() : error)
                + " - cached for "
                + ((apiResult.getCachedUntil().getTime() - System.currentTimeMillis()) / 60000)
                + " min]";
    }

}
