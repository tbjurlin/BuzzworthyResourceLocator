package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

/**
 * A database access object that manages IDs for records.
 * 
 * @author Ted Bjurlin
 * @version 1.0
 */
public interface CounterDAO {
    
    /**
     * Removes the ID tracking document for the target resource.
     * @param resourceId the ID of the resource whose counters should be removed
     */
    public void removeResourceCounters(int resourceId);

    /**
     * Gets the next available resource ID.
     * @return an available resource ID
     */
    public int getNextResourceId();

    /**
     * Gets the next available comment ID for the given resource.
     * @param resourceId the ID of the resource
     * @return an available comment ID
     */
    public int getNextCommentId(int resourceId);

    /**
     * Gets the next available flag ID for the given resource.
     * @param resourceId the ID of the resource
     * @return an available flag ID
     */
    public int getNextReviewFlagId(int resourceId);

    /**
     * Gets the next available upvote ID for the given resource.
     * @param resourceId the ID of the resource
     * @return an available upvote ID
     */
    public int getNextUpvoteId(int resourceId);
}
