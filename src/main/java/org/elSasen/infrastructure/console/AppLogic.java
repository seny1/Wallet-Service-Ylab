package org.elSasen.infrastructure.console;

import org.elSasen.infrastructure.console.state.MainState;

public class AppLogic {

    private StateConsole current;

    public AppLogic() {
        this.current = new MainState();
    }

    public void work() throws Exception {
        while (true) {
            try {
                current.process();
                if (current.nextState() != null) {
                    StateConsole nextState = current.nextState();
                    current = nextState;
                }
            } catch (Exception e) {
                System.err.println("Problem: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
