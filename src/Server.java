import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    private static final int SIZE_OF_INPUT_BUFFER = 11;
    private static final String HOST = "netology.homework"; // Задаем хост сервера для подключения клиентов
    private static final int PORT = 8088; // Задаем номер порта сервера для подключения клиентов

    public static void main(String[] args) throws IOException {
        // Занимаем порт, определяя серверный сокет
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(HOST, PORT));
        while (true) {
            // Ждем подключения клиента и получаем потоки для дальнейшей работы
            try (SocketChannel socketChannel = serverChannel.accept()) {
                // Определяем буфер для получения данных
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << (SIZE_OF_INPUT_BUFFER - 1));
                while (socketChannel.isConnected()) {
                    // Читаем данные из канала в буфер
                    int bytesCount = socketChannel.read(inputBuffer);
                    // Если из потока читать нельзя, перестаем работать с этим клиентом
                    if (bytesCount == -1) break;
                    // Получаем переданную от клиента строку в нужной кодировке и очищаем буфер
                    final String request = new String(inputBuffer.array(), 0, bytesCount,
                            StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    String answer = request.trim().replaceAll(" +", " ");
                    // Отправляем сообщение клиента назад с пометкой ЭХО
                    socketChannel.write(ByteBuffer.wrap(("Отредактированная строка: '" +
                            answer + "'").getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }

}
