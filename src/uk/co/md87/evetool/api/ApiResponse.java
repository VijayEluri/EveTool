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
