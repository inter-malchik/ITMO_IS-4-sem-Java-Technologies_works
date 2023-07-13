package argobjects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserArgObject implements ArgObject {
    public String username;

    public String password;

    public Boolean isAdmin;


    @Override
    public boolean isValid() {
        return username != null && !username.isEmpty() &&
                password != null && !password.isEmpty() &&
                isAdmin != null;
    }
}
