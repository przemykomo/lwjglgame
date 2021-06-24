package xyz.przemyk.lwjglgame.engine;

import xyz.przemyk.lwjglgame.game.DummyGame;

public class GameEngine implements Runnable {

    public static final int TARGET_TPS = 30;

    private final Window window;

    private final Timer timer;

    private final DummyGame gameLogic;

    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, DummyGame gameLogic) {
        this.window = new Window(windowTitle, width, height);
        this.gameLogic = gameLogic;
        this.timer = new Timer();
        this.mouseInput = new MouseInput();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
        mouseInput.init(window);
    }

    protected void gameLoop() {

        double deltaTime = 1.0 / 60.0;
        double timeBegin = System.nanoTime() / 1_000_000_000.0;

        double lastTickTime = -1.0;
        final double expectedTickTime = 1.0 / TARGET_TPS;

        while (!window.windowShouldClose()) {
            input();

            // ---- TICK ----
            double timeNow = System.nanoTime() / 1_000_000_000.0;
            if (timeNow >= lastTickTime + expectedTickTime) {
                lastTickTime = timeNow;
                update();
            }
            // ---- END TICK ----

            // ---- RENDER ----
            float partialTicks = (float) (((System.nanoTime() / 1_000_000_000.0f) - lastTickTime) / expectedTickTime);
            render(partialTicks);
            // ---- END RENDER ----

            timeNow = System.nanoTime() / 1_000_000_000.0;
            deltaTime = timeNow - timeBegin;
            timeBegin = timeNow;
        }
    }

    protected void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    protected void update() {
        gameLogic.tick();
    }

    protected void render(float partialTicks) {
        gameLogic.render(window, partialTicks);
        window.update();
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }
}
