package by.valvik.musicadvisor.command.impl;

import by.valvik.musicadvisor.constant.UserCommand;
import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.domain.category.Category;
import by.valvik.musicadvisor.service.SpotifyService;
import by.valvik.musicadvisor.view.View;

import static by.valvik.musicadvisor.constant.UserCommand.CATEGORIES;

@Singleton(qualifier = UserCommand.Qualifier.CATEGORIES)
public class CategoriesCommand extends ApiCommand<Category> {

    @Inject
    public CategoriesCommand(SpotifyService service, View console) {

        super(service, console, CATEGORIES, Category.class);

    }

}
