package io.github.gunjiro.hj.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import io.github.gunjiro.hj.app.AppFileOpenUnit;
import io.github.gunjiro.hj.unit.FileOpenUnit;

public class FileLoader {
    private final Implementor implementor;
    private final List<Observer> observers = new LinkedList<>();

    public static interface Implementor {
        public Reader open(String filename) throws FileNotFoundException;
        public void storeFunctions(Reader reader);
    }

    public static abstract class DefaultImplementor implements Implementor {
        private final FileOpenUnit fileOpenUnit = new AppFileOpenUnit();

        @Override
        public Reader open(String filename) throws FileNotFoundException {
            return fileOpenUnit.open(filename);
        }

    }

    public static interface Observer {
        public void receiveMessage(String message);
    }

    public FileLoader(Implementor implementor) {
        this.implementor = implementor;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void load(String filename) {
        try (Reader reader = implementor.open(filename)) {
            implementor.storeFunctions(reader);
            notifyObserversOfMessage("loaded: " + filename);
        } catch (FileNotFoundException e) {
            notifyObserversOfMessage(e.getMessage());
        } catch (IOException e) {
            notifyObserversOfMessage(e.getMessage());
        }
    }

    private void notifyObserversOfMessage(String message) {
        for (Observer observer : observers) {
            observer.receiveMessage(message);
        }
    }
}
