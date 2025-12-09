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

public interface CommentDAO {

    /**
     * Set the counterDAO used to get ids for new records.
     * @param counterDAO the data access object for record Ids
     */
    public void setCounterDAO(CounterDAO counterDAO);

    /**
     * Allows a user to add a comment to a specific resource.
     * 
     * @param user the credentials of the user adding the comment
     * @param comment the comment to be added
     * @param resourceId the ID of the resource to add the comment to
     */
    int addComment(Credentials user, Comment comment, int resourceId);

    /**
     * Allows a user to edit a comment on a specific resource.
     * 
     * @param user the credentials of the user editing the comment
     * @param commentId the ID of the comment to be edited
     * @param comment the comment containing updated information
     * @param resourceId the ID of the resource containing the comment
     */
    void editComment(Credentials user, int commentId, Comment comment, int resourceId);
    
    /**
     * A user may delete their own comment or an admin may remove any comment.
     * 
     * @param user the credentials of the user removing the comment
     * @param commentId the ID of the comment to be removed
     * @param resourceId the ID of the resource containing the comment
     */
    void removeComment(Credentials user, int commentId, int resourceId);
}