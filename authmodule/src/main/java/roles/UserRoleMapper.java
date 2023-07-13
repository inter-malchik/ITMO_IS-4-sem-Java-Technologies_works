package roles;

import daos.User;
import roles.constrains.AdminRoleConstraint;
import roles.constrains.OnlyAuthorizedRoleConstraint;
import roles.constrains.OwnerRoleConstraint;
import roles.constrains.RoleConstraint;

import java.util.ArrayList;
import java.util.List;

public class UserRoleMapper {
    static List<RoleConstraint> roles = new ArrayList<>();

    static {
        roles.add(new AdminRoleConstraint());
        roles.add(new OwnerRoleConstraint());
        roles.add(new OnlyAuthorizedRoleConstraint());
    }

    public static UserRole getRoleOf(User user) {
        return roles.stream()
                .filter(roleConstraint -> roleConstraint.checkUser(user))
                .findFirst().orElseThrow()
                .getAssociatedValue();
    }
}
