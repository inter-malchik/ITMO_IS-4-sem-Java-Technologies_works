package roles.constrains;

import daos.User;
import roles.UserRole;

public class OwnerRoleConstraint implements RoleConstraint {

    @Override
    public UserRole getAssociatedValue() {
        return UserRole.ROLE_OWNER;
    }

    @Override
    public boolean checkUser(User user) {
        return !user.isAdmin() && user.getOwner() != null;
    }
}
