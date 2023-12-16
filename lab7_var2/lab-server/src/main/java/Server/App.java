package Server;

import Common.util.Request;
import Common.util.Response;
import Server.requestHandlers.RequestExecutor;
import Server.requestHandlers.RequestReader;
import Server.requestHandlers.ResponseSender;
import Server.util.ConsoleThread;


import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Класс для установки соединения с клиентами.
 */
public class App {
    /**
     * Селектор для мониторинга каналов ввода-вывода.
     */
    private volatile Selector selector;
    /**
     * Пул потоков для чтения запросов от клиентов.
     */
    private final ExecutorService cachedService = Executors.newCachedThreadPool();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(10);
    /**
     * Множество ключей, которые в данный момент обрабатываются.
     */
    private final Set<SelectionKey> workingKeys =
            Collections.synchronizedSet(new HashSet<>());
    /**
     * Серверный канал для прослушивания входящих соединений.
     */
    private final ServerSocketChannel server;

    public App(ServerSocketChannel server) {
        this.server = server;
    }


    /**
     * Метод для запуска сервера и обработки запросов от клиентов.
     */
    public void start() {
        ConsoleThread consoleThread = new ConsoleThread();
        if (ServerConfig.isRunning) {
            consoleThread.start();
            startServer();
        }
        forkJoinPool.shutdown();
        cachedService.shutdown();
    }

    /**
     * Метод для запуска сервера и инициализации селектора.
     */
    private void startServer() {
        ServerConfig.logger.info("Сервер запущен.");
        try {
            selector = Selector.open();
            ServerSocketChannel server = initChannel(selector);
            startSelectorLoop(server);
        } catch (IOException e) {
            ServerConfig.logger.error("Проблемы с IO.");
            ServerConfig.toggleStatus();
        } catch (ClassNotFoundException e) {
            ServerConfig.logger.warn("Попытка сериализовать несериализуемый объект.");
            ServerConfig.toggleStatus();
        }
    }

    /**
     * Метод для запуска цикла селектора, который прослушивает подключения.
     * @param channel канал серверного сокета, который будет слушать подключения.
     * @throws IOException если произошла ошибка при работе с каналом.
     * @throws ClassNotFoundException если не найден класс.
     */
    private void startSelectorLoop(ServerSocketChannel channel) throws IOException, ClassNotFoundException {
        while (channel.isOpen() && ServerConfig.isRunning) {
            if (selector.select(1) != 0) {
                startIteratorLoop(channel);
            }
        }
    }

    /**
     * Метод для запуска цикла итератора, который обрабатывает готовые к обработке ключи селектора.
     * @param channel канал серверного сокета, который будет слушать подключения.
     * @throws IOException если произошла ошибка при работе с каналом.
     */
    private void startIteratorLoop(ServerSocketChannel channel) throws IOException {
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isValid() && !workingKeys.contains(key)) {
                if (key.isAcceptable()) {
                    accept(channel);
                } else if (key.isReadable()) {
                    workingKeys.add(key);
                    ServerConfig.logger.info("Клиент прислал запрос.");
                    Supplier<Request> requestReader = new RequestReader(key);
                    Function<Request, Response> requestExecutor = new RequestExecutor();
                    Consumer<Response> responseSender = new ResponseSender(key, workingKeys);
                    CompletableFuture
                            .supplyAsync(requestReader, forkJoinPool)
                            .thenApplyAsync(requestExecutor, cachedService)
                            .thenAcceptAsync(response -> {
                                new Thread(() -> responseSender.accept(response)).start();
                            }, Executors.newSingleThreadExecutor());
                }
            }
        }
    }

    /**
     * Метод для принятия входящих подключений.
     * @param channel канал серверного сокета, который будет слушать подключения.
     * @throws IOException если произошла ошибка при работе с каналом.
     */
    private void accept(ServerSocketChannel channel) throws IOException {
        SocketChannel socketChannel = channel.accept();
        ServerConfig.logger.info("Сервер получил соединение от клиента.");
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * Метод для инициализации серверного канала.
     * @param selector селектор, который будет использоваться для регистрации канала и слушания событий.
     * @return инициализированный серверный канал.
     * @throws IOException если произошла ошибка при работе с каналом.
     */
    private ServerSocketChannel initChannel(Selector selector) throws IOException {
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        return server;
    }
}
