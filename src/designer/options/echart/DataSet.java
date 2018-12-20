package designer.options.echart;

import java.util.ArrayList;

/**
 * @author kimi
 * @description 数据集（dataset）组件用于单独的数据集声明，从而数据可以单独管理，被多个组件复用，
 * @date 2018-12-17 18:33
 *  //TODO 4.0新增 暂不切换
 */


public class DataSet {
    private  String id;
    private DataSerSource[] source;
    private DimensionData dimensions;
    private boolean sourseHeader;

    public String id() {
        return id;
    }

    public DataSet id(String id) {
        this.id = id;
        return this;
    }


    public DataSerSource[] source() {
        return source;
    }

    public DataSet source(DataSerSource[] source) {
        this.source = source;
        return this;
    }

    public DimensionData dimensions() {
        return dimensions;
    }

    public DataSet dimensions(DimensionData dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public boolean sourseHeader() {
        return sourseHeader;
    }

    public DataSet sourseHeader(boolean sourseHeader) {
        this.sourseHeader = sourseHeader;
        return this;
    }

    public class  DimensionData<T> implements Data<T> {


        private ArrayList data;

        public ArrayList data() {
            if (this.data == null) {
                this.data = new ArrayList();
            }
            return this.data;
        }

        @Override
        public T data(Object... values) {
            if (values == null || values.length == 0) {
                return (T) this;
            }
            for (Object value : values) {
                this.data().add(value);
            }
            return (T) this;
        }
    }
    public class  DataSerSource<T> implements Data<T> {

        private ArrayList data;

        public ArrayList data() {
            if (this.data == null) {
                this.data = new ArrayList();
            }
            return this.data;
        }

        @Override
        public T data(Object... values) {
            if (values == null || values.length == 0) {
                return (T) this;
            }
            for (Object value : values) {
                this.data().add(value);
            }
            return (T) this;
        }
    }
}
