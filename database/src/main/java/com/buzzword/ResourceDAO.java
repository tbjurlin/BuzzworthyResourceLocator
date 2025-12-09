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

import java.util.List;

public interface ResourceDAO {

    /**
     * Set the counterDAO used to get ids for new records.
     * @param counterDAO the data access object for record Ids
     */
    public void setCounterDAO(CounterDAO counterDAO);

    /**
     * A contributor or admin may insert a resource into the database.
     * <p>
     * This resource contains information such as a title, the description, and URL link.
     * It will also contain the ID of the user who uploaded the resource as well as its date of creation.
     * 
     * @param user the credentials of the user inserting the resource
     * @param resource the resource to be inserted
     */
    int insertResource(Credentials user, Resource resource);

    /**
     * A contributor may update their own resource or an admin may update any resource.
     * <p>
     * This resource contains information such as a title, the description, and URL link.
     * It will also contain the ID of the user who uploaded the resource as well as its date of creation.
     * 
     * @param user the credentials of the user editing the resource
     * @param id the ID of the resource to be edited
     * @param resource the resource containing updated information
     */
    void editResource(Credentials user, int id, Resource resource);

    /**
     * A contributor may delete their own resource or an admin may remove any resource.
     * 
     * @param user the credentials of the user removing the resource
     * @param id the ID of the resource to be removed
     */
    void removeResource(Credentials user, int id);

    /**
     * Lists all resources available in the system.
     * 
     * @param user the credentials of the user requesting the list
     * @return list of all resources
     */
    List<Resource> listAllResources(Credentials user);

    /**
     * Retrieves a single resource by its ID.
     * 
     * @param user the credentials of the user requesting the resource
     * @param id the ID of the resource to retrieve
     * @return the resource with the specified ID
     */
    Resource getResourceById(Credentials user, int id);

    /**
     * Lists all resources available in the system, filtered by keywords.
     * 
     * @param user the credentials of the user requesting the list
     * @param keywords the list of keywords to filter resources by
     * @return list of filtered resources
     */
    List<Resource> listResourcesByKeywords(Credentials user, KeywordList keywords);
}