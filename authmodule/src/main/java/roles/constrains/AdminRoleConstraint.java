package roles.constrains;

import daos.User;
import roles.UserRole;

public class AdminRoleConstraint implements RoleConstraint {

    @Override
    public UserRole getAssociatedValue() {
        return UserRole.ROLE_ADMIN;
    }

    @Override
    public boolean checkUser(User user) {
        return user.isAdmin();
    }
}
