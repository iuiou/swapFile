package elevator.scheduler;

import java.util.ArrayList;
import java.util.List;

class ListMap<T> {
    private final List[] ds;

    ListMap(int cap) {
        ds = new List[cap];
    }

    ListMap<T> clones() {
        ListMap<T> r = new ListMap<>(ds.length);
        for (int i = 0; i < ds.length; ++i) {
            List<T> a = get(i);
            if (a != null) {
                r.ds[i] = new ArrayList<>(a);
            }
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    void add(int k, T v) {
        if (ds[k] == null) {
            List<T> a = new ArrayList<>();
            a.add(v);
            ds[k] = a;
        } else {
            ds[k].add(v);
        }
    }

    @SuppressWarnings("unchecked")
    List<T> get(int k) {
        return (List<T>) ds[k];
    }

    List<T> pop(int k) {
        List<T> a = get(k);
        ds[k] = null;
        return a;
    }

    boolean isEmpty(int k) {
        return ds[k] == null || ds[k].isEmpty();
    }

    int count(int k) {
        List<T> a = get(k);
        if (a == null) {
            return 0;
        }
        return a.size();
    }

    boolean someDirected(int p, int dir) {
        if (dir > 0) {
            for (int i = p + 1; i < ds.length; ++i) {
                if (!isEmpty(i)) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < p; ++i) {
                if (!isEmpty(i)) {
                    return true;
                }
            }
        }
        return false;
    }
}
