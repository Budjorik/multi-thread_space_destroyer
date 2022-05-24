import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientTwo {
    private static final int SIZE_OF_INPUT_BUFFER = 11; // Задаем размер буфера
    private static final int WAITING_TIME_IN_SECONDS = 5; // Задаем время ожидания
    private static final String HOST = "netology.homework"; // Задаем хост для подключения к серверу
    private static final int PORT = 8088; // Задаем номер порта для подключения к серверу

    public static void main(String[] args) throws IOException {
        // Определяем сокет сервера
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        final SocketChannel socketChannel = SocketChannel.open();
        // Подключаемся к серверу
        socketChannel.connect(socketAddress);
        // Получаем входящий и исходящий потоки информации
        try (Scanner scanner = new Scanner(System.in)) {
            // Определяем буфер для получения данных
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << (SIZE_OF_INPUT_BUFFER - 1));
            String request;
            System.out.println("Вас приветствует сервис-редактор, " +
                    "который поможет очистить Ваш текст от лишних пробелов!");
            while (true) {
                System.out.print(" - Введите текстовую строку для редактирования;\n" +
                        " - Или введите 'end', если хотите выйти.\n" +
                        "Ваш ввод:\n");
                request = scanner.nextLine();
                if ("end".equals(request)) break;
                socketChannel.write(
                        ByteBuffer.wrap(
                                request.getBytes(StandardCharsets.UTF_8)));
                TimeUnit.SECONDS.sleep(WAITING_TIME_IN_SECONDS);
                int bytesCount = socketChannel.read(inputBuffer);
                String answer = new String(inputBuffer.array(), 0, bytesCount,
                        StandardCharsets.UTF_8);
                System.out.println(answer);
                inputBuffer.clear();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }

}
