package ownersmicroservice.app;

import dto.KittenDto;
import dto.OwnerDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import queueconfigs.CatsMessage;
import queueconfigs.MQConfigOwnersQueue;
import queueconfigs.MessageStatus;
import queueconfigs.OwnersMessage;
import microservices.OwnerMicroHandler;

@Component
public class MessageListener {
    private final OwnerMicroHandler ownerMicroHandler;

    @Autowired
    public MessageListener(OwnerMicroHandler ownerMicroHandler) {
        this.ownerMicroHandler = ownerMicroHandler;
    }

    @RabbitListener(queues = MQConfigOwnersQueue.QUEUE)
    public OwnersMessage listener(OwnersMessage message) {
        message.setStatus(MessageStatus.RECEIVED);
        OwnersMessage response = message;

        try {
            switch (message.getCommand()) {
                case "getOwnerById" -> {
                    response = getOwnerById(message.getOwner().getId());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "deleteOwnerById" -> {
                    response = deleteOwnerById(message.getOwner().getId());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "updateOwnerByDto" -> {
                    response = updateOwnerByDto(message.getOwner());
                    ;
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "addOwner" -> {
                    response = addOwner(message.getOwner());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "getOwners" -> {
                    response = getOwners();
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "getAllCatsOfOwner" -> {
                    response = getAllCatsOfOwner(message.getOwner());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "assignCatToOwner" -> {
                    response = assignCatToOwner(message.getOwner(), message.getCat());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
            }
        }
        catch (RuntimeException ex) {
            response.setStatus(MessageStatus.ERROR_OCCURED);
            return response;
        }

        System.err.println(message.getCommand() + " Not Found");
        response.setStatus(MessageStatus.PROCESSING_FAILED);
        return response;
    }

    public OwnersMessage getOwnerById(int id) {
        OwnersMessage answer = new OwnersMessage();
        answer.setOwner(ownerMicroHandler.getOwner(id));
        return answer;
    }

    public OwnersMessage deleteOwnerById(int id) {
        OwnersMessage answer = new OwnersMessage();
        ownerMicroHandler.deleteOwner(id);
        answer.setOwner(null);
        return answer;
    }

    public OwnersMessage updateOwnerByDto(OwnerDto dto) {
        OwnersMessage answer = new OwnersMessage();
        answer.setOwner(ownerMicroHandler.updateOwnerByDto(dto));
        return answer;
    }

    public OwnersMessage addOwner(OwnerDto dto) {
        OwnersMessage answer = new OwnersMessage();
        answer.setOwner(ownerMicroHandler.createOwner(dto.getName(), dto.getBirthDate()));
        return answer;
    }

    public OwnersMessage getOwners() {
        OwnersMessage answer = new OwnersMessage();
        answer.setOwners(ownerMicroHandler.getAllOwners());
        return answer;
    }

    public OwnersMessage getAllCatsOfOwner(OwnerDto dto) {
        OwnersMessage answer = new OwnersMessage();
        answer.setOwner(dto);
        answer.setCats(ownerMicroHandler.getOwnerCats(dto.getId()));
        return answer;
    }

    public OwnersMessage assignCatToOwner(OwnerDto ownerDto, KittenDto kittenDto) {
        OwnersMessage answer = new OwnersMessage();
        ownerMicroHandler.addCatToOwner(ownerDto.getId(), kittenDto.getId());
        return answer;
    }
}
