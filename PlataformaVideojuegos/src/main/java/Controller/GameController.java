package Controller;

import Repository.InMemory.GameRepoInMemory;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GameRepoInMemory repository = new GameRepoInMemory();
    private List<String> errores = new ArrayList<>();
}
