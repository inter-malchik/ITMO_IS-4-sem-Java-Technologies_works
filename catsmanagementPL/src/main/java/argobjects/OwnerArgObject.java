package argobjects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OwnerArgObject implements ArgObject {
    public String name;

    public String birth_date;

    @Override
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
