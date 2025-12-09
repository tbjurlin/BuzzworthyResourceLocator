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
 * This is the interface for the flag data access object.
 * 
 * 
 *  @author Janniebeth Melendez
 *  @since 1.0
 */
public interface FlagDAO {

    /**
     * Set the counterDAO used to get ids for new records.
     * @param counterDAO the data access object for record Ids
     */
    public void setCounterDAO(CounterDAO counterDAO);

    /**
     * Adds a review flag to the database for the specific resource for managerial review.
     * 
     * @param user the credentials of the user adding the flag
     * @param flag the review flag to be added
     * @param resourceId the ID of the resource to be flagged for review
     */
    int addReviewFlag(Credentials user, ReviewFlag flag, int resourceId);

    /**
     * Edits a review flag in the database for the specific resource for managerial review.
     * 
     * @param user the credentials of the user editing the flag
     * @param flagId the ID of the review flag to be edited
     * @param flag the review flag containing updated information
     * @param resourceId the ID of the resource containing the flag
     */
    void editReviewFlag(Credentials user, int flagId, ReviewFlag flag, int resourceId);

    /**
     * After a manager has reviewed the resource, they can remove the review flag from the resource.
     * 
     * @param user the credentials of the user removing the flag
     * @param flagId the ID of the flag to be removed
     * @param resourceId the ID of the resource to have the review flag removed
     */
    void removeReviewFlag(Credentials user, int flagId, int resourceId);

}