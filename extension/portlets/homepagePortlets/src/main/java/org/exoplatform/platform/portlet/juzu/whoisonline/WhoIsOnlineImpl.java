package org.exoplatform.platform.portlet.juzu.whoisonline;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.forum.service.ForumService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.user.UserStateModel;
import org.exoplatform.services.user.UserStateService;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.service.LinkProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WhoIsOnlineImpl
 * This class implements the class WhoIsOnline. 
 * Implementation of the method getFriends. 
 * It gets userId and returns a list of users.
 * @author <a href="rtouzi@exoplatform.com">rtouzi</a>
 * @date 07/12/12
 */
public class WhoIsOnlineImpl implements WhoIsOnline {
    
  private static final Log LOG = ExoLogger.getLogger(WhoIsOnlineImpl.class);

  /**
   *It gets the user id and returns a list of objects user
   */
    public List<User> getFriends(String userId) {
     
      /**list of users*/ 
      List<User> userOnLineList = new ArrayList<User>();
       
      if (userId == null) return userOnLineList;
        
        try {
         
            ExoContainer container = ExoContainerContext.getCurrentContainer();
            IdentityManager identityManager = (IdentityManager) container.getComponentInstanceOfType(IdentityManager.class);
            UserStateService userStateService = (UserStateService) container.getComponentInstanceOfType(UserStateService.class);
            
            /**A list UserStateModel where this class administrates 
              *the characteristics of the connection of the user
              *Method online initialize the objects of UserStateModel
              */ 
            List<UserStateModel> users = userStateService.online();             
            Collections.reverse(users);
           

            User userOnLine = null;
            
           /**
            * �ccess the UserStateModel objects and gives the characteristics of
            * the connection, UserId, lastActivity , status.
            */
            for (UserStateModel userModel : users) {
              
                String user = userModel.getUserId();
                String superUserName = System.getProperty("exo.super.user");
               
                if (user.equals(userId) || user.equals(superUserName)) continue;
               
                userOnLine = new User(user);
                Identity userIdentity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, user,false);
                Profile userProfile = userIdentity.getProfile();
                userOnLine.setId(userProfile.getId());
                userOnLine.setProfileUrl(LinkProvider.getUserProfileUri(userIdentity.getRemoteId()));
                userOnLine.setAvatar(userProfile.getAvatarImageSource() != null ? userProfile.getAvatarImageSource() : LinkProvider.PROFILE_DEFAULT_AVATAR_URL);
                userOnLineList.add(userOnLine);
            }
            return userOnLineList;

        } catch (Exception e) {
            LOG.error("Error while checking logged users [WhoIsOnLine rendering phase] :" + e.getMessage(), e);
            return null;
        }
    }
}
