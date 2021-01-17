package advisor.command;

import advisor.exception.AdvisorException;
import advisor.model.Model;

public interface ICommand {

    void execute(Model model, Command pagingCommand) throws AdvisorException;

}
