package services;

import dto.KittenDto;
import jakarta.annotation.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import queueconfigs.CatsMessage;
import queueconfigs.MQConfigCatsQueue;
import queueconfigs.MessageStatus;
import queueconfigs.OwnersMessage;
import services.exceptions.CatsBLLException;
import services.interfaces.CatService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Primary
public class CatServiceImpl implements CatService {

    private final RabbitTemplate template;

    public CatServiceImpl(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public KittenDto createCat(String name, Date birth_date, Integer owner_id, String species, String color) {
        CatsMessage message = new CatsMessage("addCat");
        KittenDto catToAdd = new KittenDto();

        catToAdd.setNickname(name);
        catToAdd.setBirth_date(birth_date);
        catToAdd.setOwnerId(owner_id);
        catToAdd.setSpecies(species);
        catToAdd.setColor(color);

        message.setCat(catToAdd);

        return sendAndReceiveMessageToQueue(message).getCat();

    }

    @Override
    public void deleteCat(int id) {
        CatsMessage message = new CatsMessage("deleteCatById");

        KittenDto catToDelete = new KittenDto();
        catToDelete.setId(id);
        message.setCat(catToDelete);

        sendAndReceiveMessageToQueue(message);
    }

    @Override
    public KittenDto updateCatByDto(KittenDto cat) {
        CatsMessage message = new CatsMessage("updateCatByDto");
        message.setCat(cat);

        return sendAndReceiveMessageToQueue(message).getCat();
    }

    @Override
    public KittenDto getCat(int id) {
        CatsMessage message = new CatsMessage("getCatById");

        KittenDto catToFind = new KittenDto();
        catToFind.setId(id);
        message.setCat(catToFind);

        return sendAndReceiveMessageToQueue(message).getCat();
    }

    @Override
    public List<KittenDto> getCats(String color, String species, String nickname) {
        CatsMessage message = new CatsMessage("getCats");

        KittenDto filterCat = new KittenDto();

        filterCat.setColor(color);
        filterCat.setSpecies(species);
        filterCat.setNickname(nickname);

        message.setCat(filterCat);
        return sendAndReceiveMessageToQueue(message).getCats();
    }

    public void assignFriendToCat(int cat_id, int friend_id) {
        CatsMessage message = new CatsMessage("addFriend");

        KittenDto mainCat = new KittenDto();
        mainCat.setId(cat_id);

        KittenDto friendCat = new KittenDto();
        friendCat.setId(friend_id);

        List<KittenDto> kittenSettings = new ArrayList<>();
        kittenSettings.add(mainCat);
        kittenSettings.add(friendCat);

        message.setCats(kittenSettings);

        sendAndReceiveMessageToQueue(message);
    }

    public List<KittenDto> getFriendsOfCat(int id) {
        CatsMessage message = new CatsMessage("getFriends");

        KittenDto catToFind = new KittenDto();
        catToFind.setId(id);
        message.setCat(catToFind);


        return sendAndReceiveMessageToQueue(message).getCats();
    }

    private CatsMessage sendAndReceiveMessageToQueue(CatsMessage message) {
        message.setStatus(MessageStatus.SENT);
        CatsMessage answer = template.convertSendAndReceiveAsType
                (MQConfigCatsQueue.EXCHANGE, MQConfigCatsQueue.ROUTING_KEY, message,
                        ParameterizedTypeReference.forType(CatsMessage.class));
        checkMessage(answer);
        return answer;
    }

    private void checkMessage(@Nullable CatsMessage message) {
        if (message == null)
            throw CatsBLLException.nullMessage();

        switch (message.getStatus()) {
            case PROCESSING_FAILED -> throw CatsBLLException.messageWasNotProcessed();
            case ERROR_OCCURED -> throw CatsBLLException.errorInMessageOccured();
        }
    }
}
