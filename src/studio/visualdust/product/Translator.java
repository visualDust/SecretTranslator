package studio.visualdust.product;

public class Translator {
    private String password;
    private String dict;

    public Translator(String password, String dictionary) {
        this.password = password;
        this.dict = dictionary;
    }
    public String encrypt(String content, TranslateEventListener tel) {
        return operate(content, (passwordIndex, textIndex, dictLen) -> (dictLen + passwordIndex + textIndex) % dictLen, tel);
    }

    public String decrypt(String content, TranslateEventListener tel) {
        return operate(content, (passwordIndex, textIndex, dictLen) -> (dictLen + textIndex - passwordIndex) % dictLen, tel);
    }

    private String operate(String content, TripleIntToIntFunction strategy, TranslateEventListener tel) {
        StringBuilder sb = new StringBuilder(content.length());
        final CyclicIntRange passwordPos = new CyclicIntRange(password.length());
        final int manipulator = (content.length() % 2 == 0) ? 1 : -1;
        for (int i = 0; i < content.length(); i++) {
            char passwordCh = password.charAt(passwordPos.next());
            char textCh = content.charAt(i);
            final int passwordIndex = dict.indexOf(passwordCh);
            final int textIndex = dict.indexOf(textCh);
            if ((passwordIndex | textIndex) < 0) {
                throw CharNotFoundException.ofChars(passwordCh, textCh);
            }
            char resultCh = dict.charAt(strategy.apply(manipulator * passwordIndex, textIndex, dict.length()));
            sb.append(resultCh);
            if (tel != null) {
                tel.consume(textCh, resultCh);
            }
//            System.out.println(String.format("Translate %s to %s", textCh, resultCh));
        }
        return sb.toString();
    }

    private interface TripleIntToIntFunction {
        int apply(int a, int b, int c);
    }

    public interface TranslateEventListener {
        void consume(char textCh, char resultCh);
    }

    public static class CharNotFoundException extends RuntimeException {
        public final char c1, c2;
        public CharNotFoundException(String message, char c1, char c2) {
            super(message);
            this.c1 = c1;
            this.c2 = c2;
        }

        public static CharNotFoundException ofChars(char c1, char c2) {
            return new CharNotFoundException(String.format("Illegal combination %s, %s", c1, c2), c1, c2);
        }
    }

    private class CyclicIntRange {
        private int lowerBound;
        private int currentDelta;
        private int difference;

        CyclicIntRange(int lowerBound, int upperBound) {
            this.lowerBound = lowerBound;
            this.difference = upperBound - lowerBound;
            currentDelta = 0;
        }

        public CyclicIntRange(int upperBound) {
            this(0, upperBound);
        }

        public int next() {
            final int toReturn = currentDelta + lowerBound;
            currentDelta++;
            currentDelta %= difference;
            return toReturn;
        }

        public int get() {
            return currentDelta + lowerBound;
        }
    }
}
