package ru.assist.migrate.slack2pachka.v3.service.chat;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.assist.migrate.slack2pachka.v1.service.RecoveryUtils;
import ru.assist.migrate.slack2pachka.v3.model.chat.Chat;
import ru.assist.migrate.slack2pachka.v3.model.chat.Group;
import ru.assist.migrate.slack2pachka.v3.model.chat.Member;
import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.ChatListHandler;
import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.GroupListHandler;
import ru.assist.migrate.slack2pachka.v3.service.chat.handlers.MemberListHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatServiceImpl implements ChatService {
    final int LIMIT = 10;
    final RestClient restClient;
    final RetryTemplate retryTemplate;

    @Autowired
    public ChatServiceImpl(RetryTemplate retryTemplate, RestClient restClient) {
        this.retryTemplate = retryTemplate;
        this.restClient = restClient;
    }

    /**
     * https://api.pachca.com/api/v3/chats/12575747/memberships/450154
     * payload: {"role":"member"}
     *
     * @param chatId
     * @param memberId
     * @param role
     */
    @Override
    public void role(long chatId, long memberId, String role) {
        retryTemplate.execute(context -> {
            Member.Role memberRole = Member.Role.builder()
                    .role(role)
                    .build();
            return restClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("chats", "{chatId}", "memberships, {memberId}")
                            .build(chatId, memberId))
                    .body(memberRole)
                    .retrieve()
                    .toBodilessEntity();

        }, context -> {
            RecoveryUtils.waitIfError(5L);
            role(chatId, memberId, role);
            return null;
        });
    }

    @Override
    public Member[] list(long chatId) {
        List<Member> result = new ArrayList<>();
        long after_id = 0;
        while (true) {
            Member.Response response = getChatMembers(chatId, LIMIT, after_id);
            Member[] members = response.getData();
            result.addAll(Arrays.asList(members));
            if (members.length < LIMIT) break;
            after_id = members[members.length - 1].getId();
        }

        return result.toArray(new Member[0]);
    }

    @Override
    public void list(long chatId, MemberListHandler handler) {
        long after_id = 0;
        while (true) {
            Member.Response response = getChatMembers(chatId, LIMIT, after_id);
            Member[] members = response.getData();
            handler.handle(chatId, members);
            if (members.length < LIMIT) break;
            after_id = members[members.length - 1].getId();
        }
    }

    private Member.Response getChatMembers(long chatId, int limit, long after_id) {
        return (Member.Response) retryTemplate.execute(context -> {
            ResponseEntity<Member.Response> responseEntity = restClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder
                                .pathSegment("chats", "{chatId}", "members")
                                .queryParam("limit", String.valueOf(limit));
                        if (after_id != 0) uriBuilder.queryParam("after_id", after_id);
                        return uriBuilder.build(chatId);
                    })
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Member.Response.class);
            return Objects.requireNonNull(responseEntity.getBody());
        }, context -> {
            RecoveryUtils.waitIfError(5L);
            return getChatGroups(chatId, limit, after_id);
        });
    }


    /**
     * https://api.pachca.com/api/v3/chats/12575747/archive
     *
     * @param chatId
     */
    @Override
    public void archive(long chatId) {
        retryTemplate.execute(context -> {
            return restClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("chats", "{chatId}", "archive")
                            .build(chatId))
                    .retrieve()
                    .toBodilessEntity();

        }, context -> {
            RecoveryUtils.waitIfError(5L);
            archive(chatId);
            return null;
        });
    }

    /**
     * https://api.pachca.com/api/v3/chats/12575747/unarchive
     *
     * @param chatId
     */
    @Override
    public void unarchive(long chatId) {
        retryTemplate.execute(context -> {
            return restClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("chats", "{chatId}", "unarchive")
                            .build(chatId))
                    .retrieve()
                    .toBodilessEntity();

        }, context -> {
            RecoveryUtils.waitIfError(5L);
            unarchive(chatId);
            return null;
        });
    }

    @Override
    public void list(ChatListHandler handler) {
        long after_id = 0;
        while (true) {
            Chat.Response response = getChats(LIMIT, after_id);
            Chat[] chats = response.getData();
            handler.handle(chats);
            if (chats.length < LIMIT) break;
            after_id = chats[chats.length - 1].getId();
        }
    }

    /**
     * https://api.pachca.com/api/v3/preview/public_chats?q=&type=discussion&limit=10&after_id=12288917
     *
     * @return
     */
    Chat.Response getChats(int limit, long after_id) {
        return retryTemplate.execute(context -> {
            ResponseEntity<Chat.Response> responseEntity = restClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder
                                .pathSegment("preview", "public_chats")
                                .queryParam("q", "")
                                .queryParam("type", "discussion")
                                .queryParam("limit", limit);
                        if (after_id != 0) uriBuilder.queryParam("after_id", after_id);
                        return uriBuilder.build();
                    })
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Chat.Response.class);
            return Objects.requireNonNull(responseEntity.getBody());
        }, context -> {
            RecoveryUtils.waitIfError(5L);
            return getChats(limit, after_id);
        });
    }

    @Override
    public void list(long chatId, GroupListHandler handler) {
        long after_id = 0;
        while (true) {
            Group.Response response = getChatGroups(chatId, LIMIT, after_id);
            Group[] groups = response.getData();
            handler.handle(chatId, groups);
            if (groups.length < LIMIT) break;
            after_id = groups[groups.length - 1].getId();
        }
    }

    public Group.Response getChatGroups(long chatId, int limit, long after_id) {
        return retryTemplate.execute(context -> {
            ResponseEntity<Group.Response> responseEntity = restClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder
                                .pathSegment("chats", "{chatId}", "group_tags")
                                .queryParam("limit", String.valueOf(limit));
                        if (after_id != 0) uriBuilder.queryParam("after_id", after_id);
                        return uriBuilder.build(chatId);
                    })
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Group.Response.class);
            return Objects.requireNonNull(responseEntity.getBody());
        }, context -> {
            RecoveryUtils.waitIfError(5L);
            return getChatGroups(chatId, limit, after_id);
        });
    }

    @Override
    public void add(long chatId, long groupId) {
        long[] groupIds = new long[]{groupId};
        add(chatId, groupIds);
    }

    @Override
    public void add(long chatId, long[] groupIds) {
        retryTemplate.execute(context -> {
            Group.Tags groupTags = Group.Tags.builder()
                    .ids(groupIds)
                    .build();
            restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("chats", "{chatId}", "group_tags")
                            .build(chatId))
                    .body(groupTags)
                    .retrieve()
                    .toBodilessEntity();
            return null;
        }, context -> {
            RecoveryUtils.waitIfError(5);
            add(chatId, groupIds);
            return null;
        });
    }

    @Override
    public void remove(long chatId, long groupId) {
        retryTemplate.execute(context -> {
            return restClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("chats", "{chatId}", "group_tags", "{groupId}")
                            .build(chatId, groupId))
                    .retrieve()
                    .toBodilessEntity();
        }, context -> {
            RecoveryUtils.waitIfError(5);
            remove(chatId, groupId);
            return null;
        });
    }
}
