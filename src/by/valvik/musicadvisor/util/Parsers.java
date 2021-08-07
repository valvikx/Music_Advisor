package by.valvik.musicadvisor.util;

import by.valvik.musicadvisor.constant.Direction;
import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.exception.UtilException;
import by.valvik.musicadvisor.context.holder.ArgsHolder;
import by.valvik.musicadvisor.tuple.Tuple;

import java.util.*;
import java.util.stream.Collectors;

import static by.valvik.musicadvisor.constant.Delimiter.AMPERSAND;
import static by.valvik.musicadvisor.constant.Delimiter.EQUALS;
import static by.valvik.musicadvisor.constant.Direction.NEXT;
import static by.valvik.musicadvisor.constant.Direction.PREV;
import static by.valvik.musicadvisor.constant.UserCommand.PLAYLISTS;
import static by.valvik.musicadvisor.util.Props.getValue;
import static java.util.Collections.singletonList;
import static java.util.Map.of;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.*;

public final class Parsers {

    private static final String ARG_LIMIT = "-limit";

    private static final String KEY_INVALID_COMMAND_ARGUMENTS = "invalid_command_arguments";

    private Parsers() {}

    public static Map<String, List<String>> splitQuery(String query) {

        if (isNull(query) || query.isEmpty() || !query.contains(EQUALS.getSign())) {

            return of();

        } else if (!query.contains(AMPERSAND.getSign())) {

            Tuple<String, String> pair = splitQueryPair(query);

            return of(pair.x(), singletonList(pair.y()));

        }

        return Arrays.stream(query.split(AMPERSAND.getSign()))
                     .map(Parsers::splitQueryPair)
                     .collect(groupingBy(Tuple::x, mapping(Tuple::y, toList())));

    }

    public static ArgsHolder collectArgs(String[] tokens) throws UtilException {

        // Command args: [command name (required)] [category id (required for playlists)] [-limit (optional)]
        // Command args example: new
        // Command args example: categories -limit 10
        // Command args example: playlists hiphop -limit 10

        ArgsHolder argsHolder = new ArgsHolder();

        if (tokens.length > 0) {

            Deque<String> tokenList = Arrays.stream(tokens).collect(Collectors.toCollection(ArrayDeque::new));

            if (tokenList.isEmpty()) {

                throw new UtilException(getValue(KEY_INVALID_COMMAND_ARGUMENTS));

            }

            String commandName = tokenList.poll().toUpperCase();

            if (Objects.equals(commandName, NEXT.name()) || Objects.equals(commandName, PREV.name())) {

                argsHolder.setDirection(Direction.valueOf(commandName));

                return argsHolder;

            }

            try {

                UserCommand userCommand = UserCommand.valueOf(commandName.toUpperCase());

                argsHolder.setUserCommand(userCommand);

                if (!tokenList.isEmpty()) {

                    if (userCommand.equals(PLAYLISTS)) {

                        argsHolder.setCategoryId(tokenList.poll());

                        if (tokenList.isEmpty()) {

                            return argsHolder;

                        }

                    }

                    String limit = tokenList.poll();

                    if (!Objects.equals(limit, ARG_LIMIT) || tokenList.isEmpty()) {

                        throw new UtilException(getValue(KEY_INVALID_COMMAND_ARGUMENTS));

                    }

                    int limitValue = Integer.parseInt(tokenList.poll());

                    argsHolder.setLimit(limitValue);

                }

                return argsHolder;

            } catch (IllegalArgumentException e) {

                throw new UtilException(getValue(KEY_INVALID_COMMAND_ARGUMENTS));

            }

        }

        return argsHolder;

    }

    private static Tuple<String, String> splitQueryPair(String pair) {

        int idx = pair.indexOf(EQUALS.getSign());

        String key = pair.substring(0, idx);

        String value = pair.substring(idx + 1);

        return new Tuple<>(key, value);

    }

}
