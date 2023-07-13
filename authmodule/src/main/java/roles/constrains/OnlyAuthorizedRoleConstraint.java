package roles.constrains;

import daos.User;
import roles.UserRole;

public class OnlyAuthorizedRoleConstraint implements RoleConstraint {

    @Override
    public UserRole getAssociatedValue() {
        return UserRole.ROLE_ONLY_AUTHORIZED;
    }

    @Override
    public boolean checkUser(User user) {
        return !user.isAdmin() && user.getOwner() == null;
    }
}
