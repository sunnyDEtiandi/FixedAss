package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;
import java.util.List;

public class DataBean<T> implements Serializable {
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "data=" + data +
                '}';
    }
}
