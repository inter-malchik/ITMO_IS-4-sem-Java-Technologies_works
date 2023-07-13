package argobjects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KittenArgObject implements ArgObject {
    public String name;

    public String birth_date;

    public Integer owner_id;

    public String species;

    public String color;

    @Override
    public boolean isValid() {
        return name != null && !name.isEmpty();
    }
}
