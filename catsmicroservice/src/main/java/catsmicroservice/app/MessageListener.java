package catsmicroservice.app;

import dto.KittenDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import queueconfigs.CatsMessage;
import queueconfigs.MQConfigCatsQueue;
import microservices.CatsMicroHandler;
import queueconfigs.MessageStatus;

@Component
public class MessageListener {
    private final CatsMicroHandler catsMicroHandler;

    @Autowired
    public MessageListener(CatsMicroHandler catsMicroHandler) {
        this.catsMicroHandler = catsMicroHandler;
    }

    @RabbitListener(queues = MQConfigCatsQueue.QUEUE)
    public CatsMessage listener(CatsMessage message) {
        message.setStatus(MessageStatus.RECEIVED);
        CatsMessage response = message;

        try {
            switch (message.getCommand()) {
                case "getCatById" -> {
                    response = getCatById(message.getCat().getId());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "deleteCatById" -> {
                    response = deleteCatById(message.getCat().getId());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "updateCatByDto" -> {
                    response = updateCatByDto(message.getCat());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "addCat" -> {
                    response = addCat(message.getCat());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "getCats" -> {
                    response = getCats(message.getCat());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "getFriends" -> {
                    response = getFriends(message.getCat());
                    response.setStatus(MessageStatus.COMPLETED);
                    return response;
                }
                case "addFriend" -> {
                    response = addFriend(message.getCats().get(0), message.getCats().get(1));
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

    public CatsMessage getCatById(int id) {
        CatsMessage answer = new CatsMessage();
        answer.setCat(catsMicroHandler.getCat(id));
        return answer;
    }

    public CatsMessage deleteCatById(int id) {
        CatsMessage answer = new CatsMessage();
        catsMicroHandler.deleteCat(id);
        answer.setCat(null);
        return answer;
    }

    public CatsMessage updateCatByDto(KittenDto dto) {
        CatsMessage answer = new CatsMessage();
        answer.setCat(catsMicroHandler.updateCatByDto(dto));
        return answer;
    }

    public CatsMessage addCat(KittenDto dto) {
        CatsMessage answer = new CatsMessage();
        answer.setCat(catsMicroHandler.createCat(dto.getNickname(), dto.getBirth_date(), dto.getOwnerId(), dto.getSpecies(), dto.getColor()));
        return answer;
    }

    public CatsMessage getCats(KittenDto dto) {
        CatsMessage answer = new CatsMessage();
        answer.setCats(catsMicroHandler.getByFilters(dto.getColor(), dto.getSpecies(), dto.getNickname()));
        return answer;
    }

    public CatsMessage getFriends(KittenDto dto) {
        CatsMessage answer = new CatsMessage();
        answer.setCats(catsMicroHandler.getFriendsOfCat(dto.getId()));
        return answer;
    }

    public CatsMessage addFriend(KittenDto lhs, KittenDto rhs) {
        catsMicroHandler.assignFriendToCat(lhs.getId(), rhs.getId());
        return getCatById(lhs.getId());
    }
}
