package xyz.przemyk.lwjglgame.engine;

public interface IGameLogic {

    void init(Window window) throws Exception;
    void input(Window window, MouseInput mouseInput);
    void update(MouseInput mouseInput);
    void render(Window window, float partialTicks);
    void cleanup();
}
