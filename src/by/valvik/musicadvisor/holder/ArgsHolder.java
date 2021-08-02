package by.valvik.musicadvisor.holder;

import by.valvik.musicadvisor.constant.Direction;
import by.valvik.musicadvisor.constant.UserCommand;

import static by.valvik.musicadvisor.util.Props.getValue;
import static java.lang.Math.min;

public class ArgsHolder {

    private static final String KEY_LIMIT = "limit";

    private static final int MAX_LIMIT = 50;

    private UserCommand userCommand;

    private String categoryId;

    private int limit;

    private Direction direction;

    public UserCommand getUserCommand() {

        return userCommand;

    }

    public void setUserCommand(UserCommand userCommand) {

        this.userCommand = userCommand;

    }

    public String getCategoryId() {

        return categoryId;

    }

    public void setCategoryId(String categoryId) {

        this.categoryId = categoryId;

    }

    public int getLimit() {

        return limit == 0 ? Integer.parseInt(getValue(KEY_LIMIT)) : limit;

    }

    public void setLimit(int limit) {

        this.limit = min(limit, MAX_LIMIT);

    }

    public Direction getDirection() {

        return direction;

    }

    public void setDirection(Direction direction) {

        this.direction = direction;

    }

}