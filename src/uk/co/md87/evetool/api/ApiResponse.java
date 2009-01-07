/*
 * Copyright (c) 2009 Chris Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.co.md87.evetool.api;

import uk.co.md87.evetool.api.parser.ApiResult;

/**
 * Represents a response from the EVE API. A response can either be successful,
 * in which case the ApiResponse object will contain one object from the
 * <code>wrapper</code> package which encapsulates the retured data, or
 * unsuccessful, in which case it contains a human-readable error message.
 *
 * @param <T> The type of wrapper that will be used for a successful response
 * @author chris
 */
public class ApiResponse<T> {

    /** The wrapper object (after a successful response). */
    private final T result;

    /** The error message (after an unsuccessful response). */
    private final String error;

    /** The raw result from the API. */
    private final ApiResult apiResult;

    /**
     * Creates a new successful ApiResponse.
     *
     * @param result The wrapper object containing the resulting data
     * @param apiResult The raw API response
     */
    public ApiResponse(final T result, final ApiResult apiResult) {
        this.result = result;
        this.error = null;
        this.apiResult = apiResult;
    }

    /**
     * Creates a new unsuccessful ApiResponse.
     *
     * @param error The human-readable error message
     * @param apiResult The raw API response
     */
    public ApiResponse(final String error, final ApiResult apiResult) {
        this.result = null;
        this.error = error;
        this.apiResult = apiResult;
    }

    /**
     * Determines if this response was successful or not.
     *
     * @return True if the response indicates a success, false otherwise
     */
    public boolean wasSuccessful() {
        return result != null && error == null;
    }

    /**
     * Retrieves the wrapper object for this response.
     *
     * @return The wrapper object if successful, null otherwise
     */
    public T getResult() {
        return result;
    }

    /**
     * Retrieves the error message for this response.
     *
     * @return The error message if unsuccessful, false otherwise
     */
    public String getError() {
        return error;
    }

    /**
     * Retrieves the raw API result associated with this response.
     *
     * @return The raw {@link ApiResult} object.
     */
    public ApiResult getApiResult() {
        return apiResult;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[ApiResponse - " + (wasSuccessful() ? "success" : "failure")
                + " - " + (wasSuccessful() ? result.toString() : error)
                + " - cached for "
                + ((apiResult.getCachedUntil().getTime() - System.currentTimeMillis()) / 60000)
                + " min]";
    }

}
