import java.util.ArrayList;

public class DataList<T> {
    private ArrayList<ArrayListChangedListener> listener = new ArrayList<>();
    private ArrayList<T> dataList = new ArrayList<>();

    public void addListener(ArrayListChangedListener listener) {
        this.listener.add(listener);
    }

    public void removeListener(ArrayListChangedListener listener) {
        this.listener.remove(listener);
    }

    public void addItem(T item) {
        dataList.add(item);
        notifyListeners();
    }

    public void removeItem(T item) {
        dataList.remove(item);
        notifyListeners();
    }

    private void notifyListeners() {
        for (ArrayListChangedListener listener : listener) {
            listener.onArrayListChanged();
        }
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public int size() {
        return dataList.size();
    }

    public T get(int i) {
        return dataList.get(i);
    }
}
