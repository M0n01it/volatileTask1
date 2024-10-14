import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class BeautifulWordGenerator {

    // Статические счетчики для длин слов 3, 4 и 5
    private static AtomicInteger countLength3 = new AtomicInteger(0);
    private static AtomicInteger countLength4 = new AtomicInteger(0);
    private static AtomicInteger countLength5 = new AtomicInteger(0);

    // Набор слов
    private static String[] texts = new String[100_000];

    public static void main(String[] args) throws InterruptedException {
        // Генерация слов
        generateWords();

        // Создание потоков для проверки каждого критерия
        Thread palindromeThread = new Thread(new PalindromeChecker());
        Thread sameLetterThread = new Thread(new SameLetterChecker());
        Thread ascendingOrderThread = new Thread(new AscendingOrderChecker());

        // Запуск потоков
        palindromeThread.start();
        sameLetterThread.start();
        ascendingOrderThread.start();

        // Ожидание завершения всех потоков
        palindromeThread.join();
        sameLetterThread.join();
        ascendingOrderThread.join();

        // Вывод результатов
        System.out.println("Красивых слов с длиной 3: " + countLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + countLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + countLength5.get() + " шт");
    }

    /**
     * Генерирует 100 000 слов с длинами от 3 до 5 символов, состоящих из букв 'a', 'b', 'c'.
     */
    private static void generateWords() {
        Random random = new Random();
        for (int i = 0; i < texts.length; i++) {
            int length = 3 + random.nextInt(3); // Длина 3, 4 или 5
            texts[i] = generateText("abc", length);
        }
    }

    /**
     * Генератор случайного текста.
     *
     * @param letters Набор символов для генерации.
     * @param length  Длина генерируемого слова.
     * @return Сгенерированное слово.
     */
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    /**
     * Проверяет, является ли слово палиндромом.
     */
    static class PalindromeChecker implements Runnable {
        @Override
        public void run() {
            for (String word : texts) {
                if (isPalindrome(word)) {
                    incrementCounter(word.length());
                }
            }
        }

        /**
         * Проверяет, является ли строка палиндромом.
         *
         * @param s Проверяемая строка.
         * @return true, если палиндром, иначе false.
         */
        private boolean isPalindrome(String s) {
            int len = s.length();
            for (int i = 0; i < len / 2; i++) {
                if (s.charAt(i) != s.charAt(len - 1 - i)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Проверяет, состоит ли слово из одной и той же буквы.
     */
    static class SameLetterChecker implements Runnable {
        @Override
        public void run() {
            for (String word : texts) {
                if (isSameLetter(word)) {
                    incrementCounter(word.length());
                }
            }
        }

        /**
         * Проверяет, состоит ли строка из одной и той же буквы.
         *
         * @param s Проверяемая строка.
         * @return true, если состоит из одной буквы, иначе false.
         */
        private boolean isSameLetter(String s) {
            char first = s.charAt(0);
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i) != first) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Проверяет, идут ли буквы в слове по возрастанию.
     */
    static class AscendingOrderChecker implements Runnable {
        @Override
        public void run() {
            for (String word : texts) {
                if (isAscendingOrder(word)) {
                    incrementCounter(word.length());
                }
            }
        }

        /**
         * Проверяет, идут ли символы строки в неубывающем порядке.
         *
         * @param s Проверяемая строка.
         * @return true, если символы идут по возрастанию, иначе false.
         */
        private boolean isAscendingOrder(String s) {
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i) < s.charAt(i - 1)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Увеличивает соответствующий счетчик в зависимости от длины слова.
     *
     * @param length Длина слова.
     */
    private static void incrementCounter(int length) {
        switch (length) {
            case 3:
                countLength3.incrementAndGet();
                break;
            case 4:
                countLength4.incrementAndGet();
                break;
            case 5:
                countLength5.incrementAndGet();
                break;
            default:
                // Длины других слов не учитываем
                break;
        }
    }
}