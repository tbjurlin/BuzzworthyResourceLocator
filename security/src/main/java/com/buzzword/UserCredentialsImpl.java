/**
 * This is the customized Credentials Container.
 * A thin wrapper around a JAAS subject object.
 * 
 * @author Janniebeth Melendez
 * @version 1.0
 */
package com.buzzword;

import javax.security.auth.Subject; // represents the identity and security related info. for a single user/service.

public final class UserCredentialsImpl 
    implements UserCredentials {
    
        private Subject subject; //place holder object

        /**
         * This creates an empty UserCredential, with the inner subject holding default values.
         */
        public UserCredentialsImpl()
        {
            //LOG.debug("Constructing a UserCredential object.");
            subject = new Subject();
        }

        /**
         * {@inheritDoc}
         */
        public String[] getRole()
        {
         //LOG.debug("Returning the roles");
            
        }

        /**
         * 
         * {@inheritDoc}
         */
        public boolean hasRole(final String role)
        {
            //LOG.debug("Checking if the role exists");
            
        }
}
