package roles.constrains;

import daos.User;
import org.jetbrains.annotations.NotNull;
import roles.UserRole;

public interface RoleConstraint {
    @NotNull
    UserRole getAssociatedValue();

    boolean checkUser(@NotNull User user);
}
