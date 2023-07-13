package services;

import dto.KittenDto;
import dto.OwnerDto;
import jakarta.annotation.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import queueconfigs.MQConfigOwnersQueue;
import queueconfigs.MessageStatus;
import queueconfigs.OwnersMessage;
import services.exceptions.CatsBLLException;
import services.interfaces.OwnerService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class OwnerServiceImpl implements OwnerService {

    private final RabbitTemplate template;

    @Autowired
    public OwnerServiceImpl(RabbitTemplate template) {
        this.template = template;
    }


    public OwnerDto createOwner(String name, Date birth_date) {
        OwnersMessage message = new OwnersMessage("addOwner");
        OwnerDto ownerToAdd = new OwnerDto();

        ownerToAdd.setName(name);
        ownerToAdd.setBirthDate(birth_date);

        message.setOwner(ownerToAdd);

        return sendAndReceiveMessageToQueue(message).getOwner();
    }

    @Override
    public List<KittenDto> getOwnerCats(int id) {
        OwnersMessage message = new OwnersMessage("getAllCatsOfOwner");
        OwnerDto ownerToSeek = new OwnerDto();
        ownerToSeek.setId(id);
        message.setOwner(ownerToSeek);

        return sendAndReceiveMessageToQueue(message).getCats();
    }

    @Override
    public void deleteOwner(int id) {
        OwnersMessage message = new OwnersMessage("deleteOwnerById");
        OwnerDto ownerToSeek = new OwnerDto();
        ownerToSeek.setId(id);
        message.setOwner(ownerToSeek);

        sendAndReceiveMessageToQueue(message);
    }


    @Override
    public OwnerDto updateOwnerByDto(OwnerDto ownerDto) {
        OwnersMessage message = new OwnersMessage("updateOwnerByDto");
        message.setOwner(ownerDto);

        return sendAndReceiveMessageToQueue(message).getOwner();
    }

    @Override
    public Optional<OwnerDto> getOwner(int id) {
        OwnersMessage message = new OwnersMessage("getOwnerById");
        OwnerDto ownerToSeek = new OwnerDto();
        ownerToSeek.setId(id);
        message.setOwner(ownerToSeek);
        return Optional.ofNullable(sendAndReceiveMessageToQueue(message).getOwner());
    }

    public List<OwnerDto> getAllOwners() {
        OwnersMessage message = new OwnersMessage("getOwners");
        return sendAndReceiveMessageToQueue(message).getOwners();
    }

    @Override
    public void assignCatToOwner(int owner_id, int cat_id) {
        OwnersMessage message = new OwnersMessage("assignCatToOwner");
        OwnerDto ownerToSeek = new OwnerDto();
        ownerToSeek.setId(owner_id);
        message.setOwner(ownerToSeek);

        KittenDto catToSeek = new KittenDto();
        catToSeek.setId(cat_id);
        message.setCat(catToSeek);

        sendAndReceiveMessageToQueue(message);
    }

    private OwnersMessage sendAndReceiveMessageToQueue(OwnersMessage message) {
        message.setStatus(MessageStatus.SENT);
        OwnersMessage answer = template.convertSendAndReceiveAsType
                (MQConfigOwnersQueue.EXCHANGE, MQConfigOwnersQueue.ROUTING_KEY, message,
                        ParameterizedTypeReference.forType(OwnersMessage.class));
        checkMessage(answer);
        return answer;
    }

    private void checkMessage(@Nullable OwnersMessage message) {
        if (message == null)
            throw CatsBLLException.nullMessage();

        switch (message.getStatus()) {
            case PROCESSING_FAILED -> throw CatsBLLException.messageWasNotProcessed();
            case ERROR_OCCURED -> throw CatsBLLException.errorInMessageOccured();
        }
    }
}
