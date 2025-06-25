package io.github.gunjiro.hj.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import io.github.gunjiro.hj.ApplicationException;

public class FileLoader {
    private final Implementor implementor;
    private final List<Observer> observers = new LinkedList<>();

    public static interface Implementor {
        public Reader open(String filename) throws FileNotFoundException;
        public void addFunctions(Reader reader) throws ApplicationException;
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
            addFunctions(reader);
            notifyObserversOfLoaded(filename);
        } catch (ApplicationException | IOException e) {
            notifyObserversOfFailed(e.getMessage());
        }  
    }

    private void addFunctions(Reader reader) throws ApplicationException {
        implementor.addFunctions(reader);
    }

    private void notifyObserversOfLoaded(String filename) {
        observers.forEach(observer -> observer.loaded(filename));
    }

    private void notifyObserversOfFailed(String message) {
        observers.forEach(observer -> observer.failed(message));
    }

}
