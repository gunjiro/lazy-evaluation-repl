package io.github.gunjiro.hj.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

public class FileLoader {
    private final Implementor implementor;
    private final List<Observer> observers = new LinkedList<>();

    public static interface Implementor {
        public Reader open(String filename) throws FileNotFoundException;
        public void storeFunctions(Reader reader);
    }

    public static interface Observer {
        public void loaded(String filename);
        public void failed(String message);
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
            notifyObserversOfLoaded(filename);
        } catch (IOException e) {
            notifyObserversOfFailed(e.getMessage());
        } 
    }

    private void notifyObserversOfLoaded(String filename) {
        observers.forEach(observer -> observer.loaded(filename));
    }

    private void notifyObserversOfFailed(String message) {
        for (Observer observer : observers) {
            observer.failed(message);
        }
    }
}
