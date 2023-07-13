package queueconfigs;

import dto.KittenDto;
import dto.OwnerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OwnersMessage {
    private MessageStatus status = MessageStatus.CREATED;
    private String messageId = UUID.randomUUID().toString();
    private Date messagedate = new Date();
    private KittenDto cat = new KittenDto();
    private OwnerDto owner = new OwnerDto();
    private String command;
    private List<KittenDto> cats = new ArrayList<>();
    private List<OwnerDto> owners = new ArrayList<>();

    public OwnersMessage(String command) {
        this.command = command;
    }
}
